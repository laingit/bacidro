(ns bacidro.core
  (:require
    [korma.core :as k :only (defentity
                              set-fields
                              insert
                              update
                              values)]
    [korma.db :as kDB :only (defdb get-connection)]
    [clojure.java.jdbc :as j]))

(comment
  (def db-spec
    {:classname   "org.postgresql.Driver"
     :subprotocol "postgresql"
     :subname     "//192.168.18.51:5432/rumbl_dev"
     :user        "postgres"
     :password    "postgres"})

  (kDB/defdb korma-db db-spec)

  (defn- get-connection-from-pool []
    (kDB/get-connection korma-db))


  (def idro-live
    (let [query "SELECT \n
                idropost.id, idropost.tipo, idropost.settore, idropost.valle
                FROM public.idropost
                WHERE public.idropost.settore = 1
                ORDER BY idropost.settore,idropost.id, idropost.tipo;"]
      (j/query (get-connection-from-pool) [query])))
  )

(def idro-no-live (list {:id "F09", :tipo 1M, :settore 1M, :valle nil}
                        {:id "F10", :tipo 2M, :settore 1M, :valle "F42"}
                        {:id "F21", :tipo 2M, :settore 1M, :valle "F09"}
                        {:id "F34", :tipo 2M, :settore 1M, :valle "F42"}
                        {:id "F36", :tipo 1M, :settore 1M, :valle nil}
                        {:id "F42", :tipo 2M, :settore 1M, :valle "F21"} ;; F21
                        {:id "F70", :tipo 2M, :settore 1M, :valle "L19"} ;; L19
                        {:id "L10", :tipo 3M, :settore 1M, :valle "F70"}
                        {:id "L11", :tipo 3M, :settore 1M, :valle "F70"}
                        {:id "L19", :tipo 2M, :settore 1M, :valle "F34"} ;; F34
                        {:id "L23", :tipo 3M, :settore 1M, :valle "F34"}
                        {:id "L6", :tipo 3M, :settore 1M, :valle "F10"}))



(defn- find-loop [table-obj]
  (let [find-parent (fn [id]
                      (let [parent (get-in table-obj [id :valle])]
                        parent))

        run (fn trova-idrometri-a-valle [acc id]
              (let [{:keys [up loop]} acc
                    parent (find-parent id)
                    new-acc (if (nil? id)
                              {:up up :loop loop}
                              {:up (conj up id) :loop loop})]
                (if (= parent nil)
                  new-acc

                  (let [gia-trovati (set up)]
                    (if (gia-trovati id)
                      {:up up :loop id}

                      (trova-idrometri-a-valle
                        new-acc
                        parent)))

                  )))

        results (map
                  (fn [[k {:keys [nodo]}]]
                    (let [{:keys [up loop]} (run {:up [] :loop nil} k)]
                      {:id k :nodo nodo :up up :loop loop}))
                  table-obj)

        errori (filter
                 (fn [{:keys [id loop]}] (= id loop))
                 results)

        errori-monte (filter
                       (fn [{:keys [nodo]}] (= nodo :monte))
                       errori)

        errori-1 (map
                   (fn [{:keys [up] :as all}]
                     (assoc all :set (set up)))
                   errori)

        gruppo-errori (group-by :set errori-1)

        ]
    {:all           results
     :errori-1      errori-1
     :errori-monte  errori-monte
     :gruppo-errori 'gruppo-errori}
    ))


(defn main [table]
  (let [table-new
        (letfn [(trasforma-new [{:keys [id tipo valle]}]
                  (let [def-tipo {1M :base 2M :mezzo 3M :monte}
                        nodo (def-tipo tipo)]
                    {:id id :nodo nodo :valle valle}))]
          (map trasforma-new table))

        table-obj
        (letfn [(seq-to-obj [{:keys [id nodo valle]}]
                  {id {:nodo nodo :valle valle}})]
          (->> table-new
               (map seq-to-obj)
               (into {})))

        group-by-children (group-by :valle table-new)

        idrometri-a-monte
        (letfn [(estrai-da-children [[idrometro children]]
                  (let [valori
                        (map
                          (fn [{:keys [id]}]
                            {:a-monte id}) children)]

                    {idrometro (into [] valori)}))]
          (->>
            group-by-children
            (map estrai-da-children)
            (into {})))


        report-nodi
        (letfn [(conta-nodi [[k v]] {k (count v)})]
          (->> idrometri-a-monte
               (map conta-nodi)
               (into {})))

        table-elaborata
        (letfn [(elabora [acc v] 1
                  )]
          (reduce elabora table-obj report-nodi))

        ]
    {:table.new         table-new
     :table.obj         table-obj
     :group-by-chidren  group-by-children
     :rootName          nil
     :idrometri-a-monte idrometri-a-monte
     :report-nodi       report-nodi}))

(def ELABORATO
  {:obj         {"F42" {:nodo :mezzo, :valle "F21"},
                 "F21" {:nodo :mezzo, :valle "F09"},
                 "L10" {:nodo :monte, :valle "F70"},
                 "L19" {:nodo :mezzo, :valle "F34"},
                 "L11" {:nodo :monte, :valle "F70"},
                 "F10" {:nodo :mezzo, :valle "F42"},
                 "F09" {:nodo :base, :valle nil},
                 "L6"  {:nodo :monte, :valle "F10"},
                 "F34" {:nodo :mezzo, :valle "F42"},
                 "L23" {:nodo :monte, :valle "F34"},
                 "F36" {:nodo :base, :valle nil},
                 "F70" {:nodo :mezzo, :valle "L19"}},
   :rootName    nil,
   :children    {nil   '("F09" "F36"),
                 "F42" '("F10" "F34"),
                 "F09" '("F21"),
                 "F21" '("F42"),
                 "L19" '("F70"),
                 "F70" '("L10" "L11"),
                 "F34" '("L19" "L23"),
                 "F10" '("L6")},
   :report-nodi {nil 2, "F42" 2, "F09" 1, "F21" 1, "L19" 1, "F70" 2, "F34" 2, "F10" 1}})

(def a (main idro-no-live))

(find-loop (a :table.obj))
a


(def t (a :idrometri-a-monte))
t


(defn build-tree [t-obj id acc]
  (let [children (get t-obj id)
        monte (if (nil? children)
                []
                (->>
                  (map

                    (fn [{:keys [a-monte]}]
                      (let [child {:name a-monte :monte []}
                            new-acc (conj acc child)]
                        (build-tree t-obj a-monte new-acc)))

                    children)
                  (into [])))
        ]
    {:name id :monte monte}))

(defn build-tree-bis [t-obj id acc]
  (let [children (get t-obj id)
        monte (if (nil? children)
                acc
                (->>
                  (map

                    (fn [{:keys [a-monte]}]
                      (build-tree-bis t-obj a-monte acc))
                    children)
                  (into [])))

        ]
    {:name id :monte monte}))


(defn trova-tutti [t-obj id acc]
  (let [children (get t-obj id)
        monte (if (nil? children)
                acc
                (->>
                  (mapcat
                    (fn [{:keys [a-monte]}]
                      (trova-tutti t-obj a-monte acc))

                    children)
                  (into [])))
        ]
    (conj monte id)))


(def x (build-tree-bis t nil []))

(def y (trova-tutti t "L6" []))

(def mappa-idrometro-parti
  (->> (a :table.obj)
       (map (fn [[k _]] {k (trova-tutti t k [])}))
       (into {})))

(def tabella-parti-idro
  (mapcat
    (fn [[k v]]
      (for [vx v] {:sel k :id_parte vx}))
    mappa-idrometro-parti))

(def BC_Fiume_Flumendosa
  {:bacino "Fiume Flumendosa"
   :name   nil
   :monte  [{:name  :F09
             :tipo  :base
             :monte [{:name  :F21
                      :tipo  :mezzo
                      :monte [{:name  :F42
                               :monte [{:name  :F10
                                        :monte [{:name :L6 :monte []}]}
                                       {:name  :F34
                                        :monte [{:name :L23 :monte []}
                                                {:name  :L19
                                                 :monte [{:nome  :F70
                                                          :monte [{:name :L11 :monte []}
                                                                  {:name :L10 :monte []}]
                                                          }]
                                                 }]
                                        }]
                               }]
                      }]
             }
            {:name :F36, :tipo :monte, :monte []}
            ]})


(def db-idro
  {:settore   [:definire :flumendosa]
   :tipo      [:definire :base :mezzo :monte]
   :idrometro {:F09 {:tipo :base, :settore :definire}}})



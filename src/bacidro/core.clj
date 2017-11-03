(ns bacidro.core
  (:require
    [korma.core :as k :only (defentity
                              set-fields
                              insert
                              update
                              values)]
    [korma.db :as kDB :only (defdb get-connection)]
    [clojure.java.jdbc :as j]
    [clojure.pprint :as pp]
    [java-jdbc.ddl :as ddl]))

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

;; ---------- MICROSOFT ACCESS
(def db-spec-a (kDB/msaccess {:db "X:/_SNAPSHOT/PRJ_bac_idrometri/Bacidro.mdb"}))

(kDB/defdb korma-db db-spec-a)

(defn- get-connection-from-pool []
  (kDB/get-connection korma-db))

(def idro-live
  (let [query "SELECT \n
                id, tipo, settore, valle
                FROM tree_idrometri
                WHERE TREE_IDROMETRI.SETTORE Is Not Null
                ORDER BY settore,id;"]
    (j/query (get-connection-from-pool) [query])))
;; ------------------------------------------------------------------------------

(reduce conj #{} (map :settore idro-live))

(filter (fn [idrometro] (= "FLUMENDOSA" (:settore idrometro))) idro-live)

(def idro-no-live (list {:id "F09", :tipo "BASE", :settore "FLUMENDOSA", :valle nil}
                        {:id "F10", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F42"}
                        {:id "F21", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F09"}
                        {:id "F34", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F42"}
                        {:id "F36", :tipo "BASE", :settore "FLUMENDOSA", :valle nil}
                        {:id "F42", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F21"}
                        {:id "F70", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "L19"}
                        {:id "L10", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F70"}
                        {:id "L11", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F70"}
                        {:id "L19", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F34"}
                        {:id "L23", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F34"}
                        {:id "L6", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F10"}))


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
                  (fn [[k {:keys [tipo]}]]
                    (let [{:keys [up loop]} (run {:up [] :loop nil} k)]
                      {:id k :tipo tipo :up up :loop loop}))
                  table-obj)

        errori (filter
                 (fn [{:keys [id loop]}] (= id loop))
                 results)

        errori-monte (filter
                       (fn [{:keys [tipo]}] (= tipo :monte))
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
     :gruppo-errori gruppo-errori}
    ))


(defn main [table settore]
  (let [table-new
        (letfn [(trasforma-new [{:keys [id tipo valle]}]
                  {:id id :tipo tipo :valle valle})]
          (map trasforma-new table))

        table-obj
        (letfn [(seq-to-obj [{:keys [id tipo valle]}]
                  {id {:tipo tipo :valle valle}})]
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

        ]
    {:settore           settore
     :table-new         table-new
     :table-obj         table-obj
     :group-by-chidren  group-by-children
     :rootName          nil
     :idrometri-a-monte idrometri-a-monte
     :report-nodi       report-nodi}))

(def GLOBAL-ELABORATI
  (->> idro-live
       (group-by :settore)
       (map (fn [[settore table]] {settore (main table settore)}))
       (into {})))

(def GLOBAL-TEST-ERRORI
  (->> GLOBAL-ELABORATI
       (map (fn [[settore table]] {settore (find-loop (:table-obj table))}))
       (into {})))

(def GLOBAL-ISOLA-ERRORI
  (->> GLOBAL-TEST-ERRORI
       (map (fn [[settore errori]]
              (let [errori-settore (:gruppo-errori errori)]
                {settore (keys errori-settore)})))
       (into {})))

(def GLOBAL-REPORT
  {:errori   (filter
               (fn [[settore err]] (not (nil? err)))
               GLOBAL-ISOLA-ERRORI)

   :settori  (count GLOBAL-ISOLA-ERRORI)

   :lista-ok (->>
               GLOBAL-ISOLA-ERRORI
               (filter (fn [[settore err]] (nil? err)))
               (map first))
   })

(pp/pprint
  GLOBAL-ELABORATI)

(pp/pprint
  GLOBAL-TEST-ERRORI)

(pp/pprint GLOBAL-REPORT)

(find GLOBAL-ELABORATI "TEMO")
(find GLOBAL-TEST-ERRORI "TEMO")
GLOBAL-ELABORATI
GLOBAL-TEST-ERRORI
GLOBAL-REPORT


;; tree build NON USATO PER QUESTO MA UTILE
(comment
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

  (def my-tree (build-tree-bis t nil []))
  )

(defn- trova-tutti [t-obj id acc]
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


(defn- elabora-settore
  [[nome-settore {:keys [table-obj idrometri-a-monte]}]]
  (let [mappa-parti
        (->> table-obj
             (map (fn [[key-idro _]]
                    {key-idro (trova-tutti idrometri-a-monte key-idro [])}))
             (into {}))]

    {:settore nome-settore :mappa-parti mappa-parti}))

(defn crea-record-tabella-parti-idro
  [{:keys [settore mappa-parti]}]
  (mapcat
    (fn [[k v]]
      (for [vx v] {:settore settore :link_id_geo vx :to_dissolve k}))
    mappa-parti))

;; test singolo
(-> GLOBAL-ELABORATI
    (find "FLUMENDOSA")
    elabora-settore
    crea-record-tabella-parti-idro)

(def new-records-TABELLA-PARTI-IDRO
  (->>
    GLOBAL-ELABORATI
    (map elabora-settore)
    (mapcat crea-record-tabella-parti-idro)))

;; DELETE tabella: "lista_parti_doppie"
(j/db-do-commands
  db-spec-a
  false
  (ddl/drop-table :lista_parti_doppie))

;; CREA tabella: "lista_parti_doppie"
(j/db-do-commands db-spec-a false
                  (ddl/create-table
                    :lista_parti_doppie
                    [:link_id_geo :varchar "NOT NULL"]
                    [:to_dissolve :varchar "NOT NULL"]
                    [:settore :varchar]
                    ))

;; INSERT record -> tabella: "lista_parti_doppie"
(j/insert-multi! db-spec-a :lista_parti_doppie
                 (sort-by
                   (fn [{:keys [settore link_id_geo to_dissolve]}]
                     (str settore link_id_geo to_dissolve))
                   new-records-TABELLA-PARTI-IDRO))

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



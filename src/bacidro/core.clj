(ns bacidro.core
  (:require
    [korma.core :as k :only (defentity
                              set-fields
                              insert
                              update
                              values)]
    [korma.db :as kDB :only (defdb get-connection)]
    [clojure.java.jdbc :as j]))

(comment "database POSTGRES LIVE")
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
;; --- END

;;  MAIN SET 2 OBJ
(defn- set_2_obj [acc {:keys [id tipo valle]}]
  (let [def-tipo {1M :base 2M :mezzo 3M :monte}
        nodo (def-tipo tipo)
        obj {:nodo nodo :valle valle}]
    (assoc acc id obj)))

(defn- main-set_2_obj [table]
  (reduce set_2_obj {} table))

;;  GROUP CHILDREN
(defn- estrai-id-gruppo [gruppo]
  (let [[k v] gruppo
        solo-id (map :id v)]
    {k solo-id}))

(defn group-children [table]
  (let [gruppo (group-by :valle table)]
    (map estrai-id-gruppo gruppo)))


(defn main [table]
  (let [obj (main-set_2_obj table)

        children
        (->>
          (group-children table)
          (into {}))

        report-nodi
        (letfn [(conta-nodi [[k v]] {k (count v)})]
          (map conta-nodi children))                    ; <-INPUT

        ]
    {:obj         obj
     :rootName    nil
     :children    children
     :report-nodi (into {} report-nodi)}))



(def a
  (main idro-live))
a




(= (a :children) (into {} (a :tmp.children)))



(def BC_Fiume_Flumendosa
  {:bacino "Fiume Flumendosa"
   :idro   [{:name  :F09
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

(def idro-no-db (list {:id "F09", :tipo 1M, :settore 1M, :valle nil}
                      {:id "F10", :tipo 2M, :settore 1M, :valle "F42"}
                      {:id "F21", :tipo 2M, :settore 1M, :valle "F09"}
                      {:id "F34", :tipo 2M, :settore 1M, :valle "L11"} ;; errore
                      ;;{:id "F34", :tipo 2M, :settore 1M, :valle "F42"}
                      {:id "F36", :tipo 1M, :settore 1M, :valle nil}
                      {:id "F42", :tipo 2M, :settore 1M, :valle "F21"}
                      {:id "F70", :tipo 2M, :settore 1M, :valle "L19"}
                      {:id "L10", :tipo 3M, :settore 1M, :valle "F70"}
                      {:id "L11", :tipo 3M, :settore 1M, :valle "F70"}
                      {:id "L19", :tipo 2M, :settore 1M, :valle "F34"}
                      {:id "L23", :tipo 3M, :settore 1M, :valle "F34"}
                      {:id "L6", :tipo 3M, :settore 1M, :valle "F10"}))

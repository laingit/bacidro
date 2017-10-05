(ns bacidro.core
  (:require
    [korma.core :as k :only (defentity
                              set-fields
                              insert
                              update
                              values)]
    [korma.db :as kDB :only (defdb get-connection)]
    [clojure.java.jdbc :as j]))

(def db-spec
  {:classname   "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname     "//192.168.18.51:5432/rumbl_dev"
   :user        "postgres"
   :password    "postgres"})

(kDB/defdb korma-db db-spec)

(defn- get-connection-from-pool []
  (kDB/get-connection korma-db))


(def l2
  (let [query "SELECT \n
                idropost.id, idropost.tipo, idropost.settore, idropost.valle
                FROM public.idropost
                WHERE public.idropost.settore = 1
                ORDER BY idropost.settore,idropost.id, idropost.tipo;"]
    (j/query (get-connection-from-pool) [query])))

;;
(defn- set_2_obj [acc v]
  (let [id (:id v)]
    (assoc acc id v)))

(defn- main-set_2_obj [table]
  (reduce set_2_obj {} table))

;;
(defn- find_roots [v]
  (let [{:keys [:valle]} v]
    (if (= valle nil) true false)))

(defn- estrai_root [v]
  (let [{:keys [:id ]} v]
    id))

(defn- main-roots [table]
  (->> table
       (filter find_roots)
       (map estrai_root)))

(defn main [table]
  (let [obj (main-set_2_obj table)
        roots (main-roots table)]
    {:obj obj :roots roots}))




(def BC_Fiume_Flumendosa
  {:bacino "Fiume Flumendosa"
   :idro   [{:name  :F09
             :tipo  :base
             :monte [{:name  :F21
                      :tipo :mezzo
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
            {:name :F36, :tipo :monte, :monte[]}
            ]})


(def db-idro
  {:settore [:definire :flumendosa]
   :tipo [:definire :base :mezzo :monte]
   :idrometro {:F09 {:tipo :base, :settore :definire}}})




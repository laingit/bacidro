(ns bacidro.db-postgres
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


(def db-spec
  {:classname   "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname     "//192.168.18.51:5432/rumbl_dev"
   :user        "postgres"
   :password    "postgres"})

(kDB/defdb korma-db db-spec)

(defn- get-connection-from-pool []
  (kDB/get-connection korma-db))


(defn get-idro-data []
  (let [query "SELECT \n
              idropost.id, idropost.tipo, idropost.settore, idropost.valle
              FROM public.idropost
              WHERE public.idropost.settore = 1
              ORDER BY idropost.settore,idropost.id, idropost.tipo;"]
    (j/query (get-connection-from-pool) [query])))
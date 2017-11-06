(ns bacidro.db-access
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


;; ---------- MICROSOFT ACCESS -> NEEDS JDK 7 ;; DO NOT WORK ON JDK 8
(def  GLOBAL-db-spec (kDB/msaccess {:db "X:/_SNAPSHOT/PRJ_bac_idrometri/Bacidro.mdb"}))

(kDB/defdb korma-db  GLOBAL-db-spec)

(defn- get-connection-from-pool []
  (kDB/get-connection korma-db))

(defn get-idro-data []
  (let [query "SELECT \n
                id, tipo, settore, valle, bacino_cfd
                FROM tree_idrometri
                WHERE TREE_IDROMETRI.SETTORE Is Not Null
                ORDER BY bacino_cfd,settore,id;"]
    (j/query (get-connection-from-pool) [query])))
;; ------------------------------------------------------------------------------


(defn- delete-and-create-table [table-name]
  (let []
    ;; DELETE tabella: "lista_parti_doppie"
    (print "Delete table: " table-name "\n")
    (try
      (j/db-do-commands
        GLOBAL-db-spec
        false
        (ddl/drop-table table-name))
      (catch Exception e
        (print e)
        ))

    (print "create table: " table-name "\n")
    ;; CREA tabella: "lista_parti_doppie"
    (j/db-do-commands GLOBAL-db-spec false
                      (ddl/create-table
                        table-name
                        [:link_id_geo :varchar "NOT NULL"]
                        [:to_dissolve :varchar "NOT NULL"]
                        [:settore :varchar]
                        ))
    ))


(defn write-new-records [new-records-TABELLA-PARTI-IDRO]
  (let [table-name :lista_parti_doppie
        conteggio (count new-records-TABELLA-PARTI-IDRO)]
    (delete-and-create-table table-name)

    ;; INSERT record -> tabella: "lista_parti_doppie"
    (j/insert-multi! GLOBAL-db-spec table-name
                     (sort-by
                       (fn [{:keys [settore link_id_geo to_dissolve]}]
                         (str settore link_id_geo to_dissolve))
                       new-records-TABELLA-PARTI-IDRO))
    (print "Creati: " conteggio "\n")
    conteggio
    ))






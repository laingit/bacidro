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
#_(def GLOBAL-db-spec (kDB/msaccess {:db "X:/_SNAPSHOT/PRJ_V001_bac_idrometri/Bacidro.mdb"}))
(def GLOBAL-db-spec (kDB/msaccess {:db "X:/_SNAPSHOT/PRJ_V002_bac_idrometri/Intrometro.mdb"}))

(kDB/defdb korma-db GLOBAL-db-spec)

(defn- get-connection-from-pool []
  (kDB/get-connection korma-db))

(defn get-idro-data []
  (let [query "SELECT \n
                id, tipo, settore, valle
                FROM tree_idrometri
                WHERE TREE_IDROMETRI.SETTORE Is Not Null
                ORDER BY settore,id;"]
    (j/query (get-connection-from-pool) [query])))

#_(defn get-LITO-data []
  (let [query "SELECT \n
                LITO_DUE_AREE_KM2_IDROMETRI.SETTORE,
                LITO_DUE_AREE_KM2_IDROMETRI.IDROMETRO,
                LITO_DUE_AREE_KM2_IDROMETRI.LIV_2,
                LITO_DUE_AREE_KM2_IDROMETRI.IDROMETRO_KM2,
                LITO_DUE_AREE_KM2_IDROMETRI.AREA_FORMA_KM2,
                LITO_DUE_AREE_KM2_IDROMETRI.PERCENTO_IDROMETRO \n
                FROM LITO_DUE_AREE_KM2_IDROMETRI \n
                ORDER BY SETTORE,IDROMETRO ;"]
    (j/query (get-connection-from-pool) [query])))

#_(defn get-LITO-LIV2 []
  (let [query
        "SELECT LEGENDA_DUE.LIV_2, LEGENDA_DUE.LIV_2_DESC, LEGENDA_DUE.LABEL, LEGENDA_DUE.RGB
         FROM LEGENDA_DUE
         ORDER BY LEGENDA_DUE.LIV_2;"
        ]
    (j/query (get-connection-from-pool) [query])))



;; ------------------------------------------------------------------------------
#_(->>
  (get-LITO-data)
  (group-by :settore)

  )

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






(ns geoppr.db-access
  (:require
    [korma.core :as k :only (defentity
                              set-fields
                              insert
                              update
                              values)]
    [korma.db :as kDB :only (defdb get-connection)]
    [clojure.java.jdbc :as j]
    [clojure.pprint :as pp]
    [java-jdbc.ddl :as ddl]
    [clojure.string :as str]))


;; ---------- MICROSOFT ACCESS -> NEEDS JDK 7 ;; DO NOT WORK ON JDK 8
(def GLOBAL-db-spec (kDB/msaccess {:db "E:\\ArcGisPRO_PRJ\\GeoPPR2008_consolida\\GeoLegePPReColoriOriginale_consolida.mdb"}))

(kDB/defdb korma-db GLOBAL-db-spec)

(defn- get-connection-from-pool []
  (kDB/get-connection korma-db))

(defn get-eta []
  (let [query "SELECT \n
                geoppr, eta, unita_eta
                FROM legenda;"]
    (j/query (get-connection-from-pool) [query])))

(defn get-eta-nome []
  (let [query "SELECT \n
                sigla_eta, nome_eta
                FROM eta;"]
    (j/query (get-connection-from-pool) [query])))


(def dictionary-eta
  (reduce (fn [acc {:keys [sigla_eta nome_eta]}]
            (assoc acc sigla_eta nome_eta)) {} (get-eta-nome)))


(defn- delete-and-create-table [fisso]
  (let [table-name "controllo"]
    ;; DELETE tabella: "controllo"
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
    ;; CREA tabella: "controllo"
    (j/db-do-commands GLOBAL-db-spec false
                      (ddl/create-table
                        table-name
                        [:geoppr :integer]
                        [:eta :varchar]
                        [:eta_inf_antico :varchar]
                        [:eta_inf_x :varchar]
                        [:eta_inf_k :varchar]
                        [:eta_sup_recente :varchar]
                        [:eta_sup_x :varchar]
                        [:eta_sup_k :varchar]
                        [:eta_unita :varchar]
                        [:eta_unita_x :varchar]
                        [:eta_da_codici :varchar]
                        [:eta_da_codici_estesa :varchar]
                        [:eta_uguale :varchar]
                        [:perfetto :varchar]
                        ))
    ))

(delete-and-create-table "fisso")

(defn write-new-records [new-records-TABELLA-PARTI-IDRO]
  (let [table-name :controllo
        conteggio (count new-records-TABELLA-PARTI-IDRO)]
    (delete-and-create-table table-name)

    ;; INSERT record -> tabella: "lista_parti_doppie"
    (j/insert-multi! GLOBAL-db-spec table-name
                     (sort-by :geoppr
                              new-records-TABELLA-PARTI-IDRO))
    (print "Creati: " conteggio "\n")
    conteggio
    ))




(def table
  (->>
    (get-eta)

    (map
      (fn [{:keys [geoppr eta unita_eta]}]
        (let [converti (fn [value] (if value "SI" "NO"))
              [antico recente] (str/split eta #"-")
              antico? (str/starts-with? antico "?")
              recente? (str/starts-with? recente "?")
              k-antico (str/replace antico #"\?" "")
              k-recente (str/replace recente #"\?" "")

              eta-unita? (if unita_eta (str/includes? unita_eta "?") false)

              unita_eta (if (nil? unita_eta) "era vuoto" unita_eta)

              eta-da-codici                                 ;es. "ATB0-ATB0"
              (str
                (if antico? "?" "")
                k-antico
                "-"
                (if recente? "?" "")
                k-recente)

              eta-da-cocici-estesa                          ; es. "?PRECAMBRIANO - ?CAMBRIANO INF."
              (if (= antico recente)
                (str
                  (if antico? "?" "")
                  (dictionary-eta k-antico))                ;basta uno

                (str
                  (if antico? "?" "")
                  (dictionary-eta k-antico)
                  " - "
                  (if recente? "?" "")
                  (dictionary-eta k-recente)))]

          {:geoppr               geoppr
           :eta                  eta
           :eta_inf_antico       antico
           :eta_inf_x             (converti antico?)
           :eta_inf_k            k-antico
           :eta_sup_recente      recente
           :eta_sup_x             (converti recente?)
           :eta_sup_k            k-recente
           :eta_unita            unita_eta
           :eta_unita_x           (converti eta-unita?)
           :eta_da_codici        eta-da-codici
           :eta_da_codici_estesa eta-da-cocici-estesa
           :eta_uguale           (converti (= eta eta-da-codici))
           :perfetto             (converti (= eta-da-cocici-estesa unita_eta))}
          )))))

table

(write-new-records table)

(ns bacidro.core
  (:require
    [clojure.pprint :as pp]
    [bacidro.db-access :as my-db-access]
    [bacidro.db-access :as my-db-file]))

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

(def idro-data (my-db-access/get-idro-data))
idro-data

(group-by :settore idro-data)

(def GLOBAL-ELABORATI
  (->> idro-data
       (group-by :settore)

       (map
         (fn [[settore table]]
           {settore (main table settore)}))

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

(find GLOBAL-ELABORATI "CEDRINO")
(find GLOBAL-TEST-ERRORI "TEMO")


GLOBAL-ELABORATI
GLOBAL-TEST-ERRORI
GLOBAL-REPORT


  #_(defn build-tree [idrometri-a-monte id-root acc valore]
      (let [children (get idrometri-a-monte id-root)
            monte (if (nil? children)
                    []
                    (->>
                      (map
                        (fn [{:keys [a-monte]}]
                          (let [child {:name a-monte :valore valore :monte []}
                                new-acc (conj acc child)]
                            (build-tree idrometri-a-monte a-monte new-acc (+ valore 1))))

                        children)
                      (into [])))
            ]
        {:name   id-root
         :valore valore
         :monte  monte}))

  (defn build-tree-bis
    [{:keys [idrometri-a-monte
             contribuisce-a
             id-root
             acc
             livello
             tree-name
             sub-val]}]
    (let [children (get idrometri-a-monte id-root)
          n-child (count children)
          monte (if (nil? children)
                  acc
                  (->>
                    (map
                      (fn [{:keys [a-monte]} sub-val-children]
                        (build-tree-bis
                          {:idrometri-a-monte idrometri-a-monte
                           :contribuisce-a (conj contribuisce-a id-root)
                           :id-root           a-monte
                           :acc               acc
                           :livello           (+ livello 1)
                           :tree-name         (str tree-name "." sub-val-children)
                           :sub-val           sub-val-children}))
                      children (range 1 1000000))
                    (into [])))

          ]
      {:name    id-root
       :contribuisce-a (->> (conj contribuisce-a id-root) (into []))
       :livello livello
       :tree-name  tree-name
       :sub-val sub-val
       :n-child n-child
       :monte   monte}))

  ;; String ->
  ;; input GLOBALE   : GLOBAL-ELABORATI
  ;; input parametro : settore

  (defn sx [nome-settore]
    (let [dati-settore (GLOBAL-ELABORATI nome-settore)
          {:keys [idrometri-a-monte]} dati-settore
          root (idrometri-a-monte nil)
          errore (if (= (count root) 1) false true)         ;solo un elemento root consentito
          id-root (-> root first :a-monte)]
      {:settore                    nome-settore
       :errore                     errore
       :look-in-root-if-error-true root
       :id-root                    id-root
       :idrometri-a-monte          idrometri-a-monte}))


  (sx "CHIA")

  (defn- my-tree [nome-settore]
    (let [elabora-settore (sx nome-settore)
          {:keys [settore
                  errore
                  look-in-root-if-error-true
                  id-root
                  idrometri-a-monte]}
          elabora-settore]
      (if errore
        (print settore look-in-root-if-error-true)
        (build-tree-bis
          {:idrometri-a-monte idrometri-a-monte
           :id-root           id-root
           :contribuisce-a    ()
           :acc               []
           :livello           1
           :tree-name         "1"
           :sub-val           1}))))

  (my-tree "COGHINAS")


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


(my-db-access/write-new-records new-records-TABELLA-PARTI-IDRO) ;; SCRIVE NELLA TABELLA ACCESS *******************

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




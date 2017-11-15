(ns bacidro.gen-pdf
  (:require
    [clojure.pprint :as pp]
    [bacidro.db-access :as my-db-access]
    [bacidro.core :as baci]
    [clj-pdf.core :as pdf]))

baci/GLOBAL-ELABORATI

(def employees
  [{:country "Germany",
    :place "Nuremberg",
    :occupation "Engineer",
    :name "Neil Chetty"}
   {:country "Germany",
    :place "Ulm",
    :occupation "Engineer",
    :name "Vera Ellison"}])

(def employee-template
  (pdf/template
    [:paragraph
     [:heading (.toUpperCase $name)]
     [:chunk {:style :bold} "occupation: "] $occupation "\n"
     [:chunk {:style :bold} "place: "] $place "\n"
     [:chunk {:style :bold} "country: "] $country
     [:spacer]]))

(employee-template employees)

(pdf/pdf [{:title "Employee Table"}
         (employee-template employees)]
"employees.pdf")
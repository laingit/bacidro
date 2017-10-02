(ns bacidro.core)

(def BC_Fiume_Flumendosa
  {:bacino "Fiume Flumendosa"
   :idro   [{:name  :F09
             :monte [{:name :F21
                      :monte [{:name :F42
                               :monte [{:name :F10
                                        :monte [{:name :L6 :monte []}]}
                                       {:name :F34
                                        :monte [{:name :L23 :monte []}
                                                {:name :L19
                                                 :monte [{:nome :F70
                                                          :monte [{:name :L11 :monte []}
                                                                  {:name :L10 :monte []}]
                                                          }]
                                                 }]
                                        }]
                               }]
                      }]
             }
            {:name :F36 :monte []}
            ]})

{:L10 [:L10]}
{:L11 [:L11]}
{:F10 [:L10 :L11]}
{:L19 [:F10 :L10 :L11]}
{:L23 [:L23]}
{:F34 [:F10 :L10 :L11 :23]}
{:L6 [:L6]}
{:F10 [:F10 :L6]}
{:F42 [:F42 :F34 :F10 :L10 :L11 :23]}
{:F21 [:F21 :F42 :F34 :F10 :L10 :L11 :23]}
{:F09 [:F09:F21 :F42 :F34 :F10 :L10 :L11 :23]}
{:F36 [:F36]}




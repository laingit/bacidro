(ns bacidro.data-from-file)

(def data
  (list   {:id "F09", :tipo "BASE", :settore "FLUMENDOSA", :valle nil}
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
          {:id "L6", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F10"}
          {:id "F01", :tipo nil, :settore "MANNU", :valle "F38"}
          {:id "F19", :tipo nil, :settore "MANNU", :valle "F28"}
          {:id "F20", :tipo nil, :settore "MANNU", :valle "F37"}
          {:id "F27", :tipo nil, :settore "MANNU", :valle "F29"}
          {:id "F28", :tipo nil, :settore "MANNU", :valle "F38"}
          {:id "F29", :tipo nil, :settore "MANNU", :valle "F52"}
          {:id "F37", :tipo "BASE", :settore "MANNU", :valle nil}
          {:id "F38", :tipo nil, :settore "MANNU", :valle "F27"}
          {:id "F52", :tipo nil, :settore "MANNU", :valle "F20"}
          {:id "L12", :tipo nil, :settore "MANNU", :valle "F37"}
          {:id "L20", :tipo nil, :settore "MANNU", :valle "F19"}
          {:id "L25", :tipo nil, :settore "MANNU", :valle "F52"}
          {:id "F15", :tipo "BASE", :settore "PABILLONIS", :valle nil}
          {:id "F40", :tipo nil, :settore "PABILLONIS", :valle "F15"}
          {:id "F24", :tipo "BASE", :settore "TEMO", :valle nil}
          {:id "F33", :tipo nil, :settore "TEMO", :valle "F56"}
          {:id "F56", :tipo nil, :settore "TEMO", :valle "L9"}
          {:id "F57", :tipo nil, :settore "TEMO", :valle "L9"}
          {:id "L28", :tipo nil, :settore "TEMO", :valle "L9"}
          {:id "L9", :tipo nil, :settore "TEMO", :valle "F24"}
          {:id "F05", :tipo "BASE", :settore "TIRSO", :valle nil}
          {:id "F23", :tipo nil, :settore "TIRSO", :valle "L0"}
          {:id "F35", :tipo nil, :settore "TIRSO", :valle "L5"}
          {:id "F41", :tipo nil, :settore "TIRSO", :valle "L29"}
          {:id "F43", :tipo nil, :settore "TIRSO", :valle "F35"}
          {:id "L0", :tipo nil, :settore "TIRSO", :valle "F05"}
          {:id "L27", :tipo nil, :settore "TIRSO", :valle "F41"}
          {:id "L29", :tipo nil, :settore "TIRSO", :valle "L5"}
          {:id "L3", :tipo nil, :settore "TIRSO", :valle "L29"}
          {:id "L5", :tipo nil, :settore "TIRSO", :valle "F23"}
          {:id "F13", :tipo "BASE", :settore "TORRES", :valle nil}
          {:id "F39", :tipo nil, :settore "TORRES", :valle "F13"}
          {:id "L14", :tipo nil, :settore "TORRES", :valle "F39"}))
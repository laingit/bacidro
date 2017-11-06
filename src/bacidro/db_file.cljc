(ns bacidro.db-file)

(defn write-new-records [new-records-TABELLA-PARTI-IDRO]
  (let []
    (print new-records-TABELLA-PARTI-IDRO)
    ))

(defn get-idro-data []
  (list {:id "F30", :tipo "MEZZO", :settore "CEDRINO", :valle "F31", :bacino_cfd "Cedrino"}
        {:id "F31", :tipo "MEZZO", :settore "CEDRINO", :valle "F58", :bacino_cfd "Cedrino"}
        {:id "F32", :tipo "MEZZO", :settore "CEDRINO", :valle "F31", :bacino_cfd "Cedrino"}
        {:id "F50", :tipo "MONTE", :settore "CEDRINO", :valle "F66", :bacino_cfd "Cedrino"}
        {:id "F51", :tipo "MONTE", :settore "CEDRINO", :valle "F32", :bacino_cfd "Cedrino"}
        {:id "F58", :tipo "BASE", :settore "CEDRINO", :valle nil, :bacino_cfd "Cedrino"}
        {:id "F66", :tipo "MEZZO", :settore "CEDRINO", :valle "L15", :bacino_cfd "Cedrino"}
        {:id "L15", :tipo "MEZZO", :settore "CEDRINO", :valle "F30", :bacino_cfd "Cedrino"}
        {:id "F08", :tipo "MONTE", :settore "CIXERRI", :valle "L16", :bacino_cfd "Cixerri"}
        {:id "F22", :tipo "BASE", :settore "CIXERRI", :valle nil, :bacino_cfd "Cixerri"}
        {:id "L16", :tipo "MEZZO", :settore "CIXERRI", :valle "F22", :bacino_cfd "Cixerri"}
        {:id "L24", :tipo "MONTE", :settore "CIXERRI", :valle "L16", :bacino_cfd "Cixerri"}
        {:id "L4", :tipo "MONTE", :settore "CIXERRI", :valle "L16", :bacino_cfd "Cixerri"}
        {:id "F12", :tipo "MEZZO", :settore "COGHINAS", :valle "L17", :bacino_cfd "Coghinas"}
        {:id "F44", :tipo "MONTE", :settore "COGHINAS", :valle "F59", :bacino_cfd "Coghinas"}
        {:id "F45", :tipo "MONTE", :settore "COGHINAS", :valle "F59", :bacino_cfd "Coghinas"}
        {:id "F47", :tipo "MONTE", :settore "COGHINAS", :valle "F12", :bacino_cfd "Coghinas"}
        {:id "F48", :tipo "MEZZO", :settore "COGHINAS", :valle "F12", :bacino_cfd "Coghinas"}
        {:id "F59", :tipo "MEZZO", :settore "COGHINAS", :valle "F12", :bacino_cfd "Coghinas"}
        {:id "F62", :tipo "MONTE", :settore "COGHINAS", :valle "F12", :bacino_cfd "Coghinas"}
        {:id "F67", :tipo "MONTE", :settore "COGHINAS", :valle "F71", :bacino_cfd "Coghinas"}
        {:id "F69", :tipo "BASE", :settore "COGHINAS", :valle nil, :bacino_cfd "Coghinas"}
        {:id "F71", :tipo "MEZZO", :settore "COGHINAS", :valle "F69", :bacino_cfd "Coghinas"}
        {:id "L17", :tipo "MEZZO", :settore "COGHINAS", :valle "F71", :bacino_cfd "Coghinas"}
        {:id "L22", :tipo "MONTE", :settore "COGHINAS", :valle "F48", :bacino_cfd "Coghinas"}
        {:id "F09", :tipo "BASE", :settore "FLUMENDOSA", :valle nil, :bacino_cfd "Flumendosa"}
        {:id "F10", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F42", :bacino_cfd "Flumendosa"}
        {:id "F21", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F09", :bacino_cfd "Flumendosa"}
        {:id "F34", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F42", :bacino_cfd "Flumendosa"}
        {:id "F36", :tipo "BASE", :settore "FLUMENDOSA", :valle nil, :bacino_cfd "Flumendosa"}
        {:id "F42", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F21", :bacino_cfd "Flumendosa"}
        {:id "F70", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "L19", :bacino_cfd "Flumendosa"}
        {:id "L10", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F70", :bacino_cfd "Flumendosa"}
        {:id "L11", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F70", :bacino_cfd "Flumendosa"}
        {:id "L19", :tipo "MEZZO", :settore "FLUMENDOSA", :valle "F34", :bacino_cfd "Flumendosa"}
        {:id "L23", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F34", :bacino_cfd "Flumendosa"}
        {:id "L6", :tipo "MONTE", :settore "FLUMENDOSA", :valle "F10", :bacino_cfd "Flumendosa"}
        {:id "F01", :tipo nil, :settore "MANNU", :valle "F38", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F19", :tipo nil, :settore "MANNU", :valle "F28", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F20", :tipo nil, :settore "MANNU", :valle "F37", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F27", :tipo nil, :settore "MANNU", :valle "F29", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F28", :tipo nil, :settore "MANNU", :valle "F38", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F29", :tipo nil, :settore "MANNU", :valle "F52", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F37", :tipo "BASE", :settore "MANNU", :valle nil, :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F38", :tipo nil, :settore "MANNU", :valle "F27", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F52", :tipo nil, :settore "MANNU", :valle "F20", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "L12", :tipo nil, :settore "MANNU", :valle "F37", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "L20", :tipo nil, :settore "MANNU", :valle "F19", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "L25", :tipo nil, :settore "MANNU", :valle "F52", :bacino_cfd "Flumini Mannu di Cagliari"}
        {:id "F15", :tipo "BASE", :settore "PABILLONIS", :valle nil, :bacino_cfd "Flumini Mannu di Pabillonis"}
        {:id "F40", :tipo nil, :settore "PABILLONIS", :valle "F15", :bacino_cfd "Flumini Mannu di Pabillonis"}
        {:id "F61", :tipo "BASE", :settore "LISCIA", :valle nil, :bacino_cfd "Liscia"}
        {:id "L1", :tipo nil, :settore "LISCIA", :valle "F61", :bacino_cfd "Liscia"}
        {:id "L7", :tipo nil, :settore "LISCIA", :valle "L1", :bacino_cfd "Liscia"}
        {:id "F13", :tipo "BASE", :settore "TORRES", :valle nil, :bacino_cfd "Mannu di Porto Torres"}
        {:id "F39", :tipo nil, :settore "TORRES", :valle "F13", :bacino_cfd "Mannu di Porto Torres"}
        {:id "L14", :tipo nil, :settore "TORRES", :valle "F39", :bacino_cfd "Mannu di Porto Torres"}
        {:id "F54", :tipo "BASE", :settore "GIRASOLE", :valle nil, :bacino_cfd "Minori tra Cedrino e Flumendosa"}
        {:id "L26", :tipo "MONTE", :settore "GIRASOLE", :valle "F54", :bacino_cfd "Minori tra Cedrino e Flumendosa"}
        {:id "F03", :tipo "BASE", :settore "LOTZORAI", :valle nil, :bacino_cfd "Minori tra Cedrino e Flumendosa"}
        {:id "F04", :tipo "BASE", :settore "QUIRRA", :valle nil, :bacino_cfd "Minori tra Cedrino e Flumendosa"}
        {:id "F53", :tipo "BASE", :settore "QUIRRA", :valle nil, :bacino_cfd "Minori tra Cedrino e Flumendosa"}
        {:id "F02", :tipo "BASE", :settore "TORTOLI", :valle nil, :bacino_cfd "Minori tra Cedrino e Flumendosa"}
        {:id "F17", :tipo "BASE", :settore "CHIA", :valle nil, :bacino_cfd "Minori tra il Cixerri e il Palmas"}
        {:id "F18", :tipo "BASE", :settore "PULA", :valle nil, :bacino_cfd "Minori tra il Cixerri e il Palmas"}
        {:id "F06", :tipo "BASE", :settore "SANTALUCIA", :valle nil, :bacino_cfd "Minori tra il Cixerri e il Palmas"}
        {:id "F49", :tipo "BASE", :settore "VIGNOLA", :valle nil, :bacino_cfd "Minori tra il Coghinas e il Liscia"}
        {:id         "F07",
         :tipo       "BASE",
         :settore    "PICOCA",
         :valle      nil,
         :bacino_cfd "Minori tra il Flumendosa e il Flumini Mannu di Cagliari"}
        {:id         "F11",
         :tipo       "BASE",
         :settore    "FLUMINIMAGGIORE",
         :valle      nil,
         :bacino_cfd "Minori tra il Flumini Mannu di Cagliari, il Cixerri, il Palmas e il Flumini Mannu di Pabillonis"}
        {:id         "F16",
         :tipo       "BASE",
         :settore    "FLUMINIMAGGIORE",
         :valle      nil,
         :bacino_cfd "Minori tra il Flumini Mannu di Cagliari, il Cixerri, il Palmas e il Flumini Mannu di Pabillonis"}
        {:id         "F25",
         :tipo       "MEZZO",
         :settore    "MOGORO",
         :valle      "F26",
         :bacino_cfd "Minori tra il Flumini Mannu di Pabillonis e il Tirso"}
        {:id         "F26",
         :tipo       "BASE",
         :settore    "MOGORO",
         :valle      nil,
         :bacino_cfd "Minori tra il Flumini Mannu di Pabillonis e il Tirso"}
        {:id         "L8",
         :tipo       "MONTE",
         :settore    "MOGORO",
         :valle      "F25",
         :bacino_cfd "Minori tra il Flumini Mannu di Pabillonis e il Tirso"}
        {:id "L18", :tipo "BASE", :settore "OLMEDO", :valle nil, :bacino_cfd "Minori tra il Mannu di Portotorres e il Temo"}
        {:id "F14", :tipo "BASE", :settore "TIRSO-TEMO", :valle nil, :bacino_cfd "Minori tra il Tirso e il Temo"}
        {:id "F46", :tipo "MONTE", :settore "TIRSO-TEMO", :valle "F14", :bacino_cfd "Minori tra il Tirso e il Temo"}
        {:id "F63", :tipo "MONTE", :settore "PADRONGIANO", :valle "F65", :bacino_cfd "Padrongiano"}
        {:id "F65", :tipo "MEZZO", :settore "PADRONGIANO", :valle "F68", :bacino_cfd "Padrongiano"}
        {:id "F68", :tipo "BASE", :settore "PADRONGIANO", :valle nil, :bacino_cfd "Padrongiano"}
        {:id "F64", :tipo "BASE", :settore "PALMAS", :valle nil, :bacino_cfd "Palmas"}
        {:id "L13", :tipo "MONTE", :settore "PALMAS", :valle "L21", :bacino_cfd "Palmas"}
        {:id "L21", :tipo "MEZZO", :settore "PALMAS", :valle "F64", :bacino_cfd "Palmas"}
        {:id "F0", :tipo "BASE", :settore "POSADA", :valle nil, :bacino_cfd "Posada"}
        {:id "F55", :tipo "MONTE", :settore "POSADA", :valle "L2", :bacino_cfd "Posada"}
        {:id "L2", :tipo "MEZZO", :settore "POSADA", :valle "F0", :bacino_cfd "Posada"}
        {:id "F24", :tipo "BASE", :settore "TEMO", :valle nil, :bacino_cfd "Temo"}
        {:id "F33", :tipo nil, :settore "TEMO", :valle "F56", :bacino_cfd "Temo"}
        {:id "F56", :tipo nil, :settore "TEMO", :valle "L9", :bacino_cfd "Temo"}
        {:id "F57", :tipo nil, :settore "TEMO", :valle "L9", :bacino_cfd "Temo"}
        {:id "L28", :tipo nil, :settore "TEMO", :valle "L9", :bacino_cfd "Temo"}
        {:id "L9", :tipo nil, :settore "TEMO", :valle "F24", :bacino_cfd "Temo"}
        {:id "F05", :tipo "BASE", :settore "TIRSO", :valle nil, :bacino_cfd "Tirso"}
        {:id "F23", :tipo nil, :settore "TIRSO", :valle "L0", :bacino_cfd "Tirso"}
        {:id "F35", :tipo nil, :settore "TIRSO", :valle "L5", :bacino_cfd "Tirso"}
        {:id "F41", :tipo nil, :settore "TIRSO", :valle "L29", :bacino_cfd "Tirso"}
        {:id "F43", :tipo nil, :settore "TIRSO", :valle "F35", :bacino_cfd "Tirso"}
        {:id "L0", :tipo nil, :settore "TIRSO", :valle "F05", :bacino_cfd "Tirso"}
        {:id "L27", :tipo nil, :settore "TIRSO", :valle "F41", :bacino_cfd "Tirso"}
        {:id "L29", :tipo nil, :settore "TIRSO", :valle "L5", :bacino_cfd "Tirso"}
        {:id "L3", :tipo nil, :settore "TIRSO", :valle "L29", :bacino_cfd "Tirso"}
        {:id "L5", :tipo nil, :settore "TIRSO", :valle "F23", :bacino_cfd "Tirso"}))
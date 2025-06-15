package it.unibo.data;

public class Queries {

    // Clienti
    public static final String FIND_CLIENTE = """
        SELECT codice_cliente, nome, cognome, email, telefono, data_nascita, username
        FROM clienti
        WHERE codice_cliente = ?
        """;

    public static final String LIST_CLIENTI = """
        SELECT codice_cliente, nome, cognome, email, telefono, data_nascita, username
        FROM clienti
        ORDER BY cognome, nome
        """;

    public static final String INSERT_CLIENTE = """
        INSERT INTO clienti (codice_cliente, nome, cognome, email, telefono, data_nascita, username)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    // Ristoranti
    public static final String FIND_RISTORANTE = """
        SELECT piva, nome, indirizzo, orario, codice_zona
        FROM ristoranti
        WHERE piva = ?
        """;

    public static final String RISTORANTI_BY_ZONA = """
        SELECT r.piva, r.nome, r.indirizzo, r.orario, r.codice_zona
        FROM ristoranti r
        WHERE r.codice_zona = ?
        ORDER BY r.nome
        """;

     public static final String INSERT_RISTORANTE = """
        INSERT INTO ristoranti (piva, nome, indirizzo, orario, codice_zona)
        VALUES (?, ?, ?, ?, ?)
        """;


    // Piatti
    public static final String PIATTI_BY_RISTORANTE = """
        SELECT p.codice_piatto, p.nome, p.prezzo, p.descrizione
        FROM piatti p
        JOIN offre o ON p.codice_piatto = o.codice_piatto
        WHERE o.piva = ?
        ORDER BY p.nome
        """;

    public static final String UPDATE_PIATTO = """
    UPDATE piatti
    SET nome = ?, prezzo = ?, descrizione = ?
    WHERE codice_piatto = ?
    """;

    // Ordini
    public static final String FIND_ORDINE = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, 
               o.prezzo_totale, o.piva
        FROM ordini o
        WHERE o.codice_ordine = ?
        """;

    public static final String ORDINI_BY_CLIENTE = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, 
               o.prezzo_totale, o.piva, p.data as data_pagamento
        FROM ordini o
        JOIN pagamenti p ON o.codice_pagamento = p.codice_pagamento
        WHERE p.codice_cliente = ?
        ORDER BY p.data DESC
        """;

    public static final String ORDINI_CON_PROMOZIONE = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.piva, a.sconto_applicato
        FROM ordini o
        JOIN applicazione a ON o.codice_ordine = a.codice_ordine
        ORDER BY o.codice_ordine;
        """;
    public static final String ORDINI_BY_RISTORANTE = """
        SELECT codice_ordine, codice_pagamento, codice_stato, prezzo_totale, piva
        FROM ordini
        WHERE piva = ?
        ORDER BY codice_ordine
        """;


    // Dettagli ordini
    public static final String DETTAGLI_ORDINE = """
        SELECT do.codice_piatto, do.numero_linea, do.quantita, do.prezzo_unitario,
               p.nome as nome_piatto, p.descrizione as descrizione_piatto
        FROM dettagli_ordini do
        JOIN piatti p ON do.codice_piatto = p.codice_piatto
        WHERE do.codice_ordine = ?
        ORDER BY do.numero_linea
        """;

    // Zone geografiche
    public static final String LIST_ZONE = """
        SELECT codice_zona, nome
        FROM zone_geografiche
        ORDER BY nome
        """;
    
    public static final String RIDER_BY_ZONA = """
        SELECT codice_rider, nome, cognome
        FROM rider
        WHERE codice_zona = ?
        """;

    // Recensioni
    public static final String RECENSIONI_BY_RISTORANTE = """
        SELECT r.codice_cliente, r.numero_stelle, r.descrizione, r.titolo, r.data,
               c.nome, c.cognome
        FROM recensioni r
        JOIN clienti c ON r.codice_cliente = c.codice_cliente
        WHERE r.piva = ?
        ORDER BY r.data DESC
        """;

          public static final String INSERT_RECENSIONE = """
        INSERT INTO recensioni (codice_cliente, piva, numero_stelle, descrizione, titolo, data)
        VALUES (?, ?, ?, ?, ?, NOW())
        """;

    // Promozioni
    public static final String PROMOZIONI_ATTIVE = """
        SELECT * FROM promozioni
        WHERE piva = ? AND CURDATE() BETWEEN data_inizio AND data_fine
        """;

    public static final String APPLICA_PROMOZIONE = """
        UPDATE ordini
        SET sconto_applicato = ?, codice_promozione = ?
        WHERE codice_ordine = ?
        """;

    // Raccolta Punti
    public static final String USA_PUNTI = """
        INSERT INTO utilizza_punti (codice_raccolta, codice_ordine, punti_usati, sconto_applicato)
        VALUES (?, ?, ?, ?)
        """;

    public static final String AGGIORNA_PUNTI = """
        UPDATE raccolte_punti
        SET punti_totali = punti_totali - ?
        WHERE codice_cliente = ?
        """;

    public static final String PUNTI_GENERATI_BY_ORDINE = """
        SELECT punti_generati
        FROM genera_punti
        WHERE codice_ordine = ?
        """;

    public static final String TOP10_RISTORANTI = """
        SELECT piva, AVG(numero_stelle) AS media
        FROM recensioni
        GROUP BY piva
        ORDER BY media DESC
        LIMIT 10
        """;

    public static final String RIDER_TOP_CONSEGNE = """
        SELECT codice_rider, COUNT(*) AS totale
        FROM stati_ordini
        WHERE consegnato IS NOT NULL
        GROUP BY codice_rider
        ORDER BY totale DESC
        LIMIT 1
        """;
    public static final String CARTE_BY_CLIENTE = """
        SELECT numero, titolare, data_scadenza
        FROM carte
        WHERE codice_cliente = ?
        ORDER BY numero
        """;

    public static final String ORDINI_BY_CARTA = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.piva
        FROM ordini o
        JOIN pagamenti p ON o.codice_pagamento = p.codice_pagamento
        WHERE p.codice_cliente = ? AND p.id_carta = ?
        ORDER BY o.codice_ordine
        """;

    public static final String MOSTRA_CONTRATTO= """
        SELECT cod_contratto, codice_rider, paga_oraria, testo
        FROM contratti
        WHERE codice_rider = ?
        """;

    public static final String INDIRIZZI_BY_CLIENTE = """
        SELECT i.codice_indirizzo, i.via, i.numero_civico, i.cap, i.interno, i.scala, i.codice_zona
        FROM indirizzi i
        JOIN residenza r ON i.codice_indirizzo = r.codice_indirizzo
        WHERE r.codice_cliente = ?
        ORDER BY i.codice_indirizzo
        """;

    public static final String ZONA_BY_INDIRIZZO = """
        SELECT z.codice_zona, z.nome
        FROM zone_geografiche z
        JOIN indirizzi i ON z.codice_zona = i.codice_zona
        WHERE i.codice_indirizzo = ?
        """;

        public static final String METODI_PAGAMENTO_BY_CLIENTE = """
        SELECT nome
        FROM metodi_pagamento
        WHERE codice_cliente = ?
        ORDER BY nome
        """;
      public static final String MEZZI_BY_RIDER = """
        SELECT codice_mezzo, tipo, targa, modello
        FROM mezzi
        WHERE id_rider = ?
        ORDER BY codice_mezzo
        """;

}

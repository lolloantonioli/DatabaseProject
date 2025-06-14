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

// già presente
public static final String INSERT_CLIENTE = """
    INSERT INTO clienti (codice_cliente, nome, cognome, email, telefono, data_nascita, username)
    VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

// Inserimento metodo di pagamento
public static final String INSERT_METODO_PAGAMENTO = """
    INSERT INTO metodi_pagamento (codice_cliente, nome)
    VALUES (?, ?)
    """;

// Inserimento contratto
public static final String INSERT_CONTRATTO = """
    INSERT INTO contratti (codice_rider, paga_oraria, testo)
    VALUES (?, ?, ?)
    """;

// Inserimento carta
public static final String INSERT_CARTA = """
    INSERT INTO carte (codice_cliente, nome, numero, titolare, data_scadenza, cvv)
    VALUES (?, ?, ?, ?, ?, ?)
    """;
// Inserimento di un nuovo mezzo
public static final String INSERT_MEZZO = """
    INSERT INTO mezzi (codice_rider, codice_mezzo, tipo, targa, modello)
    VALUES (?, ?, ?, ?, ?)
    """;

    //VAFFANCULO

// Inserisce un indirizzo
public static final String INSERT_INDIRIZZO = """
    INSERT INTO indirizzi (via, numero_civico, cap, interno, scala, codice_zona)
    VALUES (?, ?, ?, ?, ?, ?)
    """;
public static final String INSERT_PROMOZIONE = """
    INSERT INTO promozioni (piva, data_inizio, data_fine, nome, descrizione, percentuale_sconto)
    VALUES (?, ?, ?, ?, ?, ?)
    """;

public static final String SELECT_PROMOZIONI_ATTIVE_BY_RISTORANTE = """
    SELECT data_inizio, data_fine, nome, descrizione, percentuale_sconto
    FROM promozioni
    WHERE piva = ? AND CURRENT_DATE BETWEEN data_inizio AND data_fine
    ORDER BY data_inizio
    """;

public static final String SELECT_RACCOLTA_BY_CLIENTE = """
    SELECT punti_totali, soglia_punti, percentuale_sconto
    FROM raccolte_punti
    WHERE codice_cliente = ?
    """;

// Associa un indirizzo a un cliente
public static final String INSERT_RESIDENZA = """
    INSERT INTO residenza (codice_cliente, codice_indirizzo)
    VALUES (?, ?)
    """;

// Inizializza la raccolta punti per un nuovo cliente
public static final String INSERT_RACCOLTA_PUNTI = """
    INSERT INTO raccolte_punti (codice_cliente, punti_totali, soglia_punti, percentuale_sconto)
    VALUES (?, 0, ?, ?)
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


// Inserisce un nuovo piatto
public static final String INSERT_PIATTO = """
    INSERT INTO piatti (codice_piatto, nome, prezzo, descrizione)
    VALUES (?, ?, ?, ?)
    """;

// Associa il piatto appena creato al ristorante
public static final String INSERT_OFFRE = """
    INSERT INTO offre (piva, codice_piatto)
    VALUES (?, ?)
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

    // Ordini che contengono un dato piatto
    public static final String ORDINI_BY_PIATTO = """
        SELECT DISTINCT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.piva
        FROM ordini o
        JOIN dettagli_ordini d ON o.codice_ordine = d.codice_ordine
        WHERE d.codice_piatto = ?
        ORDER BY o.codice_ordine
        """;

    // Classifica dei 10 piatti più ordinati in assoluto
    public static final String TOP10_PIATTI = """
        SELECT d.codice_piatto, p.nome, SUM(d.quantita) AS totale_piatti
        FROM dettagli_ordini d
        JOIN piatti p ON d.codice_piatto = p.codice_piatto
        GROUP BY d.codice_piatto, p.nome
        ORDER BY totale_piatti DESC
        LIMIT 10
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
    
    // Ordini che un rider deve consegnare
    public static final String ORDINI_DA_CONSEGNARE_BY_RIDER = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.piva
        FROM ordini o
        JOIN stati_ordini s ON o.codice_stato = s.codice_stato
        WHERE s.codice_rider = ? AND s.consegnato = FALSE
        ORDER BY o.codice_ordine
        """;

    // Login cliente: verifica esistenza
    public static final String FIND_CLIENTE_BY_ID = """
        SELECT codice_cliente, nome, cognome, email, telefono, data_nascita, username
        FROM clienti
        WHERE codice_cliente = ?
        """;

    // Lista ristoranti per zona
    public static final String LIST_RISTORANTI = """
        SELECT piva, nome, indirizzo, orario
        FROM ristoranti
        ORDER BY nome
        """;

    // Piatti per ristorante
    // già presente: PIATTI_BY_RISTORANTE

    // Inserimento pagamento
    public static final String INSERT_PAGAMENTO = """
        INSERT INTO pagamenti (codice_cliente, metodo, data, importo)
        VALUES (?, ?, CURRENT_TIMESTAMP, ?)
        """;

    // Inserimento ordine
    public static final String INSERT_ORDINE = """
        INSERT INTO ordini (codice_pagamento, codice_stato, prezzo_totale, piva)
        VALUES (?, ?, ?, ?)
        """;

    // Inserimento dettaglio ordine
    public static final String INSERT_DETTAGLIO_ORDINE = """
        INSERT INTO dettagli_ordini (codice_ordine, codice_piatto, numero_linea, quantita, prezzo_unitario)
        VALUES (?, ?, ?, ?, ?)
        """;


    public static final String PAGAMENTI_BY_CLIENTE = """
        SELECT codice_pagamento, data, importo, metodo
        FROM pagamenti
        WHERE codice_cliente = ?
        ORDER BY data DESC
        """;


        // Ordini già consegnati da un rider
    public static final String ORDINI_CONSEGNATI_BY_RIDER = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.piva
        FROM ordini o
        JOIN stati_ordini s ON o.codice_stato = s.codice_stato
        WHERE s.codice_rider = ? AND s.consegnato = TRUE
        ORDER BY o.codice_ordine
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

    // Ordini con promozione applicata

public static final String SELECT_RECENSIONI_BY_CLIENTE = """
    SELECT codice_cliente, piva, numero_stelle, descrizione, titolo, data
    FROM recensioni
    WHERE codice_cliente = ?
    ORDER BY data DESC
    """;
//DFVJHDFIOHVBOUIDF
public static final String SELECT_RECENSIONI_BY_RISTORANTE = """
    SELECT codice_cliente, piva, numero_stelle, descrizione, titolo, data
    FROM recensioni
    WHERE piva = ?
    ORDER BY numero_stelle DESC, data DESC
    """;

public static final String RESIDENZA_BY_CLIENTE = """
    SELECT codice_cliente, codice_indirizzo
    FROM residenza
    WHERE codice_cliente = ?
    """;
//vbjkjkbvjckxbv
//dbfisdbvuidfsviufubfdiuvb
// Inserisce un nuovo rider
public static final String INSERT_RIDER = """
    INSERT INTO riders
        (codice_rider, nome, cognome, data_nascita, email, telefono,
         iban, cf, patente, disponibile, codice_zona)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

// Trova un rider per ID
public static final String FIND_RIDER = """
    SELECT codice_rider, nome, cognome, data_nascita, email, telefono,
           iban, cf, patente, disponibile, codice_zona
    FROM riders
    WHERE codice_rider = ?
    """;

// Riders per zona
public static final String RIDERS_BY_ZONA = """
    SELECT codice_rider, nome, cognome, data_nascita, email, telefono,
           iban, cf, patente, disponibile, codice_zona
    FROM riders
    WHERE codice_zona = ?
    """;

// Riders disponibili per zona
public static final String AVAILABLE_RIDERS_BY_ZONA = """
    SELECT codice_rider, nome, cognome, data_nascita, email, telefono,
           iban, cf, patente, disponibile, codice_zona
    FROM riders
    WHERE codice_zona = ? AND disponibile = TRUE
    """;

public static final String INSERT_STATO_ORDINE = """
    INSERT INTO stati_ordini
        (codice_ordine, data, in_preparazione, in_consegna, consegnato, codice_rider)
    VALUES (?, ?, ?, ?, ?, ?)
    """;

public static final String UPDATE_STATO_ORDINE = """
    UPDATE stati_ordini
    SET in_preparazione = ?, in_consegna = ?, consegnato = ?
    WHERE codice_ordine = ? AND codice_rider = ?
    """;

public static final String SELECT_STATO_BY_ORDINE = """
    SELECT codice_ordine, data, in_preparazione, in_consegna, consegnato, codice_rider
    FROM stati_ordini
    WHERE codice_ordine = ?
    ORDER BY data DESC
    LIMIT 1
    """;

// Ordini in cui sono stati utilizzati punti
public static final String ORDINI_WITH_POINTS = """
    SELECT DISTINCT codice_ordine
    FROM utilizza_punti
    """;
public static final String VISUALIZZAZIONI_BY_CLIENTE_ON_DATE = """
    SELECT codice_cliente, piva
    FROM visualizzazioni
    WHERE codice_cliente = ? AND DATE(data) = ?
    """;

public static final String COUNT_VISUALIZZAZIONI_BY_RISTORANTE = """
    SELECT COUNT(*) AS total
    FROM visualizzazioni
    WHERE piva = ?
    """;
public static final String INSERT_ZONA = """
    INSERT INTO zone_geografiche (codice_zona, nome)
    VALUES (?, ?)
    """;

}

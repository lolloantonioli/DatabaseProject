package it.unibo.data;

public class Queries {

    // Clienti
    public static final String FIND_CLIENTE = """
        SELECT codice_cliente, nome, cognome, e_mail, telefono, data_di_nascita, username
        FROM clienti
        WHERE codice_cliente = ?
        """;

    public static final String LIST_CLIENTI = """
        SELECT codice_cliente, nome, cognome, e_mail, telefono, data_di_nascita, username
        FROM clienti
        ORDER BY cognome, nome
        """;
public static final String UPDATE_CLIENTE = """
    UPDATE clienti
    SET nome = ?, 
        cognome = ?, 
        e_mail = ?, 
        telefono = ?, 
        data_di_nascita = ?, 
        username = ?
    WHERE codice_cliente = ?
    """;

public static final String DELETE_CLIENTE = """
    DELETE FROM clienti
    WHERE codice_cliente = ?
    """;

public static final String TOP_RIDER_PER_CONSEGNE_PERIODO = """
        SELECT R.Codice_Rider, R.Nome AS NomeRider, R.Cognome AS CognomeRider, COUNT(*) AS ConsegneCompletate
        FROM STATI_ORDINI S
        JOIN RIDER R ON S.Codice_Rider = R.Codice_Rider
        WHERE S.Consegnato = TRUE
        AND S.Ora_Consegnato BETWEEN ? AND ?
        GROUP BY R.Codice_Rider, R.Nome, R.Cognome
        ORDER BY ConsegneCompletate DESC;
        """;

public static final String RISTORANTI_SPESA_MEDIA = """
        SELECT P_IVA AS Ristorante, AVG(Prezzo_Totale) AS SpesaMedia
        FROM ORDINI
        GROUP BY P_IVA
        HAVING AVG(Prezzo_Totale) > 30;
        """;

public static final String DELETE_RECENSIONE = """
    DELETE FROM recensioni
    WHERE codice_cliente = ? AND p_iva = ? AND titolo = ?
""";

// Rimuove una promozione identificata da piva, data inizio, data fine
public static final String DELETE_PROMOZIONE = 
    "DELETE FROM Promozioni WHERE P_IVA = ? AND Data_Inizio = ? AND Data_Fine = ?";

public static final String DELETE_OFFRE = 
    "DELETE FROM Offre WHERE Codice_Piatto = ?";

public static final String DELETE_PIATTO =
    "DELETE FROM Piatti WHERE Codice_Piatto = ?";


public static final String DELETE_CARTA = """
    DELETE FROM carte
    WHERE codice_cliente = ? AND nome = ? AND numero = ?
""";

public static final String DELETE_METODO_PAGAMENTO = """
    DELETE FROM metodi_pagamento
    WHERE codice_cliente = ? AND nome = ?
""";

public static final String DELETE_RESIDENZA = """
    DELETE FROM residenza
    WHERE codice_cliente = ? AND codice_indirizzo = ?
""";

public static final String DELETE_INDIRIZZO = """
    DELETE FROM indirizzi
    WHERE codice_indirizzo = ?
""";


// già presente
public static final String INSERT_CLIENTE = """
    INSERT INTO clienti (nome, cognome, e_mail, telefono, data_di_nascita, username)
    VALUES (?, ?, ?, ?, ?, ?)
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

public static final String DELETE_MEZZO = """
    DELETE FROM Mezzi WHERE Codice_Rider = ? AND Codice_Mezzo = ?
""";

public static final String ORDINI_PREPARAZIONE_ZONA =
    "SELECT o.Codice_Ordine, o.P_IVA, o.Prezzo_Totale, s.Data " +
    "FROM ORDINI o " +
    "JOIN STATI_ORDINI s ON o.Codice_Ordine = s.Codice_Ordine " +
    "JOIN RISTORANTI r ON o.P_IVA = r.P_IVA " +
    "WHERE s.In_Preparazione = TRUE AND r.Codice_Zona = ?";

public static final String PRENDI_IN_CARICO_ORDINE =
    "UPDATE STATI_ORDINI SET In_Preparazione = FALSE, In_Consegna = TRUE, Ora_In_Consegna = NOW(), Codice_Rider = ? WHERE Codice_Ordine = ?";

public static final String ORDINE_IN_CARICO_RIDER =
    "SELECT o.Codice_Ordine, o.P_IVA, o.Prezzo_Totale, s.Data " +
    "FROM ORDINI o " +
    "JOIN STATI_ORDINI s ON o.Codice_Ordine = s.Codice_Ordine " +
    "WHERE s.In_Consegna = TRUE AND s.Codice_Rider = ?";

public static final String CONSEGNA_ORDINE =
    "UPDATE STATI_ORDINI SET In_Consegna = FALSE, Consegnato = TRUE, Ora_Consegnato = NOW() WHERE Codice_Ordine = ? AND Codice_Rider = ?";

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

public static final String FIND_PIVA_BY_NOME = """
        SELECT p_iva
        FROM ristoranti
        WHERE nome = ?;

""";

// Inserisce un indirizzo
public static final String INSERT_INDIRIZZO = """
    INSERT INTO indirizzi (via, numero_civico, cap, interno, scala, codice_zona)
    VALUES (?, ?, ?, ?, ?, ?)
    """;
public static final String INSERT_PROMOZIONE = """
    INSERT INTO promozioni (p_iva, data_inizio, data_fine, nome, descrizione, percentuale_sconto)
    VALUES (?, ?, ?, ?, ?, ?)
    """;

public static final String SELECT_PROMOZIONI_ATTIVE_BY_RISTORANTE = """
    SELECT data_inizio, data_fine, nome, descrizione, percentuale_sconto
    FROM promozioni
    WHERE p_iva = ? AND CURRENT_DATE BETWEEN data_inizio AND data_fine
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
    SELECT p_iva, nome, indirizzo, orario, codice_zona
    FROM ristoranti
    WHERE p_iva = ?
    """;

public static final String RISTORANTI_BY_ZONA = """
    SELECT r.p_iva, r.nome, r.indirizzo, r.orario, r.codice_zona
    FROM ristoranti r
    WHERE r.codice_zona = ?
    ORDER BY r.nome
    """;

public static final String INSERT_RISTORANTE = """
    INSERT INTO ristoranti (p_iva, nome, indirizzo, orario, codice_zona)
    VALUES (?, ?, ?, ?, ?)
    """;

public static final String ALL_RISTORANTI = """
        SELECT P_IVA, Nome, Indirizzo, Orario, Codice_Zona FROM RISTORANTI;
        """;

// Inserisce un nuovo piatto
public static final String INSERT_PIATTO = """
    INSERT INTO piatti (nome, prezzo, descrizione)
    VALUES (?, ?, ?)
    """;

// Associa il piatto appena creato al ristorante
public static final String INSERT_OFFRE = """
    INSERT INTO offre (p_iva, codice_piatto)
    VALUES (?, ?)
    """;

    // Piatti
    public static final String PIATTI_BY_RISTORANTE = """
        SELECT p.codice_piatto, p.nome, p.prezzo, p.descrizione
        FROM piatti p
        JOIN offre o ON p.codice_piatto = o.codice_piatto
        WHERE o.p_iva = ?
        ORDER BY p.nome
        """;

    public static final String UPDATE_PIATTO = """
    UPDATE piatti
    SET nome = ?, prezzo = ?, descrizione = ?
    WHERE codice_piatto = ?
    """;

    // Ordini che contengono un dato piatto
    public static final String ORDINI_BY_PIATTO = """
        SELECT DISTINCT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.p_iva
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
               o.prezzo_totale, o.p_iva
        FROM ordini o
        WHERE o.codice_ordine = ?
        """;

    public static final String ORDINI_BY_CLIENTE = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, 
               o.prezzo_totale, o.p_iva, p.data as data_pagamento
        FROM ordini o
        JOIN pagamenti p ON o.codice_pagamento = p.codice_pagamento
        WHERE p.codice_cliente = ?
        ORDER BY p.data DESC
        """;

    public static final String ORDINI_CON_PROMOZIONE = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.p_iva, a.sconto_applicato
        FROM ordini o
        JOIN applicazione a ON o.codice_ordine = a.codice_ordine
        ORDER BY o.codice_ordine;
        """;

    public static final String ORDINI_BY_RISTORANTE = """
        SELECT codice_ordine, codice_pagamento, codice_stato, prezzo_totale, p_iva
        FROM ordini
        WHERE p_iva = ?
        ORDER BY codice_ordine
        """;
    
    // Ordini che un rider deve consegnare
    public static final String ORDINI_DA_CONSEGNARE_BY_RIDER = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.p_iva
        FROM ordini o
        JOIN stati_ordini s ON o.codice_stato = s.codice_stato
        WHERE s.codice_rider = ? AND s.consegnato = FALSE
        ORDER BY o.codice_ordine
        """;

    // Login cliente: verifica esistenza
    public static final String FIND_CLIENTE_BY_ID = """
        SELECT codice_cliente, nome, cognome, e_mail, telefono, data_di_nascita, username
        FROM clienti
        WHERE codice_cliente = ?
        """;
    
    public static final String FIND_CLIENTE_BY_USERNAME = """
        SELECT codice_cliente, nome, cognome, e_mail, telefono, data_di_nascita, username
        FROM clienti
        WHERE username = ?
        """;

    // Lista ristoranti per zona
    public static final String LIST_RISTORANTI = """
        SELECT p_iva, nome, indirizzo, orario
        FROM ristoranti
        ORDER BY nome
        """;

    // Piatti per ristorante
    // già presente: PIATTI_BY_RISTORANTE

    // Inserimento pagamento
    public static final String INSERT_PAGAMENTO = """
        INSERT INTO pagamenti (codice_cliente, data, importo)
        VALUES (?, CURRENT_TIMESTAMP, ?)
        """;

    // Inserimento ordine
    public static final String INSERT_ORDINE = """
        INSERT INTO ordini (codice_pagamento, codice_stato, prezzo_totale, p_iva)
        VALUES (?, ?, ?, ?)
        """;

    // Inserimento dettaglio ordine
    public static final String INSERT_DETTAGLIO_ORDINE = """
        INSERT INTO dettagli_ordini (codice_ordine, codice_piatto, numero_linea, quantita, prezzo_unitario)
        VALUES (?, ?, ?, ?, ?)
        """;


    public static final String PAGAMENTI_BY_CLIENTE = """
        SELECT codice_pagamento, data, importo, nome
        FROM pagamenti
        WHERE codice_cliente = ?
        ORDER BY data DESC
        """;


        // Ordini già consegnati da un rider
    public static final String ORDINI_CONSEGNATI_BY_RIDER = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.p_iva
        FROM ordini o
        JOIN stati_ordini s ON o.codice_ordine = s.codice_ordine
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
        WHERE r.p_iva = ?
        ORDER BY r.data DESC
        """;

          public static final String INSERT_RECENSIONE = """
        INSERT INTO recensioni (codice_cliente, p_iva, numero_stelle, descrizione, titolo, data)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    // Promozioni
    public static final String PROMOZIONI_ATTIVE = """
        SELECT * FROM promozioni
        WHERE p_iva = ? AND CURDATE() BETWEEN data_inizio AND data_fine
        """;

    public static final String APPLICA_PROMOZIONE = """
        UPDATE ordini
        SET sconto_applicato = ?, codice_promozione = ?
        WHERE codice_ordine = ?
        """;

    // Raccolta Punti
    public static final String USA_PUNTI = """
        INSERT INTO utilizza_punti (codice_ordine, codice_cliente, punti_usati, sconto_applicato)
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
        SELECT p_iva, AVG(numero_stelle) AS media
        FROM recensioni
        GROUP BY p_iva
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
        SELECT nome, numero, titolare, data_scadenza, cvv
    FROM carte
    WHERE codice_cliente = ?
    ORDER BY numero
        """;

    public static final String ORDINI_BY_CARTA = """
        SELECT o.codice_ordine, o.codice_pagamento, o.codice_stato, o.prezzo_totale, o.p_iva
        FROM ordini o
        JOIN pagamenti p ON o.codice_pagamento = p.codice_pagamento
        WHERE p.codice_cliente = ? AND p.nome = ?
        ORDER BY o.codice_ordine;

        """;

    public static final String MOSTRA_CONTRATTO= """
        SELECT codice_rider, paga_oraria, testo
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
        WHERE codice_rider = ?
        ORDER BY codice_mezzo
        """;

    // Ordini con promozione applicata

public static final String SELECT_RECENSIONI_BY_CLIENTE = """
    SELECT codice_cliente, p_iva, numero_stelle, descrizione, titolo, data
    FROM recensioni
    WHERE codice_cliente = ?
    ORDER BY data DESC
    """;
//DFVJHDFIOHVBOUIDF
public static final String SELECT_RECENSIONI_BY_RISTORANTE = """
    SELECT codice_cliente, p_iva, numero_stelle, descrizione, titolo, data
    FROM recensioni
    WHERE p_iva = ?
    ORDER BY numero_stelle DESC, data DESC
    """;

public static final String RESIDENZA_BY_CLIENTE = """
    SELECT codice_cliente, codice_indirizzo
    FROM residenza
    WHERE codice_cliente = ?
    """;

// Inserisce un nuovo rider
public static final String INSERT_RIDER = """
    INSERT INTO rider
        (nome, cognome, data_di_nascita, e_mail, telefono,
         iban, codice_fiscale, patente, disponibile, codice_zona)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

// Trova un rider per ID
public static final String FIND_RIDER = """
    SELECT codice_rider, nome, cognome, data_di_nascita, e_mail, telefono,
           iban, codice_fiscale, patente, disponibile, codice_zona
    FROM rider
    WHERE codice_rider = ?
    """;

public static final String FIND_RIDER_BY_EMAIL = """
    SELECT codice_rider, nome, cognome, data_di_nascita, e_mail, telefono,
           iban, codice_fiscale, patente, disponibile, codice_zona
    FROM rider
    WHERE e_mail = ? 
    """;

// Riders per zona
public static final String RIDERS_BY_ZONA = """
    SELECT codice_rider, nome, cognome, data_di_nascita, e_mail, telefono,
           iban, codice_fiscale, patente, disponibile, codice_zona
    FROM rider
    WHERE codice_zona = ?
    """;

// Riders disponibili per zona
public static final String AVAILABLE_RIDERS_BY_ZONA = """
    SELECT codice_rider, nome, cognome, data_di_nascita, e_mail, telefono,
           iban, codice_fiscale, patente, disponibile, codice_zona
    FROM rider
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
    SELECT codice_cliente, p_iva
    FROM visualizzazioni
    WHERE codice_cliente = ? AND DATE(data) = ?
    """;

public static final String COUNT_VISUALIZZAZIONI_BY_RISTORANTE = """
    SELECT COUNT(*) AS total
    FROM visualizzazioni
    WHERE p_iva = ?
    """;
public static final String INSERT_ZONA = """
    INSERT INTO zone_geografiche (codice_zona, nome)
    VALUES (?, ?)
    """;

}

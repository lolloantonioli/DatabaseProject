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

    // Piatti
    public static final String PIATTI_BY_RISTORANTE = """
        SELECT p.codice_piatto, p.nome, p.prezzo, p.descrizione
        FROM piatti p
        JOIN offre o ON p.codice_piatto = o.codice_piatto
        WHERE o.piva = ?
        ORDER BY p.nome
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

    // Recensioni
    public static final String RECENSIONI_BY_RISTORANTE = """
        SELECT r.codice_cliente, r.numero_stelle, r.descrizione, r.titolo, r.data,
               c.nome, c.cognome
        FROM recensioni r
        JOIN clienti c ON r.codice_cliente = c.codice_cliente
        WHERE r.piva = ?
        ORDER BY r.data DESC
        """;

}

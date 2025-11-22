package it.wiesner.mcp.codingguidelines.model;

/**
 * Modellklasse für eine Coding Guideline einer Programmiersprache.
 * 
 * Diese Klasse repräsentiert die Coding-Richtlinien für eine bestimmte
 * Programmiersprache. Die Richtlinien werden aus Markdown-Dateien im
 * resources-Verzeichnis geladen.
 */
public class CodingGuideline {
    /** Name der Programmiersprache (z.B. "java", "python") */
    private String language;
    
    /** Inhalt der Coding-Richtlinien als String (Markdown-Format) */
    private String rules;
    
    /** Dateimuster für Guideline-Dateien im resources-Verzeichnis */
    private static final String FILE_PATTERN = "coding_guidelines_$$LANGUAGE$$.md";

    /**
     * Konstruktor für eine neue CodingGuideline.
     * 
     * Erstellt ein neues CodingGuideline-Objekt und lädt automatisch die
     * entsprechenden Richtlinien aus der zugehörigen Markdown-Datei.
     * 
     * @param language Name der Programmiersprache
     * @throws RuntimeException wenn das Laden der Guideline-Datei fehlschlägt
     */
    public CodingGuideline(String language) {
        this.language = language;
        loadCodingGuideline();
    }

    /**
     * Gibt den Namen der Programmiersprache zurück.
     * 
     * @return Name der Programmiersprache
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Gibt den Inhalt der Coding-Richtlinien zurück.
     * 
     * @return Richtlinien als String im Markdown-Format
     */
    public String getRules() {
        return rules;
    }

    /**
     * Lädt die Coding Guideline aus der entsprechenden Ressourcen-Datei.
     * 
     * Diese private Methode wird automatisch beim Erstellen des Objekts aufgerufen.
     * Sie ersetzt den Platzhalter im FILE_PATTERN durch die Programmiersprache,
     * öffnet die Datei aus dem Classpath und liest den Inhalt in das rules-Feld.
     * 
     * @throws IllegalArgumentException wenn die Ressourcen-Datei nicht gefunden wird
     * @throws RuntimeException wenn beim Lesen der Datei ein I/O-Fehler auftritt
     */
    private void loadCodingGuideline() {
        // Dateinamen durch Ersetzen des Platzhalters generieren
        String fileName = FILE_PATTERN.replace("$$LANGUAGE$$", language);
        
        // Try-with-resources: InputStream wird automatisch geschlossen
        try (java.io.InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            // Prüfen, ob die Datei gefunden wurde
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource file not found: " + fileName);
            }
            
            // Dateiinhalt als UTF-8 String einlesen
            rules = new String(inputStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (java.io.IOException e) {
            // I/O-Fehler in RuntimeException umwandeln
            throw new RuntimeException("Failed to load coding guideline from: " + fileName, e);
        }
    }

}

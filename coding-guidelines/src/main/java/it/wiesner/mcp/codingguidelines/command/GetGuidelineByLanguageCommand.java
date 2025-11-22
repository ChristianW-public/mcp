package it.wiesner.mcp.codingguidelines.command;

import it.wiesner.mcp.codingguidelines.model.CodingGuideline;

/**
 * Command-Implementierung zum Abrufen einer Coding Guideline nach Programmiersprache.
 * 
 * Diese Klasse implementiert das GuidelineCommand-Interface und kapselt die
 * Operation zum Abrufen einer spezifischen Coding Guideline basierend auf dem
 * Namen der Programmiersprache. Die Suche erfolgt case-insensitive, sodass
 * "Java", "java" und "JAVA" alle zum gleichen Ergebnis führen.
 */
public class GetGuidelineByLanguageCommand implements GuidelineCommand<CodingGuideline> {
    /** Name der gesuchten Programmiersprache */
    private final String language;

    /**
     * Konstruktor für das GetGuidelineByLanguageCommand.
     * 
     * @param language Name der Programmiersprache, für die die Guideline
     *                 abgerufen werden soll (z.B. "java", "python")
     */
    public GetGuidelineByLanguageCommand(String language) {
        this.language = language;
    }

    /**
     * Führt das Command aus und gibt die Coding Guideline für die angegebene Sprache zurück.
     * 
     * Diese Methode durchsucht alle verfügbaren Guidelines und filtert nach der
     * angegebenen Programmiersprache. Der Vergleich erfolgt case-insensitive.
     * Wenn keine passende Guideline gefunden wird, wird null zurückgegeben.
     * 
     * @return CodingGuideline für die angegebene Sprache, oder null wenn nicht gefunden
     */
    @Override
    public CodingGuideline execute() {
        // Stream über alle Guidelines erstellen
        return getAllCodingGuidelines().stream()
                // Nach Sprache filtern (case-insensitive)
                .filter(cg -> cg.getLanguage().equalsIgnoreCase(language))
                // Erstes Ergebnis nehmen
                .findFirst()
                // Oder null zurückgeben, falls nicht gefunden
                .orElse(null);
    }
}

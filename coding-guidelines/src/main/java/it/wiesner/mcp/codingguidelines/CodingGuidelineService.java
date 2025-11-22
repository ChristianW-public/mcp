package it.wiesner.mcp.codingguidelines;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import it.wiesner.mcp.codingguidelines.command.GetAllGuidelinesCommand;
import it.wiesner.mcp.codingguidelines.command.GetGuidelineByLanguageCommand;
import it.wiesner.mcp.codingguidelines.command.GuidelineCommand;
import it.wiesner.mcp.codingguidelines.model.CodingGuideline;

/**
 * Service zur Verwaltung von Coding Guidelines unter Verwendung des Command Patterns.
 * 
 * Dieser Service kapselt jede Operation als Command-Objekt, was die Wartbarkeit
 * und Erweiterbarkeit der Anwendung verbessert. Die Methoden sind mit @Tool
 * annotiert und können von externen Systemen über die Tool-Schnittstelle
 * aufgerufen werden.
 */
@Service
public class CodingGuidelineService {

    /**
     * Ruft alle verfügbaren Coding Guidelines ab.
     * 
     * Diese Methode erstellt ein GetAllGuidelinesCommand und führt es aus,
     * um eine Liste aller verfügbaren Coding Guidelines zurückzugeben.
     * Aktuell werden Guidelines für Java und Python unterstützt.
     * 
     * @return Liste aller CodingGuideline-Objekte
     */
    @Tool(name = "get_coding_guidelines", description = "Get a list of coding_guideline from the collection")
    public List<CodingGuideline> getCodingGuideline() {
        // Command-Objekt erstellen und ausführen
        GuidelineCommand<List<CodingGuideline>> command = new GetAllGuidelinesCommand();
        return command.execute();
    }

    /**
     * Ruft eine spezifische Coding Guideline für eine Programmiersprache ab.
     * 
     * Diese Methode erstellt ein GetGuidelineByLanguageCommand mit der
     * angegebenen Sprache und führt es aus. Die Suche erfolgt case-insensitive.
     * 
     * @param language Name der Programmiersprache (z.B. "java", "python")
     * @return CodingGuideline für die angegebene Sprache, oder null wenn nicht gefunden
     */
    @Tool(name = "get_coding_guideline", description = "Get a single coding_guideline from from the collection by programming language")
    public CodingGuideline getCodingGuideline(String language) {
        // Command-Objekt mit Sprach-Parameter erstellen und ausführen
        GuidelineCommand<CodingGuideline> command = new GetGuidelineByLanguageCommand(language);
        return command.execute();
    }

}

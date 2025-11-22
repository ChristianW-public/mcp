package it.wiesner.mcp.codingguidelines.command;

import java.util.ArrayList;
import java.util.List;

import it.wiesner.mcp.codingguidelines.model.CodingGuideline;

/**
 * Command-Interface für Guideline-Operationen (Command Pattern).
 * 
 * Dieses Interface definiert den Vertrag für alle Guideline-bezogenen Commands.
 * Durch die Verwendung des Command Patterns werden Operationen als Objekte
 * gekapselt, was Flexibilität und Erweiterbarkeit erhöht.
 *
 * @param <T> Der Rückgabetyp der Command-Ausführung
 */
public interface GuidelineCommand<T> {
    /**
     * Führt das Command aus und gibt das Ergebnis zurück.
     * 
     * Dies ist die zentrale Methode, die von allen Command-Implementierungen
     * überschrieben werden muss. Sie enthält die konkrete Geschäftslogik.
     *
     * @return Das Ergebnis der Command-Ausführung
     */
    T execute();

    /**
     * Standard-Methode zum Abrufen aller verfügbaren Coding Guidelines.
     * 
     * Diese Default-Methode stellt eine gemeinsame Implementierung bereit,
     * die von allen Commands genutzt werden kann. Sie erstellt CodingGuideline-
     * Objekte für jede unterstützte Programmiersprache.
     * 
     * Hinweis: Bei Erweiterung um neue Sprachen muss diese Methode angepasst werden.
     * 
     * @return Liste aller verfügbaren CodingGuideline-Objekte
     */
    default List<CodingGuideline> getAllCodingGuidelines() {
        // Neue Liste für alle Guidelines erstellen
        List<CodingGuideline> allCodingGuidelines = new ArrayList<>(); 
        
        // Unterstützte Programmiersprachen hinzufügen
        allCodingGuidelines.addAll(List.of(
                new CodingGuideline("java"),    // Java Coding Guidelines
                new CodingGuideline("python")   // Python Coding Guidelines
        ));

        return allCodingGuidelines;
    }

}

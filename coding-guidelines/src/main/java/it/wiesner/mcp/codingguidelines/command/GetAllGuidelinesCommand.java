package it.wiesner.mcp.codingguidelines.command;

import java.util.List;

import it.wiesner.mcp.codingguidelines.model.CodingGuideline;

/**
 * Command-Implementierung zum Abrufen aller Coding Guidelines.
 * 
 * Diese Klasse implementiert das GuidelineCommand-Interface und kapselt
 * die Operation zum Abrufen aller verfügbaren Coding Guidelines.
 * Sie nutzt die Default-Implementierung getAllCodingGuidelines() aus dem
 * Interface, um die Liste aller Guidelines zu erhalten.
 */
public class GetAllGuidelinesCommand implements GuidelineCommand<List<CodingGuideline>> {

    /**
     * Führt das Command aus und gibt alle verfügbaren Coding Guidelines zurück.
     * 
     * Diese Methode delegiert die Arbeit an die getAllCodingGuidelines()-Methode
     * aus dem GuidelineCommand-Interface, die eine Liste aller unterstützten
     * Programmiersprachen mit ihren jeweiligen Guidelines zurückgibt.
     * 
     * @return Liste aller CodingGuideline-Objekte
     */
    @Override
    public List<CodingGuideline> execute() {
        return getAllCodingGuidelines();
    }
}

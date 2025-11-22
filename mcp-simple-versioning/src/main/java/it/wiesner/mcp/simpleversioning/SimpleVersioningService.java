package it.wiesner.mcp.simpleversioning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

/**
 * Service für einfache Dateiversionierung.
 * Erstellt versionierte Snapshots von Dateien in C:\Temp mit aufsteigenden Revisionsnummern.
 * 
 * Funktionsweise:
 * - Jede Revision erhält ein eigenes Verzeichnis mit einer fortlaufenden Nummer
 * - Dateien werden mit ihrer relativen Pfadstruktur gespeichert
 * - Das System verwaltet automatisch die Revisionsnummern
 */
@Service
public class SimpleVersioningService {

    // Basisverzeichnis für alle Revisionen - jede Revision erhält ein eigenes Unterverzeichnis
    private static final String BASE_PATH = "C:\\Temp";

    /**
     * Erstellt eine neue Revision mit den übergebenen Dateien.
     * 
     * Ablauf:
     * 1. Ermittelt die nächste verfügbare Revisionsnummer
     * 2. Erstellt ein Revisionsverzeichnis (z.B. C:\Temp\5 für Revision 5)
     * 3. Speichert alle Dateien mit ihrer relativen Pfadstruktur in diesem Verzeichnis
     * 
     * @param files Map, bei der der Schlüssel der relative Dateipfad und der Wert der Dateiinhalt ist
     * @return Die erstellte Revisionsnummer
     * @throws IOException wenn ein I/O-Fehler auftritt
     */
    @Tool(name = "create_revision", description = "Creates a new revision with the provided files. Takes a map of relative file paths to file contents.")
    public int createRevision(Map<String, String> files) throws IOException {
        // Nächste verfügbare Revisionsnummer ermitteln
        int nextRevision = getNextRevisionNumber();
        Path revisionPath = Paths.get(BASE_PATH, String.valueOf(nextRevision));
        
        // Revisionsverzeichnis erstellen (z.B. C:\Temp\5)
        Files.createDirectories(revisionPath);
        
        // Alle Dateien in das Revisionsverzeichnis kopieren
        for (Map.Entry<String, String> entry : files.entrySet()) {
            String relativePath = entry.getKey();
            String content = entry.getValue();
            
            // Zieldatei mit vollständigem Pfad erstellen (z.B. C:\Temp\5\src\main.java)
            Path targetFile = revisionPath.resolve(relativePath);
            
            // Übergeordnete Verzeichnisse anlegen, falls sie nicht existieren
            // Wichtig für verschachtelte Pfade wie "src/main/java/MyClass.java"
            Files.createDirectories(targetFile.getParent());
            
            // Dateiinhalt schreiben - CREATE erstellt neue Datei, TRUNCATE_EXISTING überschreibt existierende
            Files.writeString(targetFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        
        return nextRevision;
    }

    /**
     * Ermittelt die nächste verfügbare Revisionsnummer durch Scannen vorhandener Revisionsverzeichnisse.
     * 
     * Algorithmus:
     * - Sucht nach allen Verzeichnissen im Basisverzeichnis
     * - Filtert nur Verzeichnisse mit rein numerischen Namen (z.B. "1", "2", "3")
     * - Findet die höchste existierende Nummer und gibt die nächste zurück
     * 
     * @return Die nächste Revisionsnummer (beginnt bei 1)
     * @throws IOException wenn ein I/O-Fehler auftritt
     */
    private int getNextRevisionNumber() throws IOException {
        Path basePath = Paths.get(BASE_PATH);
        
        // Sicherstellen, dass das Basisverzeichnis existiert
        // Falls nicht vorhanden, wird es erstellt und Revision 1 zurückgegeben
        if (!Files.exists(basePath)) {
            Files.createDirectories(basePath);
            return 1;
        }
        
        // Höchste existierende Revisionsnummer finden
        // Stream wird mit try-with-resources automatisch geschlossen
        try (Stream<Path> paths = Files.list(basePath)) {
            return paths
                .filter(Files::isDirectory)                  // Nur Verzeichnisse berücksichtigen
                .map(Path::getFileName)                      // Verzeichnisnamen extrahieren
                .map(Path::toString)                         // In String konvertieren
                .filter(name -> name.matches("\\d+"))        // Nur rein numerische Namen (z.B. "1", "42")
                .mapToInt(Integer::parseInt)                 // In Integer konvertieren
                .max()                                       // Höchste Zahl finden
                .orElse(0) + 1;                             // Falls keine Revision existiert: 0 + 1 = 1
        }
    }

    /**
     * Listet alle existierenden Revisionsnummern auf.
     * 
     * Rückgabe ist ein sortiertes Array aller gefundenen Revisionen.
     * Nützlich um zu sehen, welche Versionen verfügbar sind.
     * 
     * @return Array mit Revisionsnummern in aufsteigender Reihenfolge
     * @throws IOException wenn ein I/O-Fehler auftritt
     */
    @Tool(name = "list_revisions", description = "Lists all existing revision numbers")
    public int[] listRevisions() throws IOException {
        Path basePath = Paths.get(BASE_PATH);
        
        // Falls Basisverzeichnis nicht existiert, gibt es keine Revisionen
        if (!Files.exists(basePath)) {
            return new int[0];
        }
        
        // Alle Revisionsverzeichnisse finden und als sortiertes Array zurückgeben
        try (Stream<Path> paths = Files.list(basePath)) {
            return paths
                .filter(Files::isDirectory)                  // Nur Verzeichnisse
                .map(Path::getFileName)                      // Namen extrahieren
                .map(Path::toString)                         // In String umwandeln
                .filter(name -> name.matches("\\d+"))        // Nur numerische Namen
                .mapToInt(Integer::parseInt)                 // In Integer umwandeln
                .sorted()                                    // Aufsteigend sortieren
                .toArray();                                  // Als Array zurückgeben
        }
    }
}

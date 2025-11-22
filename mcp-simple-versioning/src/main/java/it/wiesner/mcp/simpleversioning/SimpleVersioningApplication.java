package it.wiesner.mcp.simpleversioning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hauptklasse der Simple Versioning Anwendung.
 * 
 * Diese Spring Boot Anwendung stellt einen MCP (Model Context Protocol) Server bereit,
 * der Tools für die Dateiversionierung zur Verfügung stellt.
 */
@SpringBootApplication
@ComponentScan(basePackages = "it.wiesner.mcp")
public class SimpleVersioningApplication {

	/**
	 * Einstiegspunkt der Anwendung.
	 * Startet den Spring Boot Container und initialisiert alle Beans.
	 * 
	 * @param args Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		SpringApplication.run(SimpleVersioningApplication.class, args);
	}

	/**
	 * Registriert die Versioning-Tools für das MCP (Model Context Protocol).
	 * 
	 * Diese Bean-Methode macht die Methoden aus dem SimpleVersioningService
	 * als MCP-Tools verfügbar. Die Tools können dann von AI-Assistenten oder
	 * anderen MCP-Clients aufgerufen werden.
	 * 
	 * Automatisch registrierte Tools:
	 * - create_revision: Erstellt eine neue Revision mit übergebenen Dateien
	 * - list_revisions: Listet alle existierenden Revisionsnummern auf
	 * 
	 * @param svService Der Simple Versioning Service, dessen annotierte Methoden als Tools bereitgestellt werden
	 * @return Liste der registrierten Tool-Callbacks für Spring AI MCP
	 */
	@Bean
	public List<ToolCallback> danTools(SimpleVersioningService svService) {
		List<ToolCallback> tools = new ArrayList<>();
		// Extrahiert alle @Tool-annotierten Methoden aus dem Service und registriert sie
		tools.addAll(Arrays.asList(ToolCallbacks.from(svService)));
		return tools;
	}

}

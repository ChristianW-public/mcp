package it.wiesner.mcp.codingguidelines;

import java.util.List;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hauptklasse der Spring Boot Anwendung für Coding Guidelines.
 * 
 * Diese Anwendung stellt Coding-Richtlinien für verschiedene Programmiersprachen
 * über ein Tool-basiertes Interface bereit. Sie verwendet das Command Pattern
 * für bessere Wartbarkeit und Erweiterbarkeit.
 */
@SpringBootApplication
@ComponentScan(basePackages = "it.wiesner.mcp")
public class CodingGuidelinesApplication {

	/**
	 * Einstiegspunkt der Anwendung.
	 * Startet den Spring Boot Container mit allen konfigurierten Komponenten.
	 * 
	 * @param args Kommandozeilenargumente (werden an Spring Boot übergeben)
	 */
	public static void main(String[] args) {
		SpringApplication.run(CodingGuidelinesApplication.class, args);
	}

	/**
	 * Registriert die Tool-Callbacks für den CodingGuidelineService.
	 * 
	 * Diese Methode erstellt eine Liste von ToolCallbacks, die es externen
	 * Systemen ermöglichen, auf die Coding Guidelines zuzugreifen. Die Tools
	 * werden automatisch aus den @Tool-annotierten Methoden des Services
	 * generiert.
	 * 
	 * @param cgService Der CodingGuidelineService, dessen Methoden als Tools
	 *                  bereitgestellt werden sollen
	 * @return Liste von ToolCallback-Objekten für die Integration
	 */
	@Bean
	public List<ToolCallback> danTools(CodingGuidelineService cgService) {
		return List.of(ToolCallbacks.from(cgService));
	}

}

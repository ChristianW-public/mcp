package it.wiesner.mcp.codingguidelines;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.wiesner.mcp.codingguidelines.model.CodingGuideline;

@SpringBootTest
class CodingGuidelinesApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testLoadJavaCodingGuideline() {
		CodingGuideline guideline = new CodingGuideline("java");
		assertNotNull(guideline.getRules(), "Rules should not be null");
		assertFalse(guideline.getRules().isEmpty(), "Rules should not be empty");
	}

	@Test
	void testLoadPythonCodingGuideline() {
		CodingGuideline guideline = new CodingGuideline("python");
		assertNotNull(guideline.getRules(), "Rules should not be null");
		assertFalse(guideline.getRules().isEmpty(), "Rules should not be empty");
	}

	@Test
	void testLoadNonExistentCodingGuideline() {
		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> new CodingGuideline("nonexistent"),
			"Should throw IllegalArgumentException for non-existent file"
		);
		assertTrue(exception.getMessage().contains("Resource file not found"), 
			"Exception message should indicate file not found");
	}

	@Test
	void testLoadedContentIsNotEmpty() {
		CodingGuideline guideline = new CodingGuideline("java");
		String rules = guideline.getRules();
		assertTrue(rules.length() > 0, "Loaded content should have positive length");
	}

}

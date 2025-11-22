package it.wiesner.mcp.simpleversioning;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleVersioningServiceTests {

	@Autowired
	private SimpleVersioningService simpleVersioningService;

	private static final String TEST_BASE_PATH = "C:\\Temp";

	@BeforeEach
	void setUp() throws IOException {
		// Clean up test directory before each test
		cleanupTestDirectory();
	}

	@AfterEach
	void tearDown() throws IOException {
		// Clean up test directory after each test
		cleanupTestDirectory();
	}

	private void cleanupTestDirectory() throws IOException {
		Path basePath = Paths.get(TEST_BASE_PATH);
		if (Files.exists(basePath)) {
			try (Stream<Path> paths = Files.walk(basePath)) {
				paths.sorted(Comparator.reverseOrder())
					.filter(path -> !path.equals(basePath))
					.filter(path -> {
						String name = path.getFileName().toString();
						return name.matches("\\d+");
					})
					.forEach(path -> {
						try {
							if (Files.isDirectory(path)) {
								deleteDirectory(path);
							} else {
								Files.deleteIfExists(path);
							}
						} catch (IOException e) {
							// Ignore
						}
					});
			}
		}
	}

	private void deleteDirectory(Path directory) throws IOException {
		try (Stream<Path> paths = Files.walk(directory)) {
			paths.sorted(Comparator.reverseOrder())
				.forEach(path -> {
					try {
						Files.deleteIfExists(path);
					} catch (IOException e) {
						// Ignore
					}
				});
		}
	}

	@Test
	void testCreateRevision() throws IOException {
		Map<String, String> files = new HashMap<>();
		files.put("src/main/Test.java", "public class Test {}");
		files.put("README.md", "# Test Project");

		int revision = simpleVersioningService.createRevision(files);

		assertEquals(1, revision, "First revision should be 1");
		
		Path revisionPath = Paths.get(TEST_BASE_PATH, String.valueOf(revision));
		assertTrue(Files.exists(revisionPath), "Revision directory should exist");
		
		Path testFile = revisionPath.resolve("src/main/Test.java");
		assertTrue(Files.exists(testFile), "Test.java should exist");
		assertEquals("public class Test {}", Files.readString(testFile), "File content should match");
		
		Path readmeFile = revisionPath.resolve("README.md");
		assertTrue(Files.exists(readmeFile), "README.md should exist");
		assertEquals("# Test Project", Files.readString(readmeFile), "File content should match");
	}

	@Test
	void testMultipleRevisions() throws IOException {
		Map<String, String> files1 = new HashMap<>();
		files1.put("file1.txt", "Version 1");

		Map<String, String> files2 = new HashMap<>();
		files2.put("file2.txt", "Version 2");

		int revision1 = simpleVersioningService.createRevision(files1);
		int revision2 = simpleVersioningService.createRevision(files2);

		assertEquals(1, revision1, "First revision should be 1");
		assertEquals(2, revision2, "Second revision should be 2");
		
		Path revision1Path = Paths.get(TEST_BASE_PATH, "1");
		Path revision2Path = Paths.get(TEST_BASE_PATH, "2");
		
		assertTrue(Files.exists(revision1Path), "Revision 1 directory should exist");
		assertTrue(Files.exists(revision2Path), "Revision 2 directory should exist");
	}

	@Test
	void testListRevisions() throws IOException {
		Map<String, String> files = new HashMap<>();
		files.put("test.txt", "content");

		// Initially no revisions
		int[] initialRevisions = simpleVersioningService.listRevisions();
		int initialCount = initialRevisions.length;

		// Create three revisions
		simpleVersioningService.createRevision(files);
		simpleVersioningService.createRevision(files);
		simpleVersioningService.createRevision(files);

		int[] revisions = simpleVersioningService.listRevisions();
		assertEquals(initialCount + 3, revisions.length, "Should have 3 more revisions");
		
		// Check that revisions are sorted
		for (int i = 0; i < revisions.length - 1; i++) {
			assertTrue(revisions[i] < revisions[i + 1], "Revisions should be sorted");
		}
	}

	@Test
	void testCreateRevisionWithNestedDirectories() throws IOException {
		Map<String, String> files = new HashMap<>();
		files.put("deeply/nested/path/to/file.txt", "nested content");

		int revision = simpleVersioningService.createRevision(files);

		Path nestedFile = Paths.get(TEST_BASE_PATH, String.valueOf(revision), "deeply/nested/path/to/file.txt");
		assertTrue(Files.exists(nestedFile), "Nested file should exist");
		assertEquals("nested content", Files.readString(nestedFile), "File content should match");
	}

	@Test
	void testEmptyFilesMap() throws IOException {
		Map<String, String> files = new HashMap<>();

		int revision = simpleVersioningService.createRevision(files);

		Path revisionPath = Paths.get(TEST_BASE_PATH, String.valueOf(revision));
		assertTrue(Files.exists(revisionPath), "Revision directory should exist even with no files");
	}
}

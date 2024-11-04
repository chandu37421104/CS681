package umbcs681.hw01;
import umbcs681.hw01.fs.*;
import umbcs681.hw01.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


public class FileSystemTest {
    @BeforeAll
    public static void setup() {
        TestFixture.setupFixture();
    }

    @Test
    public void FindDirectoryByNameTest() {
        Directory srcDir = TestFixture.repo.getSubDirectories().stream()
            .filter(dir -> "src".equals(dir.getName()))
            .findFirst()
            .orElse(null);
        assertNotNull(srcDir);
    }

    @Test
    public void FindFileByPathTest() {
        File foundFile = TestFixture.repo.findFileByName("readme.md");
        assertNotNull(foundFile, "File 'readme.md' should be present in the file system");
    }

    @Test
    public void SingletonInstanceTest() {
        FileSystem fs1 = FileSystem.getFileSystem();
        FileSystem fs2 = FileSystem.getFileSystem();

        assertSame(fs1, fs2);
    }

    @Test
    void DirectoryCountingVisitorTest() {
        CountingVisitor visitor = new CountingVisitor();
        TestFixture.repo.accept(visitor);

        int expectedDirs = 4; 
        assertEquals(expectedDirs, visitor.getDirectoryCount(), "Counts directories including repo, src, main, and test");
    }

    @Test
    void LinkCountingVisitorTest() {
        CountingVisitor visitor = new CountingVisitor();
        TestFixture.repo.accept(visitor);

        int expectedLinks = 1;
        assertEquals(expectedLinks, visitor.getLinkCount(), "Counts one link");
    }

    @Test
    void FilesCountingVisitorTest() {
        CountingVisitor visitor = new CountingVisitor();
        TestFixture.repo.accept(visitor);

        int expectedFiles = 5; 
        assertEquals(expectedFiles, visitor.getFileCount(), "Counts files including readme.md, A.java, B.java, ATest.java, and BTest.java");
    }

    @Test
    void ReadmeFileSearchVisitorTest() {
        FileSearchVisitor searchVisitor = new FileSearchVisitor("readme.md");
        TestFixture.repo.accept(searchVisitor);

        assertEquals(2, searchVisitor.getFoundFiles().size(), "Should find one file named 'readme.md'");
    }

    @Test
    void aJavaFileSearchVisitorTest() {
        FileSearchVisitor searchVisitor = new FileSearchVisitor("A.java");
        TestFixture.repo.accept(searchVisitor);

        assertEquals(1, searchVisitor.getFoundFiles().size(), "Should find one file named 'A.java'");
    }

    @Test
    void FileCrawlingVisitorTest() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        List<String> visitedPaths = crawlVisitor.getVisitedPaths();
        assertTrue(visitedPaths.contains("Visiting directory: repo"));
        assertTrue(visitedPaths.contains("Visiting file: readme.md"));
        assertTrue(visitedPaths.contains("Visiting link: rm.md"));
        assertTrue(visitedPaths.contains("Visiting file: A.java"));
        assertTrue(visitedPaths.contains("Visiting file: B.java"));
        assertTrue(visitedPaths.contains("Visiting file: ATest.java"));
        assertTrue(visitedPaths.contains("Visiting file: BTest.java"));
    }

    @Test
    void FileCrawlingVisitorFilesStreamTest() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        //List<String> visitedPaths = crawlVisitor.getVisitedPaths();
        //visitedPaths.forEach(System.out::println);

        Stream<File> fileStream = crawlVisitor.files();

        long fileCount = fileStream.count();
        int expectedFileCount = 5; 
        assertEquals(expectedFileCount, fileCount, "The stream should contain the expected number of files.");
    }

    @Test
    void EmptyDirectoryFileCrawlingVisitorTest() {
        Directory emptyDir = new Directory(null, "empty", null);
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        emptyDir.accept(crawlVisitor);

        Stream<File> fileStream = crawlVisitor.files();
        assertEquals(0, fileStream.count(), "There should be no files in an empty directory.");
    }

    @Test
    void NonExistentFileSearchTest() {
        FileSearchVisitor searchVisitor = new FileSearchVisitor("java.txt");
        TestFixture.repo.accept(searchVisitor);

        assertEquals(0, searchVisitor.getFoundFiles().size(), "No files should be found for a non-existent file.");
    }

    @Test
    void javaFilesCreatedAfterCertainTimeTest() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        
        LocalDateTime filterTime = LocalDateTime.now().minusMinutes(1);

        
        long count = crawlVisitor.files()
            .filter(file -> file.getCreationTime().isAfter(filterTime))
            .filter(file -> file.getName().endsWith(".java"))
            .count();

        int expectedCount = 4; 
        assertEquals(expectedCount, count, "Should find 4 .java files created after the specified time.");
    }

    @Test
    void countJavaFilesOnlyTest() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        // Count files with a .java extension only
        long javaFileCount = crawlVisitor.files()
            .filter(file -> file.getName().endsWith(".java"))
            .count();

        int expectedJavaFileCount = 4; 
        assertEquals(expectedJavaFileCount, javaFileCount, "Should find 4 files with .java extension.");
    }

    @Test
    void countFilesCreatedAfterCertainTimeTest() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        
        LocalDateTime filterTime = LocalDateTime.now().minusMinutes(1);

        
        long fileCountAfterTime = crawlVisitor.files()
            .filter(file -> file.getCreationTime().isAfter(filterTime))
            .count();

        int expectedFileCountAfterTime = 5; 
        assertEquals(expectedFileCountAfterTime, fileCountAfterTime, "Should find 5 files created after the specified time.");
    }
}

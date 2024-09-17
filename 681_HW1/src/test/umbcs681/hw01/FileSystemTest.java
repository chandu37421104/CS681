package umbcs681.hw01;
import umbcs681.hw01.fs.*;
import umbcs681.hw01.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.stream.Collectors;
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
    public void testFileCrawlingVisitor() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        List<String> visitedPaths = crawlVisitor.getVisitedPaths();
        assertTrue(visitedPaths.stream().anyMatch(path -> path.contains("repo")), "Should visit repo");
        assertTrue(visitedPaths.stream().anyMatch(path -> path.contains("readme.md")), "Should visit readme.md");
        assertTrue(visitedPaths.stream().anyMatch(path -> path.contains("rm.md")), "Should visit rm.md");
        assertTrue(visitedPaths.stream().anyMatch(path -> path.contains("A.java")), "Should visit A.java");
        assertTrue(visitedPaths.stream().anyMatch(path -> path.contains("B.java")), "Should visit B.java");
        assertTrue(visitedPaths.stream().anyMatch(path -> path.contains("ATest.java")), "Should visit ATest.java");
        assertTrue(visitedPaths.stream().anyMatch(path -> path.contains("BTest.java")), "Should visit BTest.java");

    }

    @Test
    public void testCountJavaExtensionFiles() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        long javaFileCount = crawlVisitor.FileStream()
                                  .filter(e -> e instanceof File)
                                  .map(e -> (File) e)
                                  .distinct()
                                  .filter(file -> file.getName().endsWith(".java"))
                                  .peek(file -> System.out.println("Counting file: " + file.getName())) 
                                  .count();


        assertEquals(4L, javaFileCount, "Should have 4 .java files");
    }

    @Test
    public void testCountReadmeFiles() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        long javaFileCount = crawlVisitor.FileStream()
                                  .filter(e -> e instanceof File)
                                  .map(e -> (File) e)
                                  .distinct()
                                  .filter(file -> file.getName().endsWith(".md"))
                                  .peek(file -> System.out.println("Counting file: " + file.getName())) 
                                  .count();


        assertEquals(1L, javaFileCount, "Should have 1 .md files");
    }


}

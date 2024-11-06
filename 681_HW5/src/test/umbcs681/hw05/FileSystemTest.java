package umbcs681.hw05;
import umbcs681.hw05.fs.*;
import umbcs681.hw05.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;


public class FileSystemTest {

    private static Map<String, IntSummaryStatistics> statsByExtension;

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
    void JavaFileExtensionTest() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        Map<String, IntSummaryStatistics> fileStats = crawlVisitor.groupFilesByExtension();
        assertTrue(fileStats.containsKey("java"), ".java files should be found.");
        IntSummaryStatistics javaStats = fileStats.get("java");
        
        assertEquals(4, javaStats.getCount(), "There should be 4 .java files.");
        assertEquals(120 + 80 + 140 + 60, javaStats.getSum(), "The sum of .java file sizes should be correct");
    }

    @Test
    void ReadmeFileExtensionTest() {
        FileCrawlingVisitor crawlVisitor = new FileCrawlingVisitor();
        TestFixture.repo.accept(crawlVisitor);

        Map<String, IntSummaryStatistics> fileStats = crawlVisitor.groupFilesByExtension();
        assertTrue(fileStats.containsKey("md"), ".md files should be found.");
        IntSummaryStatistics mdStats = fileStats.get("md");

        assertEquals(1, mdStats.getCount(), "There should be 2 .txt files.");
        assertEquals(20, mdStats.getSum(), "The sum of .md file sizes should be 20");
    }

    @Test
    void testSingletonInstance() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<FileSystem> instances = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                instances.add(FileSystem.getFileSystem());
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();

        FileSystem firstInstance = instances.get(0);
        for (FileSystem instance : instances) {
            assertSame(firstInstance, instance);
        }
    }

    @Test
    void testAppendRootDirConcurrently() throws InterruptedException {
        FileSystem fs = FileSystem.getFileSystem();
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int dirIndex = i;
            executorService.execute(() -> {
                Directory dir = new Directory(null, "dir" + dirIndex, LocalDateTime.now());
                fs.appendRootDir(dir);
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();

        List<Directory> rootDirs = fs.getRootDirs();
        assertEquals(threadCount, rootDirs.size());

        for (int i = 0; i < threadCount; i++) {
            final int dirIndex = i;  // Make a final copy of 'i'
            assertTrue(rootDirs.stream().anyMatch(d -> d.getName().equals("dir" + dirIndex)));
        }
        
    }

}

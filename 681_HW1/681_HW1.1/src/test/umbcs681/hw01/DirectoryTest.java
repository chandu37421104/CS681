package umbcs681.hw01;
import umbcs681.hw01.fs.*;
import umbcs681.hw01.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectoryTest {
    @BeforeAll
    public static void setup() {
        TestFixture.setupFixture();
    }

    @Test
    public void SrcCountChildrenTest() {
        assertEquals(2, TestFixture.src.countChildren());
    }

    @Test
    public void MainCountChildrenTest() {
        assertEquals(2, TestFixture.main.countChildren());
    }

    @Test
    public void TestCountChildrenTest() {
        assertEquals(3, TestFixture.test.countChildren());
    }

    @Test
    public void RepoTotalSizeTest() {
        assertEquals(440, TestFixture.repo.getTotalSize());
    }

    @Test
    public void MainTotalSizeTest() {
        assertEquals(200, TestFixture.main.getTotalSize());
    }

    @Test
    public void TestTotalSizeTest() {
        assertEquals(220, TestFixture.test.getTotalSize());
    }

    @Test
    public void GetSrcSubDirectoriesTest() {
        assertEquals(2, TestFixture.src.getSubDirectories().size());
    }

    @Test
    public void GetMainSubDirectoriesTest() {
        assertEquals(0, TestFixture.main.getSubDirectories().size());
    }

    @Test
    public void GetRepoFilesTest() {
        assertEquals(1, TestFixture.repo.getFiles().size());
    }

    @Test
    public void GetMainFilesTest() {
        assertEquals(2, TestFixture.main.getFiles().size());
    }

    @Test
    public void GetTestFilesTest() {
        assertEquals(2, TestFixture.test.getFiles().size());
    }

    @Test
    public void MainContainsaJavaFileTest() {
        assertTrue(TestFixture.main.contains(TestFixture.aJava));
    }

    @Test
    public void TestContainsaTestJavaFileTest() {
        assertTrue(TestFixture.test.contains(TestFixture.aTestJava));
    }

    @Test
    public void RepoContainsaJavaFileTest() {
        assertFalse(TestFixture.repo.contains(TestFixture.aJava));
    }
}

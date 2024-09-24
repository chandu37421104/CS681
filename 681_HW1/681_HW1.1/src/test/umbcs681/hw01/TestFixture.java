package umbcs681.hw01;
import umbcs681.hw01.fs.*;
import java.time.LocalDateTime;

public class TestFixture {
    
    protected static Directory repo;
    protected static Directory src;
    protected static Directory main;
    protected static Directory test;

    protected static File readme;
    protected static File aJava;
    protected static File bJava;
    protected static File aTestJava;
    protected static File bTestJava;
    protected static Link rmMdLink;

    public static void setupFixture() {
        
        repo = new Directory(null, "repo", LocalDateTime.now());
        src = new Directory(repo, "src", LocalDateTime.now());
        main = new Directory(src, "main", LocalDateTime.now());
        test = new Directory(src, "test", LocalDateTime.now());

       
        readme = new File(repo, "readme.md", 20, LocalDateTime.now());
        aJava = new File(main, "A.java", 120, LocalDateTime.now());
        bJava = new File(main, "B.java", 80, LocalDateTime.now());
        aTestJava = new File(test, "ATest.java", 140, LocalDateTime.now());
        bTestJava = new File(test, "BTest.java", 60, LocalDateTime.now());

        
        rmMdLink = new Link(test, "rm.md", LocalDateTime.now(), readme);

        
        repo.appendChild(src);
        repo.appendChild(readme);

        src.appendChild(main);
        src.appendChild(test);

        main.appendChild(aJava);
        main.appendChild(bJava);

        test.appendChild(aTestJava);
        test.appendChild(bTestJava);
        test.appendChild(rmMdLink);
    }
}

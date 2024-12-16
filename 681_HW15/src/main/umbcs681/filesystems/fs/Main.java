package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FileCrawlingVisitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Step 1: Initialize FileSystem and directories
        FileSystem fileSystem = FileSystem.getFileSystem();

        Directory root = new Directory(null, "root");
        Directory subDir1 = new Directory(root, "subDir1");
        Directory subDir2 = new Directory(root, "subDir2");

        File file1 = new File(subDir1, "file1.txt");
        File file2 = new File(subDir2, "file2.txt");

        root.addChild(subDir1);
        root.addChild(subDir2);
        subDir1.addChild(file1);
        subDir2.addChild(file2);

        fileSystem.appendRootDir(root);

        // Step 2: Use FileCrawlingVisitor to collect files
        FileCrawlingVisitor visitor = new FileCrawlingVisitor();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        System.out.println("Starting file crawling...");

        // Step 3: Start crawling process in a separate thread
        executor.execute(() -> {
            for (Directory dir : fileSystem.getRootDirs()) {
                dir.accept(visitor);
            }
            System.out.println("Crawling completed.");
        });

        // Step 4: 2-step thread termination
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for threads to complete
        }

        // Step 5: Output collected files
        System.out.println("Files collected during crawling:");
        visitor.getFiles().forEach(file -> System.out.println(file.getName()));
    }
}


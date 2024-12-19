package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FileCrawlingVisitor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Step 1: Initialize FileSystem and directories
        FileSystem fileSystem = FileSystem.getFileSystem();

        Directory root1 = new Directory(null, "root1");
        Directory root2 = new Directory(null, "root2");
        Directory root3 = new Directory(null, "root3");

        File file1 = new File(root1, "file1.txt");
        File file2 = new File(root2, "file2.txt");
        File file3 = new File(root3, "file3.txt");

        root1.addChild(new Directory(root1, "subDir1"));
        root2.addChild(new Directory(root2, "subDir2"));
        root3.addChild(new Directory(root3, "subDir3"));

        root1.addChild(file1);
        root2.addChild(file2);
        root3.addChild(file3);

        fileSystem.appendRootDir(root1);
        fileSystem.appendRootDir(root2);
        fileSystem.appendRootDir(root3);

        // Step 2: Use ConcurrentLinkedQueue for shared file list
        ConcurrentLinkedQueue<File> sharedFileList = new ConcurrentLinkedQueue<>();

        // Step 3: Use FileCrawlingVisitor to collect files
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("Starting file crawling...");

        try {
            // Step 4: Start crawling threads for each root directory
            for (Directory root : fileSystem.getRootDirs()) {
                executor.execute(() -> {
                    FileCrawlingVisitor visitor = new FileCrawlingVisitor();
                    root.accept(visitor);
                    sharedFileList.addAll(visitor.getFiles());
                });
            }
        } finally {
            // Step 5: Initiate 2-step thread termination
            executor.shutdown(); // Step 5.1: Gracefully shut down threads
            try {
                while (!executor.isTerminated()) {
                    // Step 5.2: Wait for all threads to finish
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Step 6: Output collected files
        System.out.println("Crawling completed. Files collected:");
        sharedFileList.forEach(file -> System.out.println(file.getName()));
    }
}

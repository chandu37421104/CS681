package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FileCrawlingVisitor;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Shared file list for threads
        ConcurrentLinkedQueue<String> sharedFileList = new ConcurrentLinkedQueue<>();
        AtomicBoolean running = new AtomicBoolean(true);

        // File system setup
        FileSystem fs = FileSystem.getFileSystem();
        Directory drive1 = new Directory(null, "drive1", LocalDateTime.now());
        Directory drive2 = new Directory(null, "drive2", LocalDateTime.now());
        Directory drive3 = new Directory(null, "drive3", LocalDateTime.now());

        // Add files to drives
        drive1.appendChild(new File(drive1, "file1.txt", 10, LocalDateTime.now()));
        drive2.appendChild(new File(drive2, "file2.txt", 20, LocalDateTime.now()));
        drive3.appendChild(new File(drive3, "file3.txt", 30, LocalDateTime.now()));

        fs.appendRootDir(drive1);
        fs.appendRootDir(drive2);
        fs.appendRootDir(drive3);

        // Step 1: Initialize and start threads
        Thread t1 = createCrawler(drive1, sharedFileList, running);
        Thread t2 = createCrawler(drive2, sharedFileList, running);
        Thread t3 = createCrawler(drive3, sharedFileList, running);

        t1.start();
        t2.start();
        t3.start();

        // Step 2: Let threads run for a specific time
        System.out.println("Crawling files for 3 seconds...");
        Thread.sleep(3000);

        // Two-step termination process
        System.out.println("Initiating termination...");
        running.set(false); // Step 1: Signal threads to stop crawling

        // Wait for threads to finish
        t1.join();
        t2.join();
        t3.join();

        // Step 2: Main thread collects results
        System.out.println("Collecting results...");
        System.out.println("Crawled Files:");
        sharedFileList.forEach(System.out::println);

        System.out.println("Crawling process completed.");
    }

    /**
     * Creates a crawler thread for a given directory.
     * @param root The root directory to crawl.
     * @param sharedFileList The shared list to store results.
     * @param running AtomicBoolean flag to control thread termination.
     * @return The created thread.
     */
    private static Thread createCrawler(Directory root, ConcurrentLinkedQueue<String> sharedFileList, AtomicBoolean running) {
        return new Thread(() -> {
            FileCrawlingVisitor visitor = new FileCrawlingVisitor(sharedFileList);
            while (running.get()) {
                root.accept(visitor); // Crawl the directory
                try {
                    Thread.sleep(100); // Reduce CPU load
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " stopped. Adding remaining files...");
            root.accept(visitor); // Ensure remaining files are added
        });
    }
}

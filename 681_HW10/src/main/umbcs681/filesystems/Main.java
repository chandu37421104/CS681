package umbcs681.filesystems;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        FileSystem fs = FileSystem.getFileSystem();

        // Create root directory and populate it with some data
        Directory root = new Directory(null, "root", LocalDateTime.now());
        fs.appendRootDir(root);

        Directory home = new Directory(root, "home", LocalDateTime.now());
        root.appendChild(home);

        File file1 = new File(home, "file1.txt", 100, LocalDateTime.now());
        File file2 = new File(home, "file2.txt", 200, LocalDateTime.now());
        home.appendChild(file1);
        home.appendChild(file2);

        // AtomicBoolean flag for thread termination
        AtomicBoolean running = new AtomicBoolean(true);

        // Runnable task for threads to access file system data
        Runnable task = () -> {
            while (running.get()) {
                System.out.println(Thread.currentThread().getName() + " - Total Size: " + root.getTotalSize());
                try {
                    Thread.sleep(100); // Simulate some processing
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println(Thread.currentThread().getName() + " terminated.");
        };

        // Create and start 10+ threads
        Thread[] threads = new Thread[12]; // 12 threads
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task, "Thread-" + (i + 1));
            threads[i].start();
        }

        // Main thread sleeps for a while to allow threads to run
        try {
            Thread.sleep(2000); // Let threads run for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Two-step termination: Set the running flag to false
        System.out.println("Main thread setting running flag to false...");
        running.set(false);

        // Wait for all threads to terminate
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads terminated. Main thread exiting.");
    }
}


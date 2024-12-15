package umbcs681.emailsystem;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadSafeGmail {
    private static volatile boolean stopRequested = false;

    public static void main(String[] args) throws InterruptedException {
        EmailInboxSafe inbox = new EmailInboxSafe(1000);

        AtomicBoolean readerCompleted = new AtomicBoolean(false);
        AtomicBoolean deleterCompleted = new AtomicBoolean(false);

        Runnable reader = () -> {
            while (!stopRequested) {
                for (int i = 0; i < inbox.getTotalEmails(); i++) {
                    inbox.readEmail(i);
                }
            }
            System.out.println("Reader thread terminated gracefully.");
            readerCompleted.set(true);
        };

        Runnable deleter = () -> {
            while (!stopRequested) {
                for (int i = 0; i < inbox.getTotalEmails() / 2; i++) {
                    inbox.deleteEmail(i);
                }
            }
            System.out.println("Deleter thread terminated gracefully.");
            deleterCompleted.set(true);
        };

        Thread thread1 = new Thread(reader);
        Thread thread2 = new Thread(deleter);

        thread1.start();
        thread2.start();

        Thread.sleep(2000); // Allow threads to run for a bit

        System.out.println("Requesting threads to stop...");
        stopRequested = true; // Step 1: Request to stop

        thread1.join();
        thread2.join();

        System.out.println("Final unread count (Safe): " + inbox.getUnreadCount());
        System.out.println("Final total emails (Safe): " + inbox.getTotalEmails());

        if (readerCompleted.get() && deleterCompleted.get()) {
            System.out.println("Both threads terminated gracefully.");
        } else {
            System.out.println("Error: Threads did not terminate cleanly.");
        }
    }
}


package umbcs681.emailsystem;

public class ThreadUnsafeGmail {
    public static void main(String[] args) throws InterruptedException {
        EmailInbox inbox = new EmailInbox(1000);

        Runnable reader = () -> {
            for (int i = 0; i < 1000; i++) {
                inbox.readEmail(i % inbox.getTotalEmails());
            }
        };

        Runnable deleter = () -> {
            for (int i = 0; i < 500; i++) {
                inbox.deleteEmail(i % inbox.getTotalEmails());
            }
        };

        Thread thread1 = new Thread(reader);
        Thread thread2 = new Thread(deleter);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final unread count (Unsafe): " + inbox.getUnreadCount());
        System.out.println("Final total emails (Unsafe): " + inbox.getTotalEmails());
    }
}

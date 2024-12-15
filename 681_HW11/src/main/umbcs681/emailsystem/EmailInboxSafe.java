package umbcs681.emailsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class EmailInboxSafe {
    private List<Email> emails = new ArrayList<>();
    private int unreadCount = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public EmailInboxSafe(int totalEmails) {
        for (int i = 0; i < totalEmails; i++) {
            emails.add(new Email());
            unreadCount++;
        }
    }

    public void readEmail(int index) {
        lock.lock();
        try {
            if (index < emails.size() && !emails.get(index).isRead()) {
                Thread.sleep(1); // Simulate reading delay
                emails.get(index).markAsRead();
                unreadCount--;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
    
    public void deleteEmail(int index) {
        lock.lock();
        try {
            if (index < emails.size()) {
                Thread.sleep(1); // Simulate deletion delay
                if (!emails.get(index).isRead()) {
                    unreadCount--;
                }
                emails.remove(index);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
    

    public int getUnreadCount() {
        lock.lock();
        try {
            return unreadCount;
        } finally {
            lock.unlock();
        }
    }

    public int getTotalEmails() {
        lock.lock();
        try {
            return emails.size();
        } finally {
            lock.unlock();
        }
    }
}

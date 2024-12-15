package umbcs681.emailsystem;

import java.util.ArrayList;
import java.util.List;

public class EmailInbox {
    private List<Email> emails = new ArrayList<>();
    private int unreadCount = 0;

    public EmailInbox(int totalEmails) {
        for (int i = 0; i < totalEmails; i++) {
            emails.add(new Email());
            unreadCount++;
        }
    }

    public void readEmail(int index) {
        if (!emails.get(index).isRead()) {
            emails.get(index).markAsRead();
            unreadCount--;
        }
    }

    public void deleteEmail(int index) {
        if (index < emails.size()) {
            if (!emails.get(index).isRead()) {
                unreadCount--;
            }
            emails.remove(index);
        }
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public int getTotalEmails() {
        return emails.size();
    }
}


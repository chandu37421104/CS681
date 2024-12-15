package umbcs681.emailsystem;

public class Email {
    private boolean isRead;

    public Email() {
        this.isRead = false;
    }

    public boolean isRead() {
        return isRead;
    }

    public void markAsRead() {
        isRead = true;
    }
}


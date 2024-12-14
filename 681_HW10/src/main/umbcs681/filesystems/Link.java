package umbcs681.filesystems;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Link extends FSElement {
    private FSElement target;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Link(Directory parent, String name, LocalDateTime creationTime, FSElement target) {
        super(parent, name, 0, creationTime); // Links have size 0
        this.target = target;
    }

    public FSElement getTarget() {
        lock.readLock().lock();
        try {
            return target;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setTarget(FSElement target) {
        lock.writeLock().lock();
        try {
            this.target = target;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public String getPath() {
        StringBuilder path = new StringBuilder(name);
        Directory currentParent = parent;

        while (currentParent != null) {
            path.insert(0, currentParent.getName() + "/");
            currentParent = currentParent.getParent();
        }

        return path.toString();
    }
}

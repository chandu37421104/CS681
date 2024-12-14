package umbcs681.filesystems;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class FSElement {
    protected String name;
    protected int size;
    protected LocalDateTime creationTime;
    protected Directory parent;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FSElement(Directory parent, String name, int size, LocalDateTime creationTime) {
        this.parent = parent;
        this.name = name;
        this.size = size;
        this.creationTime = creationTime;
    }

    public Directory getParent() {
        lock.readLock().lock();
        try {
            return parent;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setParent(Directory parent) {
        lock.writeLock().lock();
        try {
            this.parent = parent;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getName() {
        lock.readLock().lock();
        try {
            return name;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setName(String name) {
        lock.writeLock().lock();
        try {
            this.name = name;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getSize() {
        lock.readLock().lock();
        try {
            return size;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setSize(int size) {
        lock.writeLock().lock();
        try {
            this.size = size;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public LocalDateTime getCreationTime() {
        lock.readLock().lock();
        try {
            return creationTime;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setCreationTime(LocalDateTime creationTime) {
        lock.writeLock().lock();
        try {
            this.creationTime = creationTime;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public abstract boolean isDirectory();

    public abstract String getPath();
}


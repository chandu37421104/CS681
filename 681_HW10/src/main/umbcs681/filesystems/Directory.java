package umbcs681.filesystems;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Directory extends FSElement {
    private List<FSElement> children;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Directory(Directory parent, String name, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime);
        this.children = new LinkedList<>();
    }

    public void appendChild(FSElement child) {
        lock.writeLock().lock();
        try {
            child.setParent(this);
            children.add(child);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<FSElement> getChildren() {
        lock.readLock().lock();
        try {
            return new LinkedList<>(children);
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Directory> getSubDirectories() {
        lock.readLock().lock();
        try {
            List<Directory> subDirs = new LinkedList<>();
            for (FSElement elem : children) {
                if (elem instanceof Directory) {
                    subDirs.add((Directory) elem);
                }
            }
            return subDirs;
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<File> getFiles() {
        lock.readLock().lock();
        try {
            List<File> files = new LinkedList<>();
            for (FSElement elem : children) {
                if (elem instanceof File) {
                    files.add((File) elem);
                }
            }
            return files;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int countChildren() {
        lock.readLock().lock();
        try {
            return children.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getTotalSize() {
        lock.readLock().lock();
        try {
            int totalSize = 0;
            for (FSElement elem : children) {
                totalSize += elem.getSize();
                if (elem instanceof Directory) {
                    totalSize += ((Directory) elem).getTotalSize();
                }
            }
            return totalSize;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isDirectory() {
        return true;
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

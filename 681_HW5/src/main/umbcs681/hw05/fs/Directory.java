package umbcs681.hw05.fs;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import umbcs681.hw05.util.FSVisitor;

public class Directory extends FSElement {
    private final List<FSElement> children;
    private final ReentrantLock lock = new ReentrantLock();

    public Directory(Directory parent, String name, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime);
        this.children = new LinkedList<>();
    }

    public void appendChild(FSElement child) {
        lock.lock();
        try {
            child.setParent(this);
            children.add(child);
        } finally {
            lock.unlock();
        }
    }

    public List<FSElement> getChildren() {
        lock.lock();
        try {
            return new LinkedList<>(children);  // Return a copy to avoid external modification
        } finally {
            lock.unlock();
        }
    }

    public List<Directory> getSubDirectories() {
        lock.lock();
        try {
            List<Directory> subDirs = new LinkedList<>();
            for (FSElement elem : children) {
                if (elem instanceof Directory) {
                    subDirs.add((Directory) elem);
                }
            }
            return subDirs;
        } finally {
            lock.unlock();
        }
    }

    public List<File> getFiles() {
        lock.lock();
        try {
            List<File> files = new LinkedList<>();
            for (FSElement elem : children) {
                if (elem instanceof File) {
                    files.add((File) elem);
                }
            }
            return files;
        } finally {
            lock.unlock();
        }
    }

    public int countChildren() {
        lock.lock();
        try {
            return children.size();
        } finally {
            lock.unlock();
        }
    }

    public int countFiles() {
        lock.lock();
        try {
            return getFiles().size();
        } finally {
            lock.unlock();
        }
    }

    public int countSubDirectories() {
        lock.lock();
        try {
            return getSubDirectories().size();
        } finally {
            lock.unlock();
        }
    }

    public int getTotalSize() {
        lock.lock();
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
            lock.unlock();
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

    public boolean contains(FSElement child) {
        lock.lock();
        try {
            return children.contains(child);
        } finally {
            lock.unlock();
        }
    }

    public File findFileByName(String name) {
        lock.lock();
        try {
            for (FSElement element : children) {
                if (element instanceof File && name.equals(element.getName())) {
                    return (File) element;
                }
                if (element instanceof Directory) {
                    File foundFile = ((Directory) element).findFileByName(name);
                    if (foundFile != null) {
                        return foundFile;
                    }
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public List<Link> getLinks() {
        lock.lock();
        try {
            List<Link> links = new LinkedList<>();
            for (FSElement elem : children) {
                if (elem instanceof Link) {
                    links.add((Link) elem);
                }
            }
            return links;
        } finally {
            lock.unlock();
        }
    }
    
    public int countLinks() {
        lock.lock();
        try {
            return getLinks().size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
        lock.lock();
        try {
            for (FSElement child : children) {
                child.accept(visitor);
            }
        } finally {
            lock.unlock();
        }
    }
}
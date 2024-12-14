package umbcs681.filesystems;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FileSystem {
    private static final AtomicReference<FileSystem> instance = new AtomicReference<>(null);
    private List<Directory> rootDirs;

    private FileSystem() {
        this.rootDirs = new LinkedList<>();
    }

    public static FileSystem getFileSystem() {
        if (instance.get() == null) {
            instance.compareAndSet(null, new FileSystem());
        }
        return instance.get();
    }

    public List<Directory> getRootDirs() {
        synchronized (this) {
            return new LinkedList<>(rootDirs);
        }
    }

    public void appendRootDir(Directory root) {
        synchronized (this) {
            rootDirs.add(root);
        }
    }
}



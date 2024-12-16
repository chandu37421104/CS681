package umbcs681.filesystems.fs;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileSystem {
    private static volatile FileSystem instance = null;
    private final Queue<Directory> rootDirs;

    private FileSystem() {
        this.rootDirs = new ConcurrentLinkedQueue<>();
    }

    public static FileSystem getFileSystem() {
        if (instance == null) {
            synchronized (FileSystem.class) {
                if (instance == null) {
                    instance = new FileSystem();
                }
            }
        }
        return instance;
    }

    public void appendRootDir(Directory root) {
        rootDirs.add(root);
    }

    public Queue<Directory> getRootDirs() {
        return rootDirs;
    }
}

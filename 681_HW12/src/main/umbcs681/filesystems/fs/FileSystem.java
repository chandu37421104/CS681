package umbcs681.filesystems.fs;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileSystem {
    private static volatile FileSystem instance = null;
    private final List<Directory> rootDirs;

    private FileSystem() {
        this.rootDirs = new CopyOnWriteArrayList<>();
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

    public List<Directory> getRootDirs() {
        return rootDirs;
    }
}
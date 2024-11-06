package umbcs681.hw05.fs;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
public class FileSystem {
    private static FileSystem instance = null;
    private final List<Directory> rootDirs;
    private static final ReentrantLock instanceLock = new ReentrantLock();
    private final ReentrantLock rootDirsLock = new ReentrantLock();

    private FileSystem() {
        this.rootDirs = new LinkedList<>();
    }

    public static FileSystem getFileSystem() {
        if (instance == null) {
            instanceLock.lock();
            try {
                if (instance == null) {
                    instance = new FileSystem();
                }
            } finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }

    public List<Directory> getRootDirs() {
        rootDirsLock.lock();
        try {
            return new LinkedList<>(rootDirs);  // Return a copy for thread safety
        } finally {
            rootDirsLock.unlock();
        }
    }

    public void appendRootDir(Directory root) {
        rootDirsLock.lock();
        try {
            rootDirs.add(root);
        } finally {
            rootDirsLock.unlock();
        }
    }

    public Directory findDirectoryByName(String name) {
        rootDirsLock.lock();
        try {
            for (Directory dir : rootDirs) {
                if (dir.getName().equals(name)) {
                    return dir;
                }
            }
            return null;
        } finally {
            rootDirsLock.unlock();
        }
    }

    public File findFileByPath(String path) {
        String[] parts = path.split("/");
        Directory currentDir = findDirectoryByName(parts[0]);

        if (currentDir == null) {
            return null;
        }

        for (int i = 1; i < parts.length - 1; i++) {
            boolean found = false;
            for (Directory subDir : currentDir.getSubDirectories()) {
                if (subDir.getName().equals(parts[i])) {
                    currentDir = subDir;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return null;
            }
        }

        for (File file : currentDir.getFiles()) {
            if (file.getName().equals(parts[parts.length - 1])) {
                return file;
            }
        }

        return null;
    }
}
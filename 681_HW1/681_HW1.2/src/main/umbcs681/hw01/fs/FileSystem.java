package umbcs681.hw01.fs;

import java.util.LinkedList;
import java.util.List;

public class FileSystem {
    private static FileSystem instance = null;
    private List<Directory> rootDirs;

    private FileSystem() {
        this.rootDirs = new LinkedList<>();
    }

    public static FileSystem getFileSystem() {
        if (instance == null) {
            instance = new FileSystem();
        }
        return instance;
    }

    public List<Directory> getRootDirs() {
        return rootDirs;
    }

    public void appendRootDir(Directory root) {
        rootDirs.add(root);
    }

    public Directory findDirectoryByName(String name) {
        for (Directory dir : rootDirs) {
            if (dir.getName().equals(name)) {
                return dir;
            }
        }
        return null;
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

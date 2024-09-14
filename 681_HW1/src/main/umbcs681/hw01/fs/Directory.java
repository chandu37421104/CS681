package umbcs681.hw01.fs;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import umbcs681.hw01.util.FSVisitor;

public class Directory extends FSElement {
    private List<FSElement> children;

    public Directory(Directory parent, String name, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime);
        this.children = new LinkedList<>();
    }

    public void appendChild(FSElement child) {
        child.setParent(this);
        children.add(child);
    }

    public List<FSElement> getChildren() {
        return children;
    }

    public List<Directory> getSubDirectories() {
        List<Directory> subDirs = new LinkedList<>();
        for (FSElement elem : children) {
            if (elem instanceof Directory) {
                subDirs.add((Directory) elem);
            }
        }
        return subDirs;
    }

    public List<File> getFiles() {
        List<File> files = new LinkedList<>();
        for (FSElement elem : children) {
            if (elem instanceof File) {
                files.add((File) elem);
            }
        }
        return files;
    }

    public int countChildren() {
        return children.size();
    }

    public int countFiles() {
        return getFiles().size();
    }

    public int countSubDirectories() {
        return getSubDirectories().size();
    }

    public int getTotalSize() {
        int totalSize = 0;
        for (FSElement elem : children) {
            totalSize += elem.getSize();
            if (elem instanceof Directory) {
                totalSize += ((Directory) elem).getTotalSize();
            }
        }
        return totalSize;
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
        return children.contains(child);
    }

    public File findFileByName(String name) {
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
    }

    public List<Link> getLinks() {
        List<Link> links = new LinkedList<>();
        for (FSElement elem : children) {
            if (elem instanceof Link) {
                links.add((Link) elem);
            }
        }
        return links;
    }
    
    public int countLinks() {
        return getLinks().size();
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
        for (FSElement child : children) {
            child.accept(visitor);
        }
    }
    
}

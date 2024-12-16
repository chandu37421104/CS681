package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FSVisitor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Directory extends FSElement {
    private final List<FSElement> children;

    public Directory(Directory parent, String name, LocalDateTime creationTime) {
        super(parent, name, 0, creationTime);
        this.children = new CopyOnWriteArrayList<>(); // Thread-safe list
    }

    public void appendChild(FSElement child) {
        child.parent = this;
        children.add(child);
    }

    public List<FSElement> getChildren() {
        return children; // Safe to iterate concurrently
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public String getPath() {
        return (parent == null) ? name : parent.getPath() + "/" + name;
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
        for (FSElement child : children) {
            child.accept(visitor);
        }
    }
}
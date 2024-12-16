package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FSVisitor;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Directory extends FSElement {
    private ConcurrentLinkedQueue<FSElement> children;

    public Directory(Directory parent, String name) {
        super(parent, name);
        this.children = new ConcurrentLinkedQueue<>();
    }

    public void addChild(FSElement child) {
        children.add(child);
    }

    public ConcurrentLinkedQueue<FSElement> getChildren() {
        return children;
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
        for (FSElement e : children) {
            e.accept(visitor);
        }
    }
}

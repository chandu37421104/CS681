package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FSVisitor;


public abstract class FSElement {
    protected String name;
    protected Directory parent;

    public FSElement(Directory parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public abstract void accept(FSVisitor visitor);

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }
}
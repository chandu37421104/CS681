package umbcs681.hw01.fs;

import java.time.LocalDateTime;

import umbcs681.hw01.util.FSVisitor;

public class Link extends FSElement {
    private FSElement target;

    public Link(Directory parent, String name, LocalDateTime creationTime, FSElement target) {
        super(parent, name, 0, creationTime);  // Links have a size of 0
        this.target = target;
    }

    public FSElement getTarget() {
        return target;
    }

    public void setTarget(FSElement target) {
        this.target = target;
    }

    @Override
    public boolean isDirectory() {
        return false;  
    }

    @Override
    public int getSize() {
        return target.getSize();
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

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
    }
}

package umbcs681.filesystems.fs;

import java.time.LocalDateTime;
import umbcs681.filesystems.util.*;

public class Link extends FSElement {
    private final FSElement target;

    public Link(Directory parent, String name, LocalDateTime creationTime, FSElement target) {
        super(parent, name, 0, creationTime);
        this.target = target;
    }

    public FSElement getTarget() {
        return target;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public String getPath() {
        return (parent == null) ? name : parent.getPath() + "/" + name;
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
        target.accept(visitor);
    }
}
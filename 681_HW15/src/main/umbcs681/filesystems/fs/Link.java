package umbcs681.filesystems.fs;

import java.time.LocalDateTime;
import umbcs681.filesystems.util.*;

public class Link extends FSElement {
    private FSElement target;

    public Link(Directory parent, String name, FSElement target) {
        super(parent, name);
        this.target = target;
    }

    public FSElement getTarget() {
        return target;
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
    }
}
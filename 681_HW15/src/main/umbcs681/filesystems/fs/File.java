package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FSVisitor;


public class File extends FSElement {

    public File(Directory parent, String name) {
        super(parent, name);
    }

    @Override
    public void accept(FSVisitor visitor) {
        visitor.visit(this);
    }
}
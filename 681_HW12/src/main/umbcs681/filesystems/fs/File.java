package umbcs681.filesystems.fs;

import umbcs681.filesystems.util.FSVisitor;

import java.time.LocalDateTime;

public class File extends FSElement {
    public File(Directory parent, String name, int size, LocalDateTime creationTime) {
        super(parent, name, size, creationTime);
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
    }
}
package umbcs681.hw05.fs;

import java.time.LocalDateTime;

import umbcs681.hw05.util.FSVisitor;

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

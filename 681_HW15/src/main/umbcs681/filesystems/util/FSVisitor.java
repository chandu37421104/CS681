package umbcs681.filesystems.util;

import umbcs681.filesystems.fs.*;

public interface FSVisitor {
    void visit(Link link);
    void visit(Directory dir);
    void visit(File file);
}

package umbcs681.hw01.util;

import umbcs681.hw01.fs.*;

public interface FSVisitor {
    void visit(Link link);
    void visit(Directory directory);
    void visit(File file);
}

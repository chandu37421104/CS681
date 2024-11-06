package umbcs681.hw05.util;

import umbcs681.hw05.fs.*;


public class CountingVisitor implements FSVisitor {
    private int fileCount = 0;
    private int directoryCount = 0;
    private int linkCount = 0;

    @Override
    public void visit(File file) {
        fileCount++;
    }

    @Override
    public void visit(Directory directory) {
        directoryCount++;
    }

    @Override
    public void visit(Link link) {
        linkCount++;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getLinkCount() {
        return linkCount;
    }
}

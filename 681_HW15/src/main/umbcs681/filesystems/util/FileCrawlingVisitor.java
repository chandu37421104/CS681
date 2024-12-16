package umbcs681.filesystems.util;

import umbcs681.filesystems.fs.*;


import java.util.concurrent.ConcurrentLinkedQueue;

public class FileCrawlingVisitor implements FSVisitor {
    private ConcurrentLinkedQueue<File> files;

    public FileCrawlingVisitor() {
        this.files = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void visit(Link link) {
        // Do nothing for links
    }

    @Override
    public void visit(Directory dir) {
        // Do nothing for directories
    }

    @Override
    public void visit(File file) {
        files.add(file);
    }

    public ConcurrentLinkedQueue<File> getFiles() {
        return files;
    }
}

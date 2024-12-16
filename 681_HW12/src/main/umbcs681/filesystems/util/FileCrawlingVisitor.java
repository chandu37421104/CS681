package umbcs681.filesystems.util;

import umbcs681.filesystems.fs.*;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FileCrawlingVisitor implements FSVisitor {
    private final ConcurrentLinkedQueue<String> sharedFileList;

    public FileCrawlingVisitor(ConcurrentLinkedQueue<String> sharedFileList) {
        this.sharedFileList = sharedFileList;
    }

    @Override
    public void visit(File file) {
        sharedFileList.add(file.getPath());
    }

    @Override
    public void visit(Directory directory) {
        for (FSElement child : directory.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(Link link) {
        link.getTarget().accept(this);
    }
}
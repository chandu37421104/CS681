package umbcs681.hw01.util;

import umbcs681.hw01.fs.*;
import java.util.LinkedList;
import java.util.List;

public class FileSearchVisitor implements FSVisitor {
    private String fileName;
    private List<File> foundFiles = new LinkedList<>();

    public FileSearchVisitor(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void visit(File file) {
        if (file.getName().equals(fileName)) {
            foundFiles.add(file);
        }
    }

    @Override
    public void visit(Directory directory) {
        
    }

    @Override
    public void visit(Link link) {
        
        if (link.getTarget() instanceof File) {
            File targetFile = (File) link.getTarget();
            if (targetFile.getName().equals(fileName)) {
                foundFiles.add(targetFile);
            }
        }
    }

    public List<File> getFoundFiles() {
        return foundFiles;
    }
}

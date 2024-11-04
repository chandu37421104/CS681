package umbcs681.hw01.util;

import umbcs681.hw01.fs.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.time.LocalDateTime;

public class FileCrawlingVisitor implements FSVisitor {
    private List<String> visitedPaths = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private Set<FSElement> visitedElements = new HashSet<>(); 
    @Override
    public void visit(File file) {
        if (!visitedElements.contains(file)) { 
            visitedPaths.add("Visiting file: " + file.getName());
            files.add(file);
            visitedElements.add(file);
        }
    }

    @Override
    public void visit(Directory directory) {
        if (!visitedElements.contains(directory)) { 
            visitedPaths.add("Visiting directory: " + directory.getName());
            visitedElements.add(directory); 
            for (FSElement child : directory.getChildren()) {
                child.accept(this); 
            }
        }
    }

    @Override
    public void visit(Link link) {
        if (!visitedElements.contains(link)) { 
            visitedPaths.add("Visiting link: " + link.getName());
            visitedElements.add(link);
            
            FSElement target = link.getTarget();
            if (target != null && !visitedElements.contains(target)) {
                target.accept(this); 
            }
        }
    }

    public List<String> getVisitedPaths() {
        return visitedPaths;
    }

    public Stream<File> files() {
        return files.stream();
    }

    public long countJavaFilesAfter(LocalDateTime date) {
        return files()
            .filter(file -> file.getName().endsWith(".java")) // Check for .java extension
            .filter(file -> file.getCreationTime().isAfter(date)) // Check creation date
            .count();
    }
}

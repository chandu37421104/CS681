package umbcs681.hw01.util;

import umbcs681.hw01.fs.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileCrawlingVisitor implements FSVisitor {
    private List<FSElement> elements = new ArrayList<>();
    private Set<Directory> visitedDirectories = new HashSet<>();

    @Override
    public void visit(File file) {
        elements.add(file);
    }

    @Override
    public void visit(Directory directory) {
        if (!visitedDirectories.add(directory)) {  
            return; 
        }
        elements.add(directory);
        directory.getChildren().forEach(child -> child.accept(this));
    }

    @Override
    public void visit(Link link) {
        elements.add(link);
        FSElement target = link.getTarget();
        if (target != null && target instanceof Directory && !visitedDirectories.contains(target)) {
            target.accept(this);
        }
    }

    public Stream<FSElement> getElementsStream() {
        return elements.stream();
    }

    public List<String> getVisitedPaths() {
        return getElementsStream()
               .map(element -> "Visiting " + (element instanceof File ? "file: " : element instanceof Directory ? "directory: " : "link: ") + element.getName())
               .collect(Collectors.toList());
    }

    public Map<String, Long> countFilesByType() {
        return getElementsStream()
               .filter(e -> e instanceof File)
               .map(e -> (File)e)
               .collect(Collectors.groupingBy(
                   file -> getFileExtension(file),
                   Collectors.counting()
               ));
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        return name.contains(".") ? name.substring(name.lastIndexOf('.') + 1) : "";
    }
}
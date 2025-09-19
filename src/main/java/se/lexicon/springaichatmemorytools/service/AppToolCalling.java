package se.lexicon.springaichatmemorytools.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppToolCalling {

    List<String> storageSimulation = new ArrayList<>();

    public AppToolCalling() {
        storageSimulation.addAll(List.of("Ivar", "Calle", "Johan", "Melker", "Tjorven", "Malin"));
        System.out.println("StorageSimulation: " + storageSimulation);
    }

    @Tool(description = "Fetch all the names from the storage")
    public List<String> fetchAllNames() {
        System.out.println("Fetching All Names...");
        List<String> result = storageSimulation.stream().toList();
        System.out.println("Stored Names: " + result);
        return result;
    }

    @Tool(description = "Add a new name to the storage")
    public String addNewName(String newName) {
        System.out.println("Adding New Name: " + newName);
        storageSimulation.add(newName);
        return "Operation successful! New name added: " + newName;
    }

    @Tool(description = "Find all names that contains one or some specific letters")
    public String findByLetters(String letters) {
        System.out.println("Finding by Letter: " + letters);
        List<String> result = storageSimulation.stream()
                .filter(s -> s.contains(letters))
                .toList();
        if (result.isEmpty()) {
            System.out.println("No such letter: " + letters);
            return "No such letter: " + letters;
        }else  {
            return "Name found: " + String.join(", ", result);
        }
    }

    @Tool(description = "Remove a name from the storage")
    public String removeNewName(String newName) {
        System.out.println("Removing New Name: " + newName);
        if (storageSimulation.remove(newName)) return "Operation successful! New name removed: " + newName;
        else return "Operation failed! New name not found: " + newName;
    }
}

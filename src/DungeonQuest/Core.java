package DungeonQuest;

// Imports
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


// Class to define the Character / with constructor
class Character {
    private String name;
    private String characterClass;
    private int health;
    private int attack;
    private int defense;

    // Constructor
    public Character(String name, String characterClass, int health, int attack, int defense) {
        this.name = name;
        this.characterClass = characterClass;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nClass: " + characterClass +
                "\nHealth: " + health + "\nAttack: " + attack + "\nDefense: " + defense;
    }
}

// Class to manage class definitions
class ClassData {
    private static final Map<String, int[]> classes = new HashMap<>();

    static {
        // Define class stats: {Health, Attack, Defense}
        classes.put("Warrior", new int[]{100, 15, 10});
        classes.put("Mage", new int[]{70, 20, 5});
        classes.put("Rogue", new int[]{80, 18, 7});
    }

    public static int[] getClassStats(String characterClass) {
        return classes.get(characterClass);
    }

    public static void displayClasses() {
        System.out.println("Available Classes:");
        for (String characterClass : classes.keySet()) {
            int[] stats = classes.get(characterClass);
            System.out.printf("%s - Health: %d, Attack: %d, Defense: %d%n",
                    characterClass, stats[0], stats[1], stats[2]);
        }
    }
}

// Class for Character Creation
class CharacterCreator {
    public static Character createCharacter() {
        Scanner scanner = new Scanner(System.in);

        // Get character name
        System.out.print("Enter your character's name: ");
        String name = scanner.nextLine();

        // Choose class
        System.out.println("\nChoose a class:");
        ClassData.displayClasses();

        String chosenClass;
        while (true) {
            System.out.print("Enter class name: ");
            chosenClass = scanner.nextLine();
            if (ClassData.getClassStats(chosenClass) != null) break;
            System.out.println("Invalid class. Please choose again.");
        }

        // Fetch stats for the chosen class
        int[] stats = ClassData.getClassStats(chosenClass);

        // Create and return the character
        return new Character(name, chosenClass, stats[0], stats[1], stats[2]);
    }
}

// Main Game with Menu
public class Core {
    private static final int GRID_SIZE = 5; // Map size
    private static int playerX = 2; // Start pos X
    private static int playerY = 2; // Start pos Y

    private static Character playerCharacter; // Instance of the Player's Character

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isExploring = true;

        loadCharacter();

        while (true) {
            System.out.println("\n=== DungeonQuest ===");
            System.out.println("1. Create a New Character");
            System.out.println("2. View Character");
            System.out.println("3. Save Character");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    playerCharacter = CharacterCreator.createCharacter();
                    System.out.println("\nCharacter created successfully!");

                    break;
                case 2:
                    if (playerCharacter == null) {
                        System.out.println("\nNo character created yet!");
                    } else {
                        System.out.println("\nCurrent Character:");
                        System.out.println(playerCharacter);
                    }
                    break;
                case 3:
                    if (playerCharacter != null) {
                        saveCharacter();
                        System.out.println("\nCharacter saved successfully!");
                    } else {
                        System.out.println("\nNo character to save!");
                    }
                    break;
                case 4:
                    System.out.println("Exiting the game. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Save Character Function
    private static void saveCharacter() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("character.txt"))) {
            writer.println(playerCharacter.getName());
            writer.println(playerCharacter.getCharacterClass());
            writer.println(playerCharacter.getHealth());
            writer.println(playerCharacter.getAttack());
            writer.println(playerCharacter.getDefense());
        } catch (IOException e) {
            System.out.println("Error saving character: " + e);
        }
    }

    // Load Character Function
    private static void loadCharacter() {
        try (BufferedReader reader = new BufferedReader(new FileReader("character.txt"))) {
            String name = reader.readLine();
            String characterClass = reader.readLine();
            int health = Integer.parseInt(reader.readLine());
            int attack = Integer.parseInt(reader.readLine());
            int defense = Integer.parseInt(reader.readLine());

            playerCharacter = new Character(name, characterClass, health, attack, defense);
            System.out.println("Character loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No saved character to load: " + e);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading character: " + e.getMessage());
        }
    }
}

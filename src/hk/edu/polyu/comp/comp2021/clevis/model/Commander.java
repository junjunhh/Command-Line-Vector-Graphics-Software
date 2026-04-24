package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.*;

/**
 * The Commander class acts as the controller for parsing and processing user commands.
 * It interprets raw string input, validates command syntax and argument counts,
 * and delegates the actual execution logic to the {@link ShapeManager}.
 */
public class Commander {
    private final ShapeManager shapeManager;

    /**
     * Constructs a new Commander instance.
     *
     * @param shapeManager The ShapeManager instance responsible for managing the shapes and executing operations.
     */
    public Commander(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
    }

    /**
     * Processes a single line of user input.
     * It splits the input string, identifies the command type (e.g., "rectangle", "move"),
     * and dispatches the request to the specific internal processing method.
     *
     * @param input The raw command string entered by the user. If null or empty, it is ignored or reported.
     */
    public void process(String input) {
        if (input == null){
            System.out.println("clevis> Error: Empty command.");
            return;
        }
        String[] command = input.trim().split("\\s+"); // Split command by whitespace
        String type = command[0].toLowerCase(); // Convert command type to lowercase for case-insensitive matching

        switch(type){
            case "rectangle":
                processRectangle(command);
                break;
            case "square":
                processSquare(command);
                break;
            case "circle":
                processCircle(command);
                break;
            case "line":
                processLine(command);
                break;
            case "group":
                processGroup(command);
                break;
            case "ungroup":
                processUngroup(command);
                break;
            case "delete":
                processDelete(command);
                break;
            case "move":
                processMove(command);
                break;
            case "boundingbox":
                processBoundingBox(command);
                break;
            case "shapeat":
                processShapeAt(command);
                break;
            case "intersect":
                processIntersect(command);
                break;
            case "list":
                processList(command);
                break;
            case "listall":
                processListAll(command);
                break;
            case "redo":
                processRedo(command);
                break;
            case "undo":
                processUndo(command);
                break;
            default:
                System.out.println("clevis> Error: Unknown command '" + type + "'.");
        }
    }

    /**
     * Tries to parse a string into a Double using regex validation.
     * Prints an error message if parsing fails.
     *
     * @param value The string to parse.
     * @param paramName The name of the parameter (for error logging).
     * @return The Double value, or null if validation failed.
     */
    private Double tryParseDouble(String value) {
        if (value == null||!value.matches("-?\\d+(\\.\\d+)?")) {
            System.out.println("clevis> Error: '" + value + "' is not a valid number.");
            return null;
        }
        return Double.parseDouble(value);
    }

    /**
     * Validates that a name complies with the application's naming conventions.
     * A valid name must consist only of letters (a-z, A-Z), numbers (0-9), and underscores (_).
     * If the name is invalid or null, an error message is printed to the console.
     *
     * @param name The name string to validate.
     * @return true if the name is valid; false otherwise.
     */
    private boolean isValidName(String name) {
        if(name==null||!name.matches("[a-zA-Z0-9_]+")) {
            System.out.println("clevis> Error: Invalid name '" + name + "'. Names must contain only letters, numbers, or underscores.");
            return false;
        }
        return true;
    }

    // Processing Method
    /**
     * Parses and executes the 'rectangle' command.
     * Usage: rectangle &lt;name&gt; &lt;x&gt; &lt;y&gt; &lt;width&gt; &lt;height&gt;
     *
     * @param command The tokenized command array.
     */
    private void processRectangle(String[] command) {
        if (command.length != 6){
            System.out.println("clevis> Error: Usage: rectangle <name> <x> <y> <width> <height>");
            return;
        }
        String name = command[1];
        Double x = tryParseDouble(command[2]);
        Double y = tryParseDouble(command[3]);
        Double width = tryParseDouble(command[4]);
        Double height = tryParseDouble(command[5]);

        if (!isValidName(name)) return;
        if(x==null||y==null||width==null||height==null) return;

        shapeManager.createRectangle(name, x, y, width, height);
    }

    /**
     * Parses and executes the 'square' command.
     * Usage: square &lt;name&gt; &lt;x&gt; &lt;y&gt; &lt;length&gt;
     *
     * @param command The tokenized command array.
     */
    private void processSquare(String[] command) {
        if (command.length != 5){
            System.out.println("clevis> Error: Usage: square <name> <x> <y> <length>");
            return;
        }
        String name = command[1];
        Double x = tryParseDouble(command[2]);
        Double y = tryParseDouble(command[3]);
        Double length = tryParseDouble(command[4]);

        if (!isValidName(name)) return;
        if(x==null||y==null||length==null) return;

        shapeManager.createSquare(name, x, y, length);
    }

    /**
     * Parses and executes the 'circle' command.
     * Usage: circle &lt;name&gt; &lt;x&gt; &lt;y&gt; &lt;radius&gt;
     *
     * @param command The tokenized command array.
     */
    private void processCircle(String[] command) {
        if (command.length != 5){
            System.out.println("clevis> Error: Usage: circle <name> <x> <y> <radius>");
            return;
        }
        String name = command[1];
        Double x = tryParseDouble(command[2]);
        Double y = tryParseDouble(command[3]);
        Double radius = tryParseDouble(command[4]);

        if (!isValidName(name)) return;
        if(x==null||y==null||radius==null) return;

        shapeManager.createCircle(name, x, y, radius);
    }

    /**
     * Parses and executes the 'line' command.
     * Usage: line &lt;name&gt; &lt;x1&gt; &lt;y1&gt; &lt;x2&gt; &lt;y2&gt;
     *
     * @param command The tokenized command array.
     */
    private void processLine(String[] command) {
        if (command.length != 6){
            System.out.println("clevis> Error: Usage: line <name> <x1> <y1> <x2> <y2>");
            return;
        }
        String name = command[1];
        Double x1 = tryParseDouble(command[2]);
        Double y1 = tryParseDouble(command[3]);
        Double x2 = tryParseDouble(command[4]);
        Double y2 = tryParseDouble(command[5]);

        if (!isValidName(name)) return;
        if(x1==null||y1==null||x2==null||y2==null) return;

        shapeManager.createLine(name, x1, y1, x2, y2);
    }

    /**
     * Parses and executes the 'group' command.
     * Usage: group &lt;group_name&gt; &lt;shape_name1&gt; &lt;shape_name2&gt; ...
     *
     * @param command The tokenized command array.
     */
    private void processGroup(String[] command){
        if (command.length < 3){
            System.out.println("clevis> Error: Usage: group <group_name> <shape_name1> <shape_name2> ...");
            return;
        }
        String name = command[1];

        if (!isValidName(name)) return;

        List<String> shapeNames = new ArrayList<>();
        for (int i = 2; i < command.length; i++){
            shapeNames.add(command[i]);
        }
        shapeManager.CreateGroup(name, shapeNames);
    }

    /**
     * Parses and executes the 'ungroup' command.
     * Usage: ungroup &lt;group_name&gt;
     *
     * @param command The tokenized command array.
     */
    private void processUngroup(String[] command){
        if (command.length != 2){
            System.out.println("clevis> Error: Usage: ungroup <group_name>");
            return;
        }
        String group_name = command[1];
        shapeManager.ungroup(group_name);
    }

    /**
     * Parses and executes the 'delete' command.
     * Usage: delete &lt;name&gt;
     *
     * @param command The tokenized command array.
     */
    private void processDelete(String[] command){
        if (command.length != 2){
            System.out.println("clevis> Error: Usage: delete <name>");
            return;
        }
        shapeManager.delete(command[1]);
    }

    /**
     * Parses and executes the 'move' command.
     * Usage: move &lt;name&gt; &lt;x&gt; &lt;y&gt;
     *
     * @param command The tokenized command array.
     */
    private void processMove(String[] command){
        if (command.length != 4){
            System.out.println("clevis> Error: Usage: move <name> <x> <y>");
            return;
        }
        String name = command[1];
        Double x = tryParseDouble(command[2]);
        Double y = tryParseDouble(command[3]);

        if(x==null||y==null) return;
        shapeManager.move(name, x, y);
    }

    /**
     * Parses and executes the 'boundingbox' command.
     * Usage: boundingbox &lt;name&gt;
     *
     * @param command The tokenized command array.
     */
    private void processBoundingBox(String[] command){
        if (command.length != 2){
            System.out.println("clevis> Error: Usage: boundingbox <name>");
            return;
        }
        shapeManager.boundingbox(command[1]);
    }

    /**
     * Parses and executes the 'shapeat' command.
     * Usage: shapeat &lt;x&gt; &lt;y&gt;
     *
     * @param command The tokenized command array.
     */
    private void processShapeAt(String[] command){
        if (command.length != 3){
            System.out.println("clevis> Error: Usage: ShapeAt <x> <y>");
            return;
        }
        Double x = tryParseDouble(command[1]);
        Double y = tryParseDouble(command[2]);

        if(x==null||y==null) return;
        String res = shapeManager.shapeat(x, y);
        System.out.println("clevis> The shape at (" + x + ", " + y + ") is '" + res + "'.");
    }

    /**
     * Parses and executes the 'intersect' command.
     * Usage: intersect &lt;name1&gt; &lt;name2&gt;
     *
     * @param command The tokenized command array.
     */
    private void processIntersect(String[] command){
        if (command.length != 3){
            System.out.println("clevis> Error: Usage: intersect <name1> <name2>");
            return;
        }
        String name1 = command[1];
        String name2 = command[2];

        boolean Flag = shapeManager.intersection(name1, name2);
        if(Flag) System.out.println("clevis> '" + name1 + "' and '" + name2 + "' intersect.");
        else System.out.println("clevis> '" + name1 + "' and '" + name2 + "' do not intersect.");
    }

    /**
     * Parses and executes the 'list' command.
     * Usage: list &lt;name&gt;
     *
     * @param command The tokenized command array.
     */
    private void processList(String[] command){
        if (command.length != 2){
            System.out.println("clevis> Error: Usage: list <name>");
            return;
        }
        String name = command[1];
        shapeManager.list(name);
    }

    /**
     * Parses and executes the 'listall' command.
     * Usage: listall
     *
     * @param command The tokenized command array.
     */
    private void processListAll(String[] command){
        if (command.length != 1){
            System.out.println("clevis> Error: Usage: listall");
            return;
        }
        shapeManager.listall();
    }

    //------------------------------------------

    /**
     * Parses and executes the 'undo' command.
     * Reverts the last operation.
     *
     * @param command The tokenized command array.
     */
    private void processUndo(String[] command) {
        if (command.length != 1) {
            System.out.println("clevis> Error: Usage: undo");
            return;
        }
        shapeManager.undo();
    }

    /**
     * Parses and executes the 'redo' command.
     * Reapplies the last undone operation.
     *
     * @param command The tokenized command array.
     */
    private void processRedo(String[] command) {
        if (command.length != 1) {
            System.out.println("clevis> Error: Usage: redo");
            return;
        }
        shapeManager.redo();
    }

}
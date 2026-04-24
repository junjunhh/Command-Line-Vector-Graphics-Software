package hk.edu.polyu.comp.comp2021.clevis;

import hk.edu.polyu.comp.comp2021.clevis.controller.CommandProcessor;
import hk.edu.polyu.comp.comp2021.clevis.Logger.CommandLogger;
import hk.edu.polyu.comp.comp2021.clevis.model.Commander;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeManager;
import java.util.Scanner;

/**
 * The main entry point for the Clevis (Command Line Vector Graphics Software) application.
 * This class handles the initialization of the application components, processes command-line arguments,
 * and manages the main user input loop.
 */
public class Application {
    private final CommandLogger logger;
    private final CommandProcessor commandProcessor;

    /**
     * Initializes the Application with the necessary components and log file paths.
     * Sets up the ShapeManager, Commander, CommandProcessor, and CommandLogger.
     *
     * @param htmlLogFile The file path where the HTML format log will be written.
     * @param txtLogFile  The file path where the plain text format log will be written.
     */
    public Application(String htmlLogFile, String txtLogFile) {
        ShapeManager shapeManager = new ShapeManager();
        Commander commander = new Commander(shapeManager);
        this.logger = new CommandLogger(htmlLogFile, txtLogFile);
        this.commandProcessor = new CommandProcessor(shapeManager, commander, logger);
    }

    /**
     * Starts the main execution loop of the application.
     * This method reads commands from the standard input (console), logs them,
     * and delegates the processing to the shared command processor.
     * The loop continues until the user inputs the "quit" command.
     */
    public void run(){
        Scanner scanner = new Scanner(System.in);
        //Welcome...
        System.out.println("Clevis - Command Line Vector Graphics Software");

        while(true){
            System.out.print("clevis> ");
            String rawCommand = scanner.nextLine().trim();

            if(rawCommand.equalsIgnoreCase("quit")) {
                break;
            }

            if(rawCommand.equalsIgnoreCase("help")) {
                showHelp();
                continue;
            }

            if(rawCommand.isEmpty()) {
                continue;
            }

            commandProcessor.processToConsole(rawCommand);
        }

        scanner.close();
        logger.close();
        System.out.println("Clevis quit successfully.");
    }

    /**
     * Displays the list of available commands and their syntax to the standard output.
     * This serves as a quick reference guide for the user.
     */
    private void showHelp() {
        System.out.println("Clevis - Available Commands:");
        System.out.println();
        System.out.println("Shape Creation:");
        System.out.println("  rectangle <name> <x> <y> <width> <height>  - Create a rectangle");
        System.out.println("  square <name> <x> <y> <length>             - Create a square");
        System.out.println("  circle <name> <x> <y> <radius>             - Create a circle");
        System.out.println("  line <name> <x1> <y1> <x2> <y2>            - Create a line");
        System.out.println();
        System.out.println("Shape Management:");
        System.out.println("  group <group_name> <shape1> <shape2> ...   - Group shapes together");
        System.out.println("  ungroup <group_name>                       - Ungroup a group");
        System.out.println("  delete <name>                              - Delete a shape or group");
        System.out.println("  move <name> <x> <y>                        - Move a shape by offset (x, y)");
        System.out.println();
        System.out.println("Shape Query:");
        System.out.println("  boundingbox <name>                         - Show bounding box of a shape");
        System.out.println("  shapeat <x> <y>                            - Find shape at coordinates");
        System.out.println("  intersect <name1> <name2>                  - Check if two shapes intersect");
        System.out.println("  list <name>                                - List details of a shape");
        System.out.println("  listall                                    - List all shapes");
        System.out.println();
        System.out.println("History:");
        System.out.println("  undo                                       - Undo last operation");
        System.out.println("  redo                                       - Redo last undone operation");
        System.out.println();
        System.out.println("General:");
        System.out.println("  help                                       - Show this help message");
        System.out.println("  quit                                       - Exit the application");
        System.out.println();
    }

    /**
     * The main method serves as the entry point of the program.
     * It parses command-line arguments to determine custom log file paths (if provided)
     * and initializes the Application instance.
     *
     * @param args Command line arguments. Supports optional flags:
     * -html <path> : Specifies the HTML log file path.
     * -txt <path>  : Specifies the TXT log file path.
     */
    public static void main(String[] args){
        String htmlFile="log.html";
        String txtFile="log.txt";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-html") && i + 1 < args.length) {
                htmlFile = args[i + 1];
                i++;
            } else if (args[i].equals("-txt") && i + 1 < args.length) {
                txtFile = args[i + 1];
                i++;
            }
        }
        System.out.println("HTML logger:"+htmlFile);
        System.out.println("TXT logger:"+txtFile);

        Application app=new Application(htmlFile,txtFile);
        app.run();
    }
}

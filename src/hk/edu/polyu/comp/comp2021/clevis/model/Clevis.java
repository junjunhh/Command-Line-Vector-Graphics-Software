package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.Logger.CommandLogger;
import java.util.Scanner;

/**
 * The Clevis class serves as the primary controller for the application session.
 * It integrates the ShapeManager, Commander, and Logger to handle the main application loop,
 * processing user input and managing the overall flow of the program.
 */
public class Clevis {

    private final ShapeManager shapeManager;
    private final Commander commander;
    private final CommandLogger logger;
    private final Scanner scanner;
    private boolean isRunning;

    /**
     * Constructs a new Clevis controller with the specified components and log paths.
     * Initializes the logger and the input scanner.
     *
     * @param shapeManager The manager responsible for maintaining the state of all shapes.
     * @param commander    The component responsible for parsing and executing specific commands.
     * @param htmlLogPath  The file path for recording the command log in HTML format.
     * @param txtLogPath   The file path for recording the command log in plain text format.
     */
    public Clevis(ShapeManager shapeManager, Commander commander, String htmlLogPath, String txtLogPath){
        this.shapeManager = shapeManager;
        this.commander = commander;
        this.logger = new CommandLogger(htmlLogPath, txtLogPath);
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    /**
     * Starts the interactive command-line interface (CLI) loop.
     * Continually prompts the user for input ("clevis> "), logs the input,
     * and delegates the command processing to the Commander until the session is terminated.
     */
    public void start(){
        System.out.println("Start!");

        while(isRunning){
            System.out.print("clevis> ");
            String input = scanner.nextLine().trim();

            if(!input.isEmpty()){
                logger.logCommand(input);
                processCommand(input);
            }
        }
    }

    /**
     * Delegates the processing of a raw command string to the Commander.
     *
     * @param command The raw command string entered by the user.
     */
    public void processCommand(String command){
        commander.process(command);
    }

    /**
     * Retrieves the ShapeManager instance associated with this session.
     *
     * @return The ShapeManager instance.
     */
    public ShapeManager getShapeManager(){
        return shapeManager;
    }
}
package hk.edu.polyu.comp.comp2021.clevis.controller;

import hk.edu.polyu.comp.comp2021.clevis.Logger.CommandLogger;
import hk.edu.polyu.comp.comp2021.clevis.model.Commander;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeManager;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Thin adapter around the existing command parser and model operations.
 * The CLI can stream output directly, while GUI callers can capture the same output as text.
 */
public class CommandProcessor {
    private static final Object OUTPUT_CAPTURE_LOCK = new Object();

    private final ShapeManager shapeManager;
    private final Commander commander;
    private final CommandLogger logger;

    public CommandProcessor(ShapeManager shapeManager, Commander commander, CommandLogger logger) {
        this.shapeManager = shapeManager;
        this.commander = commander;
        this.logger = logger;
    }

    /**
     * Executes a command and leaves command output on System.out.
     *
     * @param rawCommand command entered by the user
     */
    public void processToConsole(String rawCommand) {
        if (rawCommand == null || rawCommand.trim().isEmpty()) {
            return;
        }
        log(rawCommand);
        commander.process(rawCommand);
    }

    /**
     * Executes a command and returns the text that the legacy command layer printed.
     *
     * @param rawCommand command entered by the user
     * @return printed command result, suitable for a GUI output panel
     */
    public String processToString(String rawCommand) {
        if (rawCommand == null || rawCommand.trim().isEmpty()) {
            return "";
        }

        log(rawCommand);

        synchronized (OUTPUT_CAPTURE_LOCK) {
            PrintStream originalOut = System.out;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            PrintStream capture = new PrintStream(buffer);
            try {
                System.setOut(capture);
                commander.process(rawCommand);
            } finally {
                capture.flush();
                System.setOut(originalOut);
            }
            return buffer.toString();
        }
    }

    public ShapeManager getShapeManager() {
        return shapeManager;
    }

    public List<ShapeSnapshot> getShapeSnapshots() {
        return shapeManager.getShapeSnapshots();
    }

    public void closeLogger() {
        if (logger != null) {
            logger.close();
        }
    }

    private void log(String rawCommand) {
        if (logger != null) {
            logger.logCommand(rawCommand);
        }
    }
}

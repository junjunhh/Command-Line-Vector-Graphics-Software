package hk.edu.polyu.comp.comp2021.clevis.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the logging of user commands throughout the application session.
 * This class buffers commands in memory and supports exporting the command history
 * to both HTML (table format) and TXT (plain text) files.
 */
public class CommandLogger {
    private final String htmlFilePath;
    private final String txtFilePath;
    private int commandCount;
    private final List<String> commands;

    /**
     * Constructs a new CommandLogger with specified output file paths.
     * Initializes the internal storage for command history.
     *
     * @param htmlFilePath The file system path where the HTML formatted log will be saved.
     * @param txtFilePath  The file system path where the plain text log will be saved.
     */
    public CommandLogger(String htmlFilePath, String txtFilePath) {
        this.htmlFilePath = htmlFilePath;
        this.txtFilePath = txtFilePath;
        this.commandCount = 0;
        this.commands = new ArrayList<>();
    }

    /**
     * Records a command into the internal history buffer.
     * Increments the total command counter.
     *
     * @param command The raw command string entered by the user.
     */
    public void logCommand(String command) {
        commandCount++;
        commands.add(command);
    }

    /**
     * Generates and writes the HTML log file based on the recorded commands.
     * The HTML file includes a table with operation indices and command details.
     * Any necessary HTML escaping is applied to the command strings.
     */
    public void writeHtmlLog() {
        try (FileWriter writer = new FileWriter(htmlFilePath)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n<head>\n");
            writer.write("<title>Clevis Command Log</title>\n");
            writer.write("<style>\n");
            writer.write("table { border-collapse: collapse; width: 100%; }\n");
            writer.write("th, td { border: 1px solid black; padding: 8px; text-align: left; }\n");
            writer.write("th { background-color: #f2f2f2; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n<body>\n");
            writer.write("<h1>Clevis Command Log</h1>\n");
            writer.write("<table>\n");
            writer.write("<tr><th>Operation Index</th><th>Command</th></tr>\n");

            for (int i = 0; i < commands.size(); i++) {
                writer.write(String.format("<tr><td>%d</td><td>%s</td></tr>\n",
                        i + 1, escapeHtml(commands.get(i))));
            }

            writer.write("</table>\n");
            writer.write("</body>\n</html>\n");
        } catch (IOException e) {
            System.err.println("Error writing HTML log: " + e.getMessage());
        }
    }

    /**
     * Generates and writes the plain text log file.
     * Each recorded command is written on a new line.
     */
    public void writeTxtLog() {
        try (FileWriter writer = new FileWriter(txtFilePath)) {
            for (String command : commands) {
                writer.write(command + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to TXT log: " + e.getMessage());        
        }
    }

    /**
     * Finalizes the logging process.
     * Triggers the writing of both HTML and TXT log files to the disk and prints a confirmation message.
     * This should be called when the application is terminating.
     */
    public void close() {
        writeHtmlLog();
        writeTxtLog();
        System.out.println("Log files generated: " + htmlFilePath + ", " + txtFilePath);
    }

    /**
     * Helper method to escape special characters in command strings to ensure valid HTML syntax.
     * Replaces characters like '<', '>', '&', etc., with their corresponding HTML entities.
     *
     * @param text The input string to escape.
     * @return The escaped string safe for HTML embedding.
     */
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * Returns the total number of commands logged during this session.
     *
     * @return The command count.
     */
    public int getCommandCount() {
        return commandCount;
    }
}
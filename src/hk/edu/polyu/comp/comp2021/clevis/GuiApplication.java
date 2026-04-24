package hk.edu.polyu.comp.comp2021.clevis;

import hk.edu.polyu.comp.comp2021.clevis.Logger.CommandLogger;
import hk.edu.polyu.comp.comp2021.clevis.controller.CommandProcessor;
import hk.edu.polyu.comp.comp2021.clevis.model.Commander;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeManager;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeSnapshot;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing entry point for visualizing Clevis commands without replacing the CLI.
 */
public class GuiApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClevisFrame frame = new ClevisFrame();
            frame.setVisible(true);
        });
    }

    private static final class ClevisFrame extends JFrame {
        private final JTextField commandInput = new JTextField();
        private final JTextArea outputArea = new JTextArea();
        private final ShapeCanvas canvas = new ShapeCanvas();
        private CommandProcessor commandProcessor;

        private ClevisFrame() {
            super("Clevis - Vector Graphics");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setMinimumSize(new Dimension(960, 680));
            setLocationByPlatform(true);

            createNewSession();
            buildLayout();
            bindEvents();
            appendOutput("Clevis GUI ready. Enter the same commands used in the CLI.");
        }

        private void createNewSession() {
            if (commandProcessor != null) {
                commandProcessor.closeLogger();
            }
            ShapeManager shapeManager = new ShapeManager();
            Commander commander = new Commander(shapeManager);
            CommandLogger logger = new CommandLogger("gui_log.html", "gui_log.txt");
            commandProcessor = new CommandProcessor(shapeManager, commander, logger);
            canvas.setSnapshots(commandProcessor.getShapeSnapshots());
        }

        private void buildLayout() {
            JPanel commandPanel = new JPanel(new BorderLayout(8, 0));
            JButton runButton = new JButton("Run");
            JButton repaintButton = new JButton("Repaint");
            JButton resetButton = new JButton("New");
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(runButton);
            buttonPanel.add(repaintButton);
            buttonPanel.add(resetButton);

            commandPanel.add(commandInput, BorderLayout.CENTER);
            commandPanel.add(buttonPanel, BorderLayout.EAST);

            outputArea.setEditable(false);
            outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            JScrollPane outputScroll = new JScrollPane(outputArea);
            outputScroll.setPreferredSize(new Dimension(960, 190));

            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, canvas, outputScroll);
            splitPane.setResizeWeight(0.74);
            splitPane.setBorder(null);

            add(commandPanel, BorderLayout.NORTH);
            add(splitPane, BorderLayout.CENTER);
            pack();

            runButton.addActionListener(event -> runCommand());
            repaintButton.addActionListener(event -> repaintCanvas());
            resetButton.addActionListener(event -> {
                createNewSession();
                outputArea.setText("");
                appendOutput("New Clevis session started.");
            });
        }

        private void bindEvents() {
            commandInput.addActionListener(event -> runCommand());
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent event) {
                    commandProcessor.closeLogger();
                }
            });
        }

        private void runCommand() {
            String command = commandInput.getText().trim();
            if (command.isEmpty()) {
                return;
            }

            appendOutput("> " + command);
            if ("quit".equalsIgnoreCase(command)) {
                dispose();
                return;
            }
            if ("help".equalsIgnoreCase(command)) {
                appendOutput(helpText());
                commandInput.setText("");
                return;
            }

            String result = commandProcessor.processToString(command).trim();
            if (!result.isEmpty()) {
                appendOutput(result);
            }
            commandInput.setText("");
            repaintCanvas();
        }

        private void repaintCanvas() {
            canvas.setSnapshots(commandProcessor.getShapeSnapshots());
        }

        private void appendOutput(String text) {
            outputArea.append(text);
            outputArea.append(System.lineSeparator());
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        }

        private String helpText() {
            return "Commands: rectangle, square, circle, line, group, ungroup, delete, move, "
                    + "boundingbox, shapeat, intersect, list, listall, undo, redo, quit";
        }
    }

    private static final class ShapeCanvas extends JPanel {
        private static final int MARGIN = 44;
        private List<ShapeSnapshot> snapshots = new ArrayList<>();

        private ShapeCanvas() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(960, 480));
        }

        private void setSnapshots(List<ShapeSnapshot> snapshots) {
            this.snapshots = new ArrayList<>(snapshots);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawGrid(g);

            if (snapshots.isEmpty()) {
                g.setColor(new Color(90, 96, 112));
                g.drawString("No shapes to display", MARGIN, MARGIN);
                g.dispose();
                return;
            }

            Bounds bounds = Bounds.from(snapshots);
            double rangeX = Math.max(bounds.maxX - bounds.minX, 1.0);
            double rangeY = Math.max(bounds.maxY - bounds.minY, 1.0);
            if (rangeX < 100) {
                double expand = (100 - rangeX) / 2.0;
                bounds.minX -= expand;
                bounds.maxX += expand;
                rangeX = 100;
            }
            if (rangeY < 100) {
                double expand = (100 - rangeY) / 2.0;
                bounds.minY -= expand;
                bounds.maxY += expand;
                rangeY = 100;
            }

            double scale = Math.min((getWidth() - 2.0 * MARGIN) / rangeX,
                    (getHeight() - 2.0 * MARGIN) / rangeY);
            g.setStroke(new BasicStroke(2.0f));
            for (ShapeSnapshot snapshot : snapshots) {
                drawShape(g, snapshot, bounds, scale);
            }
            g.dispose();
        }

        private void drawGrid(Graphics2D g) {
            g.setColor(new Color(239, 242, 247));
            for (int x = MARGIN; x < getWidth(); x += 40) {
                g.drawLine(x, 0, x, getHeight());
            }
            for (int y = MARGIN; y < getHeight(); y += 40) {
                g.drawLine(0, y, getWidth(), y);
            }
        }

        private void drawShape(Graphics2D g, ShapeSnapshot snapshot, Bounds bounds, double scale) {
            g.setColor(colorFor(snapshot.getType()));
            if (snapshot.isLine()) {
                int x1 = screenX(snapshot.getX1(), bounds, scale);
                int y1 = screenY(snapshot.getY1(), bounds, scale);
                int x2 = screenX(snapshot.getX2(), bounds, scale);
                int y2 = screenY(snapshot.getY2(), bounds, scale);
                g.drawLine(x1, y1, x2, y2);
                drawLabel(g, snapshot.getName(), x2, y2);
                return;
            }

            int x = screenX(snapshot.getX(), bounds, scale);
            int y = screenY(snapshot.getY(), bounds, scale);
            int width = Math.max(1, (int) Math.round(snapshot.getWidth() * scale));
            int height = Math.max(1, (int) Math.round(snapshot.getHeight() * scale));

            if ("Circle".equals(snapshot.getType())) {
                g.drawOval(x, y, width, height);
            } else {
                g.drawRect(x, y, width, height);
            }
            drawLabel(g, snapshot.getName(), x + width, y);
        }

        private int screenX(double x, Bounds bounds, double scale) {
            return MARGIN + (int) Math.round((x - bounds.minX) * scale);
        }

        private int screenY(double y, Bounds bounds, double scale) {
            return MARGIN + (int) Math.round((y - bounds.minY) * scale);
        }

        private void drawLabel(Graphics2D g, String label, int x, int y) {
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 12.0f));
            g.drawString(label, x + 5, y - 5);
        }

        private Color colorFor(String type) {
            if ("Rectangle".equals(type)) {
                return new Color(36, 99, 235);
            }
            if ("Square".equals(type)) {
                return new Color(5, 150, 105);
            }
            if ("Circle".equals(type)) {
                return new Color(220, 38, 38);
            }
            return new Color(111, 75, 190);
        }
    }

    private static final class Bounds {
        private double minX = Double.POSITIVE_INFINITY;
        private double minY = Double.POSITIVE_INFINITY;
        private double maxX = Double.NEGATIVE_INFINITY;
        private double maxY = Double.NEGATIVE_INFINITY;

        private static Bounds from(List<ShapeSnapshot> snapshots) {
            Bounds bounds = new Bounds();
            for (ShapeSnapshot snapshot : snapshots) {
                bounds.include(snapshot.getX(), snapshot.getY());
                bounds.include(snapshot.getX() + snapshot.getWidth(), snapshot.getY() + snapshot.getHeight());
            }
            return bounds;
        }

        private void include(double x, double y) {
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
    }
}

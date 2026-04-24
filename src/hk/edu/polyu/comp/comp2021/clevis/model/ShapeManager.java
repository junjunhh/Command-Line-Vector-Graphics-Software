package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Manages the lifecycle and state of all geometric shapes in the Clevis application.
 * This class acts as the "Model" or "Receiver," handling creation, grouping, manipulation,
 * storage, and the Undo/Redo history of shapes.
 */
public class ShapeManager {
    // Stores all active shapes by their unique name.
    private final Map<String, Shape> ShapesMap;
    
    // Maintains the Z-order (creation/grouping order) of shapes. 
    // Elements at higher indices are "on top" of elements at lower indices.
    private final List<String> WeightList;
    
    // Stores names of shapes that are currently part of a Group.
    // These shapes are "locked" and cannot be selected or modified individually.
    private final Set<String> Locker;

    private final Stack<Cache> undoStack = new Stack<>();
    private final Stack<Cache> redoStack = new Stack<>();

    /**
     * Represents a snapshot of the ShapeManager's state.
     * Used to support Undo and Redo operations by storing deep copies of data.
     */
    private class Cache {
        private final Map<String, Shape> shapesBackup;
        private final List<String> weightListBackup;
        private final Set<String> lockerBackup;

        /**
         * Constructs a backup of the current state.
         * Performs a deep copy of all shapes to ensure historical data remains immutable.
         */
        public Cache() {
            this.shapesBackup = new HashMap<>();
            this.weightListBackup = new ArrayList<>();
            this.lockerBackup = new HashSet<>();

            // Deep copy every shape in the map
            for (Map.Entry<String, Shape> pair : ShapesMap.entrySet()) {
                shapesBackup.put(pair.getKey(), cloneShape(pair.getValue()));
            }
            weightListBackup.addAll(WeightList);
            lockerBackup.addAll(Locker);
        }

        /**
         * Restores the ShapeManager to the state saved in this cache.
         * Clears current data and replaces it with the backup.
         */
        public void restore() {
            ShapesMap.clear();
            WeightList.clear();
            Locker.clear();

            ShapesMap.putAll(shapesBackup);
            WeightList.addAll(weightListBackup);
            Locker.addAll(lockerBackup);
        }

        /**
         * Helper method to create a deep copy of a shape using polymorphism.
         * * @param master The shape to clone.
         * @return A new independent instance of the shape.
         */
        private Shape cloneShape(Shape master) {
            return master.copy();
        }
    }

    // ---------------------------------------------------------
    // Undo / Redo Operations
    // ---------------------------------------------------------

    /**
     * Saves the current state of the application to the Undo stack.
     * Should be called before any operation that modifies the state (create, move, delete, etc.).
     * Clears the Redo stack whenever a new action is performed.
     */
    private void saveState() {
        undoStack.push(new Cache());
        redoStack.clear();
    }

    /**
     * Reverts the last operation by restoring the state from the Undo stack.
     * The current state is pushed to the Redo stack before restoring.
     */
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("clevis> Nothing to undo.");
            return;
        }

        // Save current state to redo stack before undoing
        redoStack.push(new Cache());
        Cache cache = undoStack.pop();
        cache.restore();
        System.out.println("clevis> Undo successful.");
    }

    /**
     * Re-applies the last undone operation by restoring the state from the Redo stack.
     * The current state is pushed to the Undo stack before restoring.
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("clevis> Nothing to redo.");
            return;
        }

        // Save current state to undo stack before redoing
        undoStack.push(new Cache());
        Cache cache = redoStack.pop();
        cache.restore();
        System.out.println("clevis> Redo successful.");
    }

    // ---------------------------------------------------------
    // Core Logic
    // ---------------------------------------------------------

    /**
     * Constructs a new ShapeManager with empty storage containers.
     */
    public ShapeManager() {
        this.ShapesMap = new HashMap<>();
        this.WeightList = new ArrayList<>();
        this.Locker = new HashSet<>();
    }

    /**
     * Checks if a shape is currently locked (i.e., it belongs to a group).
     * Locked shapes cannot be directly manipulated.
     *
     * @param shapeName The name of the shape to check.
     * @return true if the shape is in the Locker, false otherwise.
     */
    public boolean isLocked(String shapeName) {
        return Locker.contains(shapeName);
    }

    /**
     * Returns immutable shape descriptions in drawing order for read-only views.
     * Groups are flattened so that their component shapes can be rendered.
     *
     * @return read-only snapshots of all visible shapes
     */
    public List<ShapeSnapshot> getShapeSnapshots() {
        List<ShapeSnapshot> snapshots = new ArrayList<>();
        for (String shapeName : WeightList) {
            Shape shape = ShapesMap.get(shapeName);
            if (shape != null) {
                addShapeSnapshots(shape, snapshots);
            }
        }
        return snapshots;
    }

    private void addShapeSnapshots(Shape shape, List<ShapeSnapshot> snapshots) {
        if (shape instanceof Group) {
            Group group = (Group) shape;
            for (Shape element : group.getElements()) {
                addShapeSnapshots(element, snapshots);
            }
            return;
        }

        if (shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) shape;
            snapshots.add(ShapeSnapshot.bounded("Rectangle", rectangle.getName(),
                    rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()));
        } else if (shape instanceof Square) {
            Square square = (Square) shape;
            snapshots.add(ShapeSnapshot.bounded("Square", square.getName(),
                    square.getX(), square.getY(), square.getSideLength(), square.getSideLength()));
        } else if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            double diameter = circle.getRadius() * 2;
            snapshots.add(ShapeSnapshot.bounded("Circle", circle.getName(),
                    circle.getCenterX() - circle.getRadius(), circle.getCenterY() - circle.getRadius(),
                    diameter, diameter));
        } else if (shape instanceof Line) {
            Line line = (Line) shape;
            snapshots.add(ShapeSnapshot.line(line.getName(), line.getPositionX_1(), line.getPositionY_1(),
                    line.getPositionX_2(), line.getPositionY_2()));
        }
    }

    /**
     * Validates if a name is suitable for a new shape.
     * Checks for null, empty strings, and duplicate names.
     *
     * @param name The name to validate.
     * @return true if the name is valid, false otherwise.
     */
    private boolean valid_name_test(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("clevis> Error: Shape name cannot be null or empty.");
            return false;
        }
        if (ShapesMap.containsKey(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' already exists.");
            return false;
        }
        return true;
    }

    /**
     * Creates a new Square and adds it to the manager.
     *
     * @param name       The unique name of the square.
     * @param TL_corner_X The X-coordinate of the top-left corner.
     * @param TL_corner_Y The Y-coordinate of the top-left corner.
     * @param length     The side length of the square (must be positive).
     */
    public void createSquare(String name, double TL_corner_X, double TL_corner_Y, double length) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be created.");
            return;
        }
        if (!valid_name_test(name)) return;

        if (length <= 0) {
            System.out.println("clevis> Error: length must be positive.");
            return;
        }

        saveState();

        int zWeight = WeightList.size();
        Shape square = new Square(name, zWeight, TL_corner_X, TL_corner_Y, length);
        ShapesMap.put(name, square);
        WeightList.add(name);
        System.out.println("clevis> Square '" + name + "' created successfully.");
    }

    /**
     * Creates a new Rectangle and adds it to the manager.
     *
     * @param name      The unique name of the rectangle.
     * @param positionX The X-coordinate of the top-left corner.
     * @param positionY The Y-coordinate of the top-left corner.
     * @param width     The width of the rectangle (must be positive).
     * @param height    The height of the rectangle (must be positive).
     */
    public void createRectangle(String name, double positionX, double positionY, double width, double height) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be created.");
            return;
        }
        if (!valid_name_test(name)) return;
        if (width <= 0) {
            System.out.println("clevis> Error: Rectangle width must be positive.");
            return;
        }
        if (height <= 0) {
            System.out.println("clevis> Error: Rectangle height must be positive.");
            return;
        }

        saveState();

        int zWeight = WeightList.size();
        Shape rectangle = new Rectangle(name, zWeight, positionX, positionY, width, height);
        ShapesMap.put(name, rectangle);
        WeightList.add(name);
        System.out.println("clevis> Rectangle '" + name + "' created successfully.");
    }

    /**
     * Creates a new Line segment and adds it to the manager.
     *
     * @param name        The unique name of the line.
     * @param positionX_1 The X-coordinate of the start point.
     * @param positionY_1 The Y-coordinate of the start point.
     * @param positionX_2 The X-coordinate of the end point.
     * @param positionY_2 The Y-coordinate of the end point.
     */
    public void createLine(String name, double positionX_1, double positionY_1, double positionX_2, double positionY_2) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be created.");
            return;
        }
        if (!valid_name_test(name)) return;

        saveState();

        int zWeight = WeightList.size();
        Shape line = new Line(name, zWeight, positionX_1, positionY_1, positionX_2, positionY_2);
        ShapesMap.put(name, line);
        WeightList.add(name);
        System.out.println("clevis> Line '" + name + "' created successfully.");
    }

    /**
     * Creates a new Circle and adds it to the manager.
     *
     * @param name    The unique name of the circle.
     * @param centerX The X-coordinate of the center.
     * @param centerY The Y-coordinate of the center.
     * @param radius  The radius of the circle (must be positive).
     */
    public void createCircle(String name, double centerX, double centerY, double radius) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be created.");
            return;
        }
        if (!valid_name_test(name)) return;
        if (radius <= 0) {
            System.out.println("clevis> Error: radius must be positive.");
            return;
        }

        saveState();

        int zWeight = WeightList.size();
        Shape circle = new Circle(name, zWeight, centerX, centerY, radius);
        ShapesMap.put(name, circle);
        WeightList.add(name);
        System.out.println("clevis> Circle '" + name + "' created successfully.");
    }

    // ======================================================================
    // Grouping Operations
    // ======================================================================

    /**
     * Groups multiple existing shapes into a single composite shape.
     * The member shapes are removed from the active list and "locked" inside the group.
     *
     * @param name       The unique name for the new group.
     * @param shapeNames The list of names of shapes to include in the group.
     */
    public void CreateGroup(String name, List<String> shapeNames) {
        if (!valid_name_test(name)) return;
        if (shapeNames.isEmpty()) {
            System.out.println("clevis> Error: Cannot create an empty group");
            return;
        }
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be created.");
            return;
        }

        saveState();

        ArrayList<Shape> shapesToGroup = new ArrayList<>();
        for (String shapeName : shapeNames) {
            Shape shape = ShapesMap.get(shapeName);
            if (shape == null) {
                System.out.println("clevis> Error: Shape '" + shapeName + "' doesn't exist");
                undoStack.pop(); // Revert saveState
                return;
            }
            shapesToGroup.add(shape);
        }

        int zWeight = WeightList.size();
        Group group = new Group(name, zWeight, shapesToGroup);

        // Remove members from top-level management and lock them
        for (String shapeName : shapeNames) {
            ShapesMap.remove(shapeName);
            Locker.add(shapeName);
        }
        WeightList.removeAll(shapeNames);

        // Add the new group
        ShapesMap.put(name, group);
        WeightList.add(name);

        System.out.println("clevis> Group '" + name + "' created successfully.");
    }

    /**
     * Dissolves a group and releases its member shapes back to the canvas.
     * [REQ7] Member shapes are re-inserted into the Z-order list attempting to preserve their relative order.
     *
     * @param name The name of the group to ungroup.
     */
    public void ungroup(String name) {
        saveState();

        if (!ShapesMap.containsKey(name)) {
            undoStack.pop();
            System.out.println("clevis> Error: Group '" + name + "' does not exist.");
            return;
        }

        Shape shape = ShapesMap.get(name);
        if (!(shape instanceof Group)) {
            undoStack.pop();
            System.out.println("clevis> Error: Shape '" + name + "' is not a group.");
            return;
        }

        Group group = (Group) shape;
        int groupIndex = WeightList.indexOf(name);
        if (groupIndex == -1) {
            undoStack.pop();
            System.out.println("clevis> Error: Group '" + name + "' not found in WeightList");
            return;
        }

        // Remove the group
        ShapesMap.remove(name);
        WeightList.remove(groupIndex);

        List<Shape> elements = group.getElements();
        // Sort elements by their original Z-Weight to restore order
        elements.sort(Comparator.comparingInt(Shape::getzWeight));

        // Re-insert elements back into the system
        for (Shape element : elements) {
            String shapeName = element.getName();
            int zWeight = element.getzWeight();
            
            // Find correct insertion position based on Z-Weight
            int insertIndex = WeightList.size();
            for (int i = 0; i < WeightList.size(); i++) {
                String existingName = WeightList.get(i);
                Shape existingShape = ShapesMap.get(existingName);
                if (existingShape != null && existingShape.getzWeight() > zWeight) {
                    insertIndex = i;
                    break;
                }
            }

            ShapesMap.put(shapeName, element);
            if (insertIndex < WeightList.size()) {
                WeightList.add(insertIndex, shapeName);
            } else {
                WeightList.add(shapeName);
            }
            Locker.remove(shapeName);
        }

        System.out.println("clevis> Group '" + name + "' ungrouped successfully.");
    }

    // =======================================================================
    // Manipulation & Queries
    // =======================================================================

    /**
     * Deletes a shape or group from the system.
     *
     * @param name The name of the shape to delete.
     */
    public void delete(String name) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be interacted with.");
            return;
        }
        if (!ShapesMap.containsKey(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' doesn't exist.");
            return;
        }

        saveState();

        Shape shape = ShapesMap.remove(name);
        unlockGroupMembers(shape);
        WeightList.remove(name);
        System.out.println("clevis> Shape '" + name + "' deleted successfully.");
    }

    /**
     * Releases all nested names held by a deleted group from the lock set.
     */
    private void unlockGroupMembers(Shape shape) {
        if (!(shape instanceof Group)) {
            return;
        }

        Group group = (Group) shape;
        for (Shape element : group.getElements()) {
            Locker.remove(element.getName());
            unlockGroupMembers(element);
        }
    }

    /**
     * Moves a shape by a specified offset.
     *
     * @param name The name of the shape to move.
     * @param x    The delta X (offset) to move.
     * @param y    The delta Y (offset) to move.
     */
    public void move(String name, double x, double y) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be interacted with.");
            return;
        }
        if (!ShapesMap.containsKey(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' doesn't exist.");
            return;
        }

        saveState();

        Shape cur_shape = ShapesMap.get(name);
        cur_shape.move(x, y);
        System.out.println("clevis> Shape '" + name + "' moved by (" + x + ", " + y + ") successfully.");
    }

    /**
     * Calculates and prints the bounding box of a shape.
     *
     * @param name The name of the shape.
     */
    public void boundingbox(String name) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be interacted with.");
            return;
        }
        if (!ShapesMap.containsKey(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' doesn't exist.");
            return;
        }

        Shape cur_shape = ShapesMap.get(name);
        cur_shape.listBoundingbox();
    }

    /**
     * Identifies the topmost shape at a specific coordinate.
     * Searches from the top of the Z-order (end of WeightList) downwards.
     *
     * @param x The X-coordinate to check.
     * @param y The Y-coordinate to check.
     * @return The name of the shape at (x, y), or a message if no shape is found.
     */
    public String shapeat(double x, double y) {
        int s = WeightList.size();
        // Iterate backwards to find the topmost shape first
        for (int i = s - 1; i >= 0; i--) {
            if (ShapesMap.get(WeightList.get(i)).containsPoint(x, y)) {
                return WeightList.get(i);
            }
        }
        return "There is no shape in this point.";
    }

    /**
     * Checks if two shapes intersect.
     *
     * @param name1 The name of the first shape.
     * @param name2 The name of the second shape.
     * @return true if they intersect, false otherwise (or if names are invalid).
     */
    public boolean intersection(String name1, String name2) {
        if (isLocked(name1)) {
            System.out.println("clevis> Error: Shape name '" + name1 + "' has already in Group and cannot be interacted with.");
            return false;
        }
        if (isLocked(name2)) {
            System.out.println("clevis> Error: Shape name '" + name2 + "' has already in Group and cannot be interacted with.");
            return false;
        }
        if (!ShapesMap.containsKey(name1)) {
            System.out.println("clevis> Error: Shape name '" + name1 + "' doesn't exist.");
            return false;
        }
        if (!ShapesMap.containsKey(name2)) {
            System.out.println("clevis> Error: Shape name '" + name2 + "' doesn't exist.");
            return false;
        }

        Shape this_shape = ShapesMap.get(name1);
        Shape other_shape = ShapesMap.get(name2);
        return this_shape.intersect(other_shape);
    }

    /**
     * Lists the details of a specific shape.
     *
     * @param name The name of the shape to list.
     */
    public void list(String name) {
        if (isLocked(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' has already in Group and cannot be interacted with.");
            return;
        }
        if (!ShapesMap.containsKey(name)) {
            System.out.println("clevis> Error: Shape name '" + name + "' doesn't exist.");
            return;
        }
        Shape cur_shape = ShapesMap.get(name);
        cur_shape.list();
    }

    /**
     * Lists all currently active shapes in reverse Z-order (top to bottom).
     */
    public void listall() {
        int s = WeightList.size();
        if (s == 0) {
            System.out.println("clevis> Error: There is no shape in the canvas.");
            return;
        }
        for (int i = s - 1; i >= 0; i--) {
            list(WeightList.get(i));
        }
    }
}

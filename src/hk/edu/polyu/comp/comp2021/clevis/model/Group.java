package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a composite shape that groups multiple shapes together.
 * A Group allows operations (like move, delete) to be performed on a collection of shapes
 * simultaneously. Groups can also contain other groups (recursive structure).
 */
public class Group extends Shape {
    private final ArrayList<Shape> elements;


    /**
     * Constructs a new Group containing the specified list of shapes.
     *
     * @param name     The unique name identifier for this group.
     * @param zWeight  The Z-order weight, usually determined by the max zWeight of its members or creation order.
     * @param elements The list of shapes to be included in this group. A copy of this list is stored.
     */
    public Group(String name, Integer zWeight, List<Shape> elements) {
        super(name, zWeight);
        this.elements = new ArrayList<>(elements);
    }

    /**
     * Moves every shape within the group by the specified offset.
     * This operation is applied recursively to all member shapes.
     *
     * @param dx The amount to move along the X-axis.
     * @param dy The amount to move along the Y-axis.
     */
    @Override
    public void move(double dx, double dy) {
        for (Shape shape : elements) {
            shape.move(dx, dy);
        }
    }

    /**
     * Calculates the smallest axis-aligned bounding box that encompasses all shapes in the group.
     * If the group is empty, returns a box of size 0 at (0,0).
     *
     * @return An array of doubles containing {min-x, min-y, total-width, total-height}.
     */
    @Override
    public double[] getBoundingBox() {
        if (elements.isEmpty()) {
            return new double[]{0, 0, 0, 0};
        }

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Shape shape : elements) {
            double[] box = shape.getBoundingBox();
            minX = Math.min(minX, box[0]);
            minY = Math.min(minY, box[1]);
            maxX = Math.max(maxX, box[0] + box[2]);
            maxY = Math.max(maxY, box[1] + box[3]);
        }

        return new double[]{minX, minY, maxX - minX, maxY - minY};
    }

    /**
     * Checks if a point lies within any of the shapes contained in this group.
     *
     * @param px The X-coordinate of the point.
     * @param py The Y-coordinate of the point.
     * @return true if the point is contained by at least one member shape, false otherwise.
     */
    @Override
    public boolean containsPoint(double px, double py) {
        for (Shape shape : elements) {
            if (shape.containsPoint(px, py)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if this group intersects with another shape.
     * The check is performed based on the bounding box of the entire group.
     *
     * @param other The other shape to check against.
     * @return true if the group's bounding box intersects the other shape's bounding box.
     */
    @Override
    public boolean intersect(Shape other) {
        double[] thisBox = this.getBoundingBox();
        double[] otherBox = other.getBoundingBox();

        return !(thisBox[0] + thisBox[2] < otherBox[0] ||
                otherBox[0] + otherBox[2] < thisBox[0] ||
                thisBox[1] + thisBox[3] < otherBox[1] ||
                otherBox[1] + otherBox[3] < thisBox[1]);
    }

    /**
     * Lists the details of the group and its members to the standard output.
     * Uses default indentation level 0.
     */
    @Override
    public void list() {
        list(0);
    }

    /**
     * Recursively lists the group and its members with indentation to show hierarchy.
     *
     * @param indentLevel The current level of indentation (0 for root, incremented for nested items).
     */
    public void list(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }
        String indentStr = indent.toString();

        System.out.print(indentStr);
        System.out.printf("Group: name = %s, z = %d, members = %d\n",
                getName(), getzWeight(), elements.size());

        for (Shape shape : elements) {
            if (shape instanceof Group) {
                ((Group) shape).list(indentLevel + 1);
            } else {
                System.out.print(indentStr);
                System.out.printf("  - %s (%s)\n", shape.getName(), shape.getType());
            }
        }
    }

    /**
     * Calculates and prints the bounding box of the entire group.
     */
    @Override
    public void listBoundingbox() {
        double[] box = getBoundingBox();
        System.out.printf("Bounding box: top left = (%.2f, %.2f), width = %.2f, height = %.2f%n", box[0], box[1], box[2], box[3]);
    }

    /**
     * Returns the type of this shape.
     *
     * @return The string "Group".
     */
    @Override
    public String getType() {
        return "Group";
    }

    /**
     * Returns the list of shapes contained in this group.
     *
     * @return A new ArrayList containing the member shapes.
     */
    public List<Shape> getElements() {
        return new ArrayList<>(elements);
    }

    /**
     * Creates a deep copy of this Group.
     * This method iterates through all elements in the group and calls their {@code copy()} method.
     * This ensures that the new Group contains new instances of shapes, not references to the old ones.
     *
     * @return A new Group containing copies of all member shapes.
     */
    @Override
    public Shape copy() {
        List<Shape> newElements = new ArrayList<>();
        for (Shape s : elements) {
            newElements.add(s.copy());
        }
        return new Group(getName(), getzWeight(), newElements);
    }
}

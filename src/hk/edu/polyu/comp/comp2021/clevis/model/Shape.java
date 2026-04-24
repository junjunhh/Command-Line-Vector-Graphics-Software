package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * The abstract base class representing a geometric shape in the Clevis application.
 * It defines the common properties (such as name and Z-order) and the contract of behaviors
 * that all concrete shapes (Rectangle, Circle, etc.) must implement.
 */
public abstract class Shape {

    private String name;
    private Integer zWeight;

    /**
     * Constructs a new Shape with the specified name and Z-order weight.
     *
     * @param name    The unique identifier for this shape.
     * @param zWeight The Z-order weight, used for determining the shape's creation order or grouping hierarchy.
     */
    public Shape(String name, Integer zWeight){
        this.name = name;
        this.zWeight = zWeight;
    }

    /**
     * Moves the shape by a specified offset in the 2D plane.
     *
     * @param dx The amount to move along the X-axis.
     * @param dy The amount to move along the Y-axis.
     */
    public abstract void move(double dx, double dy);

    /**
     * Calculates the axis-aligned bounding box of the shape.
     * [REQ9]
     *
     * @return An array of doubles containing {top-left-x, top-left-y, width, height}.
     */
    public abstract double[] getBoundingBox();

    /**
     * Checks if a specific point lies on the perimeter (contour) of the shape.
     * [REQ11] This usually involves checking the distance with a small tolerance (e.g., < 0.05).
     *
     * @param px The X-coordinate of the point.
     * @param py The Y-coordinate of the point.
     * @return true if the point is effectively on the shape's outline, false otherwise.
     */
    public abstract boolean containsPoint(double px, double py);

    /**
     * Determines if this shape intersects with another shape.
     * [REQ12] This is typically determined by checking if their bounding boxes overlap.
     *
     * @param other The other shape to check against.
     * @return true if the shapes intersect, false otherwise.
     */
    public abstract boolean intersect(Shape other);

    /**
     * Prints the specific details of the shape to the standard output.
     * [REQ13] This includes information such as dimensions and location.
     */
    public abstract void list();

    /**
     * Prints the details of the shape's bounding box to the standard output.
     * [REQ9] format usually includes the top-left corner, width, and height.
     */
    public abstract void listBoundingbox();

    /**
     * Returns the concrete type of the shape.
     *
     * @return A string representing the shape type (e.g., "Rectangle", "Circle", "Square").
     */
    public abstract String getType();

    /**
     * Returns the name of the shape.
     *
     * @return The name string.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Z-order weight of the shape.
     *
     * @return The zWeight value.
     */
    public int getzWeight() {
        return zWeight;
    }

    /**
     * Creates and returns a deep copy of this specific shape instance.
     * Subclasses must implement this method to ensure that a new object
     * with identical properties (name, z-weight, coordinates, etc.) is created.
     *
     * @return A new Shape object that is a copy of this instance.
     */
    public abstract Shape copy();
}
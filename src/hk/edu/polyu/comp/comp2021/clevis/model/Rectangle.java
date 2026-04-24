package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Represents a geometric Rectangle in the Clevis application.
 * A Rectangle is defined by its top-left corner coordinates (x, y), width, and height.
 */
public class Rectangle extends Shape {

    private double x; 
    private double y; 
    private final double width;
    private final double height;

    /**
     * Constructs a new Rectangle with the specified parameters.
     *
     * @param name    The unique name identifier for this shape.
     * @param zWeight The Z-order weight, determining the shape's grouping or creation order.
     * @param x       The X-coordinate of the rectangle's top-left corner.
     * @param y       The Y-coordinate of the rectangle's top-left corner.
     * @param width   The width of the rectangle.
     * @param height  The height of the rectangle.
     */
    public Rectangle(String name, int zWeight, double x, double y, double width, double height) {
        super(name, zWeight);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Moves the rectangle by the specified offset.
     * Updates the top-left coordinates by adding dx and dy respectively.
     *
     * @param dx The amount to move along the X-axis.
     * @param dy The amount to move along the Y-axis.
     */
    @Override
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Calculates the axis-aligned bounding box of the rectangle.
     * For a rectangle, the bounding box is identical to the shape itself.
     *
     * @return An array of doubles containing {top-left-x, top-left-y, width, height}.
     */
    @Override
    public double[] getBoundingBox() {
        return new double[]{x, y, width, height};
    }

    /**
     * Checks if a specific point lies on the perimeter (edges) of the rectangle.
     * Uses a small tolerance (0.05) to handle floating-point inaccuracies.
     * Note: This method returns false if the point is inside the rectangle but not on the edge.
     *
     * @param px The X-coordinate of the point to check.
     * @param py The Y-coordinate of the point to check.
     * @return true if the point is on one of the four edges, false otherwise.
     */
    @Override
    public boolean containsPoint(double px, double py) {

        double leftX = x;
        double rightX = x + width;
        double topY = y;
        double bottomY = y + height;

        final double TOLERANCE = 0.05;

        boolean onTopEdge = (Math.abs(py - topY) < TOLERANCE) && (px >= leftX - TOLERANCE && px <= rightX + TOLERANCE);
        boolean onBottomEdge = (Math.abs(py - bottomY) < TOLERANCE) && (px >= leftX - TOLERANCE && px <= rightX + TOLERANCE);

        boolean onLeftEdge = (Math.abs(px - leftX) < TOLERANCE) && (py >= topY - TOLERANCE && py <= bottomY + TOLERANCE);
        boolean onRightEdge = (Math.abs(px - rightX) < TOLERANCE) && (py >= topY - TOLERANCE && py <= bottomY + TOLERANCE);

        return onTopEdge || onBottomEdge || onLeftEdge || onRightEdge;
    }

    /**
     * Determines if this rectangle intersects with another shape.
     * The check is performed based on the overlapping of their bounding boxes.
     * Includes a tiny tolerance (1e-10) for boundary conditions.
     *
     * @param other The other shape to check against.
     * @return true if the bounding boxes of the two shapes intersect, false otherwise.
     */
    @Override
    public boolean intersect(Shape other) {
        double[] thisBox = this.getBoundingBox();
        double[] otherBox = other.getBoundingBox();

        final double TOLERANCE = 1e-10;

        return !(thisBox[0] + thisBox[2] < otherBox[0] - TOLERANCE ||
                otherBox[0] + otherBox[2] < thisBox[0] - TOLERANCE ||
                thisBox[1] + thisBox[3] < otherBox[1] - TOLERANCE ||
                otherBox[1] + otherBox[3] < thisBox[1] - TOLERANCE);
    }
    
    /**
     * Prints the details of the rectangle to the standard output.
     * Includes name, top-left coordinates, width, and height.
     */
    @Override
    public void list() {
        String str = String.format("Rectangle: name = %s, top-left = (%.2f, %.2f), width = %.2f, height = %.2f", getName(), x, y, width, height);
        System.out.println(str);
    }

    /**
     * Calculates and prints the details of the rectangle's bounding box.
     * Output format: top-left coordinates, width, and height.
     */
    @Override
    public void listBoundingbox() {
        System.out.printf("Bounding box: top left = (%.2f, %.2f), width = %.2f, height = %.2f%n", x, y, width, height);
    }

    /**
     * Returns the type of this shape.
     *
     * @return The string "Rectangle".
     */
    @Override
    public String getType() {
        return "Rectangle";
    }

    // Getters

    /**
     * Returns the X-coordinate of the rectangle's top-left corner.
     *
     * @return The top-left X coordinate.
     */
    public double getX() { 
        return x; 
    }

    /**
     * Returns the Y-coordinate of the rectangle's top-left corner.
     *
     * @return The top-left Y coordinate.
     */
    public double getY() {
        return y; 
    }

    /**
     * Returns the width of the rectangle.
     *
     * @return The width value.
     */
    public double getWidth() {
        return width; 
    }

    /**
     * Returns the height of the rectangle.
     *
     * @return The height value.
     */
    public double getHeight() {
        return height; 
    }

    /**
     * Creates a new Rectangle instance with the same properties as this one.
     *
     * @return A new Rectangle object with identical name, z-weight, position, and dimensions.
     */
    @Override
    public Shape copy() {
        return new Rectangle(getName(), getzWeight(), getX(), getY(), getWidth(), getHeight());
    }
}
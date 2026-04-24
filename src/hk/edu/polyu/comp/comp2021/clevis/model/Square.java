package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Represents a geometric Square in the Clevis application.
 * A Square is defined by its top-left corner coordinates (x, y) and a side length.
 */
public class Square extends Shape {

    private double x; 
    private double y; 
    private final double sideLength;

    /**
     * Constructs a new Square with the specified parameters.
     *
     * @param name       The unique name identifier for this shape.
     * @param zWeight    The Z-order weight, determining the shape's grouping or creation order.
     * @param x          The X-coordinate of the square's top-left corner.
     * @param y          The Y-coordinate of the square's top-left corner.
     * @param sideLength The length of the square's sides.
     */
    public Square(String name, int zWeight, double x, double y, double sideLength) {
        super(name, zWeight);
        this.x = x;
        this.y = y;
        this.sideLength = sideLength;
    }

    /**
     * Moves the square by the specified offset.
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
     * Calculates the axis-aligned bounding box of the square.
     *
     * @return An array of doubles containing {top-left-x, top-left-y, width, height}.
     * For a square, width and height are equal to the side length.
     */
    @Override
    public double[] getBoundingBox() {
        return new double[]{x, y, sideLength, sideLength};
    }

    /**
     * Checks if a specific point lies on the perimeter (edges) of the square.
     * Uses a small tolerance (0.05) to handle floating-point inaccuracies.
     * Note: This method returns false if the point is inside the square but not on the edge.
     *
     * @param px The X-coordinate of the point to check.
     * @param py The Y-coordinate of the point to check.
     * @return true if the point is on one of the four edges, false otherwise.
     */
    @Override
    public boolean containsPoint(double px, double py) {
        double leftX = x;
        double rightX = x + sideLength;
        double topY = y;
        double bottomY = y + sideLength;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
        final double TOLERANCE = 0.05;

        boolean onTopEdge = (Math.abs(py - topY) < TOLERANCE) && (px >= leftX - TOLERANCE && px <= rightX + TOLERANCE);
        boolean onBottomEdge = (Math.abs(py - bottomY) < TOLERANCE) && (px >= leftX - TOLERANCE && px <= rightX + TOLERANCE);
        boolean onLeftEdge = (Math.abs(px - leftX) < TOLERANCE) && (py >= topY - TOLERANCE && py <= bottomY + TOLERANCE);
        boolean onRightEdge = (Math.abs(px - rightX) < TOLERANCE) && (py >= topY - TOLERANCE && py <= bottomY + TOLERANCE);

        return onTopEdge || onBottomEdge || onLeftEdge || onRightEdge;
    }

    /**
     * Determines if this square intersects with another shape.
     * The check is performed based on the overlapping of their bounding boxes.
     *
     * @param other The other shape to check against.
     * @return true if the bounding boxes of the two shapes intersect, false otherwise.
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
     * Prints the details of the square to the standard output.
     * Includes name, top-left coordinates, and side length.
     */
    @Override
    public void list() {
        String str = String.format("Square: name = %s, top-left = (%.2f, %.2f), side length = %.2f", getName(), x, y, sideLength);
        System.out.println(str);
    }

    /**
     * Returns the type of this shape.
     *
     * @return The string "Square".
     */
    @Override
    public String getType() {
        return "Square";
    }

    /**
     * Calculates and prints the details of the square's bounding box.
     * Output format: top-left coordinates, width, and height.
     */
    @Override
    public void listBoundingbox() {
        System.out.printf("Bounding box: top left = (%.2f, %.2f), width = %.2f, height = %.2f%n", x, y, sideLength, sideLength);
    }

    /**
     * Returns the X-coordinate of the square's top-left corner.
     *
     * @return The top-left X coordinate.
     */
    public double getX() { 
        return x; 
    }

    /**
     * Returns the Y-coordinate of the square's top-left corner.
     *
     * @return The top-left Y coordinate.
     */
    public double getY() {
        return y; 
    }

    /**
     * Returns the length of the square's side.
     *
     * @return The side length.
     */
    public double getSideLength() {
        return sideLength; 
    }

    /**
     * Creates a new Square instance with the same properties as this one.
     *
     * @return A new Square object with identical name, z-weight, position, and side length.
     */
    @Override
    public Shape copy() {
        return new Square(getName(), getzWeight(), x, y, sideLength);
    }
}
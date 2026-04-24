package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Represents a geometric Circle in the Clevis application.
 * A Circle is defined by its center coordinates (x, y) and a radius.
 */
public class Circle extends Shape {

    private double centerX;
    private double centerY;
    private final double radius;

    /**
     * Constructs a new Circle with the specified parameters.
     *
     * @param name    The unique name identifier for this shape.
     * @param zWeight The Z-order weight, determining the shape's grouping or creation order.
     * @param centerX The X-coordinate of the circle's center.
     * @param centerY The Y-coordinate of the circle's center.
     * @param radius  The radius of the circle.
     */
    public Circle(String name, int zWeight, double centerX, double centerY, double radius) {
        super(name, zWeight);
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    /**
     * Moves the circle by the specified offset.
     * Updates the center coordinates by adding dx and dy respectively.
     *
     * @param dx The amount to move along the X-axis.
     * @param dy The amount to move along the Y-axis.
     */
    @Override
    public void move(double dx, double dy) {
        this.centerX += dx;
        this.centerY += dy;
    }

    /**
     * Calculates the axis-aligned bounding box of the circle.
     * The bounding box is the smallest square that completely encloses the circle.
     *
     * @return An array of doubles containing {top-left-x, top-left-y, width, height}.
     */
    @Override
    public double[] getBoundingBox() {
        double x = centerX - radius;
        double y = centerY - radius;
        double width = 2 * radius;
        double height = 2 * radius;
        return new double[]{x, y, width, height};
    }

    /**
     * Returns the X-coordinate of the circle's center.
     *
     * @return The center X coordinate.
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Returns the Y-coordinate of the circle's center.
     *
     * @return The center Y coordinate.
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * Returns the radius of the circle.
     *
     * @return The radius value.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Checks if a specific point lies on the perimeter of the circle.
     * Uses a small tolerance (0.05) to handle floating-point inaccuracies.
     *
     * @param x The X-coordinate of the point to check.
     * @param y The Y-coordinate of the point to check.
     * @return true if the point is effectively on the circle's rim, false otherwise.
     */
    @Override
    public boolean containsPoint(double x, double y) {
        final double TOLERANCE = 0.05;
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return Math.abs(distance - radius) < TOLERANCE;
    }

    /**
     * Determines if this circle intersects with another shape.
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
     * Prints the details of the circle to the standard output.
     * Includes the name, center coordinates, and radius.
     */
    @Override
    public void list() {
        String str = String.format("Circle: name = %s, center = (%.2f, %.2f), radius = %.2f",
                getName(), centerX, centerY, radius);
        System.out.println(str);
    }

    /**
     * Calculates and prints the details of the circle's bounding box.
     * Output format: top-left coordinates, width, and height.
     */
    @Override
    public void listBoundingbox() {
        double x = centerX - radius;
        double y = centerY - radius;
        double width = 2 * radius;
        double height = 2 * radius;

        System.out.printf("Bounding box: top left = (%.2f, %.2f), width = %.2f, height = %.2f%n",
                x, y, width, height);
    }

    /**
     * Returns the type of this shape.
     *
     * @return The string "Circle".
     */
    @Override
    public String getType() {
        return "Circle";
    }

    /**
     * Creates a new Circle instance with the same properties as this one.
     *
     * @return A new Circle object with identical name, z-weight, center, and radius.
     */
    @Override
    public Shape copy() {
        return new Circle(getName(), getzWeight(), getCenterX(), getCenterY(), getRadius());
    }
}
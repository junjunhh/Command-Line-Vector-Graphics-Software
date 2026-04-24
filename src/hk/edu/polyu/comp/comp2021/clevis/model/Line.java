package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Represents a geometric Line segment in the Clevis application.
 * A Line is defined by two endpoints: (x1, y1) and (x2, y2).
 */
public class Line extends Shape {

    private double positionX_1;
    private double positionY_1;
    private double positionX_2;
    private double positionY_2;

    /**
     * Constructs a new Line segment connecting two points.
     * [REQ3]
     *
     * @param name        The unique name identifier for this shape.
     * @param zWeight     The Z-order weight, determining the shape's grouping or creation order.
     * @param positionX_1 The X-coordinate of the starting point.
     * @param positionY_1 The Y-coordinate of the starting point.
     * @param positionX_2 The X-coordinate of the ending point.
     * @param positionY_2 The Y-coordinate of the ending point.
     */
    public Line(String name, int zWeight, double positionX_1, double positionY_1, double positionX_2, double positionY_2){
        super(name, zWeight);
        this.positionX_1 = positionX_1;
        this.positionY_1 = positionY_1;
        this.positionX_2 = positionX_2;
        this.positionY_2 = positionY_2;
    }

    /**
     * Calculates the axis-aligned bounding box of the line segment.
     * The box is defined by the minimum and maximum X and Y coordinates of the two endpoints.
     * [REQ9]
     *
     * @return An array of doubles containing {min-x, min-y, width, height}.
     */
    @Override
    public double[] getBoundingBox(){
        double minX = Math.min(positionX_1, positionX_2);
        double maxX = Math.max(positionX_1, positionX_2);
        double minY = Math.min(positionY_1, positionY_2);
        double maxY = Math.max(positionY_1, positionY_2);

        return new double[]{minX, minY, maxX - minX, maxY - minY};
    }

    /**
     * Moves the line by the specified offset.
     * Both endpoints are shifted by (dx, dy).
     * [REQ10]
     *
     * @param dx The amount to move along the X-axis.
     * @param dy The amount to move along the Y-axis.
     */
    @Override
    public void move(double dx, double dy){
        this.positionX_1 += dx;
        this.positionY_1 += dy;
        this.positionX_2 += dx;
        this.positionY_2 += dy;
    }

    /**
     * Checks if a specific point lies on the line segment within a small tolerance.
     * This method calculates the shortest distance from the point to the segment using vector projection.
     * [REQ11]
     *
     * @param px The X-coordinate of the point to check.
     * @param py The Y-coordinate of the point to check.
     * @return true if the distance squared is less than the tolerance (0.0025), implying a distance < 0.05.
     */
    @Override
    public boolean containsPoint(double px, double py){
        double lineLengthSquared = Math.pow(positionX_2 - positionX_1, 2) + Math.pow(positionY_2 - positionY_1, 2);
        final double TOLERANCE = 0.0025; // Equivalent to distance < 0.05
        
        if (lineLengthSquared == 0) {
            return (Math.pow(px - positionX_1, 2) + Math.pow(py - positionY_1, 2)) < TOLERANCE;
        }
        
        // Project point onto the line segment, clamping t between 0 and 1
        double position = ((px - positionX_1) * (positionX_2 - positionX_1) + (py - positionY_1) * (positionY_2 - positionY_1)) / (lineLengthSquared);
        position = Math.max(0, Math.min(1, position));
        
        double projectionX = positionX_1 + position * (positionX_2 - positionX_1);
        double projectionY = positionY_1 + position * (positionY_2 - positionY_1);
        double distanceSquared = Math.pow(px - projectionX, 2) + Math.pow(py - projectionY, 2);

        return distanceSquared < TOLERANCE;
    }

    /**
     * Determines if this line intersects with another shape.
     * The check is currently performed based on the overlapping of their bounding boxes.
     * [REQ12]
     *
     * @param other The other shape to check against.
     * @return true if the bounding boxes intersect, false otherwise.
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
     * Prints the details of the line segment to the standard output.
     * Includes the name, start coordinates, and end coordinates.
     */
    @Override
    public void list() {
        String str = String.format("Line: name = %s, from = (%.2f, %.2f), to = (%.2f, %.2f)", 
                getName(), positionX_1, positionY_1, positionX_2, positionY_2);
        System.out.println(str);
    }

    /**
     * Calculates and prints the details of the line's bounding box.
     * Output format: top-left coordinates, width, and height.
     */
    @Override
    public void listBoundingbox() {
        double minX = Math.min(positionX_1, positionX_2);
        double maxX = Math.max(positionX_1, positionX_2);
        double minY = Math.min(positionY_1, positionY_2);
        double maxY = Math.max(positionY_1, positionY_2);
        System.out.printf("Bounding box: top left = (%.2f, %.2f), width = %.2f, height = %.2f%n", 
                minX, minY, maxX - minX, maxY - minY);
    }

    // Getters

    /**
     * Returns the type of this shape.
     *
     * @return The string "Line".
     */
    @Override
    public String getType(){
        return "Line";
    }

    /**
     * Returns the X-coordinate of the first endpoint (start).
     *
     * @return The X coordinate of point 1.
     */
    public double getPositionX_1() {
        return positionX_1;
    }

    /**
     * Returns the Y-coordinate of the first endpoint (start).
     *
     * @return The Y coordinate of point 1.
     */
    public double getPositionY_1() {
        return positionY_1;
    }

    /**
     * Returns the X-coordinate of the second endpoint (end).
     *
     * @return The X coordinate of point 2.
     */
    public double getPositionX_2() {
        return positionX_2;
    }

    /**
     * Returns the Y-coordinate of the second endpoint (end).
     *
     * @return The Y coordinate of point 2.
     */
    public double getPositionY_2() {
        return positionY_2;
    }

    /**
     * Creates a new Line instance with the same properties as this one.
     *
     * @return A new Line object with identical name, z-weight, and endpoint coordinates.
     */
    @Override
    public Shape copy() {
        return new Line(getName(), getzWeight(), positionX_1, positionY_1, positionX_2, positionY_2);
    }
}
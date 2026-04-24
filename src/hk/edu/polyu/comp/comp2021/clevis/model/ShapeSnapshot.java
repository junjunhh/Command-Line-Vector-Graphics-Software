package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Immutable read-only description of a shape for views such as the Swing canvas.
 */
public final class ShapeSnapshot {
    private final String type;
    private final String name;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    private ShapeSnapshot(String type, String name, double x, double y, double width, double height,
                          double x1, double y1, double x2, double y2) {
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public static ShapeSnapshot bounded(String type, String name, double x, double y, double width, double height) {
        return new ShapeSnapshot(type, name, x, y, width, height, 0, 0, 0, 0);
    }

    public static ShapeSnapshot line(String name, double x1, double y1, double x2, double y2) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        return new ShapeSnapshot("Line", name, x, y, width, height, x1, y1, x2, y2);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public boolean isLine() {
        return "Line".equals(type);
    }
}

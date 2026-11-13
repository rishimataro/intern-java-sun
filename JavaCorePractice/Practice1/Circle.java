public class Circle extends Shape {
    private static final double CIRCUMFERENCE_PI = 3.14;

    private final double radius;

    public Circle(double radius) {
        super(2 * radius, 2 * radius);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getCircumference() {
        return getWidth() * CIRCUMFERENCE_PI;
    }

    @Override
    public void displayInfo() {
        System.out.printf(
                "Circle - radius: %.2f, area: %.2f, circumference: %.2f%n",
                radius, getArea(), getCircumference());
    }
}

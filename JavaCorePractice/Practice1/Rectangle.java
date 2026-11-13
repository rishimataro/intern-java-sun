public class Rectangle extends Shape {
    public Rectangle(double width, double height) {
        super(width, height);
    }

    public double getArea() {
        return getWidth() * getHeight();
    }

    public double getPerimeter() {
        return 2 * (getWidth() + getHeight());
    }

    @Override
    public void displayInfo() {
        System.out.printf(
                "Rectangle - width: %.2f, height: %.2f, area: %.2f, perimeter: %.2f%n",
                getWidth(), getHeight(), getArea(), getPerimeter());
    }
}

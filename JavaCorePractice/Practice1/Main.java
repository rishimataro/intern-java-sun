public class Main {
    public static void main(String[] args) {
        Shape shape = new Shape(5, 3);
        Rectangle rectangle = new Rectangle(5, 3);
        Circle circle = new Circle(4);

        System.out.println("Shape:");
        shape.displayInfo();

        rectangle.displayInfo();
        circle.displayInfo();
    }
}

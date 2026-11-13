package foodordering;
import java.awt.Color;

/** Non-vegetarian menu item. Demonstrates Inheritance + Polymorphism. */
public class NonVegItem extends FoodItem {
    public NonVegItem(int id, String name, String category, double price, String description) {
        super(id, name, category, price, description);
    }
    public NonVegItem(int id, String name, String category, double price) {
        super(id, name, category, price);
    }
    @Override public String getFoodType()   { return "Non-Vegetarian"; }
    @Override public Color  getBadgeColor() { return new Color(185, 28, 28); }
}

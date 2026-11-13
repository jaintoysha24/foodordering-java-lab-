package foodordering;
import java.awt.Color;

/** Vegetarian menu item. Demonstrates Inheritance + Polymorphism. */
public class VegItem extends FoodItem {
    public VegItem(int id, String name, String category, double price, String description) {
        super(id, name, category, price, description);
    }
    public VegItem(int id, String name, String category, double price) {
        super(id, name, category, price);
    }
    @Override public String getFoodType()   { return "Vegetarian"; }
    @Override public Color  getBadgeColor() { return new Color(34, 139, 34); }
}

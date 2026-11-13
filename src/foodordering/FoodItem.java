package foodordering;

import java.util.Objects;

/**
 * Abstract base class for all menu items.
 * Demonstrates: Abstraction, Encapsulation, Inheritance (parent).
 */
public abstract class FoodItem {
    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private final String description;

    public FoodItem(int id, String name, String category, double price, String description) {
        if (id <= 0)                               throw new IllegalArgumentException("Food item ID must be positive.");
        if (name == null || name.isBlank())         throw new IllegalArgumentException("Food item name cannot be empty.");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Category cannot be empty.");
        if (price <= 0)                            throw new IllegalArgumentException("Price must be greater than zero.");

        this.id          = id;
        this.name        = name;
        this.category    = category;
        this.price       = price;
        this.description = (description == null || description.isBlank()) ? "" : description;
    }

    public FoodItem(int id, String name, String category, double price) {
        this(id, name, category, price, "");
    }

    public int    getId()          { return id; }
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public double getPrice()       { return price; }
    public String getDescription() { return description; }

    public abstract String       getFoodType();
    public abstract java.awt.Color getBadgeColor();

    @Override public String toString() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodItem)) return false;
        return id == ((FoodItem) o).id;
    }

    @Override public int hashCode() { return Objects.hash(id); }
}

package foodordering;

import java.util.*;

/**
 * Stores selected items and quantities.
 * Demonstrates: Collections (LinkedHashMap), Exception Handling, Interface (Billable).
 */
public class Cart implements Billable {
    private final Map<FoodItem, Integer> items = new LinkedHashMap<>();

    public void addItem(FoodItem item, int quantity) {
        validateQuantity(quantity);
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(FoodItem item) {
        items.remove(item);
    }

    public void updateQuantity(FoodItem item, int quantity) {
        validateQuantity(quantity);
        if (!items.containsKey(item))
            throw new IllegalArgumentException("Item is not in the cart.");
        items.put(item, quantity);
    }

    public Map<FoodItem, Integer> getItems() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(items));
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public int getTotalItemCount() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void clear() { items.clear(); }

    @Override
    public double calculateSubtotal() {
        double sub = 0;
        for (Map.Entry<FoodItem, Integer> e : items.entrySet())
            sub += e.getKey().getPrice() * e.getValue();
        return sub;
    }

    private void validateQuantity(int q) {
        if (q < 1 || q > 50)
            throw new IllegalArgumentException("Quantity must be between 1 and 50.");
    }
}

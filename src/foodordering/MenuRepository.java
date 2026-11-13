package foodordering;

import java.util.*;

/**
 * In-memory menu store with expanded items and descriptions.
 * Demonstrates: Collections (ArrayList), Encapsulation.
 */
public class MenuRepository {
    private final List<FoodItem> menuItems = new ArrayList<>();

    public MenuRepository() { loadMenu(); }

    private void loadMenu() {
        // Pizza
        menuItems.add(new VegItem(101, "Margherita Pizza",   "Pizza",     199, "Classic tomato, mozzarella & fresh basil"));
        menuItems.add(new VegItem(102, "Farmhouse Pizza",    "Pizza",     249, "Capsicum, mushroom, onion & paneer"));
        menuItems.add(new NonVegItem(103, "Chicken Pizza",   "Pizza",     299, "Spiced chicken, jalapeños & cheese blend"));
        menuItems.add(new NonVegItem(104, "Pepperoni Pizza", "Pizza",     329, "Double pepperoni with oregano crust"));
        // Burger
        menuItems.add(new VegItem(201, "Veg Burger",         "Burger",     99, "Aloo tikki patty with mint chutney"));
        menuItems.add(new NonVegItem(202, "Chicken Burger",  "Burger",    149, "Grilled chicken, coleslaw & smoky sauce"));
        menuItems.add(new NonVegItem(203, "Zinger Burger",   "Burger",    179, "Crispy spiced chicken fillet, lettuce"));
        // Starters
        menuItems.add(new VegItem(301, "Paneer Tikka",       "Starters",  179, "Marinated paneer grilled in tandoor"));
        menuItems.add(new NonVegItem(302, "Chicken Tikka",   "Starters",  229, "Bone-in chicken with chaat masala"));
        menuItems.add(new VegItem(303, "Veg Spring Rolls",   "Starters",  129, "Crispy rolls with sweet chilli dip"));
        menuItems.add(new NonVegItem(304, "Chicken Wings",   "Starters",  249, "Buffalo-style wings, ranch dip"));
        // Rice & Biryani
        menuItems.add(new VegItem(401, "Veg Biryani",        "Rice",      189, "Dum-cooked basmati with saffron"));
        menuItems.add(new NonVegItem(402, "Chicken Biryani", "Rice",      249, "Hyderabadi-style with raita"));
        menuItems.add(new VegItem(403, "Jeera Rice",         "Rice",       99, "Ghee-tempered cumin rice"));
        // Beverages
        menuItems.add(new VegItem(501, "Cold Coffee",        "Beverages",  89, "Chilled espresso with cream"));
        menuItems.add(new VegItem(502, "Fresh Lime Soda",    "Beverages",  59, "Mint, lime & soda"));
        menuItems.add(new VegItem(503, "Mango Lassi",        "Beverages",  79, "Thick alphonso mango lassi"));
        // Dessert
        menuItems.add(new VegItem(601, "Chocolate Brownie",  "Dessert",   119, "Warm fudge brownie with ice cream"));
        menuItems.add(new VegItem(602, "Gulab Jamun",        "Dessert",    79, "Soft khoya dumplings in sugar syrup"));
        menuItems.add(new VegItem(603, "Ice Cream Sundae",   "Dessert",    99, "3 scoops, hot chocolate sauce"));
    }

    public List<FoodItem> getAllItems()                        { return Collections.unmodifiableList(menuItems); }

    public List<FoodItem> getItemsByCategory(String category) {
        if (category == null || category.equals("All")) return getAllItems();
        List<FoodItem> out = new ArrayList<>();
        for (FoodItem f : menuItems)
            if (f.getCategory().equalsIgnoreCase(category)) out.add(f);
        return out;
    }

    public List<String> getCategories() {
        List<String> cats = new ArrayList<>();
        cats.add("All");
        for (FoodItem f : menuItems)
            if (!cats.contains(f.getCategory())) cats.add(f.getCategory());
        return cats;
    }
}

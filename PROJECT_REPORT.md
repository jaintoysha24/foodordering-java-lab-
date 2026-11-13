# Project Report
## Food Ordering System
### International Institute of Professional Studies
### Devi Ahilya Vishwavidyalaya (DAVV), Indore

---

| Field          | Details                                        |
|----------------|------------------------------------------------|
| Student Name   | Rachit Neema                                   |
| Programme      | M.Tech (Information Technology)                |
| Institute      | IIPS, DAVV, Indore                             |
| Subject        | Object-Oriented Programming with Java          |
| Project Title  | Food Ordering System                           |
| Language       | Java 17+                                       |
| GUI Framework  | Java Swing                                     |

---

## 1. Introduction

The **Food Ordering System** is a desktop application that simulates the core workflow of a restaurant ordering counter. A staff operator can browse the menu, filter by category or search by name, build a customer's cart, review a live bill summary, and confirm the order — generating a formatted receipt that is saved automatically as a text file.

The project was designed to demonstrate at least **eight** of the prescribed Java programming concepts within a single cohesive, working application.

---

## 2. Objectives

- Implement a fully functional food ordering workflow using Java Swing GUI.
- Apply object-oriented design principles: encapsulation, inheritance, polymorphism, and abstraction.
- Use Java Collections to manage menu items and cart state.
- Handle all edge cases (empty cart, invalid input, file I/O errors) through structured Exception Handling.
- Save order receipts to disk using File Handling.
- Produce a professional, maintainable codebase through clear separation of concerns.

---

## 3. Java Concepts Used

| # | Concept              | Where Applied                                                                 |
|---|----------------------|-------------------------------------------------------------------------------|
| 1 | **Classes & Objects**    | `FoodItem`, `Cart`, `Order`, `MenuRepository`, `FoodOrderingFrame`        |
| 2 | **Constructors**         | Parameterised constructors in every class; legacy constructors for backward compatibility |
| 3 | **Inheritance**          | `VegItem` and `NonVegItem` extend abstract `FoodItem`                     |
| 4 | **Polymorphism**         | `getFoodType()` and `getBadgeColor()` overridden in each subclass; `Billable` interface used polymorphically |
| 5 | **Interfaces**           | `Billable` interface with `calculateSubtotal()` and default `calculateTax()` methods |
| 6 | **Exception Handling**   | `try-catch` blocks in `addSelectedItem`, `updateQuantity`, `checkout`; `IllegalArgumentException` thrown from domain classes |
| 7 | **Collections**          | `LinkedHashMap<FoodItem, Integer>` in `Cart`; `ArrayList<FoodItem>` in `MenuRepository`; `Collections.unmodifiableList` for safety |
| 8 | **File Handling**        | `Order.saveReceipt()` writes a `.txt` receipt using `java.nio.file.Files.writeString` |
| 9 | **Multithreading**       | `SwingUtilities.invokeLater` in `Main.java` ensures the GUI runs on the Event Dispatch Thread |
| 10| **GUI**                 | Full Swing application: `JFrame`, `JTable`, `JComboBox`, `JSpinner`, `JScrollPane`, custom renderers, custom-painted buttons |

---

## 4. System Architecture

```
foodordering/
├── Main.java               ← Entry point; starts GUI on EDT
├── FoodItem.java           ← Abstract base class (Abstraction, Encapsulation)
├── VegItem.java            ← Concrete subclass (Inheritance, Polymorphism)
├── NonVegItem.java         ← Concrete subclass (Inheritance, Polymorphism)
├── Billable.java           ← Interface (default + abstract methods)
├── Cart.java               ← Shopping cart; implements Billable
├── Order.java              ← Confirmed order; generates & saves receipt
├── MenuRepository.java     ← In-memory menu data store
├── FoodOrderingFrame.java  ← Main Swing window (MVC Controller + View)
├── UIConstants.java        ← Centralised design tokens (colours, fonts)
├── UIHelper.java           ← Component factory (buttons, cards, fields)
└── FoodTableRenderer.java  ← Custom JTable cell renderer
```

### Class Hierarchy

```
         ┌──────────────┐       «interface»
         │  FoodItem    │◄──── Billable
         │  (abstract)  │          │
         └──────┬───────┘    ┌─────┴───────┐
                │             │             │
         ┌──────┴──┐    ┌────┴────┐    ┌───┴────┐
         │ VegItem │    │NonVeg   │    │  Cart  │
         └─────────┘    │ Item   │    └────────┘
                        └─────────┘
```

---

## 5. Features

### 5.1 Menu Dashboard
- Displays all menu items in a clean, alternating-row table.
- **Category filter** (dropdown): All / Pizza / Burger / Starters / Rice / Beverages / Dessert.
- **Live search** field: filters items by name as the user types.
- Vegetarian items highlighted in green; Non-Vegetarian in red.
- Quantity spinner (1–50) with one-click "Add to Cart."

### 5.2 Cart Management
- Items added in insertion order (LinkedHashMap).
- **Remove** individual items; **Update Quantity** via dialog; **Clear Cart** with confirmation.
- Live subtotal, GST (5%), and total recalculated after every change.
- Cart item count shown in the application header.

### 5.3 Order & Receipt
- Customer name, phone number, and payment mode (Cash / UPI / Card / Net Banking).
- Confirmation dialog before placing the order.
- Formatted receipt displayed inline after confirmation.
- Receipt saved automatically to `receipts/receipt-ORDxxxx.txt` in the working directory.
- Status bar confirms every action.

### 5.4 UI & UX
- Deep navy header with warm-orange accent — intentional, food-industry palette.
- System look-and-feel applied at startup; graceful fallback to Swing default.
- All errors surfaced as user-friendly dialogs, never stack traces.
- Keyboard-accessible controls throughout.

---

## 6. Menu

| ID  | Item                | Category   | Type       | Price (Rs.) |
|-----|---------------------|------------|------------|-------------|
| 101 | Margherita Pizza    | Pizza      | Veg        | 199         |
| 102 | Farmhouse Pizza     | Pizza      | Veg        | 249         |
| 103 | Chicken Pizza       | Pizza      | Non-Veg    | 299         |
| 104 | Pepperoni Pizza     | Pizza      | Non-Veg    | 329         |
| 201 | Veg Burger          | Burger     | Veg        | 99          |
| 202 | Chicken Burger      | Burger     | Non-Veg    | 149         |
| 203 | Zinger Burger       | Burger     | Non-Veg    | 179         |
| 301 | Paneer Tikka        | Starters   | Veg        | 179         |
| 302 | Chicken Tikka       | Starters   | Non-Veg    | 229         |
| 303 | Veg Spring Rolls    | Starters   | Veg        | 129         |
| 304 | Chicken Wings       | Starters   | Non-Veg    | 249         |
| 401 | Veg Biryani         | Rice       | Veg        | 189         |
| 402 | Chicken Biryani     | Rice       | Non-Veg    | 249         |
| 403 | Jeera Rice          | Rice       | Veg        | 99          |
| 501 | Cold Coffee         | Beverages  | Veg        | 89          |
| 502 | Fresh Lime Soda     | Beverages  | Veg        | 59          |
| 503 | Mango Lassi         | Beverages  | Veg        | 79          |
| 601 | Chocolate Brownie   | Dessert    | Veg        | 119         |
| 602 | Gulab Jamun         | Dessert    | Veg        | 79          |
| 603 | Ice Cream Sundae    | Dessert    | Veg        | 99          |

---

## 7. Sample Receipt

```
=================================================
             FOODIE'S RESTAURANT
         Sarafa Bazaar, Indore - 452001
         Ph: +91-731-000-0000  GSTIN: 23XXXXX
=================================================
  Order ID  : ORD1001
  Customer  : Rachit Neema
  Phone     : 9876543210
  Date/Time : 12-11-2025  14:35:22
  Payment   : UPI
-------------------------------------------------
  Item                        Qty     Amount
-------------------------------------------------
  Chicken Biryani               2  Rs.  498.00
  Mango Lassi                   2  Rs.  158.00
  Gulab Jamun                   2  Rs.  158.00
-------------------------------------------------
  Subtotal:                        Rs.  814.00
  GST  (5%) :                      Rs.   40.70
  TOTAL      :                     Rs.  854.70
=================================================
         Thank you for dining with us!
         Visit again  |  Rate us on Google
=================================================
```

---

## 8. How to Run

### Prerequisites
- JDK 17 or higher (download from [adoptium.net](https://adoptium.net))

### Steps

**Windows:**
```
Double-click compile_and_run.bat
```

**Linux / macOS:**
```bash
chmod +x compile_and_run.sh
./compile_and_run.sh
```

**Manual compile:**
```bash
mkdir out
javac -d out -sourcepath src src/foodordering/*.java
java -cp out foodordering.Main
```

---

## 9. Future Enhancements

| Enhancement             | Description                                              |
|-------------------------|----------------------------------------------------------|
| JDBC / Database         | Replace `MenuRepository` with a MySQL/SQLite back-end    |
| Login Screen            | Staff authentication before accessing the dashboard      |
| PDF Receipts            | Generate PDF bills using iText or Apache PDFBox          |
| Order History           | Persist and browse past orders                           |
| Discount / Coupon       | Apply promo codes at checkout                            |
| Admin Panel             | Add/remove/edit menu items at runtime                    |

---

## 10. Conclusion

This project integrates ten core Java programming concepts into a fully working, professionally designed desktop application. The codebase is modular (each class has a single clear responsibility), extensible (new food types or payment modes require minimal changes), and robust (all invalid inputs are caught and reported gracefully). The Food Ordering System fulfils all minimum requirements of the M.Tech project assignment and demonstrates practical, real-world application of object-oriented programming principles.

---

*Report prepared for IIPS, DAVV, Indore — M.Tech (IT) Java Programming Project, 2025*

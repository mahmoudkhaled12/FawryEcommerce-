import java.util.*;

// Interfaces
interface Expirable {
    boolean isExpired();
}

interface Shippable {
    String getName();
    double getWeight(); // in kg
}

// Product Class
abstract class Product {
    protected String name;
    protected double price;
    protected int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void reduceQuantity(int q) {
        this.quantity -= q;
    }
}

// Concrete Product Types
class Cheese extends Product implements Expirable, Shippable {
    private boolean expired;
    private double weight;

    public Cheese(String name, double price, int quantity, double weight, boolean expired) {
        super(name, price, quantity);
        this.weight = weight;
        this.expired = expired;
    }

    public boolean isExpired() { return expired; }
    public double getWeight() { return weight; }
    public String getName() { return name; }
}

class TV extends Product implements Shippable {
    private double weight;

    public TV(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    public double getWeight() { return weight; }
    public String getName() { return name; }
}

class Mobile extends Product {
    public Mobile(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class Biscuits extends Product implements Expirable, Shippable {
    private boolean expired;
    private double weight;

    public Biscuits(String name, double price, int quantity, double weight, boolean expired) {
        super(name, price, quantity);
        this.weight = weight;
        this.expired = expired;
    }

    public boolean isExpired() { return expired; }
    public double getWeight() { return weight; }
    public String getName() { return name; }
}

class ScratchCard extends Product {
    public ScratchCard(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

// Customer
class Customer {
    String name;
    double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public boolean canAfford(double amount) {
        return balance >= amount;
    }

    public void pay(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}

// Cart Item
class CartItem {
    Product product;
    int quantity;

    public CartItem(Product p, int q) {
        product = p;
        quantity = q;
    }
}

// Cart
class Cart {
    List<CartItem> items = new ArrayList<>();

    public void add(Product p, int q) {
        if (q > p.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity for product: " + p.getName());
        }
        items.add(new CartItem(p, q));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

// Shipping Service
class ShippingService {
    public static void ship(List<ShippableItem> shippables) {
        System.out.println("** Shipment notice **");
        double totalWeight = 0;
        for (ShippableItem item : shippables) {
            System.out.printf("%dx %s %.0fg\n", item.quantity, item.getName(), item.getWeight() * 1000);
            totalWeight += item.getWeight();
        }
        System.out.printf("Total package weight %.1fkg\n", totalWeight);
    }

    static class ShippableItem {
        String name;
        double weight;
        int quantity;

        public ShippableItem(String name, double weight, int quantity) {
            this.name = name;
            this.weight = weight;
            this.quantity = quantity;
        }

        public double getWeight() {
            return weight * quantity;
        }

        public String getName() {
            return name;
        }
    }
}

// E-Commerce Checkout
class ECommerceSystem {
    static final double SHIPPING_COST_PER_KG = 30;

    public static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) throw new IllegalStateException("Cart is empty!");

        double subtotal = 0;
        double totalWeight = 0;
        List<ShippingService.ShippableItem> shippables = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            Product p = item.product;
            int q = item.quantity;

            // Expiry check
            if (p instanceof Expirable && ((Expirable) p).isExpired()) {
                throw new IllegalStateException("Product expired: " + p.getName());
            }

            // Quantity check
            if (q > p.getQuantity()) {
                throw new IllegalStateException("Not enough stock: " + p.getName());
            }

            // Subtotal
            subtotal += p.getPrice() * q;

            // Prepare for shipping
            if (p instanceof Shippable) {
                double weight = ((Shippable) p).getWeight();
                shippables.add(new ShippingService.ShippableItem(p.getName(), weight, q));
                totalWeight += weight * q;
            }
        }

        double shippingFees = totalWeight * SHIPPING_COST_PER_KG;
        double total = subtotal + shippingFees;

        // Balance check
        if (!customer.canAfford(total)) {
            throw new IllegalStateException("Insufficient balance!");
        }

        // Deduct payment
        customer.pay(total);
        for (CartItem item : cart.getItems()) {
            item.product.reduceQuantity(item.quantity);
        }

        // Shipping
        if (!shippables.isEmpty()) {
            ShippingService.ship(shippables);
        }

        // Receipt
        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            System.out.printf("%dx %s %.0f\n", item.quantity, item.product.getName(), item.product.getPrice() * item.quantity);
        }
        System.out.println("----------------------");
        System.out.printf("Subtotal %.0f\n", subtotal);
        System.out.printf("Shipping %.0f\n", shippingFees);
        System.out.printf("Amount %.0f\n", total);
        System.out.println("END.");
        System.out.printf("Customer balance: %.0f\n", customer.getBalance());
    }
}


public class Main {
    public static void main(String[] args) {
        Product cheese = new Cheese("Cheese", 100, 10, 0.2, false);
        Product biscuits = new Biscuits("Biscuits", 150, 5, 0.7, false);
        Product tv = new TV("TV", 300, 3, 5.0);
        Product scratchCard = new ScratchCard("ScratchCard", 50, 20);
        Customer customer = new Customer("Mahmoud", 1000);

        Cart cart = new Cart();
        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        cart.add(scratchCard, 1);

        ECommerceSystem.checkout(customer, cart);
    }
}


//
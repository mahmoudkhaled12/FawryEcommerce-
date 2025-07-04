# Fawry Rise - E-commerce System Challenge 💻🛒

This repository contains my solution to the **Fawry Rise Full Stack Development Internship Challenge**.

## ✅ Features Implemented

- Define products with name, price, quantity.
- Support for both expirable and non-expirable products.
- Support for shippable and non-shippable products, with weight handling.
- Cart functionality with quantity validation.
- Checkout process with:
  - Subtotal calculation
  - Shipping fee based on total weight
  - Total amount paid
  - Customer balance update
- Error handling for:
  - Empty cart
  - Expired products
  - Out-of-stock items
  - Insufficient balance
- Integration with a `ShippingService` to list items that require shipping.

## 📦 Example Products

- Cheese (expirable + shippable)
- Biscuits (expirable + shippable)
- TV (non-expirable + shippable)
- Mobile (non-shippable)
- Scratch Card (non-expirable, non-shippable)

## 💡 Technologies Used

- Java (OOP principles)
- No frameworks, plain Java for logic clarity

## 🧪 Sample Test in `Main.java`

```java
cart.add(cheese, 2);
cart.add(biscuits, 1);
cart.add(scratchCard, 1);
checkout(customer, cart);

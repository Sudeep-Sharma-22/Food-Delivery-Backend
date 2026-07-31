package com.fooddelivery.main;

import com.fooddelivery.model.Address;
import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.User;
import com.fooddelivery.service.AddressService;
import com.fooddelivery.service.CouponService;
import com.fooddelivery.service.MenuItemService;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.PaymentService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.service.ReviewService;
import com.fooddelivery.service.UserService;
import com.fooddelivery.model.Coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static User loggedInUser = null;
    private static Scanner scanner = new Scanner(System.in);

    private static UserService userService = new UserService();
    private static RestaurantService restaurantService = new RestaurantService();
    private static MenuItemService menuItemService = new MenuItemService();
    private static OrderService orderService = new OrderService();
    private static AddressService addressService = new AddressService();
    private static PaymentService paymentService = new PaymentService();
    private static ReviewService reviewService = new ReviewService();
    private static CouponService couponService = new CouponService();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("           FOOD DELIVERY APP             ");
        System.out.println("=========================================");

        while (true) {
            if (loggedInUser == null) {
                showMainMenu();
            } else {
                switch (loggedInUser.getRole()) {
                    case "CUSTOMER":
                        showCustomerMenu();
                        break;
                    case "OWNER":
                        showOwnerMenu();
                        break;
                    case "DELIVERY_PARTNER":
                        showDeliveryMenu();
                        break;
                    default:
                        System.out.println("Unknown role. Logging out.");
                        loggedInUser = null;
                }
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choice: ");
        int choice = getIntInput();

        if (choice == 1) {
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Phone: "); String phone = scanner.nextLine();
            System.out.print("Password: "); String password = scanner.nextLine();
            System.out.print("Role (CUSTOMER/OWNER/DELIVERY_PARTNER): "); String role = scanner.nextLine().toUpperCase();
            if (userService.registerUser(name, email, phone, password, role)) {
                System.out.println("SUCCESS: Registered! Please login.");
            }
        } else if (choice == 2) {
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Password: "); String password = scanner.nextLine();
            loggedInUser = userService.loginUser(email, password);
            if (loggedInUser != null) {
                System.out.println("SUCCESS: Welcome, " + loggedInUser.getName() + " (" + loggedInUser.getRole() + ")");
            }
        } else if (choice == 3) {
            System.out.println("Goodbye!");
            System.exit(0);
        }
    }

    private static void showCustomerMenu() {
        System.out.println("\n--- CUSTOMER MENU ---");
        System.out.println("1. View Open Restaurants");
        System.out.println("2. View Menu & Place Order");
        System.out.println("3. Manage Addresses");
        System.out.println("4. View Order History");
        System.out.println("5. Leave a Review");
        System.out.println("6. Logout");
        System.out.print("Choice: ");
        int choice = getIntInput();

        switch (choice) {
            case 1:
                restaurantService.displayOpenRestaurants();
                break;
            case 2:
                placeOrderFlow();
                break;
            case 3:
                System.out.println("1. View Addresses | 2. Add Address");
                int addrChoice = getIntInput();
                if (addrChoice == 1) {
                    for (Address a : addressService.getUserAddresses(loggedInUser.getUserId())) {
                        System.out.println(a);
                    }
                } else if (addrChoice == 2) {
                    System.out.print("Address Line: "); String line = scanner.nextLine();
                    System.out.print("City: "); String city = scanner.nextLine();
                    System.out.print("State: "); String state = scanner.nextLine();
                    System.out.print("Pincode: "); String pin = scanner.nextLine();
                    System.out.print("Label (HOME/WORK): "); String label = scanner.nextLine();
                    if (addressService.addAddress(loggedInUser.getUserId(), line, city, state, pin, label)) {
                        System.out.println("Address Added!");
                    }
                }
                break;
            case 4:
                List<Order> orders = orderService.getCustomerOrders(loggedInUser.getUserId());
                for (Order o : orders) System.out.println(o);
                break;
            case 5:
                System.out.println("--- Your Past Orders ---");
                List<Order> pastOrders = orderService.getCustomerOrders(loggedInUser.getUserId());
                if (pastOrders.isEmpty()) {
                    System.out.println("You have no past orders to review.");
                    break;
                }
                for (Order o : pastOrders) {
                    Restaurant r = restaurantService.getRestaurantById(o.getRestaurantId());
                    String rName = (r != null) ? r.getName() : "Unknown";
                    System.out.println("Order #" + o.getOrderId() + " | Restaurant: " + rName + " | Status: " + o.getStatus());
                }

                System.out.print("Enter Order ID to review: "); int oId = getIntInput();
                Order selectedOrder = null;
                for (Order o : pastOrders) {
                    if (o.getOrderId() == oId) {
                        selectedOrder = o;
                        break;
                    }
                }

                if (selectedOrder == null) {
                    System.out.println("Invalid Order ID.");
                    break;
                }

                System.out.print("Rating (1-5): "); int rating = getIntInput();
                System.out.print("Comment: "); String comment = scanner.nextLine();
                if (reviewService.leaveReview(loggedInUser.getUserId(), selectedOrder.getRestaurantId(), selectedOrder.getOrderId(), rating, comment)) {
                    System.out.println("Review added successfully!");
                }
                break;
            case 6:
                loggedInUser = null;
                break;
        }
    }

    private static void placeOrderFlow() {
        restaurantService.displayOpenRestaurants();
        System.out.print("Enter Restaurant ID to order from: ");
        int restId = getIntInput();

        List<MenuItem> menu = menuItemService.getMenu(restId);
        System.out.println("--- MENU FOR RESTAURANT " + restId + " ---");
        if (menu.isEmpty()) {
            System.out.println("No items available currently.");
            return;
        }
        for (MenuItem item : menu) {
            System.out.println(item.toString());
        }

        List<Address> addresses = addressService.getUserAddresses(loggedInUser.getUserId());
        if (addresses.isEmpty()) {
            System.out.println("You have no addresses! Please add an address first.");
            return;
        }
        System.out.println("\nYour Addresses:");
        for (Address a : addresses) System.out.println(a);
        System.out.print("Select Address ID: ");
        int addrId = getIntInput();

        List<OrderItem> cart = new ArrayList<>();
        while (true) {
            System.out.print("Enter Item ID (0 to finish): ");
            int itemId = getIntInput();
            if (itemId <= 0) break;

            MenuItem selectedItem = null;
            for (MenuItem item : menu) {
                if (item.getItemId() == itemId) {
                    selectedItem = item;
                    break;
                }
            }

            if (selectedItem == null) {
                System.out.println("Invalid Item ID. Try again.");
                continue;
            }

            System.out.print("Quantity: ");
            int qty = getIntInput();
            if (qty <= 0) {
                System.out.println("Quantity must be at least 1.");
                continue;
            }

            double price = selectedItem.getPrice();
            cart.add(new OrderItem(0, itemId, qty, price, price * qty));
            System.out.println(selectedItem.getName() + " added to cart.");
        }

        if (cart.isEmpty()) return;

        double totalBeforeDiscount = 0;
        for (OrderItem item : cart) totalBeforeDiscount += item.getSubtotal();

        System.out.println("\nCart Subtotal: " + totalBeforeDiscount + " INR");
        couponService.displayActiveCoupons();
        System.out.print("Enter Coupon Code (or press Enter to skip): ");
        String code = scanner.nextLine();

        double discount = 0;
        if (!code.isEmpty()) {
            Coupon coupon = couponService.validateAndGetCoupon(code, totalBeforeDiscount);
            if (coupon != null) {
                discount = couponService.calculateDiscount(coupon, totalBeforeDiscount);
                System.out.println("Coupon Applied! Discount: " + discount + " INR");
            }
        }

        Order newOrder = new Order();
        newOrder.setCustomerId(loggedInUser.getUserId());
        newOrder.setRestaurantId(restId);
        newOrder.setDeliveryAddressId(addrId);
        newOrder.setDiscountAmount(discount);

        if (orderService.placeOrder(newOrder, cart)) {
            System.out.println("Order Placed! Total Amount to Pay: " + newOrder.getFinalAmount() + " INR");
            while (true) {
                System.out.print("Enter payment amount to proceed: ");
                double amount = getDoubleInput();
                if (amount <= 0) {
                    System.out.println("Invalid amount. Please try again.");
                    continue;
                }
                if (paymentService.makePayment(newOrder.getOrderId(), amount, "CARD")) {
                    System.out.println("Payment Successful. ACID Transaction Complete.");
                    break;
                } else {
                    System.out.println("Payment failed. Please try again.");
                }
            }
        }
    }

    private static void showOwnerMenu() {
        System.out.println("\n--- OWNER MENU ---");
        System.out.println("1. Add Restaurant");
        System.out.println("2. Add Menu Item");
        System.out.println("3. View My Restaurants");
        System.out.println("4. Update Order Status");
        System.out.println("5. Logout");
        System.out.print("Choice: ");
        int choice = getIntInput();

        switch (choice) {
            case 1:
                System.out.print("Restaurant Name: "); String name = scanner.nextLine();
                System.out.print("Cuisine: "); String cuisine = scanner.nextLine();
                System.out.print("Address: "); String addr = scanner.nextLine();
                if (restaurantService.addRestaurant(loggedInUser.getUserId(), name, cuisine, addr)) {
                    System.out.println("Restaurant added!");
                }
                break;
            case 2:
                System.out.println("--- Your Restaurants ---");
                List<Restaurant> myRests = restaurantService.getRestaurantsByOwner(loggedInUser.getUserId());
                if (myRests.isEmpty()) {
                    System.out.println("You have no restaurants yet.");
                    break;
                }
                for (Restaurant r : myRests) System.out.println(r);

                System.out.print("Restaurant ID: "); int rId = getIntInput();
                System.out.print("Item Name: "); String iName = scanner.nextLine();
                System.out.print("Description: "); String desc = scanner.nextLine();
                System.out.print("Price: "); double price = getDoubleInput();
                System.out.print("Category: "); String cat = scanner.nextLine();
                if (menuItemService.addMenuItem(rId, iName, desc, price, cat, true)) {
                    System.out.println("Menu item added!");
                }
                break;
            case 3:
                for (Restaurant r : restaurantService.getRestaurantsByOwner(loggedInUser.getUserId())) {
                    System.out.println(r);
                }
                break;
            case 4:
                System.out.println("--- Your Restaurants ---");
                List<Restaurant> myRests2 = restaurantService.getRestaurantsByOwner(loggedInUser.getUserId());
                if (myRests2.isEmpty()) {
                    System.out.println("You have no restaurants yet.");
                    break;
                }
                for (Restaurant r : myRests2) System.out.println(r);

                System.out.print("Enter Restaurant ID to manage orders: ");
                int targetRestId = getIntInput();
                List<Order> restOrders = orderService.getRestaurantOrders(targetRestId);
                if (restOrders.isEmpty()) {
                    System.out.println("No orders for this restaurant.");
                } else {
                    for (Order o : restOrders) System.out.println(o);
                }

                System.out.print("Order ID to update: "); int oId = getIntInput();
                System.out.print("New Status (PREPARING/READY): "); String status = scanner.nextLine().toUpperCase();
                if (orderService.updateOrderStatus(oId, status)) {
                    System.out.println("Status updated!");
                }
                break;
            case 5:
                loggedInUser = null;
                break;
        }
    }

    private static void showDeliveryMenu() {
        System.out.println("\n--- DELIVERY PARTNER MENU ---");
        System.out.println("1. View READY Orders");
        System.out.println("2. Accept Delivery");
        System.out.println("3. Mark Order as DELIVERED");
        System.out.println("4. Logout");
        System.out.print("Choice: ");
        int choice = getIntInput();

        switch (choice) {
            case 1:
                List<Order> ready = orderService.getReadyOrders();
                if (ready.isEmpty()) System.out.println("No orders ready for pickup.");
                else for (Order o : ready) System.out.println(o);
                break;
            case 2:
                System.out.println("--- READY Orders ---");
                List<Order> rOrders = orderService.getReadyOrders();
                if (rOrders.isEmpty()) {
                    System.out.println("No orders ready for pickup.");
                    break;
                }
                for (Order o : rOrders) System.out.println(o);

                System.out.print("Order ID to Accept: "); int oId = getIntInput();
                if (orderService.acceptDelivery(oId, loggedInUser.getUserId())) {
                    System.out.println("Order accepted! Status is OUT_FOR_DELIVERY.");
                }
                break;
            case 3:
                System.out.println("--- Your Assigned Orders ---");
                List<Order> myDeliveries = orderService.getDeliveryPartnerOrders(loggedInUser.getUserId());
                if (myDeliveries.isEmpty()) {
                    System.out.println("You have no assigned orders.");
                    break;
                }
                for (Order o : myDeliveries) System.out.println(o);

                System.out.print("Order ID to complete: "); int completeId = getIntInput();
                if (orderService.updateOrderStatus(completeId, "DELIVERED")) {
                    System.out.println("Delivery completed successfully.");
                }
                break;
            case 4:
                loggedInUser = null;
                break;
        }
    }

    private static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return input.isEmpty() ? -1 : Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            String input = scanner.nextLine();
            return input.isEmpty() ? -1.0 : Double.parseDouble(input);
        } catch (Exception e) {
            return -1.0;
        }
    }
}

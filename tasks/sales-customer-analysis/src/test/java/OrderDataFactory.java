import com.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public final class OrderDataFactory {

    public static List<Order> generateOrders() {
        List<Order> orders = new ArrayList<>();
        orders.addAll(generateDeliveredOrders());
        orders.addAll(generateNoDeliveredOrders());
        orders.addAll(generateOrdersWithTheSameCustomer(5));

        return orders;
    }

    public static List<Order> generateDeliveredOrders() {
        List<Customer> customers = generateCustomers();
        List<OrderItem> orderItems = generateOrderItems();

        return Arrays.asList(
                new Order("ORDER-4", LocalDateTime.of(2023, 5, 4, 9, 0), customers.get(0),
                        Arrays.asList(orderItems.get(0), orderItems.get(1)),
                        OrderStatus.DELIVERED),
                new Order("ORDER-6", LocalDateTime.of(2023, 5, 6, 15, 0), customers.get(4),
                        Arrays.asList(orderItems.get(0), orderItems.get(2), orderItems.get(3), orderItems.get(4)),
                        OrderStatus.DELIVERED)
        );
    }

    public static List<Order> generateNoDeliveredOrders() {
        List<Customer> customers = generateCustomers();
        List<OrderItem> orderItems = generateOrderItems();

        return Arrays.asList(
                new Order("ORDER-1", LocalDateTime.of(2023, 5, 1, 10, 0), customers.get(0),
                        Arrays.asList(orderItems.get(5), orderItems.get(6)),
                        OrderStatus.NEW),
                new Order("ORDER-2", LocalDateTime.of(2023, 5, 2, 12, 0), customers.get(1),
                        Arrays.asList(orderItems.get(7), orderItems.get(8)),
                        OrderStatus.PROCESSING),
                new Order("ORDER-3", LocalDateTime.of(2023, 5, 3, 14, 0), customers.get(2),
                        Arrays.asList(orderItems.get(6), orderItems.get(9), orderItems.get(10)),
                        OrderStatus.SHIPPED),
                new Order("ORDER-5", LocalDateTime.of(2023, 5, 5, 11, 30), customers.get(3),
                        Arrays.asList(orderItems.get(11), orderItems.get(12)),
                        OrderStatus.CANCELLED),
                new Order("ORDER-7", LocalDateTime.of(2023, 5, 20, 15, 0), customers.get(3),
                        Arrays.asList(orderItems.get(6), orderItems.get(13), orderItems.get(14)),
                        OrderStatus.SHIPPED)
        );
    }

    public static List<Order> generateOrdersWithTheSameCustomer(int ordersQuantity) {
        Customer customer = new Customer("CUST-6", "Vital", "vital@mail.com",
                LocalDateTime.of(2020, 1, 10, 9, 0), 25, "Berlin");
        List<OrderItem> orderItems = generateOrderItems();

        return IntStream.rangeClosed(1, ordersQuantity)
                .mapToObj(i ->
                     new Order(
                            "ORDER-" + i,
                            LocalDateTime.of(2023, 5, i, 10, 0),
                            customer,
                            Arrays.asList(orderItems.get(0), orderItems.get(1)),
                            OrderStatus.NEW
                    )
                )
                .toList();
    }


    private static List<Customer> generateCustomers() {
        return Arrays.asList(
                new Customer("CUST-1", "Alice", "alice@mail.com",
                        LocalDateTime.of(2020, 1, 10, 9, 0), 25, "Berlin"),
                new Customer("CUST-2", "Bob", "bob@mail.com",
                        LocalDateTime.of(2019, 3, 15, 14, 0), 32, "Madrid"),
                new Customer("CUST-3", "Charlie", "charlie@mail.com",
                        LocalDateTime.of(2021, 6, 20, 10, 0), 28, "Berlin"),
                new Customer("CUST-4", "Diana", "diana@mail.com",
                        LocalDateTime.of(2022, 7, 5, 12, 0), 30, "Paris"),
                new Customer("CUST-5", "Ethan", "ethan@mail.com",
                        LocalDateTime.of(2018, 11, 25, 11, 0), 35, "Berlin"));
    }

    private static List<OrderItem> generateOrderItems() {
        return Arrays.asList(
                new OrderItem("Sofa", 1, 100, Category.HOME),
                new OrderItem("Table Lamp", 3, 20, Category.HOME),
                new OrderItem("Toy Car", 1, 40, Category.TOYS),
                new OrderItem("LEGO Set", 1, 60, Category.TOYS),
                new OrderItem("Board Game", 1, 35, Category.TOYS),
                new OrderItem("iPhone", 1, 999.50, Category.ELECTRONICS),
                new OrderItem("Headphones", 2, 150.30, Category.ELECTRONICS),
                new OrderItem("T-Shirt", 3, 25.50, Category.CLOTHING),
                new OrderItem("Sneakers", 1, 89.70, Category.CLOTHING),
                new OrderItem("Book: Java Basics", 2, 15.70, Category.BOOKS),
                new OrderItem("Lamp", 1, 40.50, Category.HOME),
                new OrderItem("Lipstick", 2, 20.50, Category.BEAUTY),
                new OrderItem("Shampoo", 1, 12.70, Category.BEAUTY),
                new OrderItem("Eyeliner", 1, 12.30, Category.BEAUTY),
                new OrderItem("Face Cream", 1, 25.70, Category.BEAUTY)
        );
    }

}

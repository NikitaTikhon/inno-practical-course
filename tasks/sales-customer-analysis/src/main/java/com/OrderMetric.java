package com;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for calculating metrics and extracting information from a list of orders.
 * Provides methods for cities, total income, popular products, average checks, and customer activity.
 */
public final class OrderMetric {

    private OrderMetric() {}

    /**
     * Returns a list of unique city names where orders were placed.
     *
     * @param orders List of orders to analyze
     * @return List of unique city names
     */
    public static List<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .distinct()
                .toList();
    }

    /**
     * Calculates the total income of all delivered orders.
     *
     * @param orders List of orders to analyze
     * @return Total income as a double
     */
    public static double calculateTotalIncomeCompletedOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.DELIVERED))
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Returns the name of the most popular product (by quantity sold) among delivered orders.
     *
     * @param orders List of orders to analyze
     * @return Product name of the most sold item
     * @throws RuntimeException if there are no delivered orders
     */
    public static String getMostPopularProduct(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.DELIVERED))
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.toMap(OrderItem::getProductName, OrderItem::getQuantity, Integer::sum))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("No order found"))
                .getKey();
    }

    /**
     * Calculates the average total check for all delivered orders.
     * The check for each order is the sum of price * quantity of all items in the order.
     *
     * @param orders List of orders to analyze
     * @return Average check as a double; returns 0.0 if there are no delivered orders
     */
    public static double calculateAverageCheckDeliveredOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.DELIVERED))
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum())
                .average()
                .orElse(0.0);
    }

    /**
     * Returns a list of customers who have more than 5 orders in total.
     *
     * @param orders List of orders to analyze
     * @return List of customers with more than five orders
     */
    public static List<Customer> getCustomersWithMoreThanFiveOrders(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }

}
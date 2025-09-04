package com;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class OrderMetric {

    public static List<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .distinct()
                .toList();
    }

    public static double calculateTotalIncomeCompletedOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.DELIVERED))
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

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

    public static double calculateAverageCheckDeliveredOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.DELIVERED))
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum())
                .average()
                .orElse(0.0);
    }

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
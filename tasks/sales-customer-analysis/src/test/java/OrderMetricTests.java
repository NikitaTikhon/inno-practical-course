import com.innowise.dto.Customer;
import com.innowise.service.OrderMetric;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.OrderDataFactory;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderMetricTests {

    @Test
    @DisplayName("List of unique cities")
    public void returnUniqueCities() {
        List<String> uniqueCities = OrderMetric.getUniqueCities(OrderDataFactory.generateOrders());
        List<String> expectedCities = List.of("Berlin", "Madrid", "Paris");

        assertEquals(expectedCities.size(), uniqueCities.size());
        assertTrue(expectedCities.containsAll(uniqueCities));
    }

    @Test
    @DisplayName("Total income for all completed orders")
    void returnCalculatedTotalIncomeCompletedOrders() {
        double totalIncome = OrderMetric.calculateTotalIncomeCompletedOrders(OrderDataFactory.generateOrders());
        double expectedIncome = (100 + 20 * 3) + (40 + 60 + 35 + 100);

        assertEquals(expectedIncome, totalIncome, 0.0001);
    }

    @Test
    @DisplayName("The most popular product by sales")
    void returnMostPopularProduct() {
        String mostPopularProduct = OrderMetric.getMostPopularProduct(OrderDataFactory.generateOrders());
        String expectedProduct = "Table Lamp";

        assertEquals(expectedProduct, mostPopularProduct);
    }

    @Test
    @DisplayName("The most popular product by sales with no delivered orders")
    void returnMostPopularProductWithNoDeliveredOrders() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> OrderMetric.getMostPopularProduct(OrderDataFactory.generateNoDeliveredOrders()));
        assertEquals("No order found", e.getMessage());
    }

    @Test
    @DisplayName("Average check for successfully delivered orders")
    void returnCalculatedAverageCheckDeliveredOrders() {
        double averageCheck = OrderMetric.calculateAverageCheckDeliveredOrders(OrderDataFactory.generateOrders());
        double expectedAverage = (double) ((100 + 20 * 3) + (40 + 60 + 35 + 100)) / 2;

        assertEquals(expectedAverage, averageCheck, 0.0001);
    }

    @Test
    @DisplayName("Average check with no delivered orders")
    void returnCalculatedAverageCheckWithNoDeliveredOrders() {
        double averageCheck = OrderMetric.calculateAverageCheckDeliveredOrders(OrderDataFactory.generateNoDeliveredOrders());
        double expectedAverage = 0.0;

        assertEquals(expectedAverage, averageCheck, 0.0001);
    }

    @Test
    @DisplayName("Customers who have more than 5 orders")
    void returnCustomersWithMoreThanFiveOrders() {
        List<Customer> customers = OrderMetric.getCustomersWithMoreThanFiveOrders(OrderDataFactory.generateOrdersWithTheSameCustomer(6));
        List<Customer> expectedCustomers = List.of(new Customer("CUST-6", "Vital", "vital@mail.com",
                LocalDateTime.of(2020, 1, 10, 9, 0), 25, "Berlin"));

        assertEquals(expectedCustomers.size(), customers.size());
        assertTrue(expectedCustomers.containsAll(customers));
    }

}

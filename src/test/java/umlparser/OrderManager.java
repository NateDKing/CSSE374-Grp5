package umlparser;

//Generated with ChatGPT, for purposes for testing.

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private List<Order> orders;
    
    public OrderManager() {
        orders = new ArrayList<>();
    }
    
    public void addOrder(Order order) {
        orders.add(order);
        Logger.getInstance().log("Order added: " + order.getOrderId());
    }
    
    public void processOrders() {
        for (Order order : orders) {
            System.out.println("Processing Order #" + order.getOrderId() 
                + ": " + order.getDescription() 
                + " - $" + order.getCost());
        }
        orders.clear();
        Logger.getInstance().log("All orders processed.");
    }
}

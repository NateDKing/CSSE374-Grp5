package umlparser;

//Generated with ChatGPT, for purposes for testing.

public class Order {
    private int orderId;
    private Beverage beverage;
    
    public Order(int orderId, Beverage beverage) {
        this.orderId = orderId;
        this.beverage = beverage;
        Logger.getInstance().log("Order created: " + orderId);
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public Beverage getBeverage() {
        return beverage;
    }
    
    public double getCost() {
        return beverage.cost();
    }
    
    public String getDescription() {
        return beverage.getDescription();
    }
}

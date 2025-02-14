package umlparser;

//Generated with ChatGPT, for purposes for testing.

public class JCUTApplication {
    public static void main(String[] args) {
        OrderManager orderManager = new OrderManager();
        
        // Create first beverage: Espresso with Milk and Mocha
        Beverage beverage1 = new Espresso();
        beverage1 = new Milk(beverage1);    // Decorator: Milk
        beverage1 = new Mocha(beverage1);   // Decorator: Mocha
        
        Order order1 = new Order(1, beverage1);
        orderManager.addOrder(order1);
        
        // Create second beverage: HouseBlend with Soy, Mocha, and Milk
        Beverage beverage2 = new HouseBlend();
        beverage2 = new Soy(beverage2);      // Decorator: Soy
        beverage2 = new Mocha(beverage2);    // Decorator: Mocha
        beverage2 = new Milk(beverage2);     // Decorator: Milk
        
        Order order2 = new Order(2, beverage2);
        orderManager.addOrder(order2);
        
        // Process all orders
        orderManager.processOrders();
    }
}

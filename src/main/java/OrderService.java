import java.util.List;

public class OrderService {
    
    private int thresholdAmount = 0;
    private int discountAmount = 0;
    private boolean buyOneGetOneActive = false;
    private boolean doubleElevenActive = false;
    
    public void configureThresholdDiscount(int threshold, int discount) {
        this.thresholdAmount = threshold;
        this.discountAmount = discount;
    }
    
    public void configureBuyOneGetOne(boolean active) {
        this.buyOneGetOneActive = active;
    }
    
    public void configureDoubleEleven(boolean active) {
        this.doubleElevenActive = active;
    }
    
    public Order checkout(List<OrderItem> items) {
        Order order = new Order();
        
        // Add items to the order and apply buy-one-get-one if applicable
        for (OrderItem item : items) {
            OrderItem newItem = new OrderItem(item.getProduct(), item.getQuantity());
            
            // Apply buy-one-get-one for cosmetics
            if (buyOneGetOneActive && "cosmetics".equals(item.getProduct().getCategory())) {
                newItem.setQuantity(item.getQuantity() + 1); // Add 1 free item for cosmetics
            }
            
            order.addOrderItem(newItem);
        }
        
        // Calculate original amount (based on original quantities, not doubled)
        int originalAmount = 0;
        for (OrderItem item : items) {
            originalAmount += item.getProduct().getUnitPrice() * item.getQuantity();
        }
        
        // Apply threshold discount if applicable
        int discount = 0;
        if (thresholdAmount > 0 && originalAmount >= thresholdAmount) {
            discount = discountAmount;
        }
        
        // Apply double eleven discount if applicable
        if (doubleElevenActive) {
            discount += calculateDoubleElevenDiscount(items);
        }
        
        int totalAmount = originalAmount - discount;
        
        order.setOriginalAmount(originalAmount);
        order.setDiscount(discount);
        order.setTotalAmount(totalAmount);
        
        return order;
    }
    
    private int calculateDoubleElevenDiscount(List<OrderItem> items) {
        int doubleElevenDiscount = 0;
        
        for (OrderItem item : items) {
            int quantity = item.getQuantity();
            int unitPrice = item.getProduct().getUnitPrice();
            
            // Double eleven rule: same product quantity-based discount
            if (quantity >= 12) {
                // For 12+ items of same product: discount based on quantity
                if (quantity >= 12 && quantity < 24) {
                    // 12-23 items: 200 discount
                    doubleElevenDiscount += 200;
                } else if (quantity >= 24) {
                    // 24+ items: 400 discount
                    doubleElevenDiscount += 400;
                }
            }
        }
        
        return doubleElevenDiscount;
    }
}
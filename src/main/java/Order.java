import java.util.ArrayList;
import java.util.List;

public class Order {
    private int totalAmount;
    private int originalAmount;
    private int discount;
    private List<OrderItem> orderItems;
    
    public Order() {
        this.orderItems = new ArrayList<>();
    }
    
    public int getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getOriginalAmount() {
        return originalAmount;
    }
    
    public void setOriginalAmount(int originalAmount) {
        this.originalAmount = originalAmount;
    }
    
    public int getDiscount() {
        return discount;
    }
    
    public void setDiscount(int discount) {
        this.discount = discount;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }
}
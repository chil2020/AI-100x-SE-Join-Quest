import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderStepDefinitions {
    
    private OrderService orderService;
    private Order resultOrder;
    private int thresholdAmount;
    private int discountAmount;
    private boolean buyOneGetOneActive;
    private boolean doubleElevenActive;
    
    public OrderStepDefinitions() {
        this.orderService = new OrderService();
    }

    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        // No setup needed for this scenario
    }
    
    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> promotionConfig = rows.get(0);
        
        thresholdAmount = Integer.parseInt(promotionConfig.get("threshold"));
        discountAmount = Integer.parseInt(promotionConfig.get("discount"));
        
        // Configure the order service with threshold discount
        orderService.configureThresholdDiscount(thresholdAmount, discountAmount);
    }
    
    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        buyOneGetOneActive = true;
        orderService.configureBuyOneGetOne(true);
    }
    
    @Given("雙十一優惠活動已啟動")
    public void 雙十一優惠活動已啟動() {
        doubleElevenActive = true;
        orderService.configureDoubleEleven(true);
    }

    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        List<OrderItem> items = new ArrayList<>();
        
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int quantity = Integer.parseInt(row.get("quantity"));
            int unitPrice = Integer.parseInt(row.get("unitPrice"));
            String category = row.get("category"); // may be null for some scenarios
            
            Product product = new Product(productName, unitPrice, category);
            OrderItem item = new OrderItem(product, quantity);
            items.add(item);
        }
        
        resultOrder = orderService.checkout(items);
    }

    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable dataTable) {
        List<Map<String, String>> expectedData = dataTable.asMaps(String.class, String.class);
        Map<String, String> expected = expectedData.get(0);
        
        if (expected.containsKey("totalAmount")) {
            int expectedTotalAmount = Integer.parseInt(expected.get("totalAmount"));
            assertEquals(expectedTotalAmount, resultOrder.getTotalAmount());
        }
        
        if (expected.containsKey("originalAmount")) {
            int expectedOriginalAmount = Integer.parseInt(expected.get("originalAmount"));
            assertEquals(expectedOriginalAmount, resultOrder.getOriginalAmount());
        }
        
        if (expected.containsKey("discount")) {
            int expectedDiscount = Integer.parseInt(expected.get("discount"));
            assertEquals(expectedDiscount, resultOrder.getDiscount());
        }
    }

    @And("the customer should receive:")
    public void the_customer_should_receive(DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps(String.class, String.class);
        
        assertEquals(expectedItems.size(), resultOrder.getOrderItems().size());
        
        for (int i = 0; i < expectedItems.size(); i++) {
            Map<String, String> expectedItem = expectedItems.get(i);
            OrderItem actualItem = resultOrder.getOrderItems().get(i);
            
            assertEquals(expectedItem.get("productName"), actualItem.getProduct().getName());
            assertEquals(Integer.parseInt(expectedItem.get("quantity")), actualItem.getQuantity());
        }
    }
}
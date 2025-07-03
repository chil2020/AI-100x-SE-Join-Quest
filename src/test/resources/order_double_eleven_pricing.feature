@order_double_eleven_pricing
Feature: E-commerce Double Eleven discount
  As a shopper
  I want the system to calculate my order total with applicable promotions
  So that I can understand how much to pay and what items I will receive

  Scenario: 雙十一優惠 - 購買12件同種商品享有部分折扣
    Given 雙十一優惠活動已啟動
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1200           | 200      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: 雙十一優惠 - 購買27件同種商品享有多層折扣
    Given 雙十一優惠活動已啟動
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 27       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 2700           | 400      | 2300        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 27       |

  Scenario: 雙十一優惠 - 購買10種不同商品各1件不享有折扣
    Given 雙十一優惠活動已啟動
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 商品A         | 1        | 100       |
      | 商品B         | 1        | 100       |
      | 商品C         | 1        | 100       |
      | 商品D         | 1        | 100       |
      | 商品E         | 1        | 100       |
      | 商品F         | 1        | 100       |
      | 商品G         | 1        | 100       |
      | 商品H         | 1        | 100       |
      | 商品I         | 1        | 100       |
      | 商品J         | 1        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 商品A         | 1        |
      | 商品B         | 1        |
      | 商品C         | 1        |
      | 商品D         | 1        |
      | 商品E         | 1        |
      | 商品F         | 1        |
      | 商品G         | 1        |
      | 商品H         | 1        |
      | 商品I         | 1        |
      | 商品J         | 1        |

  Scenario: 雙十一優惠 - 混合商品部分享有折扣
    Given 雙十一優惠活動已啟動
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 15       | 100       |
      | T-shirt     | 5        | 200       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 2500           | 200      | 2300        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 15       |
      | T-shirt     | 5        |

  Scenario: 雙十一優惠與門檻折扣疊加
    Given 雙十一優惠活動已啟動
    And the threshold discount promotion is configured:
      | threshold | discount |
      | 1000      | 100      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
      | T-shirt     | 2        | 500       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 2200           | 300      | 1900        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |
      | T-shirt     | 2        |

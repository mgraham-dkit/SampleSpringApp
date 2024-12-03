package web_patterns.persistence;

import web_patterns.business.Order;

import java.util.List;

public interface OrderDao {
    List<Order> getAllOrders();
}

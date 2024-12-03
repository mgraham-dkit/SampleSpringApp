package web_patterns.persistence;

import lombok.extern.slf4j.Slf4j;
import web_patterns.business.Order;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderDaoImpl extends MySQLDao implements OrderDao {
    public OrderDaoImpl(){
        super();
    }

    public OrderDaoImpl(Connection conn){
        super(conn);
    }

    public OrderDaoImpl(String propertiesFilename){
        super(propertiesFilename);
    }

    @Override
    public List<Order> getAllOrders() {
        // Create variable to hold the customer info from the database
        List<Order> orders = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // Get a statement from the connection
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders")) {
            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Repeatedly try to get a customer from the resultset
                while(rs.next()){
                    Order o = mapRow(rs);
                    orders.add(o);
                }
            }
        } catch (SQLException e) {
            log.error("SQLException occurred when attempting to get all orders", e);
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }

        return orders;
    }

    private Order mapRow(ResultSet rs) throws SQLException{
        // ShippedDate can be null, so it has to be handled a bit more carefully
        Timestamp shipping = rs.getTimestamp("shippedDate");
        LocalDateTime shippedDate = null;
        if(shipping != null){
            shippedDate = shipping.toLocalDateTime();
        }

        return Order.builder()
                .orderNumber(rs.getInt("orderNumber"))
                .orderDate(rs.getTimestamp("orderDate").toLocalDateTime())
                .requiredDate(rs.getTimestamp("requiredDate").toLocalDateTime())
                .shippedDate(shippedDate)
                .status(rs.getString("status"))
                .comments(rs.getString("comments"))
                .customerNumber(rs.getInt("customerNumber"))
                .build();
    }

    public static void main(String[] args) {
        OrderDao orderDao = new OrderDaoImpl("database.properties");
        List<Order> orders = orderDao.getAllOrders();
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}

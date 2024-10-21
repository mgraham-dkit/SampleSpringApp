package web_patterns.persistence;

import web_patterns.business.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl extends MySQLDao implements CustomerDao {

    public CustomerDaoImpl(){
        super();
    }

    public CustomerDaoImpl(Connection conn){
        super(conn);
    }

    public CustomerDaoImpl(String propertiesFilename){
        super(propertiesFilename);
    }

    @Override
    public Customer getById(int id) {
        Customer customer = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers where customerNumber = ?")) {
            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, id);

            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if(rs.next()) {
                    customer = mapRow(rs);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        // Create variable to hold the customer info from the database
        List<Customer> customers = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // Get a statement from the connection
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers")) {
            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Repeatedly try to get a customer from the resultset
                while(rs.next()){
                    Customer c = mapRow(rs);
                    customers.add(c);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }

        return customers;
    }

    @Override
    public boolean deleteById(int id) {
        int rowsAffected = 0;

        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM customers where customerNumber = ?")) {
            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, id);

            // Execute the operation
            // Remember that when you are doing an update, a delete or an insert,
            // your only result will be a number indicating how many rows were affected
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the superclass method
            super.freeConnection(conn);
        }

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        // Get the pieces of a customer from the resultset and create a new Customer
        return Customer.builder()
                .customerNumber(rs.getInt("customerNumber"))
                .customerName(rs.getString("customerName"))
                .contactLastName(rs.getString("contactLastName"))
                .contactFirstName(rs.getString("contactFirstName"))
                .phone(rs.getString("phone"))
                .addressLine1(rs.getString("addressLine1"))
                .addressLine2(rs.getString("addressLine2"))
                .city(rs.getString("city"))
                .state(rs.getString("state"))
                .postalCode(rs.getString("postalCode"))
                .country(rs.getString("country"))
                .salesRepEmployeeNumber(rs.getInt("salesRepEmployeeNumber"))
                .creditLimit(rs.getDouble("creditLimit"))
                .build(); // Without this, we won't make anything!
    }

    public static void main(String[] args) {
        CustomerDao customerDao = new CustomerDaoImpl("database.properties");
        List<Customer> customers = customerDao.getAllCustomers();
        System.out.println(customers);
        System.out.println("------------------------------");
        System.out.println("Customer with id 119: " + customerDao.getById(119));
    }
}

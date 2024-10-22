package web_patterns.persistence;

import web_patterns.business.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

public class UserDaoImpl extends MySQLDao implements UserDao {
    public UserDaoImpl(String propertiesFile) {
        super(propertiesFile);
    }

    public UserDaoImpl(Connection c) {
        super(c);
    }

    public UserDaoImpl() {
        super();
    }


    @Override
    public boolean register(User user) {
        boolean added = false;

        Connection c = super.getConnection();
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?)")) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getEmail());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                added = true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(LocalDateTime.now() + ": SQLIntegrityConstraintViolationException occurred in " +
                    "register() when " +
                    "adding new User");
            System.out.println("\tError: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(LocalDateTime.now() + ": SQLException occurred in register() when preparing SQL");
            System.out.println("\tError: " + e.getMessage());
        }

        super.freeConnection(c);

        return added;
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl("database.properties");
        User user = new User("michelle", "password", "Michelle", "Graham", "michelle@dkit.ie");
        boolean added = userDao.register(user);
        if(added){
            System.out.println(user + " was added correctedly");
        }else{
            System.out.println(user + "could not be added.");
        }

    }
}

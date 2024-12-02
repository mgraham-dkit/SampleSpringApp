package web_patterns.persistence;

import lombok.extern.slf4j.Slf4j;
import web_patterns.business.User;

import java.sql.*;
import java.time.LocalDateTime;

@Slf4j
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
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setBoolean(5, user.isAdmin());
            ps.setString(5, user.getEmail());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                added = true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            log.error("SQLIntegrityConstraintViolationException occurred when attempting to register new User", e);
        } catch (SQLException e) {
            log.error("SQLException occurred when attempting to register new User", e);
        }

        super.freeConnection(c);

        return added;
    }

    @Override
    public User login(String username, String password) {
        User user = null;
        Connection c = super.getConnection();
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    user = mapRow(rs);
                }
            }catch (SQLException e) {
                log.error("SQLException occurred when processing login query resultset", e);
            }
        }catch (SQLException e) {
            log.error("SQLException occurred when attempting to login User", e);
        }
        super.freeConnection(c);

        return user;
    }

    private static User mapRow(ResultSet rs) throws SQLException {
        return User.builder()
                .username(rs.getString("username"))
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .isAdmin(rs.getBoolean("isAdmin"))
                .email(rs.getString("email"))
                .build();
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl("database.properties");
        User user = new User("michelle", "password", "Michelle", "Graham", false, "michelle@dkit.ie");
        boolean added = userDao.register(user);
        if(added){
            System.out.println(user + " was added correctly");
        }else{
            System.out.println(user + "could not be added.");
        }

        String username = "MichelleA";
        String password = "password";
        User loggedIn = userDao.login(username, password);
        if(loggedIn != null){
            System.out.println("Logged in as "+ loggedIn);
        }else{
            System.out.println("User with username " + username + " could not be logged in");
        }
    }
}

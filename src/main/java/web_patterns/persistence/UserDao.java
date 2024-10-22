package web_patterns.persistence;

import web_patterns.business.User;

public interface UserDao {
    public boolean register(User user);
}

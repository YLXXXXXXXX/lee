package priv.jesse.ercode.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import priv.jesse.ercode.entity.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    /**
     * 根据用户名，密码查询用户
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPasswordAndRole(String username, String password,Integer role);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    List<User> findByUsername(String username);
}

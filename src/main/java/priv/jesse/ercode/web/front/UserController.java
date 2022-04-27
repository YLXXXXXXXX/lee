package priv.jesse.ercode.web.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.jesse.ercode.entity.Product;
import priv.jesse.ercode.entity.User;
import priv.jesse.ercode.entity.pojo.ResultBean;
import priv.jesse.ercode.service.ProductService;
import priv.jesse.ercode.service.UserService;
import priv.jesse.ercode.service.exception.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    /**
     * 打开编辑页面
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping("/toEdit.html")
    public String toEdit(int id, Map<String, Object> map) {
        User user = userService.findById(id);
        map.put("user", user);
        return "front/user/edit";
    }

    @RequestMapping("/toLogin.html")
    public String toLogin() {
        return "front/user/login";
    }

    @RequestMapping("/toSecret.html")
    public String toSecret(int productId, Map<String, Object> map) {
        map.put("productId", productId);
        return "front/user/secret";
    }

    @RequestMapping("/toSecretShow.html")
    public String toSecretShow(String secret, Map<String, Object> map) {
        map.put("secret", secret);
        return "front/user/secret_show";
    }


    @RequestMapping("/toRegister.html")
    public String toRegister() {
        return "front/user/register";
    }


    @RequestMapping("/add.do")
    public void add(String username,
                    String password,
                    String name,
                    String phone,
                    String jobTitle,
                    int role,
                    HttpServletResponse response) throws IOException {
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password);
        user.setName(name);
        user.setJobTitle(jobTitle);
        user.setRole(role);
        userService.create(user);
        // 注册完成后重定向到登录页面
        response.sendRedirect("/ercode/user/toLogin.html");
    }

    /*@RequestMapping("/check_secret.do")
    public String check_secret(int productId,
                             String secret,
                             HttpServletResponse response, Map<String, Object> map) throws IOException {
        Product product = productService.findByIdAndSecret(productId, secret);
        if (product != null) {
            map
            return "front/product/detail";
        }
        // 注册完成后重定向到登录页面
        response.sendRedirect("/ercode/user/toLogin.html");
    }*/


    /**
     * 验证用户名是否唯一
     *
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUsername.do")
    public ResultBean<Boolean> checkUsername(String username) {
        List<User> users = userService.findByUsername(username);
        if (users == null || users.isEmpty()) {
            return new ResultBean<>(true);
        }
        return new ResultBean<>(false);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/update.do")
    public ResultBean<Boolean> update(int id, String username,
                                      String password, String name,
                                      String phone, String email,
                                      String addr, int age, String nation) {
        // 更新前先查询
        User user = userService.findById(id);
        user.setId(id);
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        userService.update(user);
        return new ResultBean<>(true);
    }


    /**
     * 登录请求
     *
     * @param username
     * @param password
     */
    //@ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/login.do")
    public void login(String username, String password, int productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userService.checkLogin(username, password, 0);
        if (user != null) {
            Product product = productService.findById(productId);
            response.sendRedirect("/ercode/user/toSecretShow.html?secret=" + product.getSecret());
        } else {
            throw new LoginException("用户名或密码错误");
        }


    }

}

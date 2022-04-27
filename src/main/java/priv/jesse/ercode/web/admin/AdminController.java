package priv.jesse.ercode.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import priv.jesse.ercode.entity.User;
import priv.jesse.ercode.service.UserService;
import priv.jesse.ercode.service.exception.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    /**
     * 访问首页
     *
     * @return
     */
    @RequestMapping("/toIndex.html")
    public String toIndex() {
        return "admin/index";
    }

    /**
     * 访问登录页面
     *
     * @return
     */
    @RequestMapping("/toLogin.html")
    public String toLogin() {
        return "admin/login";
    }

    /**
     * 登录请求
     *
     * @param username
     * @param password
     */
    //@ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login.do")
    public void login(String username, String password, Integer role,HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userService.checkLogin(username, password,role);
        if (user != null) {
            request.getSession().setAttribute("login_user", user);
            if (user.getRole() == 1) {
                response.sendRedirect("/ercode/admin/toIndex.html");
            } else if (user.getRole() == 2) {
                response.sendRedirect("/ercode/factory/toIndex.html");
            }  else if (user.getRole() == 3) {
                response.sendRedirect("/ercode/quality/toIndex.html");
            } else if (user.getRole() == 4) {
                response.sendRedirect("/ercode/shop/toIndex.html");
            }
        } else {
            throw new LoginException("用户名或密码错误");
        }


    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/logout.do")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("login_user");
        response.sendRedirect("toLogin.html");
    }
}

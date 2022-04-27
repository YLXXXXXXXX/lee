package priv.jesse.ercode.web.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import priv.jesse.ercode.service.UserService;

@Controller
@RequestMapping("/factory")
public class FactoryController {
    @Autowired
    private UserService userService;

    /**
     * 访问首页
     *
     * @return
     */
    @RequestMapping("/toIndex.html")
    public String toIndex() {
        return "factory/index";
    }
}

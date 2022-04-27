package priv.jesse.ercode.web.quality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import priv.jesse.ercode.service.UserService;

@Controller
@RequestMapping("/quality")
public class QualityController {
    @Autowired
    private UserService userService;

    /**
     * 访问首页
     *
     * @return
     */
    @RequestMapping("/toIndex.html")
    public String toIndex() {
        return "quality/index";
    }
}

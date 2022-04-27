package priv.jesse.ercode.web.front;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.jesse.ercode.entity.Information;
import priv.jesse.ercode.entity.Product;
import priv.jesse.ercode.entity.pojo.ResultBean;
import priv.jesse.ercode.service.InformationService;
import priv.jesse.ercode.service.ProductService;
import priv.jesse.ercode.service.exception.LoginException;
import priv.jesse.ercode.utils.AESUtil;


import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/toDetail.html")
    public String toDetail(int productId, String secret, Map<String, Object> map) throws Exception {
        Product product = productService.findByIdAndSecret(productId, secret);
        if (product != null) {
            String informationDetail = JSON.toJSONString(informationService.getInfoDetail(product.getId()));
            String listSecretData = AESUtil.Encrypt(informationDetail, product.getSecret());
            map.put("listSecretData", listSecretData);
            map.put("product", product);
            return "front/product/detail";
        } else {
            throw new LoginException("密钥错误");
        }
    }


    @RequestMapping("/detail.do")
    public ResultBean<List<Information>> detail(int productId) {
        List<Information> list = informationService.findAllByProductId(productId);
        return new ResultBean<>(list);
    }


}

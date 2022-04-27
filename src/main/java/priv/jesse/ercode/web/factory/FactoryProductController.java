package priv.jesse.ercode.web.factory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import priv.jesse.ercode.entity.Classification;
import priv.jesse.ercode.entity.Product;
import priv.jesse.ercode.entity.pojo.ResultBean;
import priv.jesse.ercode.service.ClassificationService;
import priv.jesse.ercode.service.ProductService;
import priv.jesse.ercode.utils.FileUtil;
import priv.jesse.ercode.utils.QRCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/factory/product")
public class FactoryProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ClassificationService classificationService;

    @RequestMapping("/toList.html")
    public String toList() {
        return "factory/product/list";
    }

    @ResponseBody
    @RequestMapping("/list.do")
    public ResultBean<List<Product>> listProduct(int pageindex,
                                                 @RequestParam(value = "pageSize", defaultValue = "15") int pageSize, HttpServletRequest request) {
        Pageable pageable = new PageRequest(pageindex, pageSize, null);
        String keyword = request.getParameter("keyword");
        List<Product> list = null;
        if ("".equals(keyword)) {
            list = productService.findAllByStatusGreaterThanEqual(1,pageable).getContent();
        } else {
            list = productService.findByTitleIsLikeAndStatusAfter("%" + keyword + "%",1, pageable).getContent();
        }
        for (Product product : list) {
            try {
                InetAddress localHost = Inet4Address.getLocalHost();
                String bufferedImageStr = QRCodeUtil.encodeBase64(localHost.getHostAddress() + ":8081/ercode/user/toSecret.html?productId="+product.getId());
                product.setErcodeUrl(bufferedImageStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResultBean<>(list);
    }

    @ResponseBody
    @RequestMapping("/getTotal")
    public ResultBean<Integer> getTotal(HttpServletRequest request) {
        Pageable pageable = new PageRequest(1, 15, null);
        String keyword = request.getParameter("keyword");
        int total = 0;
        if ("".equals(keyword)) {
            total = (int) productService.findAllByStatusGreaterThanEqual(1, pageable).getTotalElements();
        } else {
            total = (int) productService.findByTitleIsLikeAndStatusAfter("%" + keyword + "%",1, pageable).getTotalElements();
        }
        return new ResultBean<>(total);
    }
    

    //更新状态
    @RequestMapping(method = RequestMethod.POST, value = "/complete_info.do")
    @ResponseBody
    public ResultBean<Boolean> complete_info(int id, int status,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        Product product = productService.findById(id);
        product.setStatus(status);
        boolean flag = false;
        try {
            productService.update(product);
            flag = true;
        } catch (Exception e) {
            throw new Exception(e);
        }

        if (!flag) {
            return new ResultBean<>(false);
        }
        return new ResultBean<>(true);

    }
    


}

package priv.jesse.ercode.web.admin;

import com.alibaba.fastjson.JSON;
import com.google.zxing.WriterException;
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
import priv.jesse.ercode.service.InformationService;
import priv.jesse.ercode.service.ProductService;
import priv.jesse.ercode.utils.AESUtil;

import priv.jesse.ercode.utils.FileUtil;
import priv.jesse.ercode.utils.QRCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private InformationService informationService;

    @RequestMapping("/toList.html")
    public String toList() {
        return "admin/product/list";
    }

    @RequestMapping("/toAdd.html")
    public String toAdd() {
        return "admin/product/add";
    }

    @RequestMapping("/toEdit.html")
    public String toEdit(int id, Map<String, Object> map) {
        Product product = productService.findById(id);
        Classification classification = classificationService.findById(product.getCsid());
        product.setCategorySec(classification);
        map.put("product", product);
        return "admin/product/edit";
    }

    @ResponseBody
    @RequestMapping("/list.do")
    public ResultBean<List<Product>> listProduct(int pageindex,
                                                 @RequestParam(value = "pageSize", defaultValue = "15") int pageSize, HttpServletRequest request) {
        Pageable pageable = new PageRequest(pageindex, pageSize, null);
        String keyword = request.getParameter("keyword");
        List<Product> list = null;
        if ("".equals(keyword)) {
            list = productService.findAllByStatusGreaterThanEqual(0, pageable).getContent();
        } else {
            list = productService.findByTitleIsLikeAndStatusAfter("%" + keyword + "%", 0, pageable).getContent();
        }
        for (Product product : list) {
            try {
                InetAddress localHost = Inet4Address.getLocalHost();
                String url = localHost.getHostAddress() + ":8081/ercode/user/toSecret.html?productId=" + product.getId() ;
                String bufferedImageStr = QRCodeUtil.encodeBase64(url);
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
            total = (int) productService.findAllByStatusGreaterThanEqual(0, pageable).getTotalElements();
        } else {
            total = (int) productService.findByTitleIsLikeAndStatusAfter("%" + keyword + "%", 0, pageable).getTotalElements();
        }
        return new ResultBean<>(total);
    }

    @RequestMapping("/del.do")
    @ResponseBody
    public ResultBean<Boolean> del(int id) {
        productService.delById(id);
        return new ResultBean<>(true);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add.do")
    public void add(MultipartFile image,
                    String title,
                    String desc,
                    int csid,
                    HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        Product product = new Product();
        product.setTitle(title);
        product.setCsid(csid);
        product.setDescription(desc);
        product.setPdate(new Date());
        product.setSecret(FileUtil.getRandomStr());
        String imgUrl = FileUtil.saveFile(image);
        if (StringUtils.isNotBlank(imgUrl)) {
            product.setImage(imgUrl);
        }
        /*String fileName = FileUtil.getRandomStr() + ".png";
        String QRcodePath = "/ercode/admin/product/img/"+fileName;
        boolean flat = true;
        try {
            QRCodeUtil.generateQRCodeImage("xxx",100,100,"file/"+fileName);
        }  catch (WriterException e) {
            e.printStackTrace();
            flat = false;
        } catch (IOException e) {
            e.printStackTrace();
            flat = false;
        }
        if (flat) {
            product.setErcodeUrl(QRcodePath);
        }*/
        int id = productService.create(product);
        if (id <= 0) {
            request.setAttribute("message", "添加失败！");
            request.getRequestDispatcher("toAdd.html").forward(request, response);
        } else {
            request.getRequestDispatcher("toEdit.html?id=" + id).forward(request, response);
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/update.do")
    public void update(int id,
                       String title,
                       String desc,
                       int csid,
                       MultipartFile image,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        Product product = productService.findById(id);
        product.setTitle(title);
        product.setDescription(desc);

        product.setCsid(csid);
        product.setPdate(new Date());
        String imgUrl = FileUtil.saveFile(image);
        if (StringUtils.isNotBlank(imgUrl)) {
            product.setImage(imgUrl);
        }
        boolean flag = false;
        try {
            productService.update(product);
            flag = true;
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (!flag) {
            request.setAttribute("message", "更新失败！");
        }
        response.sendRedirect("toList.html");
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


    @RequestMapping(method = RequestMethod.GET, value = "/img/{filename:.+}")
    public void getImage(@PathVariable(name = "filename", required = true) String filename,
                         HttpServletResponse res) throws IOException {
        File file = new File("file/" + filename);
        if (file != null && file.exists()) {
            res.setHeader("content-type", "application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            res.setContentLengthLong(file.length());
            Files.copy(Paths.get(file.toURI()), res.getOutputStream());
        }
    }


}

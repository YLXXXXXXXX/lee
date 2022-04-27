package priv.jesse.ercode.web.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import priv.jesse.ercode.entity.Classification;
import priv.jesse.ercode.entity.Information;
import priv.jesse.ercode.entity.Product;
import priv.jesse.ercode.entity.User;
import priv.jesse.ercode.entity.pojo.ResultBean;
import priv.jesse.ercode.service.ClassificationService;
import priv.jesse.ercode.service.InformationService;
import priv.jesse.ercode.service.ProductService;
import priv.jesse.ercode.service.exception.LoginException;
import priv.jesse.ercode.utils.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/factory/information")
public class FactoryInformationController {

    @Autowired
    private InformationService informationService;


    @Autowired
    private ProductService productService;

    /**
     * 返回列表页面
     *
     * @return
     */
    @RequestMapping("/toList.html")
    public String toList() {
        return "factory/information/list";
    }

    /**
     * 打开添加分类页面
     *
     * @return
     */
    @RequestMapping("/toAdd.html")
    public String toAdd(int productId,Map<String, Object> map) {
        map.put("productId",productId);
        return "factory/information/add";
    }

    /**
     * 打开编辑页面
     *
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("/toEdit.html")
    public String toEdit(int productId,  Map<String, Object> map,HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("login_user");
        List<Information> allByUserId = informationService.findAllByUserIdAndProductId(user.getId(),productId);
        int maxId = 0;
        for (Information information: allByUserId) {
            if (information.getId() > maxId) {
                maxId = information.getId();
            }
        }
        map.put("informations", allByUserId);
        map.put("productId", productId);
        map.put("maxId", maxId);
        return "factory/information/edit";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/add.do")
    public void add(HttpServletRequest request, HttpServletResponse response,Integer productId) throws Exception {
        String[] keys = request.getParameterValues("key[]");
        String[] types = request.getParameterValues("type[]");
        String[] values = request.getParameterValues("value[]");

        int length = keys.length;
        String[] images = new String[length];

        MultiValueMap<String, MultipartFile> multiFileMap = ((StandardMultipartHttpServletRequest) request).getMultiFileMap();
        Collection<List<MultipartFile>> values1 = multiFileMap.values();
        for (List<MultipartFile> list : values1) {
            int listCount = list.size();
            for (int a = 0; a < listCount; a++) {
                MultipartFile multipartFile = list.get(a);
                if (!"".equals(multipartFile.getOriginalFilename())) {
                    String imgUrl = FileUtil.saveFile(multipartFile);
                    images[a] = imgUrl;
                } else {
                    images[a] = "";
                }

            }
        }
        User loginUser = (User) request.getSession().getAttribute("login_user");
        for (int i = 0; i < length; i++) {
            Information information = new Information();
            information.setUserId(loginUser.getId());
            information.setKeyTitle(keys[i]);
            information.setProductId(productId);
            information.setTypes(Integer.parseInt(types[i]));
            information.setLevel(2);
            information.setAddTime(new Date());
            if (types[i].equals("1")) {
                information.setValueTitle(values[i]);
            } else {
                information.setValueTitle(images[i]);
            }
            informationService.create(information);
        }
        request.getRequestDispatcher("toList.html").forward(request, response);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/update.do")
    public void update(HttpServletRequest request,HttpServletResponse response,Integer productId) throws Exception{

        String[] keys = request.getParameterValues("key[]");
        String[] types = request.getParameterValues("type[]");
        String[] values = request.getParameterValues("value[]");
        String[] informationIds = request.getParameterValues("informationId[]");

        int length = keys.length;
        String[] images = new String[length];

        MultiValueMap<String, MultipartFile> multiFileMap = ((StandardMultipartHttpServletRequest) request).getMultiFileMap();
        Collection<List<MultipartFile>> values1 = multiFileMap.values();
        for (List<MultipartFile> list : values1) {
            int listCount = list.size();
            for (int a = 0; a < listCount; a++) {
                MultipartFile multipartFile = list.get(a);
                if (!"".equals(multipartFile.getOriginalFilename())) {
                    String imgUrl = FileUtil.saveFile(multipartFile);
                    images[a] = imgUrl;
                } else {
                    images[a] = "";
                }

            }
        }
        for (int i = 0; i < length; i++) {
            Information information = null;
            if (! "0".equals(informationIds[i])) {
                information = informationService.findById(Integer.parseInt(informationIds[i]));
                information.setKeyTitle(keys[i]);
                information.setTypes(Integer.parseInt(types[i]));
                if ("1".equals(types[i])) {
                    information.setValueTitle(values[i]);
                } else {
                    if (!"".equals(images[i])) {
                        information.setValueTitle(images[i]);
                    }
                }
                informationService.update(information);
            } else {
                User loginUser = (User) request.getSession().getAttribute("login_user");
                information = new Information();
                information.setUserId(loginUser.getId());
                information.setKeyTitle(keys[i]);
                information.setProductId(productId);
                information.setTypes(Integer.parseInt(types[i]));
                information.setLevel(2);
                information.setAddTime(new Date());
                if (types[i].equals("1")) {
                    information.setValueTitle(values[i]);
                } else {
                    information.setValueTitle(images[i]);
                }
                informationService.create(information);
            }

        }
        request.getRequestDispatcher("toList.html").forward(request, response);
    }

    @ResponseBody
    @RequestMapping("/del.do")
    public ResultBean<Boolean> del(int id) {
        informationService.delById(id);
        return new ResultBean<>(true);
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public ResultBean<List<Information>> findAll(int type,
                                                 int pageindex, @RequestParam(value = "pageSize", defaultValue = "15") int pageSize) {
        Pageable pageable = new PageRequest(1, 15, null);
        List<Information> list = informationService.findAll(pageable).getContent();
        return new ResultBean<>(list);
    }

    @ResponseBody
    @RequestMapping("/getTotal.do")
    public ResultBean<Integer> getTotal(int type) {
        Pageable pageable = new PageRequest(1, 15, null);
        int count = (int) informationService.findAll(pageable).getTotalElements();
        return new ResultBean<>(count);
    }




    @RequestMapping("/toDetail.html")
    public String toDetail(int productId, Map<String, Object> map) {
        Product product = productService.findById(productId);
        Map<String, List<Information>> listMap = informationService.getInfoDetail(productId);
        map.put("listMap", listMap);
        map.put("product", product);
        return "factory/information/detail";

    }
}

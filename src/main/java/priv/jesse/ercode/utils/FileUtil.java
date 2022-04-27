package priv.jesse.ercode.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Random;

public class FileUtil {

    /**
     * 保存上传的文件
     *
     * @param file
     * @return 文件下载的url
     * @throws Exception
     */
    public static String saveFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty())
            return "";
        File target = new File("file");
        if (!target.isDirectory()) {
            target.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
       /* MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(file.getBytes());*/
        String fileName = getRandomStr() + "." + getPostfix(originalFilename);
        File file1 = new File(target.getPath() + "/" + fileName);
        Files.write(Paths.get(file1.toURI()), file.getBytes(), StandardOpenOption.CREATE_NEW);
        return "/ercode/admin/product/img/" + fileName;
    }

    /**
     * 获得文件的后缀名
     *
     * @param fileName
     * @return
     */
    public static String getPostfix(String fileName) {
        if (fileName == null || "".equals(fileName.trim())) {
            return "";
        }
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        return "";
    }

    public static String getRandomStr() {
        int len = 16;
        char[] pwd = new char[len];
        int i = 0;
        while(i < len) {
            boolean b = new Random().nextBoolean();
            if (b) {
                pwd[i] = (char) ('0' + Math.random() * 10);
            } else {
                pwd[i] = (char) ('a' + Math.random() * 26);
            }
            i++;
        }
        return String.valueOf(pwd);
    }

}

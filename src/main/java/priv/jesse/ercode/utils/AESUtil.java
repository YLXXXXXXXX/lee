package priv.jesse.ercode.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


import java.util.Base64;

import static cn.hutool.crypto.symmetric.SymmetricAlgorithm.AES;

/**
 *
 * @author zhouzhou
 * 2020-9-29 15:27:42
 *
 */
public class AESUtil {
    private static  final String IV = "0102030405060708";

    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        cn.hutool.crypto.symmetric.AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, sKey.getBytes(), IV.getBytes());
        return  aes.encryptHex(sSrc);
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, sKey.getBytes(), IV.getBytes());
        String decryptStr = aes.decryptStr(sSrc, CharsetUtil.CHARSET_UTF_8);
        return  decryptStr;
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "1234567890123456";
        // 需要加密的字串
        String cSrc = "{\"管理员\":[{\"addTime\":1650624792000,\"id\":1,\"keyTitle\":\"111111111111\",\"level\":1,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"v1111111111\"},{\"addTime\":1650624793000,\"id\":2,\"keyTitle\":\"2222222222222\",\"level\":1,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"v22222222222\"},{\"addTime\":1650625138000,\"id\":9,\"keyTitle\":\"图\",\"level\":1,\"productId\":10,\"types\":2,\"userId\":23,\"valueTitle\":\"/ercode/admin/product/img/6zG5lN5qL6dY9sD.jpeg\"}],\"工厂\":[{\"addTime\":1650624793000,\"id\":3,\"keyTitle\":\"3333333333333\",\"level\":2,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"rrrrrrrrrrrrrrrrrrrr\"},{\"addTime\":1650624793000,\"id\":4,\"keyTitle\":\"444444444444\",\"level\":2,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"eeeeeeeeeee\"}],\"质检员\":[{\"addTime\":1650624793000,\"id\":5,\"keyTitle\":\"55555555555\",\"level\":3,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"ffffffff\"},{\"addTime\":1650624793000,\"id\":6,\"keyTitle\":\"66666666666666\",\"level\":3,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"ffffffffffffffff\"}],\"商家\":[{\"addTime\":1650624793000,\"id\":7,\"keyTitle\":\"777777777777\",\"level\":4,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"222222222\"},{\"addTime\":1650624793000,\"id\":8,\"keyTitle\":\"888888888888888\",\"level\":4,\"productId\":10,\"types\":1,\"userId\":23,\"valueTitle\":\"wwwwwwww\"}]}";
        System.out.println(cSrc);
        // 加密
        String enString = AESUtil.Encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AESUtil.Decrypt(enString, cKey);
        System.out.println("解密后的字串是：" + DeString);
    }
}
package com.pharmacy.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * 用于密码加密存储
 */
public class MD5Util {
    /**
     * MD5加密方法
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        try {
            // 获取MD5实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用UTF-8编码确保跨平台一致性
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // 将字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xff);
                // 确保每个字节转换为两位十六进制
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 异常处理，打印堆栈信息并返回原始密码
            e.printStackTrace();
            return password;
        }
    }
}
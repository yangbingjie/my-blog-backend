package cn.edu.tongji.myblogbackend.utils;

import java.net.URL;
import java.util.Random;
import java.net.MalformedURLException;


public class StringUtils {
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            stringBuffer.append(base.charAt(number));
        }
        return stringBuffer.toString();
    }
    public static String getLastString(String str) {
        URL url;
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            return null;
        }

        String file = url.getFile();
        String[] splitStr = file.split("/");
        int len = splitStr.length;
        String result = splitStr[len-1];
        return result;
    }
}

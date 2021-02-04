package com.yhy.like;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class DataInsertInRedis extends Thread {
    private static int i = 0;
    private static ArrayList<String> list = new ArrayList<>();

    @Override
    public void run() {
        synchronized (DataInsertInRedis.class) {
            HttpClient.doPost("http://localhost:8080/like", list.get(i++));
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            Map<String, String> params = new HashMap<>();
            params.put("type", String.valueOf(random(2)));
            params.put("typeId", String.valueOf(random(10)+1));
            params.put("status", String.valueOf(1));
            params.put("userId", String.valueOf(i));
            params.put("likeTime", String.valueOf(new Date()));
            String param = builderUrlParams(params);
            list.add(param);
        }
        for (int i = 0; i < 100; i++) {
            new DataInsertInRedis().start();
        }
    }

    public static String builderUrlParams(Map<String, String> params) {
        Set<String> keySet = params.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);
        StringBuilder sb = new StringBuilder();
        for (String key : keyList) {
            String value = params.get(key);
            if (isEmpty(value)) {
                continue;
            }
            sb.append(key);
            sb.append("=");
            try {
                sb.append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static int random(int upperLimit) {
        return (int) (Math.random() * (upperLimit + 1));
    }
}
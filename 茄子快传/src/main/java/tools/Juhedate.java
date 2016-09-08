package tools;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信精选调用示例代码 － 聚合数据 在线接口文档：http://www.juhe.cn/docs/147
 **/

public class Juhedate {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 5000;
    public static final int DEF_READ_TIMEOUT = 5000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    // 配置您申请的KEY
    public static final String WE_CHART_APPKEY = "71ab5d10fc641f79c55c61d7842b8e5d";// 微信精选
    public static final String NEWS_APPKEY = "736ecafde0bb432abce9b70ff9e4ace3";// 历史上的今天
    public static final String QIWEN_APPKEY = "03b19f99c146441bb3b8ca6a3f298387";// 奇闻轶事


    /**
     * 1.微信精选列表
     *
     * @param pager   当前页数，默认1
     * @param maxsize 每页返回条数，最大100，默认20
     */
    public static Object getwechartlist(String pager, String maxsize) {
        String result = null;
        String url = "http://v.juhe.cn/weixin/query";// 请求接口地址
        Map params = new HashMap();// 请求参数
        params.put("pno", pager);// 当前页数，默认1
        params.put("ps", maxsize);// 每页返回条数，最大100，默认20
        params.put("key", WE_CHART_APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("dtype", "json");// 返回数据的格式,xml或json，默认json

        try {
            result = net(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                return object.get("result");
            } else {
                System.out.println(object.get("error_code") + ":"
                        + object.get("reason"));
                return object.get("error_code") + ":" + object.get("reason");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    http://api.avatardata.cn/TechNews/Query?key=736ecafde0bb432abce9b70ff9e4ace3&page=1&rows=10

    //科技新闻
    public static Object getnews(String page) {
        String result = null;
        String url = "http://api.avatardata.cn/TechNews/Query";// 请求接口地址
        Map params = new HashMap();// 请求参数
        params.put("key", NEWS_APPKEY);//qppkey
        params.put("page", page);//页数
        params.put("rows", 10);//一页多少条数据
        try {
            result = net(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                return object.get("result");
            } else {
                System.out.println(object.get("error_code") + ":"
                        + object.get("reason"));
                return object.get("error_code") + ":" + object.get("reason");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    http://api.avatardata.cn/QiWenNews/Query?key=03b19f99c146441bb3b8ca6a3f298387&page=1&rows=10
    //奇闻轶事
    public static Object QiWenNews(String page) {
        String result = null;
        String url = "http://api.avatardata.cn/QiWenNews/Query";// 请求接口地址
        Map params = new HashMap();// 请求参数
        params.put("key", QIWEN_APPKEY);//qppkey
        params.put("page", page);//页数
        params.put("rows", 10);//一页多少条数据
        try {
            result = net(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                return object.get("result");
            } else {
                System.out.println(object.get("error_code") + ":"
                        + object.get("reason"));
                return object.get("error_code") + ":" + object.get("reason");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method)
            throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }

            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(
                            conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    // 将map型转为请求参数型
    //将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        int i = sb.lastIndexOf("&");
        String substring = sb.substring(0, i);
        System.out.println(i + "sssss" + substring);
        return substring;
    }
}
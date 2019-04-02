package yifuyuan;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BrokenBarrierException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 模拟Jsoup跳过登陆进行爬虫
 * 
 * @author 
 *
 */
public class JsoupCookieCraw {
//    private static Map<String, String> cookies = null;
    private static Map<String, String> cookies = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // 先模拟登录获取到cookie和sessionid并存到全局变量cookies中
//         login();
         
        cookies.put("JSESSIONID", "12C5B7CD704FBCA235A6A08E9EC7B762");
//        String url = "http://www.cdyfy.com:8080/workbench/query/hquery_interface.do?returnvalue=dxt&a_query=1&b_query=link&a_inforkind=1&home=1";
        String selecturl = "http://www.cdyfy.com:8080/workbench/query/query_interface.do";
        String selecturl1 = "http://www.cdyfy.com:8080/workbench/query/query_interface.do?returnvalue=dxt&b_query=link&a_inforkind=1&home=1";
        
        //点击快速查询-不进行该操作，后续无法查询
        Document document1 = Jsoup.connect(selecturl1)
        		.cookies(cookies)
//        		.data("returnvalue","dxt","b_query","link","a_inforkind","1","home","1")
        		.get();
//        System.out.println(document1.toString());
        
        // 直接获取DOM树，带着cookies去获取
        Document document2 = Jsoup.connect(selecturl)
        		.cookies(cookies)
        		.data("fieldlist[2].value","00315","a0111","on","dbpre","Usr").data("home", "1").data("b_mquery", "查询")
        		.post();
//        System.err.println(document2.toString());
        String allInfoUrl = browBook(document2);
        System.out.println("allInfoUrl----"+allInfoUrl);
        System.err.println(allInfoUrl.contains("http"));
        if(allInfoUrl.contains("a0100")){
        	System.err.println("找到该工号");
        	//大概信息，再点击名字可查询详细信息
        	Document document3 = Jsoup.connect(allInfoUrl)
        			.cookies(cookies)
        			.get();
//        	System.out.println(document3.toString());
        	
        	//详细信息
        	String allInfoUrl2 = "http://www.cdyfy.com:8080/workbench/browse/browseinfo.jsp";
        	Document document4 = Jsoup.connect(allInfoUrl2)
        			.cookies(cookies)
        			.get();
//        	System.err.println(document4.toString());
        	Elements es =  document4.getElementsByTag("input");
        	for(Element e :es){
        		System.err.println(e.attr("value"));
        	}
        	System.out.println("姓名："+es.get(5).attr("value"));
        	System.out.println("用工形式："+es.get(6).attr("value"));
        	System.out.println("身份证号："+es.get(13).attr("value"));
        	
        }else{
        	System.err.println("未找到该工号");
        }
        
    }
    
    

    /**
     * 模拟登录获取cookie和sessionid
     */
    public static void login() throws IOException {
        String urlLogin = "http://www.cdyfy.com:8080/templates/index/hrlogon.do?logon.x=link&username=%C8%CB%CA%C2%D7%A8%D4%B1&password=r8692712";
//        String urlLogin = "http://www.cdyfy.com:8080/templates/index/hrlogon.do";
//        String urlLogin = "http://219.229.252.135/reader/login.php";
        Connection connect = Jsoup.connect(urlLogin);
        // 伪造请求头
        
        connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        connect.header("Accept-Encoding", "gzip, deflate");
        connect.header("Accept-Language", "zh,en-US;q=0.9,en;q=0.8,zh-CN;q=0.7");
        connect.header("Cache-Control", "no-cache");
        connect.header("Connection", "keep-alive");
//        connect.header("Content-Length", "86");
        connect.header("Content-Type", "application/x-www-form-urlencoded");
        connect.header("Host", "www.cdyfy.com:8080");
        connect.header("Pragma", "no-cache");
        connect.header("Referer", "http://www.cdyfy.com:8080/templates/index/hrlogon.jsp");
        connect.header("Upgrade-Insecure-Requests", "1");
        connect.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        // 携带登陆信息
//        connect.data("logon.x","link","username", "人事专员","password", "r8692712");
        
        //请求url获取响应信息
        Response res = connect.method(Method.POST).execute();// 执行请求
        // 获取返回的cookie
        cookies = res.cookies();
        for (Entry<String, String> entry : cookies.entrySet()) {
            System.err.println(entry.getKey() + "-" + entry.getValue());
        }
        System.out.println(res.statusCode());
        
        System.err.println(res.body().toString());
    }
    
    public static String browBook(Document d) throws IOException{
    	String a = d.getElementsByTag("a").first().attr("onclick");
    	System.out.println("a----"+a);
    	String[] as = a.split("'");
    	String url = as[1];
    	System.out.println("as1----"+as[1]);
    	return "http://www.cdyfy.com:8080"+as[1];
    }
    

}
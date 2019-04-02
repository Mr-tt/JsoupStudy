package FaceCompare;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.baidu.aip.util.Base64Util;

public class MyFaceDemo {
	
	 //设置APPID/AK/SK
    public static final String APP_ID = "15916856";
    public static final String API_KEY = "Vv4EHWSt1v1koK2ju5g5yksE";
    public static final String SECRET_KEY = "90A1LyRu1ZOPXCxDn3KhjQmu5ZaIATg5";

    public static void main(String[] args) {
        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
//        String path = "test.jpg";
//        JSONObject res = client.detect(path, new HashMap<String, String>());
//        System.out.println(res.toString(2));
        
//        compareFace();
    }
	
    
    
	public static void compareFace(String url1,String url2) {
		
		// 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
		
        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        
//		String image1Url="/Users/yehaitao/Desktop/6.jpg";
//		String image2Url="/Users/yehaitao/Desktop/2.png";
		String image1Url=url1;
		String image2Url=url2;
		
		
	    String image1 = imageToBase64(image1Url);
	    String image2 = imageToBase64(image2Url);
	
	    // image1/image2也可以为url或facetoken, 相应的imageType参数需要与之对应。
	    MatchRequest req1 = new MatchRequest(image1, "BASE64");
	    MatchRequest req2 = new MatchRequest(image2, "BASE64");
//	    MatchRequest req1 = new MatchRequest(image1, "URL");
//	    MatchRequest req2 = new MatchRequest(image2, "URL");
	    ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
	    requests.add(req1);
	    requests.add(req2);
	
	    JSONObject res = client.match(requests);
	    System.err.println(((JSONObject)res.get("result")).get("score"));
	    System.out.println(res.toString(2));
	
	}
	
	/**
	 * 图片转成Base64位编码形式
	 * @param path
	 * @return
	 */
	public static String imageToBase64(String path) {
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64Util base64 = new Base64Util();
        String a =base64.encode(data);
        
        System.err.println(a);
		return a;
    }
	
	public static String imageToBase64(BufferedImage bi) {
		byte[] data = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "png", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		data = out.toByteArray();
		Base64Util base64 = new Base64Util();
		String a =base64.encode(data);
		
		return a;
	}
	
	
	public static String compareFace(BufferedImage bi,String url){
		// 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
		
        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        
//		String image1Url="/Users/yehaitao/Desktop/6.jpg";
//		String image2Url="/Users/yehaitao/Desktop/2.png";
		String image2Url=url;
		
		
	    String image1 = imageToBase64(bi);
	    String image2 = imageToBase64(image2Url);
	
	    // image1/image2也可以为url或facetoken, 相应的imageType参数需要与之对应。
	    MatchRequest req1 = new MatchRequest(image1, "BASE64");
	    MatchRequest req2 = new MatchRequest(image2, "BASE64");
	    ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
	    requests.add(req1);
	    requests.add(req2);
	    
	    JSONObject res = client.match(requests);
	    System.err.println("resJson="+res);
	    System.err.println(((JSONObject)res.get("result")).get("score"));
	    String score =  ((JSONObject)res.get("result")).get("score").toString();
	    return score;
	}
	
}


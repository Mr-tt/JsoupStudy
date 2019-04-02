package FaceCompare;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.baidu.aip.util.Base64Util;

public class  Base6Utils{
	
	public static void main(String[] args) {
		String url = "/Users/yehaitao/Desktop/test.png";

		imageToBase64(url);
	}
	
	
	
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
	
}


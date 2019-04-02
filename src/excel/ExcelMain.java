package excel;


import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import test.WebCamService;
import test.WebCamView;
import yifuyuan.JsoupCookieCraw;

public class ExcelMain extends Application {
	
	private WebCamService service ;
	private Webcam cam;
	
	public void init() {
		
		//自定义照相机的分辨率
		Dimension[] nonStandardResolutions = new Dimension[] {
				WebcamResolution.PAL.getSize(),
				WebcamResolution.HD.getSize(),
//				new Dimension(295, 413), //设置摄像头分辨率
//				new Dimension(350, 413),
//				new Dimension(1000, 500),
		};
		
		cam = Webcam.getWebcams().get(0);
		service = new WebCamService(cam);	
		
		cam.setCustomViewSizes(nonStandardResolutions);
		cam.open();		
//		System.err.println(WebcamResolution.HD.getSize());
		
//		cam.setViewSize(new Dimension(350,413));
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("一附院资格审查系统-Design By HaiTao");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		
		
		
		Text scenetitle = new Text("请输入考生身份证号进行查询");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		grid.add(scenetitle, 0, 0, 2, 1);

		// 创建Label对象，放到第0列，第1行
		Label userName = new Label("身份证号:");
		grid.add(userName, 0, 1);

		// 创建文本输入框，放到第1列，第1行
		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

//		Label pw = new Label("请为考生设置考号:");
//		grid.add(pw, 0, 2);
//
//		PasswordField pwBox = new PasswordField();
//		grid.add(pwBox, 1, 2);
		
		Label pw = new Label("请为考生设置考号:");
		grid.add(pw, 0, 2);

		TextField kaohaoText = new TextField();
		grid.add(kaohaoText, 1, 2);

		// 创建登录按钮
		Button btn = new Button("审核通过");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);// 将按钮控件作为子节点
		grid.add(hbBtn, 1, 4);// 将HBox pane放到grid中的第1列，第4行

//		Button loginButton = new Button("登录");
//		HBox hbBtn1 = new HBox(10);
//		hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
//		hbBtn1.getChildren().add(loginButton);// 将按钮控件作为子节点
//		grid.add(hbBtn1, 2, 3);// 将HBox pane放到grid中的第1列，第4行

		final Text actiontarget = new Text();// 增加用于显示信息的文本
		grid.add(actiontarget, 1, 6);
		final Text studentText = new Text();
		grid.add(studentText, 1, 7);
		
		
		
		try {
			MyExcel.read();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// 给btn按钮绑定事件
		btn.setOnAction(new EventHandler<ActionEvent>() {// 注册事件handler
			@Override
			public void handle(ActionEvent e) {
//				btn.setVisible(false);
//				btn.setDisable(true);
				
//				 Stage stage = new Stage();
//                 Label l = new Label("显示出来了");
//                 Scene scene = new Scene(l,200,100);
//                 stage.setScene(scene);
//                 stage.show();
				
				
				
	           
//				Alert alert = new Alert(AlertType.INFORMATION);
//				alert.setTitle("提示框标题");
//				alert.setHeaderText("Look, an Information Dialog");
//				alert.setContentText("I have a great message for you!");
//				alert.showAndWait();
				
				try {
					actiontarget.setFill(Color.ALICEBLUE);
					studentText.setFill(Color.ALICEBLUE);
					Student s =  MyExcel.search(userTextField.getText());
					if(s!=null){
						s.setId(kaohaoText.getText());
						MyExcel.write(s);
						actiontarget.setFill(Color.GREEN);// 将文字颜色变成 firebrick red
						actiontarget.setText("写入成功");
						studentText.setFill(Color.GREEN);
						studentText.setText(s.toString());
						
					}else{
						actiontarget.setFill(Color.FIREBRICK);// 将文字颜色变成 firebrick red
						actiontarget.setText("未查询到该考生，检查是否输入错误");
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					actiontarget.setText("非法操作！程序故障！请联系-海涛");
				} 
				
				System.err.println();
					
				
				
				
			}
		});
		
		
		//给登录按钮绑定事件
//		loginButton.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				login();
//				actiontarget.setFill(Color.DARKGREEN);
//				actiontarget.setText("恭喜！登录成功,请查询！");
//			}
//		});
		
		//增加摄像头-拍照功能
		Button startStop = new Button();
		startStop.textProperty().bind(Bindings.
				when(service.runningProperty()).
				then("Stop").
				otherwise("Start"));
		
		startStop.setOnAction(e -> {
			if (service.isRunning()) {
				service.cancel();
			} else {
				service.restart();
				startStop.setVisible(false);
			}
		});	
		
		//拍照按钮
		Button paizhao = new Button("拍照");
		paizhao.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					
					ImageIO.write(cam.getImage(), "PNG", new File("/Users/yehaitao/Desktop/test/程序截图/"+System.currentTimeMillis()+".png"));
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Design By Haitao-609715176@qq.com");
				alert.setHeaderText("恭喜，拍照成功！");
				alert.setContentText("文件已经存储");
				alert.showAndWait();
			}
		});
		
		
		WebCamView view = new WebCamView(service);
		grid.add(view.getView(),4,0,9,4);
		grid.add(startStop, 6, 4);
		grid.add(paizhao, 7, 4);
				
				
				
				
		Scene scene = new Scene(grid, 550, 270);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	
	protected void login()  {
		try {
			JsoupCookieCraw.login();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("登录出错");
		}
	}

	
	public static void main(String[] args) {
		System.err.println("ready");
		launch(args);
	}

	
	
}

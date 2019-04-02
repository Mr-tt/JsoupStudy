package excel;


import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import FaceCompare.MyFaceDemo;
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
import javafx.scene.image.Image;
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
	//用于保存拍照的图片
	BufferedImage bImage;
	
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
		
//		cam.setCustomViewSizes(nonStandardResolutions);
//		cam.setViewSize(new Dimension(176,144));
		cam.open();		
//		System.err.println(WebcamResolution.HD.getSize());
		
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

		// 创建审核按钮
		Button btn = new Button("审核通过");
		btn.setVisible(false);
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);// 将按钮控件作为子节点
		grid.add(hbBtn, 1, 4);// 将HBox pane放到grid中的第1列，第4行

		
//		Button loginButton = new Button("登录");
//		HBox hbBtn1 = new HBox(10);
//		hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
//		hbBtn1.getChildren().add(loginButton);// 将按钮控件作为子节点
//		grid.add(hbBtn1, 2, 3);// 将HBox pane放到grid中的第1列，第4行
		
		Button searchBtn = new Button("查找该学生");
		btn.setVisible(false);
		HBox searchhbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_LEFT);
		hbBtn.getChildren().add(searchBtn);// 将按钮控件作为子节点
		grid.add(searchBtn, 0, 4);// 将HBox pane放到grid中的第0列，第4行
		

		final Text actiontarget = new Text();// 增加用于显示信息的文本
		grid.add(actiontarget, 1, 6);
		final Text studentText = new Text();
		grid.add(studentText, 1, 7);
		
		
		
		try {
			MyExcel.read();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// 给审核按钮btn按钮绑定事件
		btn.setOnAction(new EventHandler<ActionEvent>() {// 注册事件handler
			@Override
			public void handle(ActionEvent e) {
				
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
						btn.setVisible(false);
					}else{
						actiontarget.setFill(Color.FIREBRICK);// 将文字颜色变成 firebrick red
						actiontarget.setText("请输入考生的身份证号，否则无法审核！");
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					actiontarget.setText("非法操作！程序故障！请联系-海涛");
				} 
				
			}
		});
		
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				actiontarget.setFill(Color.ALICEBLUE);
				studentText.setFill(Color.ALICEBLUE);
				Student s =  MyExcel.search(userTextField.getText());
				if(s!=null){
					actiontarget.setFill(Color.GREEN);// 将文字颜色变成 firebrick red
					actiontarget.setText("找到该学生\r");
					studentText.setFill(Color.GREEN);
					studentText.setText(s.toString());
					
				}else{
					actiontarget.setFill(Color.FIREBRICK);// 将文字颜色变成 firebrick red
					actiontarget.setText("未查询到该考生，检查是否输入错误");
				}
			}
		});

		
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
		
		//保存照片的按钮
		Button savePhotoBtn = new Button("是否保存照片");
		savePhotoBtn.setVisible(false);
		//拍照按钮
		Button paizhao = new Button("拍照");
		
		paizhao.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				savePhotoBtn.setVisible(true);
				//bufferImage --转--InputStream
				bImage = cam.getImage();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					ImageIO.write(bImage, "png", os);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				
				Image image = new Image(is);
				ImageView imageview = new ImageView(image);
//				imageview.setFitHeight(50); //设置照片大小
//				imageview.setFitWidth(50);
				
				grid.add(imageview, 15,0,9,4);	
				
				//显示保存照片按钮
				
				
			}
		});
		
		//存储照片按钮绑定事件
		savePhotoBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				System.out.println("***"+kaohaoText.getText());
				String s = kaohaoText.getText();
				if(!s.isEmpty()){  //如果未填考号弹出提示框，要求先填考号才能保存照片
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("照片确认-Design By HaiTao");
					alert.setHeaderText("考试照片如下：");
					alert.setContentText("是否保存?");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						// ... user chose OK
						try {
							
							ImageIO.write(bImage, "PNG", new File("/Users/yehaitao/Desktop/test/程序截图/"+s+".png"));
							btn.setVisible(true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else { //取消保存
						
					}
				}else{   //弹出提示框，要求先填考号
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Design By HaiTao");
					alert.setHeaderText("");
					alert.setContentText("请先为考生输入准考证号");
					alert.showAndWait();
				}
			}
		});
		
		Button faceBtn = new Button("人脸识别");
		faceBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String score = MyFaceDemo.compareFace(bImage, "/Users/yehaitao/Desktop/6.jpg");
				int iscore;
				if(score.contains(".")){
					
					String[] ss = score.split("\\.");
					iscore =Integer.parseInt(ss[0]);
					
				}else{
					iscore = Integer.parseInt(score);
				}
				
				if (iscore<50){
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Design By HaiTao");
					alert.setHeaderText("");
					alert.setContentText("疑似不是本人！！与本人相似度为+"+score);
					alert.showAndWait();
					actiontarget.setFill(Color.FIREBRICK);// 将文字颜色变成 firebrick red
					actiontarget.setText("与本人相似度为+"+score);
				}else{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Design By HaiTao");
					alert.setHeaderText("");
					alert.setContentText("高可信！与本人相似度为+"+score);

					alert.showAndWait();
					actiontarget.setFill(Color.GREEN);// 将文字颜色变成 firebrick red
					actiontarget.setText("与本人相似度为+"+score);
				}
			}
		});
		
		
		//显示相机摄像区域
		WebCamView view = new WebCamView(service);
		Node viewNode = view.getView();
		
		grid.add(viewNode,4,0,9,4);
		
		
		//显示相机按钮
		grid.add(startStop, 6, 4);
		//拍照按钮
		grid.add(paizhao, 7, 4);
		//保存照片按钮
		grid.add(savePhotoBtn, 15, 4);
		//人脸识别按钮
		grid.add(faceBtn, 18, 4);
		
		
		Scene scene = new Scene(grid, 1100, 550);
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

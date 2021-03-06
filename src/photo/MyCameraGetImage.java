package photo;
import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
/**
 * 推荐JDK8及以上（适应lambda表达式），需导入lib下三个Jar包，支持摄像头选择、开始摄像、停止摄像、拍照存储
 */
public class MyCameraGetImage extends Application {
    /**
     * 拍照存储的文件路径
     */
    String filePath = "/Users/yehaitao/Desktop/";
 
    private class WebCamInfo {
        private String webCamName;
        private int webCamIndex;
 
        public String getWebCamName() {
            return webCamName;
        }
 
        public void setWebCamName(String webCamName) {
            this.webCamName = webCamName;
        }
 
        public int getWebCamIndex() {
            return webCamIndex;
        }
 
        public void setWebCamIndex(int webCamIndex) {
            this.webCamIndex = webCamIndex;
        }
 
        @Override
        public String toString() {
            return "摄像头" + (Integer.parseInt(webCamName.split("Integrated Webcam ")[1]) + 1);
        }
    }
 
    private FlowPane bottomCameraControlPane;
    private FlowPane topPane;
    private BorderPane root;
    private String cameraListPromptText = "选择摄像头：";
    private ImageView imgWebCamCapturedImage;
    private Webcam webCam = null;
    private boolean stopCamera = false;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private BorderPane webCamPane;
    private Button btnCamreaStop;
    private Button btnCamreaStart;
    private Button btnCamreaGetImage;
 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("摄像");
        root = new BorderPane();
        topPane = new FlowPane();
        topPane.setAlignment(Pos.CENTER);
        topPane.setHgap(20);
        topPane.setOrientation(Orientation.HORIZONTAL);
        topPane.setPrefHeight(40);
        root.setTop(topPane);
        webCamPane = new BorderPane();
        webCamPane.setStyle("-fx-background-color: #ccc;");
        imgWebCamCapturedImage = new ImageView();
        webCamPane.setCenter(imgWebCamCapturedImage);
        root.setCenter(webCamPane);
        createTopPanel();
        bottomCameraControlPane = new FlowPane();
        bottomCameraControlPane.setOrientation(Orientation.HORIZONTAL);
        bottomCameraControlPane.setAlignment(Pos.CENTER);
        bottomCameraControlPane.setHgap(20);
        bottomCameraControlPane.setVgap(10);
        bottomCameraControlPane.setPrefHeight(40);
        bottomCameraControlPane.setDisable(true);
        createCameraControls();
        root.setBottom(bottomCameraControlPane);
        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(700);
        primaryStage.setWidth(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
        Platform.runLater(() ->
                setImageViewSize()
        );
    }
 
    protected void setImageViewSize() {
        double height = webCamPane.getHeight();
        double width = webCamPane.getWidth();
        imgWebCamCapturedImage.setFitHeight(height);
        imgWebCamCapturedImage.setFitWidth(width);
        imgWebCamCapturedImage.prefHeight(height);
        imgWebCamCapturedImage.prefWidth(width);
        imgWebCamCapturedImage.setPreserveRatio(true);
    }
 
    private void createTopPanel() {
        int webCamCounter = 0;
        Label lbInfoLabel = new Label("选择摄像头：");
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
        topPane.getChildren().add(lbInfoLabel);
        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter++;
        }
 
        ComboBox<WebCamInfo> cameraOptions = new ComboBox<>();
        cameraOptions.setItems(options);
        cameraOptions.setPromptText(cameraListPromptText);
        cameraOptions.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) -> {
            if (arg2 != null) {
                System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
                initializeWebCam(arg2.getWebCamIndex());
            }
        });
        topPane.getChildren().add(cameraOptions);
    }
 
    protected void initializeWebCam(final int webCamIndex) {
        Task<Void> webCamTask = new Task<Void>() {
            @Override
            protected Void call() {
                if (webCam != null) {
                    disposeWebCamCamera();
                }
                webCam = Webcam.getWebcams().get(webCamIndex);
                webCam.open();
                startWebCamStream();
                return null;
            }
        };
        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();
        bottomCameraControlPane.setDisable(false);
        btnCamreaStart.setDisable(true);
    }
 
    protected void startWebCamStream() {
        stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                while (!stopCamera) {
                    try {
                        if ((grabbedImage = webCam.getImage()) != null) {
                            Platform.runLater(() -> {
                                Image mainiamge = SwingFXUtils.toFXImage(grabbedImage, null);
                                imageProperty.set(mainiamge);
                            });
                            grabbedImage.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);
    }
 
    private void createCameraControls() {
        btnCamreaStop = new Button();
        btnCamreaStop.setOnAction(event -> stopWebCamCamera());
        btnCamreaStop.setText("停止摄像");
        btnCamreaStart = new Button();
        btnCamreaStart.setOnAction(event -> startWebCamCamera());
        btnCamreaStart.setText("开始摄像");
        btnCamreaGetImage = new Button();
        btnCamreaGetImage.setOnAction(event -> getImagine());
        btnCamreaGetImage.setText("拍照存储");
        bottomCameraControlPane.getChildren().add(btnCamreaStart);
        bottomCameraControlPane.getChildren().add(btnCamreaStop);
        bottomCameraControlPane.getChildren().add(btnCamreaGetImage);
    }
 
    protected void getImagine() {
        Image image = imgWebCamCapturedImage.getImage();
        ImageView imageView = new ImageView(image);
        Label label = new Label("图片名称：");
        TextField textField = new TextField();
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(label, textField);
        Button button = new Button("保存");
        Stage stage = new Stage();
        button.setOnAction(event -> {
            try {
                File file = new File(filePath + textField.getText() + ".png");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
 
            stage.close();
        });
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.getChildren().addAll(imageView, hBox, button);
        stage.setScene(new Scene(vBox));
        stage.show();
    }
 
    protected void disposeWebCamCamera() {
        stopCamera = true;
        webCam.close();
//        Webcam.shutdown();
        btnCamreaStart.setDisable(true);
        btnCamreaStop.setDisable(true);
    }
 
    protected void startWebCamCamera() {
        stopCamera = false;
        startWebCamStream();
        btnCamreaStop.setDisable(false);
        btnCamreaStart.setDisable(true);
    }
 
    protected void stopWebCamCamera() {
        stopCamera = true;
        btnCamreaStart.setDisable(false);
        btnCamreaStop.setDisable(true);
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
package javafxapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.AnalogClock;

/**
 *
 * @author Ahmed
 */
public class Main extends Application {
    
    public static BorderPane root;
    public static boolean isLoggedin = false;
    public static AnalogClock clock = new AnalogClock();
    

    @Override
    public void start(Stage stage) throws Exception {
        
        Image image = new Image("/res/images/pp.jpg");
        stage.getIcons().add(image);
        Parent content = null;
        
        if(isLoggedin){
            System.out.println("Main Home");
            content = FXMLLoader.load(getClass().getResource("/res/fxml/Main.fxml"));
            
        }else{
            System.out.println("Login");
            content = FXMLLoader.load(getClass().getResource("/res/fxml/Login.fxml"));
        }
        //Root 
        root = new BorderPane();
	root.setCenter(content);
        
        Scene scene = new Scene(root);
        stage.setMinHeight(700); 
        stage.setMinWidth(980);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/res/css/clock.css").toExternalForm());
        stage.show();
        
        
        stage.setTitle("كمبيوتر شوب");
        
        
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);      

    }
    
    public static void setContent(Node node , String title) {
        root.setCenter(null);
        root.setCenter(node);
    }
    
}
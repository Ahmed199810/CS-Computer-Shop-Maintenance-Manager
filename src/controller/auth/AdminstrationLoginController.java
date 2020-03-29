package controller.auth;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafxapp.Main;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;

/**
 *
 * @author Ahmed
 */
public class AdminstrationLoginController  implements Initializable{
    
    @FXML
    private Label login_msg;
    
    @FXML
    private TextField txt_user;
    
    @FXML
    private PasswordField txt_pass;
    
    
    @FXML
    private void BtnExit(ActionEvent event){
        Platform.exit();
    }
    
        
    @FXML
    private void BtnLogin(ActionEvent event) throws IOException{
        String user = txt_user.getText().toString();
        String pass = txt_pass.getText().toString();
        
        RequestBody formBody = new FormBody.Builder()
                .add("user", user)
                .add("pass", pass)
                .add("type", "0")
                .build();
        
        String url = APIRequests.LOGIN_ADMIN;
        String response = database.RemoteDB.loginUser(url, formBody);
        if(response != null){
            JSONObject object = new JSONObject(response.toString());
            JSONArray arr = object.getJSONArray("users");
            System.out.println(arr.get(0));
            Main.isLoggedin = true;
            // start Main
            startSettings();
        }else{
            login_msg.setText("خطأ في الدخول");
        }

        
        
    }
            
        

    @FXML
    private void BtnCancel(ActionEvent event) {
        System.out.println("Cancel");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Main.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        // init
    }

    private void startSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Settings.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
        } catch (Exception e) {
            
        }
        
    }

    private void checkMachineId() {
        InetAddress ip;
        
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("" + ip);
        } catch (Exception e) {
            
        }
    }
    
}
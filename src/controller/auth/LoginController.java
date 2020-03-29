package controller.auth;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafxapp.Main;
import model.User;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;
import utils.genVars;
import utils.utils;

/**
 *
 * @author Ahmed
 */
public class LoginController implements Initializable{
    
    public static String host_ip = "";
    public static String host_ip_online = "";
    
    
    @FXML
    private Label login_msg;
    
    @FXML
    private TextField txt_user;
    
    @FXML
    private PasswordField txt_pass;
    
    @FXML
    private TextField txt_host_ip;
    
    @FXML
    private TextField txt_host_ip_online;
    
    @FXML
    private void BtnExit(ActionEvent event){
        Platform.exit();
    }
    
    @FXML
    private void BtnGetIp(ActionEvent event) throws UnknownHostException{
        txt_host_ip.setText(getIpAddress());
    }
    
    
        
    @FXML
    private void BtnLogin(ActionEvent event) throws IOException{
        host_ip = txt_host_ip.getText().toString();
        host_ip_online = txt_host_ip_online.getText().toString();
        String user = txt_user.getText().toString();
        String pass = txt_pass.getText().toString();
        /*try {
            if(networkCheck()){
                System.out.println("true");
            }else{
                System.out.println("false");
                utils.AlertMSG("غير متصل بالشبكه");
            }
        } catch (SocketException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        RequestBody formBody = new FormBody.Builder()
                .add("user", user)
                .add("pass", pass)
                .add("type", "1")
                .build();
        
        String url = APIRequests.LOGIN_USER;
        String response = database.RemoteDB.loginUser(url, formBody);
        if(response != null){
            JSONObject object = new JSONObject(response.toString());
            JSONArray arr = object.getJSONArray("users");
            System.out.println(arr.get(0));
            JSONObject o = arr.getJSONObject(0);
            User u = new User();
            u.setId(o.getInt("id"));
            u.setName(o.getString("user"));
            u.setPassword(o.getString("pass"));
            u.setType(o.getInt("type"));
            genVars.user = u;
            Main.isLoggedin = true;
            // start Main
            startMain();
        }else{
            login_msg.setText("خطأ في الدخول");
        }
        
        
        
        
    }
    @FXML
    private void BtnRegister(ActionEvent event) throws IOException{
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        
        /*try {
            if(networkCheck()){
                System.out.println("true");
            }else{
                System.out.println("false");
                utils.AlertMSG("غير متصل بالشبكه");
            }
        } catch (SocketException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        try {
            // init
            txt_host_ip.setText(getIpAddress());
            txt_host_ip_online.setText("server_url");
        } catch (UnknownHostException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Main.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
        } catch (Exception e) {
            
        }
        
    }


    private static String getIpAddress() throws UnknownHostException{
       InetAddress inetAddress = InetAddress.getLocalHost();
       return inetAddress.getHostAddress() + "";
    }
    
    
    private boolean networkCheck() throws SocketException{
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            
            NetworkInterface networkInterface = interfaces.nextElement();
            if(networkInterface.getDisplayName().contains("ireless") && networkInterface.isUp()){
                //System.out.println("Network Interface Name : [" + networkInterface.getDisplayName() + "]");
                //System.out.println("Is It connected? : [" + networkInterface.isUp() + "]");
                return true;
            }
            //System.out.println("Network Interface Name : [" + networkInterface.getDisplayName() + "]");
            //System.out.println("Is It connected? : [" + networkInterface.isUp() + "]");
            /*for (InterfaceAddress i : networkInterface.getInterfaceAddresses()){
                //System.out.println("Host Name : "+i.getAddress().getCanonicalHostName());
                //System.out.println("Host Address : "+i.getAddress().getHostAddress());
            }*/
            //System.out.println("----------------------");
        }
        
        return false;
    }
}
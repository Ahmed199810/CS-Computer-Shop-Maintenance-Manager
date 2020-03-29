package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafxapp.Main;
import model.Permition;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;
import utils.utils;

/**
 *
 * @author Ahmed
 */
public class SettingsController  implements Initializable{
    
    @FXML
    private ComboBox<String> combo_perm;
    
    @FXML
    private VBox user_box;
    
    @FXML
    private TextField txt_user;
    
    @FXML
    private TextField txt_pass;
    
    @FXML
    private Label l_pass;
    
    @FXML
    private Label l_user;
    
    @FXML
    private Label txt_perm;
    
    @FXML
    private Button btn_del;
    
    private static int UID = 0;
        
    @FXML
    private ListView<String> list_users;
    
    @FXML
    private Label txt_state;
    
    List<Permition> list_perm = new ArrayList<Permition>();
    
    @FXML
    private void BtnSave(ActionEvent event) throws ClassNotFoundException, UnsupportedEncodingException, IOException{
        int index = combo_perm.getSelectionModel().getSelectedIndex();
        if(index != -1){
            Permition p = list_perm.get(index);
            RequestBody formBody = new FormBody.Builder()
                    .add("user", txt_user.getText().toString())
                    .add("pass", txt_pass.getText().toString())
                    .add("type", p.getId() + "")
                    .build();

            String url = APIRequests.INSERT_USER;
            database.RemoteDB.postData(url, formBody);
            utils.AlertMSG("تم التسجيل بنجاح");
            getAllUsers();

            txt_user.setText("");
            txt_pass.setText("");
            user_box.setVisible(false);
            txt_state.setText("");
        }else{
            txt_state.setText("اختر الصلاحيات");
        }
    }
    
        
    @FXML
    private void BtnAddUser(ActionEvent event) throws IOException{
        user_box.setVisible(true);
        l_user.setVisible(false);
        l_pass.setVisible(false);
        txt_perm.setVisible(false);
        btn_del.setVisible(false);
        
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
    
    @FXML
    private void BtnDel(ActionEvent event) throws IOException {
        System.out.println("DELETE");
            
        RequestBody formBody = new FormBody.Builder()
                .add("id", UID + "")
                .add("table", "users")
                .build();
        String url = APIRequests.DELETE_DEVICE;
        database.RemoteDB.postData(url, formBody);
        utils.AlertMSG("تم الحذف بنجاح");
                    
        l_user.setText("");
        l_pass.setText("");
        l_user.setVisible(false);
        l_pass.setVisible(false);
        txt_perm.setVisible(false);
        btn_del.setVisible(false); 
        getAllUsers();
            

    }
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            // get all users
            getAllUsers();
        } catch (IOException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // get all permition list
        fillPermitionList();
    }

    private void startMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Main.fxml"));
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

    private void getAllUsers() throws IOException {
        String url = APIRequests.GET_ALL_USERS;
        String response = database.RemoteDB.getData(url);
        JSONObject object = new JSONObject(response.toString());
        JSONArray arr = object.getJSONArray("users"); // getting users
        LinkedList<String> strList = new LinkedList();
        
        for(int i = 0; i < arr.length(); i++){
            JSONObject obj = arr.getJSONObject(i);
            strList.add(obj.getString("user")); 
        }
        
        ObservableList<String> items = FXCollections.observableArrayList (strList);
        list_users.setItems(items);
        
        list_users.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                int index = list_users.getSelectionModel().getSelectedIndex();
                System.out.println(index);
                // show new window
                l_user.setVisible(true);
                l_pass.setVisible(true);
                txt_perm.setVisible(true);
                
                JSONObject user = arr.getJSONObject(index);
                
                l_user.setText(user.getString("user"));
                
                UID = user.getInt("id");
                if(user.getInt("type") == 0){
                    btn_del.setVisible(false);
                    l_pass.setText("");
                    previewPermitions(user.getInt("type"));
                }else{
                    btn_del.setVisible(true);
                    l_pass.setText(user.getString("pass"));
                    previewPermitions(user.getInt("type"));
                }
                user_box.setVisible(false);
                
                
            }

            private void previewPermitions(int type) {
                
                if(type == 0){
                    txt_perm.setText("Super Admin");
                }else if(type == 1){
                    txt_perm.setText("Casher");
                } else if(type == 2){
                    txt_perm.setText("Maintenance Engineer");
                } else if(type == 3){
                    txt_perm.setText("Supervisor");
                }
                
            }
        });
    }

    private void fillPermitionList() {
        
        Permition p1 = new Permition(1, "Casher");
        Permition p2 = new Permition(2, "Maintenance Engineer");
        Permition p3 = new Permition(3, "Supervisor");
        
        list_perm.add(p1);
        list_perm.add(p2);
        list_perm.add(p3);
        
        combo_perm.getItems().add(p1.getPerm());
        combo_perm.getItems().add(p2.getPerm());
        combo_perm.getItems().add(p3.getPerm());
        
    }
    
    
    
}
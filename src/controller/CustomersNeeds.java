package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafxapp.Main;
import model.Customer;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;
import utils.utils;

/**
 *
 * @author Ahmed
 */
public class CustomersNeeds  implements Initializable{
    
    @FXML
    private ListView<String> list = new ListView<String>();
    
    @FXML
    private TextField txt_name;
        
    @FXML
    private TextField txt_phone;
        
    @FXML
    private TextField txt_desc;

    @FXML
    private Button btn_save;
      
    @FXML
    private Button btn_del;
    
    @FXML
    private Button btn_new;
        
    @FXML
    private Button btn_done;
            
    @FXML
    private Label l_title;
    
    private static int ID = 0;
    
    private boolean edit = false;
    
        
    private LinkedList<Customer> customersList = new LinkedList<>();
    private List<String> names = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        
        // init
        try {    
            getAllNeeds(); 
        } catch (IOException ex) {
            Logger.getLogger(CustomersNeeds.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
                
        try {
            fillCustomersList();
        } catch (IOException ex) {
            Logger.getLogger(HardwareFixController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TextFields.bindAutoCompletion(txt_name, names).addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override    
            public void handle(Event event) {
                txt_phone.setText(phones.get(names.indexOf(txt_name.getText().toString())));            }
        });
        
        TextFields.bindAutoCompletion(txt_phone, phones).addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                txt_name.setText(names.get(phones.indexOf(txt_phone.getText().toString())));
            }
        });
        
       
    }
    @FXML
    private void BtnCancel(ActionEvent event) {
        System.out.println("Cancel");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Main.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "");
            
        } catch (Exception e) {
            
        }
        
    }
    
        
    @FXML
    private void BtnSave(ActionEvent event) throws ClassNotFoundException, UnsupportedEncodingException, IOException {
        System.out.println("SAVE");
        
        if(edit){ // edite
            RequestBody formBody = new FormBody.Builder()
                    .add("id", ID + "")
                    .add("customer_name", txt_name.getText().toString())
                    .add("phone_num", txt_phone.getText().toString())
                    .add("needs", txt_desc.getText().toString())
                    .build();

            String url = APIRequests.UPDATE_CUSTOMER_NEED;
            if(database.RemoteDB.postData(url, formBody)){
                utils.AlertMSG("تم التعديل بنجاح");
                getAllNeeds();
            }else{
                utils.AlertMSG("فشل في التعديل");
            }

            
        }else{ // add new 
            
            // check if customer alrady exist
            checkUserExist();
            RequestBody formBody = new FormBody.Builder()
                    .add("customer_name", txt_name.getText().toString())
                    .add("phone_num", txt_phone.getText().toString())
                    .add("needs", txt_desc.getText().toString())
                    .add("date_time", String.valueOf(getDate()))
                    .build();

            
            String url = APIRequests.INSERT_CUSTOMER_NEED;
            
            if(database.RemoteDB.postData(url, formBody)){
                utils.AlertMSG("تم التسجيل بنجاح");
                emptifyForm();
                getAllNeeds();
            }else{
                utils.AlertMSG("برجاء ادخال البيانات");
            }

            
        }
    }
    
        
    @FXML
    private void BtnDel(ActionEvent event) throws IOException {
        System.out.println("DELETE");
            
        RequestBody formBody = new FormBody.Builder()
                .add("id", ID + "")
                .add("table", "customers_needs")
                .build();
        
        String url = APIRequests.DELETE_DEVICE;
        database.RemoteDB.postData(url, formBody);
        emptifyForm();
        utils.AlertMSG("تم الحذف");
        getAllNeeds();
        btn_del.setDisable(true);

    }
    
    @FXML
    private void BtnDone(ActionEvent event) throws IOException {
        System.out.println("Done");
            
        RequestBody formBody = new FormBody.Builder()
                .add("id", ID + "")
                .add("state", "1")
                .add("table", "customers_needs")
                .build();
        
        String url = APIRequests.UPDATE_DEVICE;
        database.RemoteDB.postData(url, formBody);
        utils.AlertMSG("تم التسليم");
        emptifyForm();
        getAllNeeds();

    }
    
    
    @FXML
    private void BtnNew(ActionEvent event) throws IOException {
        System.out.println("New");
        l_title.setVisible(true);
        btn_done.setVisible(false);
        edit = false;
        btn_save.setText("حفظ");
        emptifyForm();
        btn_del.setDisable(true);
    }

    private void emptifyForm() {
        txt_name.setText("");
        txt_phone.setText("");
        txt_desc.setText("");
        
    }
    
    private void getAllNeeds() throws IOException{
        String url = APIRequests.GET_ALL_CUSTOMER_NEEDS;
        String response = database.RemoteDB.getData(url);
        JSONObject object = new JSONObject(response.toString());
        if(object.getInt("success") != 0){
            JSONArray arr = object.getJSONArray("needs"); // getting needs
            LinkedList<String> strList = new LinkedList();
                
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                strList.add(obj.getString("needs") + "\n" + obj.getString("customer_name"));
            }
            ObservableList<String> items = FXCollections.observableArrayList (strList);
            list.setItems(items);
            list.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    int index = list.getSelectionModel().getSelectedIndex();
                    System.out.println(index);
                    selectNeed(arr, index);
                    btn_del.setDisable(false);
                    btn_save.setText("حفظ التعديل");
                    btn_new.setVisible(true);
                    btn_done.setVisible(true);
                    edit = true;
                }

            });


        }
        
    }
    
    private void selectNeed(JSONArray arr, int index) {
        JSONObject order = arr.getJSONObject(index);
        ID = order.getInt("id");
        txt_name.setText(order.getString("customer_name"));
        txt_phone.setText(order.getString("phone_num"));
        txt_desc.setText(order.getString("needs"));
    }
    
     
    private static Timestamp getDate() {
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        return date;
    }
    
    
            
    private void fillCustomersList() throws IOException{
        String customer_name = txt_name.getText().toString();
        String url_search = APIRequests.GET_CUSTOMER;
        String response = database.RemoteDB.checkDataExist(url_search, customer_name, "name");;
        JSONObject object = new JSONObject(response.toString());
        int success = object.getInt("success");
        if(success != 0){
            JSONArray arr = object.getJSONArray("customers");
            for(int i = 0; i < arr.length(); i++){
                JSONObject o = arr.getJSONObject(i);
                Customer customer = new Customer(o.getString("id"), o.getString("name"), o.getString("phone"), o.getString("sec_phone"), o.getString("address"));
                customersList.add(customer);
                names.add(customer.getName());
                phones.add(customer.getPhone());
            }
            
        }else{
            
        }
    
    }
    
        
    private void checkUserExist() throws IOException {
        String url_search = APIRequests.GET_CUSTOMER;
        String customer_name = txt_name.getText().toString();
        String response = database.RemoteDB.checkDataExist(url_search, customer_name, "name");
        JSONObject object = new JSONObject(response.toString());
        int success = object.getInt("success");
        if(success != 0){
            // already exist
        }else{
            System.out.println("NOT EXIST CUSTOMER");
            RequestBody formBody = new FormBody.Builder()
                    .add("name", txt_name.getText().toString())
                    .add("phone", txt_phone.getText().toString())
                    .add("address", "")
                    .build();
            String url = APIRequests.INSERT_CUSTOMER;

            if(database.RemoteDB.postData(url, formBody)){
                System.out.println("INSERTED CUSTOMER");
                fillCustomersList();
            }
        }
    }
    
    
}
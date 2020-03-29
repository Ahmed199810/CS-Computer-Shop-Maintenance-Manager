package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import model.User;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;
import utils.genVars;
import utils.utils;

/**
 *
 * @author Ahmed
 */

public class CustomersController implements Initializable{
   
    @FXML
    private ListView<String> list = new ListView<String>();
    
    @FXML
    private TextField txt_name;
        
    @FXML
    private TextField txt_phone;
    
    @FXML
    private TextField txt_phone_sec;
        
    @FXML
    private TextField txt_address;

    @FXML
    private Button btn_save;
      
    @FXML
    private Button btn_del;
    
    @FXML
    private Button btn_new;
    
    @FXML
    private Label l_title;
    
    @FXML
    private TextField txt_search;
    
    private static int ID = 0;
    
    private boolean edit = false;
    private JSONArray listDev = new JSONArray();
        
    private LinkedList<Customer> customersList = new LinkedList<>();
    private List<String> names = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    private List<String> sec_phones = new ArrayList<>();
    
    private User user;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        initUsersPermitions();
        // init
        try {
            if(user.getType() == 3 || user.getType() == 0){
                getAllCustomers(); 
            }
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
        
        TextFields.bindAutoCompletion(txt_phone_sec, phones);
        
        try {
            searchCustomers();
        } catch (IOException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
                    .add("name", txt_name.getText().toString())
                    .add("phone", txt_phone.getText().toString())
                    .add("sec_phone", txt_phone_sec.getText().toString())
                    .add("address", txt_address.getText().toString())
                    .build();

            String url = APIRequests.UPDATE_CUSTOMER;
            if(database.RemoteDB.postData(url, formBody)){
                utils.AlertMSG("تم التعديل بنجاح");
                getAllCustomers();
            }else{
                utils.AlertMSG("فشل في التعديل");
            }
            
        }else{ // add new 
            
            RequestBody formBody = new FormBody.Builder()
                    .add("name", txt_name.getText().toString())
                    .add("phone", txt_phone.getText().toString())
                    .add("sec_phone", txt_phone_sec.getText().toString())
                    .add("address", txt_address.getText().toString())
                    .build();

            
            String url = APIRequests.INSERT_CUSTOMER;
            
            if(database.RemoteDB.postData(url, formBody)){
                utils.AlertMSG("تم التسجيل بنجاح");
                emptifyForm();
                // permitions
                if(user.getType() == 3 || user.getType() == 0){
                    getAllCustomers(); 
                }
                
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
                .add("table", "customers")
                .build();
        
        String url = APIRequests.DELETE_DEVICE;
        database.RemoteDB.postData(url, formBody);
        emptifyForm();
        utils.AlertMSG("تم الحذف");
        // permitions
        if(user.getType() == 3 || user.getType() == 0){
            getAllCustomers(); 
        }
        btn_del.setDisable(true);

    }
    
    
    @FXML
    private void BtnNew(ActionEvent event) throws IOException {
        System.out.println("New");
        l_title.setVisible(true);
        edit = false;
        btn_save.setText("حفظ");
        emptifyForm();
        btn_del.setDisable(true);
    }

    private void emptifyForm() {
        txt_name.setText("");
        txt_phone.setText("");
        txt_phone_sec.setText("");
        txt_address.setText("");
        
    }
    
    private void getAllCustomers() throws IOException{
        String url = APIRequests.GET_ALL_CUSTOMERS;
        String response = database.RemoteDB.getData(url);
        JSONObject object = new JSONObject(response.toString());
        if(object.getInt("success") != 0){
            JSONArray arr = object.getJSONArray("customers"); // getting customers
            LinkedList<String> strList = new LinkedList();
                
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                strList.add(obj.getString("name") + "\n" + obj.getString("phone"));
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
                    edit = true;
                }

            });


        }
        
    }
    
    private void selectNeed(JSONArray arr, int index) {
        JSONObject order = arr.getJSONObject(index);
        ID = order.getInt("id");
        txt_name.setText(order.getString("name"));
        txt_phone.setText(order.getString("phone"));
        txt_phone_sec.setText(order.getString("sec_phone"));
        txt_address.setText(order.getString("address"));
    }
    
    
            
    private void fillCustomersList() throws IOException{
        String customer_name = txt_name.getText().toString();
        String url_search = APIRequests.GET_ALL_CUSTOMERS;
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
                sec_phones.add(customer.getSec_phone());
            }
            
        }else{
            
        }
    
    }

    private void searchCustomers() throws IOException {
        if(txt_search.getText().equals("") || txt_search.getText().equals(null)){
            // permitions
            if(user.getType() == 3 || user.getType() == 0){
                getAllCustomers(); 
            }
        }
        txt_search.setOnKeyReleased(event -> {
            if (!event.getCode().isModifierKey()) {
                System.out.println(txt_search.getText());
                
                if(user.getType() == 1 || user.getType() == 2){
                    if(!txt_search.getText().toString().equals("")){
                        try {
                        // search func.
                        RequestBody formBody = new FormBody.Builder()
                                .add("word", txt_search.getText().toString())
                                .add("param", "name")
                                .build();
                        String url_search = APIRequests.GET_CUSTOMER;
                        String response = database.RemoteDB.searchDevices(url_search, formBody);
                        if(response != null){
                            JSONObject devices = new JSONObject(response.toString());
                            JSONArray arr = devices.getJSONArray("customers");
                            previewSearch(arr);
                        }else{
                            list.setItems(null);
                        }

                        } catch (IOException ex) {
                            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }else if(user.getType() == 0 || user.getType() == 3){
                    
                    try {
                        // search func.
                        RequestBody formBody = new FormBody.Builder()
                                .add("word", txt_search.getText().toString())
                                .add("param", "name")
                                .build();
                        String url_search = APIRequests.GET_CUSTOMER;
                        String response = database.RemoteDB.searchDevices(url_search, formBody);
                        if(response != null){
                            JSONObject devices = new JSONObject(response.toString());
                            JSONArray arr = devices.getJSONArray("customers");
                            previewSearch(arr);
                        }else{
                            list.setItems(null);
                        }

                        } catch (IOException ex) {
                            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
                        
                        }
                }
                
            }
        });
        
    }

    private void previewSearch(JSONArray arr) {
        LinkedList<String> strList = new LinkedList();
        for(int i = 0; i < arr.length(); i++){
            JSONObject obj = arr.getJSONObject(i);
            strList.add(obj.getString("name") + "\n" + obj.getString("phone"));
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
                edit = true;
            }
            
        });

    }
    
        
    private void initUsersPermitions() {
        this.user = genVars.user;
        
        if(user.getType() == 0){
            // super admin
        }else if(user.getType() == 1){
            // casher
            btn_del.setVisible(false);
        }else if(user.getType() == 2){
            // maintenance engineer
            btn_del.setVisible(false);
        }else if(user.getType() == 3){
            // supervisor
        }
    }
    
}
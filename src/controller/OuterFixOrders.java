package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
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
public class OuterFixOrders implements Initializable{
        
    @FXML
    private ListView<String> list = new ListView<String>();
    
    @FXML
    private TextField txt_name;
        
    @FXML
    private TextField txt_phone;
        
    @FXML
    private TextField txt_desc;

    @FXML
    private TextField txt_loc;
    
    @FXML
    private TextField txt_hr;
    
    @FXML
    private TextField txt_min;
    
    @FXML
    private DatePicker date_pick;
    
    @FXML
    private ChoiceBox<String> am_pm;
    
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
    
    private User user;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        initUsersPermitions();
        // init
        try {    
            getAllOrders(); 
        } catch (IOException ex) {
            Logger.getLogger(CustomersNeeds.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.AM_PM) == 0){
            am_pm.setValue("AM");
        }else{
            am_pm.setValue("PM");
        }
        am_pm.getItems().add("AM");
        am_pm.getItems().add("PM");
        
        date_pick.setValue(LocalDate.now());
        
        System.out.println("CURRENT " + cal.get(Calendar.AM_PM));
        int hr = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        
        if(hr == 0){
            hr = 12;
        }
        
        txt_hr.setText(hr + "");
        txt_min.setText(min + "");
        
        txt_hr.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
                try {
                    if(newValue.length() > 2 || Integer.parseInt(newValue) > 12 || !newValue.matches("[0-9]*") || Integer.parseInt(newValue) < 1){
                        txt_hr.setText(oldValue);
                    }
                } catch (Exception e) {
                    txt_hr.setText(oldValue);
                }
            
            }
        });
        
                
        txt_min.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
                try {
                    Integer.parseInt(newValue);
                    if(newValue.length() > 2 || Integer.parseInt(newValue) > 59 || !newValue.matches("[0-9]*") || Integer.parseInt(newValue) < 0){
                        txt_min.setText(oldValue);
                    }
                } catch (Exception e) {
                    txt_min.setText(oldValue);
                }
            }
        });
        
         
        
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
        System.out.println(date_pick.getValue());
        System.out.println(txt_hr.getText().toString() +":"+ txt_min.getText().toString());
        
        String time = txt_hr.getText().toString() + ":" + txt_min.getText().toString();
        
        if(am_pm.getValue().toString().equals("AM")){
            if(Integer.parseInt(txt_hr.getText().toString()) == 12){
                time = "0" +":"+ txt_min.getText().toString();
            }else{
                time = txt_hr.getText().toString() +":"+ txt_min.getText().toString();
            }
        }else{
            if(Integer.parseInt(txt_hr.getText().toString()) != 12){
                time = Integer.parseInt(txt_hr.getText().toString()) + 12 +":"+ txt_min.getText().toString();
            }else{
                time = Integer.parseInt(txt_hr.getText().toString()) +":"+ txt_min.getText().toString();
            }
        }
        
        if(edit){ // edite
            
                
            RequestBody formBody = new FormBody.Builder()
                    .add("id", ID + "")
                    .add("customer_name", txt_name.getText().toString())
                    .add("phone_num", txt_phone.getText().toString())
                    .add("needs", txt_desc.getText().toString())
                    .add("loc", txt_loc.getText().toString())
                    .add("date_time", String.valueOf(getDate()))
                    .add("due_date_time", date_pick.getValue().toString() + " " + time)
                    .build();

            String url = APIRequests.UPDATE_ORDER;
            if(database.RemoteDB.postData(url, formBody)){
                utils.AlertMSG("تم التعديل بنجاح");
                getAllOrders();
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
                    .add("loc", txt_loc.getText().toString())
                    .add("date_time", String.valueOf(getDate()))
                    .add("due_date_time", date_pick.getValue().toString() + " " + time)
                    .build();

            
            String url = APIRequests.INSERT_NEED;
            
            if(database.RemoteDB.postData(url, formBody)){
                utils.AlertMSG("تم التسجيل بنجاح");
                emptifyForm();
                getAllOrders();
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
                .add("table", "outer_fix_orders")
                .build();
        
        String url = APIRequests.DELETE_DEVICE;
        database.RemoteDB.postData(url, formBody);
        emptifyForm();
        utils.AlertMSG("تم الحذف");
        getAllOrders();
        btn_del.setDisable(true);

    }
    
    @FXML
    private void BtnDone(ActionEvent event) throws IOException {
        System.out.println("Done");
            
        RequestBody formBody = new FormBody.Builder()
                .add("id", ID + "")
                .add("state", "1")
                .add("table", "outer_fix_orders")
                .build();
        
        String url = APIRequests.UPDATE_DEVICE;
        database.RemoteDB.postData(url, formBody);
        utils.AlertMSG("تم التسليم");
        emptifyForm();
        getAllOrders();

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
        txt_loc.setText("");
        
    }
    
    private void getAllOrders() throws IOException{
        String url = APIRequests.GET_ALL_ORDERS;
        String response = database.RemoteDB.getData(url);
        JSONObject object = new JSONObject(response.toString());
        JSONArray arr = object.getJSONArray("orders"); // getting orders
        LinkedList<String> strList = new LinkedList();
        
        if(arr != null){
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                strList.add(obj.getString("customer_name") + "\n" + obj.getString("due_date_time"));
            }

            ObservableList<String> items = FXCollections.observableArrayList (strList);
            list.setItems(items);

            list.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    int index = list.getSelectionModel().getSelectedIndex();
                    System.out.println(index);
                    
                    selectOrder(arr, index);
                    if(user.getType() == 2){
                        btn_del.setDisable(true);
                    }else{
                        btn_del.setDisable(false);
                    }
                    btn_save.setText("حفظ التعديل");
                    btn_new.setVisible(true);
                    btn_done.setVisible(true);
                    edit = true;
                }


            });
        }
    }
    
    private void selectOrder(JSONArray arr, int index) {
        JSONObject order = arr.getJSONObject(index);
        ID = order.getInt("id");
        txt_name.setText(order.getString("customer_name"));
        txt_phone.setText(order.getString("phone_num"));
        txt_desc.setText(order.getString("needs"));
        txt_loc.setText(order.getString("loc"));
        String hr = order.getString("due_date_time").substring(11, 13) + "";
        if(Integer.parseInt(order.getString("due_date_time").substring(11, 13)) > 12){
            am_pm.setValue("PM");
            hr = (Integer.parseInt(order.getString("due_date_time").substring(11, 13)) - 12) + "";
        }else if(Integer.parseInt(order.getString("due_date_time").substring(11, 13)) == 0){
            hr = "12";
            am_pm.setValue("AM");
        }else{
            am_pm.setValue("AM");
        }
        System.out.println(hr);
        txt_hr.setText(hr);
        txt_min.setText(order.getString("due_date_time").substring(14, 16));
        
        System.out.println(order.getString("due_date_time").substring(11, 13) );
        System.out.println(order.getString("due_date_time").substring(14, 16) );
        System.out.println(order.getString("due_date_time").substring(0, 10));
        date_pick.setValue(LocalDate.parse(order.getString("due_date_time").substring(0, 10)));
        
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
                    .add("sec_phone", "")
                    .add("address", "")
                    .build();
            String url = APIRequests.INSERT_CUSTOMER;

            if(database.RemoteDB.postData(url, formBody)){
                System.out.println("INSERTED CUSTOMER");
                fillCustomersList();
            }
        }
    }
    
    
        
    private void initUsersPermitions() {
        this.user = genVars.user;
        
        if(user.getType() == 0){
            // super admin
        }else if(user.getType() == 1){
            // casher
            
        }else if(user.getType() == 2){
            // maintenance engineer
            btn_del.setDisable(true);
            btn_done.setDisable(true);
            btn_new.setDisable(true);
            btn_save.setDisable(true);
        }else if(user.getType() == 3){
            // supervisor
        }
        
        
    }
    
    
}
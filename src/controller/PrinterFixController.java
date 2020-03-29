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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxapp.Main;
import model.Customer;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;
import utils.genVars;

/**
 *
 * @author Ahmed
 */
public class PrinterFixController   implements Initializable {
    
    @FXML
    private CheckBox check_print;
    
    @FXML
    private CheckBox check_sticker;

    @FXML
    public TextField txt_loading;
    
    @FXML
    private TextField txt_device_num;
    
    @FXML
    private TextField txt_customer_name;
    
    @FXML
    private TextField txt_phone_num;
    
    @FXML
    private TextField txt_prob;
    
    @FXML
    private CheckBox Cfill_printer;
    
    @FXML
    private CheckBox Ccheck_all;
    
    @FXML
    private CheckBox Cchange_dram;
    
    @FXML
    private CheckBox Ccable_power;
    
    @FXML
    private CheckBox Ccable_data;
    
    @FXML
    private CheckBox Ccd;
    
    @FXML
    private TextField other;
    
    @FXML
    private TextField device_name;
    
    private int fill_printer = 0;
    private int change_dram = 0;
    private int check_all = 0;
    private int cable_power = 0;
    private int cable_data = 0;
    private int cd = 0;
    private int state = 0;
    private int num_char = 1;
        
    private LinkedList<Customer> customersList = new LinkedList<>();
    private List<String> names = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    
    @FXML
    private void BtnSave(ActionEvent event) throws ClassNotFoundException, IOException {        
        submit();
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
    public void initialize(URL url, ResourceBundle rb) {

        setDeviceNum();
        txt_device_num.setText(database.DatabaseHelper.getCounter() + 1 + "");
        Main.root.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                switch(event.getCode()){
                    case ENTER:
                        try {
                        submit();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(HardwareFixController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(HardwareFixController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                }
            }

            
        });
        
        
        try {
            fillCustomersList();
        } catch (IOException ex) {
            Logger.getLogger(HardwareFixController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TextFields.bindAutoCompletion(txt_customer_name, names).addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                txt_phone_num.setText(phones.get(names.indexOf(txt_customer_name.getText().toString())));
            }
        });
        
        TextFields.bindAutoCompletion(txt_phone_num, phones).addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                txt_customer_name.setText(names.get(phones.indexOf(txt_phone_num.getText().toString())));
            }
        });
    }    

    private void checkCheckBoxes() {
        if(Cfill_printer.isSelected()){
            fill_printer = 1;
        }
        if(Cchange_dram.isSelected()){
            change_dram = 1;
        }
        if(Ccheck_all.isSelected()){
            check_all = 1;
        }
        if(Ccable_power.isSelected()){
            cable_power = 1;
        }
        if(Ccable_data.isSelected()){
            cable_data = 1;
        }
        if(Ccd.isSelected()){
            cd = 1;
        }
        
    }
          
    public  void emptfyForm() throws IOException{
        txt_customer_name.setText("");
        txt_device_num.setText("");
        txt_prob.setText("");
        device_name.setText("");
        txt_phone_num.setText("");
        other.setText("");
        Ccable_data.setSelected(false);
        Ccable_power.setSelected(false);
        Cchange_dram.setSelected(false);
        Cfill_printer.setSelected(false);
        Ccheck_all.setSelected(false);
        Ccd.setSelected(false);
        check_print.setSelected(false);
        check_sticker.setSelected(false);
        
        fill_printer = 0;
        change_dram = 0;
        check_all = 0;
        cable_power = 0;
        cable_data = 0;
        cd = 0;
        
        database.DatabaseHelper.getCount();
        txt_device_num.setText(database.DatabaseHelper.getCounter() + 1 + "");
    }
    
    private void ppp() throws IOException{
        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = job.getPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        if (job != null) {
           boolean success = job.printPage(pageLayout, comp());
           if (success) {
               job.endJob();
           }
        }
        
    }
    
    private void print() throws IOException{
        /*PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = job.setPrinter(utils.utils.PrinterDialog());*/
        utils.utils.PrinterDialog(comp(), "تم التسجيل بنجاح");
        
    }
    
    private Node comp() throws IOException{
        BorderPane root;
        root = new BorderPane();
        Label title = new Label("ايصال صيانه احبار                       \n                       ============");
        title.setFont(new Font("arial", 20));
        title.setAlignment(Pos.CENTER);
        title.setContentDisplay(ContentDisplay.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        
        Image image = new Image("/res/images/pp.jpg");
        ImageView img = new ImageView(image);
        img.setFitWidth(60);
        img.setFitHeight(60);
        HBox hb = new HBox(img, title);
        hb.setAlignment(Pos.CENTER_LEFT);
        
        
        Label details = new Label(getProblemsString());
        details.setContentDisplay(ContentDisplay.CENTER);
        details.setTextAlignment(TextAlignment.CENTER);
        details.setFont(new Font("Arabic Typesetting", 18));
        
        Label other = new Label("ملحقات \n=====");
        other.setFont(new Font("arial", 20));
        other.setContentDisplay(ContentDisplay.CENTER);
        
        Label others = new Label(getOthersString());
        others.setContentDisplay(ContentDisplay.CENTER);
        others.setTextAlignment(TextAlignment.CENTER);
        others.setFont(new Font("Arabic Typesetting", 19));
        
        Label num = new Label("فاتوره رقم  " + txt_device_num.getText().toString());
        num.setContentDisplay(ContentDisplay.CENTER);
        num.setTextAlignment(TextAlignment.CENTER);
        
        Label date = new Label("\n   " + getDate() + " \n");
        date.setContentDisplay(ContentDisplay.CENTER);
        date.setTextAlignment(TextAlignment.CENTER);
        
        Label name = new Label(txt_customer_name.getText().toString());
        name.setContentDisplay(ContentDisplay.CENTER);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setFont(new Font("Arabic Typesetting", 20));
        Label policy = new Label(utils.PrinterText.POLICY_1);
        policy.setContentDisplay(ContentDisplay.CENTER);
        policy.setTextAlignment(TextAlignment.CENTER);
        policy.setFont(new Font("arial", 16));
        
        Label policy2 = new Label(utils.PrinterText.POLICY_2);
        policy2.setContentDisplay(ContentDisplay.CENTER);
        policy2.setTextAlignment(TextAlignment.CENTER);
        policy2.setFont(new Font("arial", 16));
        
        Label signature = new Label(utils.PrinterText.SIGNATURE);
        signature.setContentDisplay(ContentDisplay.RIGHT);
        signature.setTextAlignment(TextAlignment.RIGHT);
        signature.setFont(new Font("arial", 16));
        signature.setAlignment(Pos.BOTTOM_RIGHT);
        
        Label phone = new Label(utils.PrinterText.PHONE);
        phone.setContentDisplay(ContentDisplay.CENTER);
        phone.setTextAlignment(TextAlignment.CENTER);
        phone.setFont(new Font("arial", 16));
        phone.setAlignment(Pos.BOTTOM_CENTER);
        
        Label logo = new Label("CS Computer Shop");
        logo.setContentDisplay(ContentDisplay.CENTER);
        logo.setTextAlignment(TextAlignment.CENTER);
        logo.setFont(new Font("arial", 25));
        logo.setAlignment(Pos.CENTER);

        
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-padding: 30px, 30px, 30px, 30px");
        vb.getChildren().addAll(logo,hb,name,num,details,other,others,date,policy,policy2,signature,phone);
	root.setCenter(vb);
        return root;
    }
    
        
    private static Timestamp getDate() {
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        return date;
    }
    
    
    private String getProblemsString(){
        String txt = device_name.getText().toString() + "\n";
        if(fill_printer == 1){
            txt = txt + "ملئ حبر";
        }
        if(change_dram == 1){
            txt = txt + "\n تغير درام";
        }
        if(check_all == 1){
            txt = txt + "\n check all";
        }
        
        if(txt_prob.getText().toString() != null || !txt_prob.getText().toString().equals("")){
            txt = txt + "\n  مشكلات اخري";
            txt = txt + "\n" + txt_prob.getText().toString() + "\n";
        }
        
        return txt + "\n";
        
    }
    
    private String getOthersString(){
        String txt = "";
        if(cable_power == 1){
            txt = txt + "\n cable power";
        }
        if(cable_data == 1){
            txt = txt + "\n cable power";
        }          if(cd == 1){
            txt = txt + "\n CD";
        }   
              
        if(other.getText().toString() != null || !other.getText().toString().equals("")){
            txt = txt + "\n  ملحقات اخري";
            txt = txt + "\n" + other.getText().toString();
        }
        
        return txt;
    }
        
    private void submit() throws ClassNotFoundException, UnsupportedEncodingException, IOException {
        System.out.println("Save");
        checkCheckBoxes();
        // check if customer alrady exist
        checkUserExist();
        setDeviceNum();
        RequestBody formBody = new FormBody.Builder()
                .add("device_num", txt_device_num.getText().toString())
                .add("customer_name", txt_customer_name.getText().toString())
                .add("phone_num", txt_phone_num.getText().toString())
                .add("prob", txt_prob.getText().toString())
                .add("check_all", check_all + "")
                .add("fill_printer", fill_printer + "")
                .add("change_dram", change_dram + "")
                .add("cable_power", cable_power + "")
                .add("cable_data", cable_data + "")
                .add("cd", cd + "")
                .add("date_time", String.valueOf(getDate()))
                .add("other", other.getText().toString())
                .add("device_name", device_name.getText().toString())
                .add("state", state + "")
                .add("device_receiver", genVars.user.getName() + "")
                .add("device_submitter", "")
                .add("date_change_dep", "")
                .build();
        String url = APIRequests.INSERT_PRINTER;
        
        if(database.RemoteDB.postData(url, formBody)){
            
            if(check_sticker.isSelected()){
                String prob = txt_prob.getText().toString();
                if(prob.equals("") || prob.equals(null)){
                    prob = "عطل طابعات";
                }
                utils.utils.PrinterStickerDialog(null, "تم الارسال الي الطباعه", txt_device_num.getText().toString()
                        , txt_customer_name.getText().toString(), device_name.getText().toString(),
                        prob, "احبار");
            }else{
                //utils.utils.AlertMSG("تم التسجيل بنجاح");
            }
            
            if(check_print.isSelected()){
                print();
            }else{
                utils.utils.AlertMSG("تم التسجيل بنجاح");
            }
            
            // increment devices counter
            database.DatabaseHelper.updateCount(Integer.parseInt(txt_device_num.getText().toString()));
            emptfyForm();
        }else{
            utils.utils.AlertMSG("عفوا لم يتم الحفظ");
        }
        
    }
    
    
        
    private void fillCustomersList() throws IOException{
        String customer_name = txt_customer_name.getText().toString();
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
        String customer_name = txt_customer_name.getText().toString();
        String response = database.RemoteDB.checkDataExist(url_search, customer_name, "name");
        JSONObject object = new JSONObject(response.toString());
        int success = object.getInt("success");
        if(success != 0){
            // already exist
        }else{
            System.out.println("NOT EXIST CUSTOMER");
            RequestBody formBody = new FormBody.Builder()
                    .add("name", txt_customer_name.getText().toString())
                    .add("phone", txt_phone_num.getText().toString())
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
    
        
    private void setDeviceNum() {
        try {
            // TODO
            database.DatabaseHelper.getCount();
        } catch (IOException ex) {
            Logger.getLogger(HardwareFixController.class.getName()).log(Level.SEVERE, null, ex);
        }
        txt_device_num.setText(database.DatabaseHelper.getCounter() + 1 + "");
    }
    
    
}
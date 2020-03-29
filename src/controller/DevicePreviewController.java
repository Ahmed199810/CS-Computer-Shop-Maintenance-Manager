package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafxapp.Main;
import model.User;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONObject;
import utils.APIRequests;
import utils.MenuItemIds;
import utils.genVars;

/**
 *
 * @author Ahmed
 */
public class DevicePreviewController implements Initializable {
    
    @FXML
    private Label txt_date_change_dep;
    
    @FXML
    private TextField txt_receiver;
    
    @FXML
    private TextField txt_submitter;
    
    @FXML
    private TextField txt_device_num;
    
    @FXML
    private TextField txt_customer_name;
    
    @FXML
    private TextField txt_phone_num;
    
    @FXML
    private TextField txt_rep;
    
    @FXML
    private TextField txt_phone_rep;
    
    @FXML
    private TextField txt_prob;
    
    @FXML
    private CheckBox Cwin7;
    
    @FXML
    private CheckBox Cwin8;
    
    @FXML
    private CheckBox Cwin10;
    
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
    private CheckBox Csoftware;
    
    @FXML
    private Label txt_date_time;
    
    @FXML
    private CheckBox Chardware;
    
    @FXML
    private CheckBox Cbattery;
    
    @FXML
    private CheckBox Ccharger;
    
    @FXML
    private CheckBox Cbag;
    
    @FXML
    private CheckBox Ccd;
    
    @FXML
    private TextField other;
    
    @FXML
    private TextField device_name;
        
    @FXML
    private TextField txt_price;
    
    @FXML
    private Button btn_submit;
    
    @FXML
    private Button btn_done;
        
    @FXML
    private Button btn_not_done;
    
    @FXML
    private Button btn_del;
    
    @FXML
    private Button btn_save;
    
    @FXML
    private Label txt_state;
    
    @FXML
    private MenuButton btn_department;
    
    
    private int win7 = 0;
    private int win8 = 0;
    private int win10 = 0;
    private int check_all = 0;
    private int software = 0;
    private int hardware = 0;
    private int battery = 0;
    private int charger = 0;
    private int bag = 0;
    private int cd = 0;
    private int fill_printer = 0;
    private int change_dram = 0;
    private int cable_power = 0;
    private int cable_data = 0;
    
    private int HARDWARE = 0;
    private int SOFTWARE = 1;
    private int PRINTER = 2;
    private int OUTER_FIX = 3;
    private int DONE = 1;
    private int NOT_DONE = 2;
    private int SUBMIT = 3;
    private int WAIT = 0;
    
    private MenuItemIds ids;
    
    private String submitter = "";
    
    private User user;

   
    
    
    @FXML
    private void BtnCancel(ActionEvent event) {
        System.out.println("Cancel");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
    @FXML
    private void BtnPrint(ActionEvent event) throws IOException {
        System.out.println("Print");
        print();
    }
    
    
    @FXML
    private void BtnSticker(ActionEvent event) throws IOException {
        System.out.println("Print Sticker");
        String prob = txt_prob.getText().toString();
        if(prob.equals("") || prob.equals(null)){
            prob = "عطل ";
        }
        String title = "صيانه";
        if(utils.genVars.type == HARDWARE){
            title = "هاردوير";
        }else if(utils.genVars.type == SOFTWARE){
            title = "سوفت وير";
        }else if(utils.genVars.type == PRINTER){
            title = "احبار";
        }else if(utils.genVars.type == OUTER_FIX){
            title = "خارجي";
        }
        
        utils.utils.PrinterStickerDialog(null, "تم الارسال الي الطباعه", txt_device_num.getText().toString()
                , txt_customer_name.getText().toString(), device_name.getText().toString(),prob, title);
            
    }
    
    
    @FXML
    private void BtnSaveChange(ActionEvent event) throws IOException, ClassNotFoundException {
        System.out.println("Save Changes");
        // check CheckBoxes
        GetCheckedCheckBoxes();
        SaveDataChanges();

    }
    
    
    @FXML
    private void BtnDelete(ActionEvent event) throws IOException {
        System.out.println("Delete");
        deleteData();
    }
    
    @FXML
    private void BtnDone(ActionEvent event) throws IOException {
        System.out.println("Done");
        done(DONE);
        genVars.device.accumulate("device_submitter", genVars.user.getName());
    }
    
        
    @FXML
    private void BtnNotDone(ActionEvent event) throws IOException {
        System.out.println("Not Done");
        done(NOT_DONE);
    }
    
    @FXML
    private void BtnSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        System.out.println("Submit");
        done(SUBMIT);
        submitter = genVars.user.getName();
        SaveDataChanges();
        txt_submitter.setText(genVars.user.getName());
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initUsersPermitions();
        init();
        LinkedList<MenuItem> list = utils.utils.getDepartments();
        btn_department.getItems().addAll(list);
        
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                String id = ((MenuItem)e.getSource()).getId();
                
                if(id.equals(ids.btn_dep_hard + "")){
                    try {
                        System.out.println(id);
                        utils.utils.changeToDep(utils.genVars.type, HARDWARE);
                    } catch (IOException ex) {
                        Logger.getLogger(DevicePreviewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(id.equals(ids.btn_dep_soft + "")){
                    try {
                        System.out.println(id);
                        utils.utils.changeToDep(utils.genVars.type, SOFTWARE);
                    } catch (IOException ex) {
                        Logger.getLogger(DevicePreviewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(id.equals(ids.btn_dep_print + "")){
                    try {
                        System.out.println(id);
                        utils.utils.changeToDep(utils.genVars.type, PRINTER);
                    } catch (IOException ex) {
                        Logger.getLogger(DevicePreviewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(id.equals(ids.btn_dep_out_fix + "")){
                    try {
                        System.out.println(id);
                        utils.utils.changeToDep(utils.genVars.type, OUTER_FIX);
                    } catch (IOException ex) {
                        Logger.getLogger(DevicePreviewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            } 

            
        };
        
        for(int i = 0; i < list.size(); i++){
            list.get(i).setOnAction(event1);
        }
    }    
    
    private void init(){
        JSONObject deviceItem = utils.genVars.device;
        if(utils.genVars.type == HARDWARE){// hardware
            
            txt_device_num.setText(deviceItem.get("device_num") + "");
            txt_customer_name.setText(deviceItem.get("customer_name") + "");
            txt_phone_num.setText(deviceItem.get("phone_num") + "" + "");
            txt_prob.setText(deviceItem.get("prob") + "");
            txt_date_time.setText(deviceItem.get("date_time") + "");
            device_name.setText(deviceItem.get("device_name") + "");
            txt_price.setText(deviceItem.get("price") + "");
            other.setText(deviceItem.get("other") + "");
            win7 =  deviceItem.getInt("win7");
            win8 =  deviceItem.getInt("win8");
            win10 =  deviceItem.getInt("win10");
            check_all =  deviceItem.getInt("check_all");
            software =  deviceItem.getInt("software");
            hardware =  deviceItem.getInt("hardware");
            battery =  deviceItem.getInt("battery");
            charger =  deviceItem.getInt("charger");
            bag =  deviceItem.getInt("bag");
            cd =  deviceItem.getInt("cd");
            
            if(deviceItem.getInt("state") == 3){
                btn_submit.setDisable(true);
                btn_done.setDisable(true);
                btn_not_done.setDisable(true);
                
                txt_state.setText("تم التسليم");
                
            }
            if(deviceItem.getInt("state") == 1){
                txt_state.setText("تمت الصيانه");
            }
            if(deviceItem.getInt("state") == 2){
                txt_state.setText("لم تتم الصيانه");
            }          
            if(deviceItem.getInt("state") == 0){
                txt_state.setText("في الانتظار");
            }                      
                        
        }else if(utils.genVars.type == SOFTWARE){ // software
            txt_device_num.setText(deviceItem.get("device_num") + "");
            txt_customer_name.setText(deviceItem.get("customer_name") + "");
            txt_phone_num.setText(deviceItem.get("phone_num") + "" + "");
            txt_prob.setText(deviceItem.get("prob") + "");
            txt_date_time.setText(deviceItem.get("date_time") + "");
            device_name.setText(deviceItem.get("device_name") + "");
            txt_price.setText(deviceItem.get("price") + "");
            other.setText(deviceItem.get("other") + "");
            win7 =  deviceItem.getInt("win7");
            win8 =  deviceItem.getInt("win8");
            win10 =  deviceItem.getInt("win10");
            check_all =  deviceItem.getInt("check_all");
            software =  deviceItem.getInt("software");
            battery =  deviceItem.getInt("battery");
            charger =  deviceItem.getInt("charger");
            bag =  deviceItem.getInt("bag");
            cd =  deviceItem.getInt("cd");
            
            if(deviceItem.getInt("state") == 3){
                btn_submit.setDisable(true);
                btn_done.setDisable(true);
                btn_not_done.setDisable(true);
                
                txt_state.setText("تم التسليم");
                
            }
            if(deviceItem.getInt("state") == 1){
                txt_state.setText("تمت الصيانه");
            }
            if(deviceItem.getInt("state") == 2){
                txt_state.setText("لم تتم الصيانه");
            }          
            if(deviceItem.getInt("state") == 0){
                txt_state.setText("في الانتظار");
            }                      
                             
            
        }else if(utils.genVars.type == PRINTER){ // printer
            
            txt_device_num.setText(deviceItem.get("device_num") + "");
            txt_customer_name.setText(deviceItem.get("customer_name") + "");
            txt_phone_num.setText(deviceItem.get("phone_num") + "");
            txt_prob.setText(deviceItem.get("prob") + "");
            txt_date_time.setText(deviceItem.get("date_time") + "");
            device_name.setText(deviceItem.get("device_name") + "");
            txt_price.setText(deviceItem.get("price") + "");
            other.setText(deviceItem.get("other") + "");
            fill_printer =  deviceItem.getInt("fill_printer");
            cable_power =  deviceItem.getInt("cable_power");
            cable_data =  deviceItem.getInt("cable_data");
            change_dram =  deviceItem.getInt("change_dram");
            check_all = deviceItem.getInt("check_all");
            cd =  deviceItem.getInt("cd");
            
            if(deviceItem.getInt("state") == 3){
                btn_submit.setDisable(true);
                btn_done.setDisable(true);
                btn_not_done.setDisable(true);
                txt_state.setText("تم التسليم");
            }
            if(deviceItem.getInt("state") == 1){
                txt_state.setText("تمت الصيانه");
            }
            if(deviceItem.getInt("state") == 2){
                txt_state.setText("لم تتم الصيانه");
            }          
            if(deviceItem.getInt("state") == 0){
                txt_state.setText("في الانتظار");
            }    

        }else if(utils.genVars.type == OUTER_FIX){
            
            txt_device_num.setText(deviceItem.get("device_num") + "");
            txt_customer_name.setText(deviceItem.get("customer_name") + "");
            txt_phone_num.setText(deviceItem.get("customer_phone_num") + "" + "");
            txt_rep.setText(deviceItem.get("rep_name") + "" + "");
            txt_phone_rep.setText(deviceItem.get("rep_phone_num") + "" + "");
            txt_prob.setText(deviceItem.get("prob") + "");
            txt_date_time.setText(deviceItem.get("date_time") + "");
            device_name.setText(deviceItem.get("device_name") + "");
            txt_price.setText(deviceItem.get("price") + "");
            other.setText(deviceItem.get("other") + "");
            win7 =  deviceItem.getInt("win7");
            win8 =  deviceItem.getInt("win8");
            win10 =  deviceItem.getInt("win10");
            check_all =  deviceItem.getInt("check_all");
            software =  deviceItem.getInt("software");
            hardware =  deviceItem.getInt("hardware");
            battery =  deviceItem.getInt("battery");
            charger =  deviceItem.getInt("charger");
            bag =  deviceItem.getInt("bag");
            cd =  deviceItem.getInt("cd");
            fill_printer =  deviceItem.getInt("fill_printer");
            cable_power =  deviceItem.getInt("cable_power");
            cable_data =  deviceItem.getInt("cable_data");
            change_dram =  deviceItem.getInt("change_dram");
            
            if(deviceItem.getInt("state") == 3){
                btn_submit.setDisable(true);
                btn_done.setDisable(true);
                btn_not_done.setDisable(true);
                
                txt_state.setText("تم التسليم");
                
            }
            if(deviceItem.getInt("state") == 1){
                txt_state.setText("تمت الصيانه");
            }
            if(deviceItem.getInt("state") == 2){
                txt_state.setText("لم تتم الصيانه");
            }          
            if(deviceItem.getInt("state") == 0){
                txt_state.setText("في الانتظار");
            }
            
            
        }
        
        txt_receiver.setText(deviceItem.getString("device_receiver"));
        txt_submitter.setText(deviceItem.getString("device_submitter"));
        if(deviceItem.getString("date_change_dep").equals("") || deviceItem.getString("date_change_dep").equals("null")){
            txt_date_change_dep.setText("غير محول");
        }else{
            txt_date_change_dep.setText(deviceItem.getString("date_change_dep"));
        }
        
        checkCheckBoxes();
    }
    

    private void checkCheckBoxes() {
        if(win7 == 1){
            Cwin7.setSelected(true);
        }
        if(win8 == 1){
            Cwin8.setSelected(true);
        }
        if(win10 == 1){
            Cwin10.setSelected(true);
        }
        if(check_all == 1){
            Ccheck_all.setSelected(true);
        }
        if(software == 1){
            Csoftware.setSelected(true);
        }
        if(hardware == 1){
            Chardware.setSelected(true);
        }if(battery == 1){
            Cbattery.setSelected(true);
        }if(charger == 1){
            Ccharger.setSelected(true);
        }if(bag == 1){
            Cbag.setSelected(true);
        }
        if(cd == 1){
            Ccd.setSelected(true);
        }
        if(fill_printer == 1){
            Cfill_printer.setSelected(true);
        }
        if(change_dram == 1){
            Cchange_dram.setSelected(true);
        }
        if(cable_power == 1){
            Ccable_power.setSelected(true);
        }
        if(cable_data == 1){
            Ccable_data.setSelected(true);
        }

    }
    
    private void GetCheckedCheckBoxes() {
        win7 = 0;
        win8 = 0;
        win10 = 0;
        check_all = 0;
        software = 0;
        hardware = 0;
        battery = 0;
        charger = 0;
        bag = 0;
        cd = 0;
        fill_printer = 0;
        change_dram = 0;
        cable_power = 0;
        cable_data = 0;
        
        
        if(Cwin7.isSelected()){
            win7 = 1;
        }
        if(Cwin8.isSelected()){
            win8 = 1;
        }
        if(Cwin10.isSelected()){
            win10 = 1;
        }
        if(Ccheck_all.isSelected()){
            check_all = 1;
        }
        if(Csoftware.isSelected()){
            software = 1;
        }
        if(Chardware.isSelected()){
            hardware = 1;
        }if(Cbattery.isSelected()){
            battery = 1;
        }if(Ccharger.isSelected()){
            charger = 1;
        }if(Cbag.isSelected()){
            bag = 1;
        }
        if(Ccd.isSelected()){
            cd = 1;
        }
        if(Cfill_printer.isSelected()){
            fill_printer = 1;
        }
        if(Cchange_dram.isSelected()){
            change_dram = 1;
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
    
        
    private String getProblemsString(){
        String txt = device_name.getText().toString() + "\n";
        if(win7 == 1){
            txt = txt + "win7";
        }
        if(win8 == 1){
            txt = txt + "\n win8";
        }
        if(win10 == 1){
            txt = txt + "\n win10";
        }
        if(check_all == 1){
            txt = txt + "\n check all";
        }
        if(software == 1){
            txt = txt + "\n software";
        }
        if(hardware == 1){
            txt = txt + "\n hardware";
        }
        if(fill_printer == 1){
            txt = txt + "\n ملئ حبر";
        }
        if(change_dram == 1){
            txt = txt + "\n تغيير درام";
        }
        
        if(txt_prob.getText().toString() != null || !txt_prob.getText().toString().equals("")){
            txt = txt + "\n  مشكلات اخري";
            txt = txt + "\n" + txt_prob.getText().toString() + "\n";
        }
        
        return txt + "\n";
        
    }
    
    private String getOthersString(){
        String txt = "";
        if(battery == 1){
            txt = txt + "\n بطاريه";
        }
        if(charger == 1){
            txt = txt + "\n شاحن";
        }
        if(cd == 1){
            txt = txt + "\n CD";
        }        
        if(bag == 1){
            txt = txt + "\n حقيبه";
        }
        if(cable_power == 1){
            txt = txt + "\n Cable Power";
        }        
        if(cable_data == 1){
            txt = txt + "\n Cable Data";
        }
        if(other.getText().toString() != null || !other.getText().toString().equals("")){
            txt = txt + "\n ملحقات اخري";
            txt = txt + "\n" + other.getText().toString();
        }
        
        return txt;
    }
    
    private void print() throws IOException{
        utils.utils.PrinterDialog(comp(), "تم الارسال الي الطابعه");
    }
    
    private Node comp() throws IOException{
        BorderPane root;
        root = new BorderPane();
        Label title = new Label();
        if(utils.genVars.type == HARDWARE){
            title.setText("ايصال صيانه هاردوير                      \n                       ============");
        }else if(utils.genVars.type == SOFTWARE){
            title.setText("ايصال صيانه سوفت وير                      \n                      ============");
        }else if(utils.genVars.type == PRINTER){
            title.setText("ايصال صيانه احبار                        \n                       ============");
        }else if(utils.genVars.type == OUTER_FIX){
            title.setText("ايصال صيانه                         \n                          ==========");
        }
        
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
        other.setTextAlignment(TextAlignment.CENTER);
        
        Label others = new Label(getOthersString());
        others.setContentDisplay(ContentDisplay.CENTER);
        others.setTextAlignment(TextAlignment.CENTER);
        others.setFont(new Font("Arabic Typesetting", 19));
        
        Label num = new Label("ايصال رقم  " + txt_device_num.getText().toString());
        num.setContentDisplay(ContentDisplay.CENTER);
        num.setTextAlignment(TextAlignment.CENTER);
        
        Label date = new Label("\n   " + txt_date_time.getText() + "\n ------------------------------------------------------------------------");
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

    private void SaveDataChanges() throws ClassNotFoundException, UnsupportedEncodingException, IOException{
        int id = utils.genVars.device.getInt("id");
        if(utils.genVars.type == HARDWARE){
            String url = APIRequests.UPDATE_HARDWARE;
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")    
                .add("device_num", txt_device_num.getText().toString())
                .add("customer_name", txt_customer_name.getText().toString())
                .add("phone_num", txt_phone_num.getText().toString())
                .add("prob", txt_prob.getText().toString())
                .add("win7", win7 + "")
                .add("win8", win8 + "")
                .add("win10", win10 + "")
                .add("check_all", check_all + "")
                .add("software", software + "")
                .add("hardware", hardware + "")
                .add("battery", battery + "")
                .add("charger", charger + "")
                .add("bag", bag + "")
                .add("cd", cd + "")
                .add("price", txt_price.getText().toString())
                .add("other", other.getText().toString())
                .add("device_name", device_name.getText().toString())
                .add("device_submitter", submitter)
                .build();
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم التعديل بنجاح");
            }else{
                utils.utils.AlertMSG("عفوا لم يتم الحفظ");
            }
            
        }else if(utils.genVars.type == SOFTWARE){
            
            String url = APIRequests.UPDATE_SOFTWARE;
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")    
                .add("device_num", txt_device_num.getText().toString())
                .add("customer_name", txt_customer_name.getText().toString())
                .add("phone_num", txt_phone_num.getText().toString())
                .add("prob", txt_prob.getText().toString())
                .add("win7", win7 + "")
                .add("win8", win8 + "")
                .add("win10", win10 + "")
                .add("check_all", check_all + "")
                .add("software", software + "")
                .add("battery", battery + "")
                .add("charger", charger + "")
                .add("bag", bag + "")
                .add("cd", cd + "")
                .add("price", txt_price.getText().toString())
                .add("other", other.getText().toString())
                .add("device_name", device_name.getText().toString())
                    .add("device_submitter", submitter)
                .build();
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم التعديل بنجاح");
            }else{
                utils.utils.AlertMSG("عفوا لم يتم الحفظ");
            }
            
            
        }else if(utils.genVars.type == PRINTER){
            String url = APIRequests.UPDATE_PRINTER;
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
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
                .add("other", other.getText().toString())
                .add("device_name", device_name.getText().toString())
                .add("price", txt_price.getText().toString())
                    .add("device_submitter", submitter)
                .build();
            
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم التعديل بنجاح");
            }else{
                utils.utils.AlertMSG("عفوا لم يتم الحفظ");
            }
        }else if(utils.genVars.type == OUTER_FIX){
            String url = APIRequests.UPDATE_OUTER_FIX;
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id + "")    
                    .add("device_num", txt_device_num.getText().toString())
                    .add("customer_name", txt_customer_name.getText().toString())
                    .add("customer_phone_num", txt_phone_num.getText().toString())
                    .add("rep_name", txt_rep.getText().toString())
                    .add("rep_phone_num", txt_phone_rep.getText().toString())
                    .add("prob", txt_prob.getText().toString())
                    .add("win7", win7 + "")
                    .add("win8", win8 + "")
                    .add("win10", win10 + "")
                    .add("check_all", check_all + "")
                    .add("software", software + "")
                    .add("hardware", hardware + "")
                    .add("battery", battery + "")
                    .add("charger", charger + "")
                    .add("bag", bag + "")
                    .add("cd", cd + "")
                    .add("price", txt_price.getText().toString())
                    .add("other", other.getText().toString())
                    .add("device_name", device_name.getText().toString())
                    .add("fill_printer", fill_printer + "")
                    .add("change_dram", change_dram + "")
                    .add("cable_power", cable_power + "")
                    .add("cable_data", cable_data + "")
                    .add("device_submitter", submitter)
                    .build();
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم التعديل بنجاح");
            }else{
                utils.utils.AlertMSG("عفوا لم يتم الحفظ");
            }
            
        }
    }

    private void deleteData() throws IOException {
        int id = utils.genVars.device.getInt("id");
        if(utils.genVars.type == HARDWARE){
            String url = APIRequests.DELETE_DEVICE;
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("table", "hardware_fix")
                .build();
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم الحذف بنجاح");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }
                
            }else{
                utils.utils.AlertMSG("لم يتم الحذف ");
            }
                
        }else if(utils.genVars.type == SOFTWARE){
            
            String url = APIRequests.DELETE_DEVICE;
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("table", "software_fix")
                .build();
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم الحذف بنجاح");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }
                
            }else{
                utils.utils.AlertMSG("لم يتم الحذف ");
            }
            
        }else if(utils.genVars.type == PRINTER){
        
            String url = APIRequests.DELETE_DEVICE;
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("table", "printer_fix")
                .build();
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم الحذف بنجاح");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }
                
            }else{
                utils.utils.AlertMSG("لم يتم الحذف ");
            }
        
        }else if(utils.genVars.type == OUTER_FIX){
            String url = APIRequests.DELETE_DEVICE;
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("table", "outer_fix")
                .build();
            if(database.RemoteDB.postData(url, formBody)){
                utils.utils.AlertMSG("تم الحذف بنجاح");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }
                
            }else{
                utils.utils.AlertMSG("لم يتم الحذف ");
            }
        }
        
    }

    private void done(int state) throws IOException {
        
        int id = utils.genVars.device.getInt("id");
        String url = APIRequests.UPDATE_DEVICE; 
        if(utils.genVars.type == HARDWARE){
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("state", state + "")
                .add("table", "hardware_fix")
                .build();
            database.RemoteDB.postData(url, formBody);
        }else if(utils.genVars.type == SOFTWARE){
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("state", state + "")
                .add("table", "software_fix")
                .build();
            database.RemoteDB.postData(url, formBody);
        }else if(utils.genVars.type == PRINTER){
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("state", state + "")
                .add("table", "printer_fix")
                .build();
            database.RemoteDB.postData(url, formBody);
        }else if(utils.genVars.type == OUTER_FIX){
            RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("state", state + "")
                .add("table", "outer_fix")
                .build();
            database.RemoteDB.postData(url, formBody);
        }
        // REFRESHING UI
        utils.genVars.device.put("state", state);
        init();
        
    }
    
    
    private void initUsersPermitions() {
        this.user = genVars.user;
        
        if(user.getType() == 0){
            // super admin
        }else if(user.getType() == 1){
            // casher
            
        }else if(user.getType() == 2){
            // maintenance engineer
            btn_submit.setDisable(true);
            btn_not_done.setDisable(true);
            btn_submit.setDisable(true);
            btn_done.setDisable(true);
            btn_save.setDisable(true);
            btn_del.setDisable(true);
            
            
        }else if(user.getType() == 3){
            // supervisor
        }
        
        
        
    }
    
    
    
    

}
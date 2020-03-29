package utils;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import controller.ReportsController;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapp.Main;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ahmed
 */
public class utils {
    
    private static int HARDWARE = 0;
    private static int SOFTWARE = 1;
    private static int PRINTER = 2;
    private static int OUTER_FIX = 3;
    private static ListView<String> list = new ListView<>();
    
    private static Image red  = new Image("/res/images/red.png");
    private static Image green  = new Image("/res/images/green.png");
    private static Image blue  = new Image("/res/images/blue.png");
    private static Image right  = new Image("/res/images/true.png");
    
    public static void AlertMSG(String msg){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Button btn = new Button("OK");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialogStage.hide();
            }
        });
        VBox vbox = new VBox(new Text(msg), btn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
        
        
    }
    
    public static void PreviewCurrentDevices(String msg, JSONArray arr){
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        list.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        previewDevices(arr);
        VBox vbox = new VBox(new Text(msg), list);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
    }
    
    
    private static void previewDevices(JSONArray listDev){
        List<String> strList = new ArrayList<>();
        for(int i = 0; i < listDev.length(); i++){
            String state = "";
            JSONObject device = listDev.getJSONObject(i);
            if(device.getInt("state") == 0){
                state = "في الانتظار";
            }else if(device.getInt("state") == 1){
                state = "تمت الصيانه";
            }else if(device.getInt("state") == 2){ // 2
                state = "لم تتم الصيانه";
            }else{
                state = "تم التسليم";
            }
            strList.add( 
                      "  اسم الجهاز : " + device.getString("device_name") + "\n "
                    + "   التاريخ:  " + device.get("date_time")  + "\n"
                    + " الحاله: " + state + "\n "
                    + " عميل: " + device.getString("customer_name") + "\n "
                    + " جهاز رقم: " + device.getString("device_num")
            );
        }
        
        ObservableList<String> items = FXCollections.observableArrayList (strList);
        list.setItems(items);
        
        list.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if(item.contains("في الانتظار"))
                        imageView.setImage(blue);
                    else if(item.contains("تمت الصيانه"))
                        imageView.setImage(green);
                    else if(item.contains("تم التسليم"))
                        imageView.setImage(right);
                    else
                        imageView.setImage(red);
                    
                    setText(item);
                    setGraphic(imageView);
                    getStylesheets().add("/res/css/stylesheet.css");
                }
            }
        });
        
        list.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                int index = list.getSelectionModel().getSelectedIndex();
                System.out.println(index);
                genVars.device = listDev.getJSONObject(index);
                System.out.println(genVars.device);
                // open new window
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/DevicePreview.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "");
                    
                } catch (Exception e) {
                    
                }
            }
        });
    }
    
    public static void PrinterDialog(Node node, String msg){
        ObservableSet<Printer> printers = Printer.getAllPrinters();
        LinkedList<Printer> printerList = new LinkedList<>();
        List<String> strList = new ArrayList();
        PrinterJob job = PrinterJob.createPrinterJob();
        ListView list = new ListView();
        for(Printer printer : printers){
            strList.add(printer.getName());
            printerList.add(printer);
        }
        ObservableList<String> items = FXCollections.observableArrayList (strList);
        list.setItems(items);
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        
        list.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Printer printer = printerList.get(list.getSelectionModel().getSelectedIndex());
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
                job.setPrinter(printer);
                if (job != null/* && job.showPrintDialog(comp().getScene().getWindow())*/) {
                   boolean success = job.printPage(pageLayout, node);
                   if (success) {
                       job.endJob();
                   }
                }
        
                dialogStage.hide();
                AlertMSG(msg);
            }
        });
        
        VBox vbox = new VBox(list);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        dialogStage.setTitle("طباعه ايصال");
        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
        
    }
    
        
    public static void PrinterStickerDialog(Node node, String msg, String c, String n, String d, String p, String t){
        ObservableSet<Printer> printers = Printer.getAllPrinters();
        LinkedList<Printer> printerList = new LinkedList<>();
        List<String> strList = new ArrayList();
        PrinterJob job = PrinterJob.createPrinterJob();
        ListView list = new ListView();
        for(Printer printer : printers){
            strList.add(printer.getName());
            printerList.add(printer);
        }
        ObservableList<String> items = FXCollections.observableArrayList (strList);
        list.setItems(items);
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        /*List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(5);
        
        
        ObservableList<Integer> ol = FXCollections.observableArrayList (l);
        ComboBox cb = new ComboBox(ol);
        cb.setItems(ol);*/
        list.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Printer printer = printerList.get(list.getSelectionModel().getSelectedIndex());
                Paper size = PrintHelper.createPaper("5x2.5", 120, 35, Units.MM);
                PageLayout pageLayout = printer.createPageLayout(size, PageOrientation.PORTRAIT,0f,0f,0f,0f);
                job.setPrinter(printer);
                //int copies = cb.getSelectionModel().getSelectedIndex() + 1;
                //job.getJobSettings().setCopies(copies);
                if (job != null/* && job.showPrintDialog(comp().getScene().getWindow())*/) {
                   boolean success = job.printPage(pageLayout, StickerNode(c, n, d, p, t));
                   if (success) {
                       job.endJob();
                   }
                }
        
                dialogStage.hide();
                AlertMSG(msg);
            }
        });
        
        
        VBox vbox = new VBox(/*cb , */list);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        dialogStage.setTitle("طباعه استيكر");
        dialogStage.setScene(new Scene(vbox));
        
        //int copies = cb.getSelectionModel().getSelectedIndex() + 1;
        
        
        dialogStage.show();
        
    }
    
    public static Node StickerNode(String c, String n, String d, String p, String t){
        Text code = new Text("كود" + " : " + c);
        Text name = new Text("اسم العميل" + " : " + n);
        Text device = new Text("اسم الجهاز" + " : " + d);
        Text prob = new Text("المشكله" + " : " + p);
        Text title = new Text("   " + t + "   ");
        
        code.setFont(Font.font("Arabic Typesetting", 16));
        name.setFont(Font.font("Arabic Typesetting", 16));
        device.setFont(Font.font("Arabic Typesetting", 16));
        prob.setFont(Font.font("Arabic Typesetting", 16));
        title.setFont(Font.font("Arabic Typesetting", 18));
        
        
        code.setTextAlignment(TextAlignment.LEFT);
        name.setTextAlignment(TextAlignment.LEFT);
        device.setTextAlignment(TextAlignment.LEFT);
        title.setTextAlignment(TextAlignment.LEFT);
        
        code.setBoundsType(TextBoundsType.VISUAL);
        name.setBoundsType(TextBoundsType.VISUAL);
        device.setBoundsType(TextBoundsType.VISUAL);
        title.setBoundsType(TextBoundsType.VISUAL);
        
        Image image = new Image("/res/images/sticker.png");
        ImageView img = new ImageView(image);
        img.setFitWidth(25);
        img.setFitHeight(25);
        
        
        HBox hb = new HBox(img, title, code);
        hb.setAlignment(Pos.CENTER_RIGHT);
        
        VBox vb = new VBox(hb, name, device, prob);
        vb.setAlignment(Pos.CENTER_RIGHT);
        
        return vb;
    }
    
    
    public static LinkedList<MenuItem> getDepartments(){
        String items[] = {"صيانه هاردوير", "صيانه سوفت وير", "صيانه طابعات", "صيانات خارجيه"};
        String ids[] = {"btn_dep_hard", "btn_dep_soft", "btn_dep_print", "btn_dep_out_fix"};
        LinkedList<MenuItem> list = new LinkedList<>();
        
        for(int i = 0; i < items.length; i++){
            MenuItem menuItem = new MenuItem(items[i]);
            menuItem.setId(ids[i]);
            list.add(menuItem);
        }
        
        return list;
    }
    
    
    public static void changeToDep(int current, int newType) throws IOException {
        
        if(current == HARDWARE){
            fromHardwareTO(newType);
        }else if(current == SOFTWARE){
            fromSoftwareTo(newType);
        }else if(current == PRINTER){
            fromPrinter(newType);
        }else if(current == OUTER_FIX){
            fromOuterFixTo(newType);
        }
        
    }

    private  static void fromHardwareTO(int newType) throws IOException {
        JSONObject object = genVars.device;
        if(newType == SOFTWARE){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("phone_num", object.getString("phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("win7", object.getString("win7"))
                    .add("win8", object.getString("win8"))
                    .add("win10", object.getString("win10"))
                    .add("check_all", object.getString("check_all"))
                    .add("software", object.getString("software"))
                    .add("price", object.getString("price"))
                    .add("battery", object.getString("battery"))
                    .add("charger", object.getString("charger"))
                    .add("bag", object.getString("bag"))
                    .add("cd", object.getString("cd"))
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other"))
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_SOFTWARE;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم السوفت وير");
                del("hardware_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == PRINTER){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("phone_num", object.getString("phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("check_all", object.getString("check_all"))
                    .add("fill_printer","0")
                    .add("change_dram", "0")
                    .add("cable_power", "0")
                    .add("cable_data", "0")
                    .add("price", object.getString("price"))
                    .add("cd", object.getString("cd"))
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other"))
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_PRINTER;

            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم الطابعات");
                del("hardware_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == OUTER_FIX){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("customer_phone_num", object.getString("phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("win7", object.getString("win7"))
                    .add("win8", object.getString("win8"))
                    .add("win10", object.getString("win10"))
                    .add("check_all", object.getString("check_all"))
                    .add("software", object.getString("software"))
                    .add("hardware", object.getString("hardware"))
                    .add("battery", object.getString("battery"))
                    .add("charger", object.getString("charger"))
                    .add("fill_printer", "0")
                    .add("change_dram", "0")
                    .add("cable_power", "0")
                    .add("cable_data", "0")
                    .add("bag", object.getString("bag"))
                    .add("cd", object.getString("cd"))
                    .add("rep_name", "")
                    .add("rep_phone_num", "")
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other"))
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("price", object.getString("price"))
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_OUTER_FIX;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم  الصيانه الخارجيه");
                del("hardware_fix");
            }
        }else if(newType == HARDWARE){
            AlertMSG("لا يمكن التحويل لنفس القسم");
        }
    }
    
    
    private  static void fromSoftwareTo(int newType) throws IOException {
        JSONObject object = genVars.device;
        if(newType == HARDWARE){
            RequestBody formBody = new FormBody.Builder()
                .add("device_num", object.getString("device_num"))
                .add("customer_name", object.getString("customer_name"))
                .add("phone_num", object.getString("phone_num"))
                .add("prob", object.getString("prob"))
                .add("win7", object.getString("win7"))
                .add("win8", object.getString("win8"))
                .add("win10", object.getString("win10"))
                .add("check_all", object.getString("check_all"))
                .add("software", object.getString("software"))
                .add("hardware", "0")
                .add("battery", object.getString("battery"))
                .add("charger", object.getString("charger"))
                .add("price", object.getString("price"))
                .add("bag", object.getString("bag"))
                .add("cd", object.getString("cd"))
                .add("date_time", object.getString("date_time"))
                .add("other", object.getString("other"))
                .add("device_name", object.getString("device_name"))
                .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                .build();
            String url = APIRequests.INSERT_HARDWARE;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم الهارد وير");
                del("software_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == PRINTER){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("phone_num", object.getString("phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("check_all", object.getString("check_all"))
                    .add("fill_printer","0")
                    .add("change_dram", "0")
                    .add("cable_power", "0")
                    .add("cable_data", "0")
                    .add("price", object.getString("price"))
                    .add("cd", object.getString("cd"))
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other"))
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_PRINTER;

            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم الطابعات");
                del("software_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == OUTER_FIX){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("customer_phone_num", object.getString("phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("win7", object.getString("win7"))
                    .add("win8", object.getString("win8"))
                    .add("win10", object.getString("win10"))
                    .add("check_all", object.getString("check_all"))
                    .add("software", object.getString("software"))
                    .add("hardware", "0")
                    .add("battery", object.getString("battery"))
                    .add("charger", object.getString("charger"))
                    .add("fill_printer", "0")
                    .add("change_dram", "0")
                    .add("cable_power", "0")
                    .add("cable_data", "0")
                    .add("bag", object.getString("bag"))
                    .add("cd", object.getString("cd"))
                    .add("rep_name", "")
                    .add("rep_phone_num", "")
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other"))
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("price", object.getString("price"))
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_OUTER_FIX;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم  الصيانه الخارجيه");
                del("software_fix");
            }
        }else if(newType == SOFTWARE){
            AlertMSG("لا يمكن التحويل لنفس القسم");
        }
    }
    
    
    private  static void fromPrinter(int newType) throws IOException {
        JSONObject object = genVars.device;
        String supp = "";
        if(object.getString("cable_power").equals("1")){
            supp = supp + "كابل باور";
        }
        if(object.getString("cable_data").equals("1")){
            supp = supp + "كابل داتا";
        }
        if(newType == HARDWARE){
            RequestBody formBody = new FormBody.Builder()
                .add("device_num", object.getString("device_num"))
                .add("customer_name", object.getString("customer_name"))
                .add("phone_num", object.getString("phone_num"))
                .add("prob", object.getString("prob"))
                .add("win7", "0")
                .add("win8", "0")
                .add("win10", "0")
                .add("check_all", object.getString("check_all"))
                .add("software", "0")
                .add("hardware", "0")
                .add("battery", "0")
                .add("charger", "0")
                .add("price", object.getString("price"))
                .add("bag", "0")
                .add("cd", object.getString("cd"))
                .add("date_time", object.getString("date_time"))
                .add("other", object.getString("other") + supp)
                .add("device_name", object.getString("device_name"))
                .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                .build();
            String url = APIRequests.INSERT_HARDWARE;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم الهارد وير");
                del("printer_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == SOFTWARE){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("phone_num", object.getString("phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("win7", "0")
                    .add("win8", "0")
                    .add("win10", "0")
                    .add("check_all", object.getString("check_all"))
                    .add("software", "0")
                    .add("battery", "0")
                    .add("charger", "0")
                    .add("price", object.getString("price"))
                    .add("bag", "0")
                    .add("cd", object.getString("cd"))
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other") + supp)
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_SOFTWARE;

            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم السوفت وير");
                del("printer_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == OUTER_FIX){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("customer_phone_num", object.getString("phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("win7", "0")
                    .add("win8", "0")
                    .add("win10", "0")
                    .add("check_all", "0")
                    .add("software", "0")
                    .add("hardware", "0")
                    .add("battery", "0")
                    .add("charger", "0")
                    .add("fill_printer", object.getString("fill_printer"))
                    .add("change_dram", object.getString("change_dram"))
                    .add("cable_power", object.getString("cable_power"))
                    .add("cable_data", object.getString("cable_data"))
                    .add("bag", "0")
                    .add("cd", object.getString("cd"))
                    .add("rep_name", "")
                    .add("rep_phone_num", "")
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other"))
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("price", object.getString("price"))
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_OUTER_FIX;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم  الصيانه الخارجيه");
                del("printer_fix");
            }
        }else if(newType == PRINTER){
            AlertMSG("لا يمكن التحويل لنفس القسم");
        }
    }
    

    private static void del(String table) throws IOException {
        int id = genVars.device.getInt("id");
        String url = APIRequests.DELETE_DEVICE;
        RequestBody formBody = new FormBody.Builder()
                .add("id", id + "")
                .add("table", table)
                .build();
        if(database.RemoteDB.postData(url, formBody)){
            
        }else{
                
        } 
    
    }

    
    private  static void fromOuterFixTo(int newType) throws IOException {
        JSONObject object = genVars.device;
        if(newType == SOFTWARE){
            RequestBody formBody = new FormBody.Builder()
                .add("device_num", object.getString("device_num"))
                .add("customer_name", object.getString("customer_name"))
                .add("phone_num", object.getString("customer_phone_num"))
                .add("prob", object.getString("prob"))
                .add("win7", object.getString("win7"))
                .add("win8", object.getString("win8"))
                .add("win10", object.getString("win10"))
                .add("check_all", object.getString("check_all"))
                .add("software", object.getString("software"))
                .add("price", object.getString("price"))
                .add("battery", object.getString("battery"))
                .add("charger", object.getString("charger"))
                .add("bag", object.getString("bag"))
                .add("cd", object.getString("cd"))
                .add("date_time", object.getString("date_time"))
                .add("other", object.getString("other"))
                .add("device_name", object.getString("device_name"))
                .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                .build();
            String url = APIRequests.INSERT_SOFTWARE;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم السوفت وير");
                del("outer_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == PRINTER){
            RequestBody formBody = new FormBody.Builder()
                    .add("device_num", object.getString("device_num"))
                    .add("customer_name", object.getString("customer_name"))
                    .add("phone_num", object.getString("customer_phone_num"))
                    .add("prob", object.getString("prob"))
                    .add("check_all", object.getString("check_all"))
                    .add("fill_printer", object.getString("fill_printer"))
                    .add("change_dram", object.getString("change_dram"))
                    .add("cable_power", object.getString("cable_power"))
                    .add("cable_data", object.getString("cable_data"))
                    .add("price", object.getString("price"))
                    .add("cd", object.getString("cd"))
                    .add("date_time", object.getString("date_time"))
                    .add("other", object.getString("other"))
                    .add("device_name", object.getString("device_name"))
                    .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                    .build();
            String url = APIRequests.INSERT_PRINTER;

            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم الطابعات");
                del("outer_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == HARDWARE){
            RequestBody formBody = new FormBody.Builder()
                .add("device_num", object.getString("device_num"))
                .add("customer_name", object.getString("customer_name"))
                .add("phone_num", object.getString("customer_phone_num"))
                .add("prob", object.getString("prob"))
                .add("win7", object.getString("win7"))
                .add("win8", object.getString("win8"))
                .add("win10", object.getString("win10"))
                .add("check_all", object.getString("check_all"))
                .add("software", object.getString("software"))
                .add("hardware", object.getString("hardware"))
                .add("battery", object.getString("battery"))
                .add("charger", object.getString("charger"))
                .add("price", object.getString("price"))
                .add("bag", object.getString("bag"))
                .add("cd", object.getString("cd"))
                .add("date_time", object.getString("date_time"))
                .add("other", object.getString("other"))
                .add("device_name", object.getString("device_name"))
                .add("state", object.getInt("state") + "")
                    .add("device_receiver", object.getString("device_receiver"))
                    .add("device_submitter", object.getString("device_submitter"))
                    .add("date_change_dep", getDate() + "")
                .build();
            String url = APIRequests.INSERT_HARDWARE;
            if(database.RemoteDB.postData(url, formBody)){
                AlertMSG("تم التحويل لقسم الهارد وير");
                del("outer_fix");
                /*try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");
                } catch (Exception e) {
                    
                }*/
            }
        }else if(newType == OUTER_FIX){
            AlertMSG("لا يمكن التحويل لنفس القسم");
        }
    }
    
    
    private static Timestamp getDate() {
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        return date;
    }
    
    
}
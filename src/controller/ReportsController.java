package controller;

import javafx.scene.paint.*; 
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafxapp.Main;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.*;
import utils.APIRequests;

/**
 *
 * @author Ahmed
 */
public class ReportsController implements Initializable {

    private int last = 0;
    private int currentList = 0;
    private JSONArray listDev = new JSONArray();
    private boolean search = false;
    private Image red  = new Image("/res/images/red.png");
    private Image green  = new Image("/res/images/green.png");
    private Image blue  = new Image("/res/images/blue.png");
    private Image right  = new Image("/res/images/true.png");
    private int type = 0;
    
    private int HARDWARE = 0;
    private int SOFTWARE = 1;
    private int PRINTER = 2;
    private int OUTER_FIX = 3;
    @FXML
    private TextField txt_search;
    
    @FXML
    private ListView<String> list = new ListView<String>();
    
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
    private void BtnHardware(ActionEvent event) throws IOException {
        String url = APIRequests.GET_HARDWARE;
        String response = database.RemoteDB.getData(url);
        JSONObject devices = new JSONObject(response.toString());
        listDev = devices.getJSONArray("devices");
        type = HARDWARE;
        utils.genVars.type = type;
        previewDevices();
    }
    
    @FXML
    private void BtnSoftware(ActionEvent event) throws IOException {
        String url = APIRequests.GET_SOFTWARE;
        String response = database.RemoteDB.getData(url);
        JSONObject devices = new JSONObject(response.toString());
        listDev = devices.getJSONArray("devices");
        type = SOFTWARE;
        utils.genVars.type = type;
        previewDevices();
    }
    
    @FXML
    private void BtnPrinters(ActionEvent event) throws IOException {
        String url = APIRequests.GET_PRINTER;
        String response = database.RemoteDB.getData(url);
        JSONObject devices = new JSONObject(response.toString());
        listDev = devices.getJSONArray("devices");
        type = PRINTER;
        utils.genVars.type = type;
        previewDevices();

    }
    
        
    @FXML
    private void BtnOuterFix(ActionEvent event) throws IOException {
        String url = APIRequests.GET_OUTER_FIX;
        String response = database.RemoteDB.getData(url);
        JSONObject devices = new JSONObject(response.toString());
        listDev = devices.getJSONArray("devices");
        type = OUTER_FIX;
        utils.genVars.type = type;
        previewDevices();

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //getLastView();
        if(txt_search.getText().equals("") || txt_search.getText().equals(null)){
            previewDevices();
        }
        txt_search.setOnKeyReleased(event -> {
            if (!event.getCode().isModifierKey()) {
                System.out.println(txt_search.getText());
                try {
                    // search func.
                    String table = "";
                    if(type == HARDWARE){
                        table = "hardware_fix";
                    }else if(type == SOFTWARE){
                        table = "software_fix";
                    }else if(type == OUTER_FIX){
                        table = "outer_fix";
                    }else{
                        table = "printer_fix";
                    }
                    RequestBody formBody = new FormBody.Builder()
                            .add("word", txt_search.getText().toString())
                            .add("table", table)
                            .build();
                    String url_search = APIRequests.SEARCH_DEVICE;
                    String response = database.RemoteDB.searchDevices(url_search, formBody);
                    if(response != null){
                        JSONObject devices = new JSONObject(response.toString());
                        listDev = devices.getJSONArray("devices");
                        previewDevices();
                    }else{
                        list.setItems(null);
                    }
                   
                } catch (IOException ex) {
                    Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }
    
    
    private void previewDevices(){
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
                utils.genVars.device = listDev.getJSONObject(index);
                System.out.println(utils.genVars.device);
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

    
}
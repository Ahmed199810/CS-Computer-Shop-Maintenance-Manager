package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafxapp.Main;
import javafxapp.MainController;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;

/**
 *
 * @author Ahmed
 */
public class DashboardController implements Initializable {
    
    
    
    @FXML
    private Button num_hard_done; 
    
    @FXML
    private Button num_soft_done; 
        
    @FXML
    private Button num_print_done; 

    @FXML
    private Button num_hard_sub; 
    
    @FXML
    private Button num_soft_sub; 
        
    @FXML
    private Button num_print_sub; 

    @FXML
    private Button num_hard_not; 
    
    @FXML
    private Button num_soft_not; 
        
    @FXML
    private Button num_print_not; 

    @FXML
    private Button num_out_not; 
    
    @FXML
    private Button num_out_wait; 
        
    @FXML
    private Button num_out_done; 

    
    private int HARDWARE = 0;
    private int SOFTWARE = 1;
    private int PRINTER = 2;
    private int OUTER_FIX = 3;
    private int DONE = 1;
    private int NOT_DONE = 2;
    private int SUBMIT = 3;
    private int WAIT = 0;
    
    
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
    private void BtnRefresh(ActionEvent event) throws IOException {
        System.out.println("Refresh");
       
        String url_analysis = APIRequests.DEVICES_ANALYSIS;
        try {
            String response = database.RemoteDB.getData(url_analysis);
            JSONObject object = new JSONObject(response.toString());
            JSONArray analysis = object.getJSONArray("analysis");
                        
            JSONObject obj = analysis.getJSONObject(0);


            num_hard_done.setText(obj.get("num_hardware_done") + "");
            num_soft_done.setText(obj.get("num_software_done") + "");
            num_print_done.setText(obj.get("num_printer_done") + "");

            num_hard_sub.setText(obj.get("num_hardware_submitted") + "");
            num_soft_sub.setText(obj.get("num_software_submitted") + "");
            num_print_sub.setText(obj.get("num_printer_submitted") + "");

            num_hard_not.setText(obj.get("num_hardware_not") + "");
            num_soft_not.setText(obj.get("num_software_not") + "");
            num_print_not.setText(obj.get("num_printer_not") + "");   
            
            // outer fix
            num_out_done.setText(obj.get("num_out_done") + "");
            num_out_not.setText(obj.get("num_out_not") + "");
            num_out_wait.setText(obj.get("num_out_wait") + "");
            
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
  
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String url_analysis = APIRequests.DEVICES_ANALYSIS;
        try {
            String response = database.RemoteDB.getData(url_analysis);
            JSONObject object = new JSONObject(response.toString());
            JSONArray analysis = object.getJSONArray("analysis");
                        
            JSONObject obj = analysis.getJSONObject(0);

            num_hard_done.setText(obj.get("num_hardware_done") + "");
            num_soft_done.setText(obj.get("num_software_done") + "");
            num_print_done.setText(obj.get("num_printer_done") + "");

            num_hard_sub.setText(obj.get("num_hardware_submitted") + "");
            num_soft_sub.setText(obj.get("num_software_submitted") + "");
            num_print_sub.setText(obj.get("num_printer_submitted") + "");

            num_hard_not.setText(obj.get("num_hardware_not") + "");
            num_soft_not.setText(obj.get("num_software_not") + "");
            num_print_not.setText(obj.get("num_printer_not") + "");   
            
            // outer fix
            num_out_done.setText(obj.get("num_out_done") + "");
            num_out_not.setText(obj.get("num_out_not") + "");
            num_out_wait.setText(obj.get("num_out_wait") + "");   
            
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        buttonsClicks();
  

    }

    private void buttonsClicks() {
        
        num_hard_done.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_hard_done");
                try {
                    utils.genVars.type = HARDWARE;
                    getDevices("hardware_fix", DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
       });
        
        num_soft_done.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_soft_done");
                try {
                    utils.genVars.type = SOFTWARE;
                    getDevices("software_fix", DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        num_print_done.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_print_done");
                try {
                    utils.genVars.type = PRINTER;
                    getDevices("printer_fix", DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        num_hard_not.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_hard_not");
                try {
                    utils.genVars.type = HARDWARE;
                    getDevices("hardware_fix", NOT_DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        num_soft_not.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_soft_not");
                try {
                    utils.genVars.type = SOFTWARE;
                    getDevices("software_fix", NOT_DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        num_print_not.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_print_not");
                try {
                    utils.genVars.type = PRINTER;
                    getDevices("printer_fix", NOT_DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        num_hard_sub.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_hard_sub");
                try {
                    utils.genVars.type = HARDWARE;
                    getDevices("hardware_fix", WAIT);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        num_soft_sub.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_soft_sub");
                try {
                    utils.genVars.type = SOFTWARE;
                    getDevices("software_fix", WAIT);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        num_print_sub.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_print_sub");
                try {
                    utils.genVars.type = PRINTER;
                    getDevices("printer_fix", WAIT);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
        
        // outer Fix
            
        num_out_not.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_out_not");
                try {
                    utils.genVars.type = OUTER_FIX;
                    getDevices("outer_fix", NOT_DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        num_out_done.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_out_done");
                try {
                    utils.genVars.type = OUTER_FIX;
                    getDevices("outer_fix", DONE);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        num_out_wait.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("num_out_wait");
                try {
                    utils.genVars.type = OUTER_FIX;
                    getDevices("outer_fix", WAIT);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    private void getDevices(String table, int state) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("table", table)
                .add("state", state + "")
                .build();
            String url = APIRequests.GET_ANALYSIS;
            String response = database.RemoteDB.searchDevices(url, formBody);
            if(response != null){
                JSONObject o = new JSONObject(response.toString());
                JSONArray arr = o.getJSONArray("devices");
                utils.utils.PreviewCurrentDevices("", arr);
            }
    }


}
package javafxapp;

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
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import static javafxapp.Main.clock;
import model.User;
import utils.genVars;
import utils.utils;

/**
 *
 * @author Ahmed
 */
public class MainController implements Initializable {
    
    
    
    @FXML
    private ImageView btn_setting;
    
    @FXML
    private ImageView btn_history;
    
    @FXML
    private ImageView btn_call;
    
    @FXML
    private ImageView btn_sync;
    
    @FXML
    private ImageView btn_update;
    
    @FXML
    private Button btn_hard_fix;
        
    @FXML
    private Button btn_soft_fix;
            
    @FXML
    private Button btn_printer_fix;
    
    @FXML
    private MenuItem menu_customers_other;
    
    @FXML
    private MenuItem menu_customers_orders;
                
                
    @FXML
    private VBox clk;
    
    
    private User user;
    
    
    @FXML
    private void BtnHardwareFix(ActionEvent event) {
        System.out.println("Hardware Fix");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/HardwareFix.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
    @FXML
    private void BtnSoftwareFix(ActionEvent event) {
        System.out.println("Software Fix");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/SoftwareFix.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "");
            
        } catch (Exception e) {
            
        }
    }
    
    @FXML
    private void Btndashboard(ActionEvent event) {
        System.out.println("Control Panel");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Dashboard.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "");
            
        } catch (Exception e) {
            
        }
    }    
    
    @FXML
    private void BtnPrinterFix(ActionEvent event) {
        System.out.println("Priner Fix");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/PrinterFix.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
    @FXML
    private void BtnReports(ActionEvent event) {
        System.out.println("Reports");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Reports.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
    @FXML
    private void BtnCustomerNeeds(ActionEvent event) {
        System.out.println("BtnCustomerNeeds");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/CustomersNeeds.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
        
    
    @FXML
    private void OuterFixOrders(ActionEvent event) {
        System.out.println("BtnOuterFix");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/OuterFixOrders.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
        
    @FXML
    private void OuterFix(ActionEvent event) {
        System.out.println("BtnOuterFix");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/OuterFix.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
        
    @FXML
    private void BtnCustomers(ActionEvent event) {
        System.out.println("BtnCustomers");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Customers.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Main.setContent(root, "Student home");
            
        } catch (Exception e) {
            
        }
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //database.DatabaseHelper.makeDB();
        
        initUsersPermitions();
        
        clk.getChildren().add(clock);
        
        btn_setting.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("SETTINGS");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/Adminstrator.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");

                } catch (Exception e) {

                }
                event.consume();
            }
       });
        
        
        btn_history.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("HISTORY");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/History.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Main.setContent(root, "Student home");

                } catch (Exception e) {

                }
                event.consume();
            }
       });
        
        btn_call.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("CALL");
                
                utils.AlertMSG("للمساعده والدعم الفني تواصل معنا علي " + "\n" + "PHONE : 01113207046   OR  01020968319" + "\n" + "EMAIL : ahmed.a199810@gmail.com");
                
                event.consume();
            }
       });
        
        btn_sync.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Sync");
                try {
                    database.RemoteDB.Sync();
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
                
        btn_update.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Update");
                try {
                    database.RemoteDB.UpdatePro();
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
       });
        
        
        final KeyCombination keyCombinationShiftC = new KeyCodeCombination(
        KeyCode.F, KeyCombination.CONTROL_DOWN);
        Main.root.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if (keyCombinationShiftC.match(event)) {
                    System.out.println("CTRL + F");
                }
            }

            
        });
        
         
    }

    private void initUsersPermitions() {
        this.user = genVars.user;
        
        if(user.getType() == 0){
            // super admin
        }else if(user.getType() == 1){
            // casher
            
        }else if(user.getType() == 2){
            // maintenance engineer
            menu_customers_other.setVisible(false);
            menu_customers_orders.setVisible(false);
            btn_hard_fix.setDisable(true);
            btn_soft_fix.setDisable(true);
            btn_printer_fix.setDisable(true);
        }else if(user.getType() == 3){
            // supervisor
        }
        
        
        
    }
    
    
     
}
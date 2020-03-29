/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafxapp.Main;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;

/**
 *
 * @author Ahmed
 */
public class HistoryController   implements Initializable {
    
        
    @FXML
    private ListView<String> list = new ListView<String>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getAllDoneOrders();
        } catch (IOException ex) {
            Logger.getLogger(HistoryController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void BtnCustomerOrders(ActionEvent event) throws IOException {
        System.out.println("BtnCustomerOrders");
        getAllDoneCustomerNeeds();
    }
    
    
    @FXML
    private void BtnOuterFix(ActionEvent event) throws IOException {
        System.out.println("BtnOuterFix");
        getAllDoneOrders();
    }
    
    private void getAllDoneOrders() throws IOException{
        String url = APIRequests.GET_DONE_ORDERS;
        String response = database.RemoteDB.getData(url);
        JSONObject object = new JSONObject(response.toString());
        if(object.getInt("success") != 0){
            JSONArray arr = object.getJSONArray("orders"); // getting done orders
            LinkedList<String> strList = new LinkedList();
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                strList.add(obj.getString("customer_name") + "\n" + obj.getString("due_date_time") + "\n" + obj.getString("needs") +  "\n" + obj.getString("loc"));
            }
            ObservableList<String> items = FXCollections.observableArrayList (strList);
            list.setItems(items);
            list.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    int index = list.getSelectionModel().getSelectedIndex();
                    System.out.println(index);
                }

            });

        }
        
        
    }
    
    
        
    private void getAllDoneCustomerNeeds() throws IOException{
        String url = APIRequests.GET_ALL_DONE_CUSTOMER_NEEDS;
        String response = database.RemoteDB.getData(url);
        JSONObject object = new JSONObject(response.toString());
        if(object.getInt("success") != 0){
            JSONArray arr = object.getJSONArray("needs"); // getting done orders
            LinkedList<String> strList = new LinkedList();
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                strList.add(obj.getString("needs") + "\n" + obj.getString("customer_name") + "\n" + obj.getString("phone_num"));
            }
            ObservableList<String> items = FXCollections.observableArrayList (strList);
            list.setItems(items);
            list.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    int index = list.getSelectionModel().getSelectedIndex();
                    System.out.println(index);
                }

            });

        }
        
        
    }
}
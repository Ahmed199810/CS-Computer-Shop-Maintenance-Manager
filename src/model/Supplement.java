package model;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 *
 * @author Ahmed
 */
public class Supplement {
    
    private int battery;
    private int charger;
    private int bag;
    private int cd;
    private int cable_power;
    private int cable_data;
    private String otherSupp;
    
        
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

    public Supplement(int battery, int charger, int bag, int cd, int cable_power, int cable_data, String otherSupp) {
        this.battery = battery;
        this.charger = charger;
        this.bag = bag;
        this.cd = cd;
        this.cable_power = cable_power;
        this.cable_data = cable_data;
        this.otherSupp = otherSupp;
    }

    public Supplement() {
        this.otherSupp = other.getText().toString();
        checkCheckBoxes();
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getCharger() {
        return charger;
    }

    public void setCharger(int charger) {
        this.charger = charger;
    }

    public int getBag() {
        return bag;
    }

    public void setBag(int bag) {
        this.bag = bag;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public int getCable_power() {
        return cable_power;
    }

    public void setCable_power(int cable_power) {
        this.cable_power = cable_power;
    }

    public int getCable_data() {
        return cable_data;
    }

    public void setCable_data(int cable_data) {
        this.cable_data = cable_data;
    }

    public String getOtherSupp() {
        return otherSupp;
    }

    public void setotherSupp(String otherSupp) {
        this.otherSupp = otherSupp;
    }
    
        
    private void checkCheckBoxes() {
        if(Cbattery.isSelected()){
            battery = 1;
        }
        if(Ccharger.isSelected()){
            charger = 1;
        }
        if(Cbag.isSelected()){
            bag = 1;
        }
        if(Ccd.isSelected()){
            cd = 1;
        }
        
    }
    
}

package model;

import java.io.IOException;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

/**
 *
 * @author Ahmed
 */
public class Customer {
    
    private String id;
    private String name;
    private String phone;
    private String sec_phone;
    private String address;
        
    

    public Customer(String id, String name, String phone, String sec_phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.sec_phone = sec_phone;
        this.address = address;
    }

    public Customer(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSec_phone() {
        return sec_phone;
    }

    public void setSec_phone(String sec_phone) {
        this.sec_phone = sec_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
    
}
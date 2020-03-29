package model;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 * @author Ahmed
 */
public class Device {
    
    private String id;
    private String deviceName;
    private String device_num; // CODE OF DEVICE
    private Customer customer;
    private DeviceProblem deviceProblem;
    private Supplement supplement;
    private int state;
    


    public Device(String id, String deviceName, String device_num, Customer customer, DeviceProblem deviceProblem, Supplement supplement, int state) {
        this.id = id;
        this.deviceName = deviceName;
        this.device_num = device_num;
        this.customer = customer;
        this.deviceProblem = deviceProblem;
        this.supplement = supplement;
        this.state = state;
    }

    public Device() {
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevice_num() {
        return device_num;
    }

    public void setDevice_num(String device_num) {
        this.device_num = device_num;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public DeviceProblem getDeviceProblem() {
        return deviceProblem;
    }

    public void setDeviceProblem(DeviceProblem deviceProblem) {
        this.deviceProblem = deviceProblem;
    }

    public Supplement getSupplement() {
        return supplement;
    }

    public void setSupplement(Supplement supplement) {
        this.supplement = supplement;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
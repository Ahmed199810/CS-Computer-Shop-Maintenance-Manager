/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 *
 * @author Ahmed
 */
public class DeviceProblem {
    
    private int win7;
    private int win8;
    private int win10;
    private int software;
    private int hardware;
    private int check_all;
    private int fill_printer;
    private int change_dram;
    private String prob;
    
    

    public DeviceProblem() {
       
    }

    public DeviceProblem(int win7, int win8, int win10
            , int software, int hardware
            , int check_all, int fill_printer
            , int change_dram, String prob) {
        this.win7 = win7;
        this.win8 = win8;
        this.win10 = win10;
        this.software = software;
        this.hardware = hardware;
        this.check_all = check_all;
        this.fill_printer = fill_printer;
        this.change_dram = change_dram;
        this.prob = prob;
    }

    public int getWin7() {
        return win7;
    }

    public void setWin7(int win7) {
        this.win7 = win7;
    }

    public int getWin8() {
        return win8;
    }

    public void setWin8(int win8) {
        this.win8 = win8;
    }

    public int getWin10() {
        return win10;
    }

    public void setWin10(int win10) {
        this.win10 = win10;
    }

    public int getSoftware() {
        return software;
    }

    public void setSoftware(int software) {
        this.software = software;
    }

    public int getHardware() {
        return hardware;
    }

    public void setHardware(int hardware) {
        this.hardware = hardware;
    }

    public int getCheck_all() {
        return check_all;
    }

    public void setCheck_all(int check_all) {
        this.check_all = check_all;
    }

    public int getFill_printer() {
        return fill_printer;
    }

    public void setFill_printer(int fill_printer) {
        this.fill_printer = fill_printer;
    }

    public int getChange_dram() {
        return change_dram;
    }

    public void setChange_dram(int change_dram) {
        this.change_dram = change_dram;
    }

    public String getProb() {
        return prob;
    }

    public void setProb(String prob) {
        this.prob = prob;
    }
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import controller.auth.LoginController;

/**
 *
 * @author Ahmed
 */
public class APIRequests {
    
    public static String host = LoginController.host_ip;
    public static String host_online = LoginController.host_ip_online;

    // sync to online
    //public static final String SYNC_ONLINE_QUERY = "http://path_to_online_server/syncQuery.php";

    // sync to online
    public static final String SYNC_ONLINE_QUERY = "http://"+ host_online +"/syncQuery.php";
    
    // update Pro
    public static final String UPDATE_PRO = "http://"+ host +"/javafxApp/updatePro.php";
    
    // Queue Queries
    public static final String INSERT_QUEUE_QUERY = "http://"+ host +"/javafxApp/insertQueueQuery.php";
    public static final String GET_QUEUE_QUERY = "http://"+ host +"/javafxApp/getQueueQuery.php";
    public static final String RESET_QUERY = "http://"+ host +"/javafxApp/resetQueries.php";
    
    // Devices
    public static final String INSERT_HARDWARE = "http://"+ host +"/javafxApp/insertHardwareFix.php";
    public static final String INSERT_SOFTWARE = "http://"+ host +"/javafxApp/insertSoftwareFix.php";
    public static final String INSERT_PRINTER = "http://"+ host +"/javafxApp/insertPrinterFix.php";
    public static final String GET_HARDWARE = "http://"+ host +"/javafxApp/getHardwareFix.php";
    public static final String GET_SOFTWARE = "http://"+ host +"/javafxApp/getSoftwareFix.php";
    public static final String GET_PRINTER = "http://"+ host +"/javafxApp/getPrinterFix.php";
    public static final String UPDATE_DEVICE = "http://"+ host +"/javafxApp/updateDevice.php";
    public static final String UPDATE_HARDWARE = "http://"+ host +"/javafxApp/updateHardware.php";
    public static final String UPDATE_SOFTWARE = "http://"+ host +"/javafxApp/updateSoftware.php";
    public static final String UPDATE_PRINTER = "http://"+ host +"/javafxApp/updatePrinter.php";
    public static final String DELETE_DEVICE = "http://"+ host +"/javafxApp/deleteDevice.php";
    
    // Outer Fix
    public static final String INSERT_OUTER_FIX = "http://"+ host +"/javafxApp/insertOuterFix.php";
    public static final String GET_OUTER_FIX = "http://"+ host +"/javafxApp/getOuterFix.php";
    public static final String UPDATE_OUTER_FIX = "http:/"+ host +"/javafxApp/updateOuterFix.php";
    
    
    //Analysis
    public static final String DEVICES_ANALYSIS = "http://"+ host +"/javafxApp/analysis.php";
    public static final String GET_ANALYSIS = "http://"+ host +"/javafxApp/getAnalysis.php";
    
    // Search
    public static final String SEARCH_DEVICE = "http://"+ host +"/javafxApp/searchDevice.php";
    public static final String GENERAL_SEARCH = "http://"+ host +"/javafxApp/generalSearch.php";
    
    // Login
    public static final String LOGIN_USER = "http://"+ host + "/javafxApp/loginUser.php";
    public static final String LOGIN_ADMIN = "http://"+ host +"/javafxApp/loginAdmin.php";
    
    // Users
    public static final String INSERT_USER = "http://"+ host +"/javafxApp/insertUser.php";
    public static final String GET_ALL_USERS = "http://"+ host +"/javafxApp/getAllUsers.php";
    
    // Outer Fix
    public static final String INSERT_NEED = "http://"+ host +"/javafxApp/insertNeed.php";
    public static final String GET_ALL_ORDERS = "http://"+ host +"/javafxApp/getAllOrders.php";
    public static final String UPDATE_ORDER = "http://"+ host +"/javafxApp/updateNeed.php";
    public static final String GET_DONE_ORDERS = "http://"+ host +"/javafxApp/getDoneOrders.php";
    
    // Customer
    public static final String GET_CUSTOMER = "http://"+ host +"/javafxApp/checkCustomerExist.php";
    public static final String INSERT_CUSTOMER = "http://"+ host +"/javafxApp/insertCustomer.php";
    public static final String GET_ALL_CUSTOMERS = "http://"+ host +"/javafxApp/getAllCustomers.php";
    public static final String UPDATE_CUSTOMER = "http://"+ host +"/javafxApp/updateCustomer.php";

    
    // Customer Needs
    public static final String INSERT_CUSTOMER_NEED = "http://"+ host +"/javafxApp/insertCustomerNeed.php";
    public static final String GET_ALL_CUSTOMER_NEEDS = "http://"+ host +"/javafxApp/getAllCustomerNeeds.php";
    public static final String UPDATE_CUSTOMER_NEED = "http://"+ host +"/javafxApp/updateCustomerNeeds.php";
    public static final String GET_ALL_DONE_CUSTOMER_NEEDS = "http://"+ host +"/javafxApp/getAllDoneCustomerNeeds.php";

    public static final String INCREMENT_DEVICE_COUNT = "http://"+ host +"/javafxApp/incrementDeviceCount.php";
    public static final String GET_DEVICE_COUNT = "http://"+ host +"/javafxApp/getDeviceCount.php";

    
    
}
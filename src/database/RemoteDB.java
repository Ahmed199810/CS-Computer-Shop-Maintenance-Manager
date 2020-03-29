package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafxapp.MainController;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;

/**
 *
 * @author Ahmed
 */
public class RemoteDB {
    
    private static JSONArray searchArr;
    private static OkHttpClient client = new OkHttpClient();  
    private static List<String> list_udates = new ArrayList<>();
    
    
    public static boolean checkInternetConnection(){
        try { 
            URL url = new URL("https://www.google.com"); 
            URLConnection connection = url.openConnection(); 
            connection.connect(); 
  
            System.out.println("Connection Successful"); 
            return true;
        } 
        catch (Exception e) { 
            System.out.println("Internet Not Connected"); 
            return false;
        } 
    }

    
    public static String searchDevices(String url, RequestBody formBody) throws MalformedURLException, IOException{
                
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String str = response.body().string();
        System.out.println(str);
        JSONObject o = new JSONObject(str);
        int success = (int) o.get("success");
        if(success == 1){
            return str;
        }else{
            return null;
        }
    }
    

    public static boolean postData(String url, RequestBody formBody) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String str = response.body().string();
        System.out.println(str);
        JSONObject o = new JSONObject(str);
        int success = (int) o.get("success");
        if(success == 1){
            // save to file
            System.out.println("Sent OFFLINE");
            String query = o.getString("query");
            System.out.println("HI " + query);
            // add to query text file
            queueQuery(query);
            
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean postQuery(String url, RequestBody formBody) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String str = response.body().string();
        System.out.println(str);
        JSONObject o = new JSONObject(str);
        int success = (int) o.get("success");
        if(success == 1){
            return true;
        }else{
            return false;
        }
    }
    
    public static String checkDataExist(String url, String word, String param) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("word", word)
                .add("param", param)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    
        
    public static String getData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    
        
    public static String loginUser(String url, RequestBody formBody) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String str = response.body().string();
        System.out.println(str);
        JSONObject o = new JSONObject(str);
        int success = (int) o.get("success");
        if(success == 1){
            return str;
        }else{
            return null;
        }
    }
    
    
    private static void queueQuery(String query) throws FileNotFoundException, IOException {
        
        //String content = query;
        /*File file = new File("query.txt");

        try (FileWriter writer = new FileWriter(file, true);
              BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(content + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        RequestBody formBody = new FormBody.Builder()
                .add("query", query)
                .build();
        String url = APIRequests.INSERT_QUEUE_QUERY;
        
        if(postQuery(url, formBody)){
            System.out.println("sync offline");
        }else{
            System.out.println("Failed to sync offline");
        }
        
    }




    private static LinkedList getQuery() throws FileNotFoundException, IOException {
        
        LinkedList<String> list = new LinkedList<>();
        /*File file = new File("query.txt");
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        if(line != null){
            list.add(line);
        }
        while(line != null){
            //sb.append(line);
            line = br.readLine();
            if(line != null){
                list.add(line);
            }
        }*/
        
        String url = APIRequests.GET_QUEUE_QUERY;
        String response = getData(url);
        JSONObject o = new JSONObject(response.toString());
        if(o.getInt("success") != 0){
            JSONArray arr = o.getJSONArray("queries");
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                list.add(obj.getString("query"));
            }
        }
        
        return list;
    }
    
    private static void syncToOnline() throws IOException {
        LinkedList<String> queries = getQuery();
        System.out.println(Arrays.asList(queries));
        for(int i = 0; i < queries.size(); i++){
            RequestBody formBody = new FormBody.Builder()
                    .add("query", queries.get(i))
                    .build();
            String url = APIRequests.SYNC_ONLINE_QUERY;
            if(postQuery(url, formBody)){
                System.out.println("Sync to ONLINE SUCCESS !");
            }else{
                System.out.println("ERROR");
            }
        }
        
        resetQueriesFile();
    }

    private static void resetQueriesFile() throws IOException {
        /*File file = new File(APIRequests.host + ":3306/C:/" + "query.txt");
        try (FileWriter writer = new FileWriter(file);
              BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        
        RequestBody formBody = new FormBody.Builder()
                .add("query", "test")
                .build();
        String url = APIRequests.RESET_QUERY;
        if(postQuery(url, formBody)){
            System.out.println("Reset Success");
            utils.utils.AlertMSG("تمت المزامنه علي الانترنت");
        }else{
            System.out.println("Reset ERORR");
            utils.utils.AlertMSG(" عفوا لم تتم المزامنه علي الانترنت");
        }
    }
    
    
    
    public static void Sync() throws IOException{
        if(checkInternetConnection()){
            syncToOnline();
        }else{
            utils.utils.AlertMSG("NO INTERNET CONNECTION !");
        }
    }
    
    
    
    public static boolean checkIsSync() throws FileNotFoundException, IOException{
        File file = new File(APIRequests.host + ":3306/" + "query.txt");
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        int size = br.readLine().length();
        if(size > 0){
            return false;
        }else{
            return true;
        }
        
    }
    
    
    public static void UpdatePro() throws IOException{
        getUpdates();
        String url = APIRequests.UPDATE_PRO;
        for(int i = 0; i < list_udates.size(); i++){
            RequestBody formBody = new FormBody.Builder()
                .add("query", list_udates.get(i))
                .build();
            if(postQuery(url, formBody)){
                System.out.println("Reset Success");
                utils.utils.AlertMSG("تم التحديث الي اخر اصدار");
                
            }else{
                System.out.println("Reset ERORR");
                utils.utils.AlertMSG("عفوا لم يتم التحديث");
            }
        }
        
    }
    
    
    private static void getUpdates(){
        list_udates.add("ALTER TABLE `hardware_fix` ADD `device_receiver` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `price`,ADD `device_submitter` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_receiver`,ADD `date_change_dep` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_submitter`;");
        list_udates.add("ALTER TABLE `software_fix` ADD `device_receiver` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `price`,ADD `device_submitter` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_receiver`,ADD `date_change_dep` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_submitter`;");
        list_udates.add("ALTER TABLE `printer_fix` ADD `device_receiver` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `price`,ADD `device_submitter` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_receiver`,ADD `date_change_dep` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_submitter`;");
        list_udates.add("ALTER TABLE `outer_fix` ADD `device_receiver` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `price`,ADD `device_submitter` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_receiver`,ADD `date_change_dep` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `device_submitter`;");
    }
    
}
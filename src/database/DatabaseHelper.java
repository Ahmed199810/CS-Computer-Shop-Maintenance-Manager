package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.APIRequests;


/**
 *
 * @author Ahmed
 */
public class DatabaseHelper {

    private static int counter;
    
    public DatabaseHelper() throws IOException{
        getCount();
    }
    
    public static void updateCount(int counter) throws IOException{      
            
        RequestBody formBody = new FormBody.Builder()
                .add("count", counter + "")
                .build();
        String url = APIRequests.INCREMENT_DEVICE_COUNT;
            
        if(database.RemoteDB.postData(url, formBody)){
            System.out.println("Incremented successfully");
        }
        
    }
    
    
    
     public static void getCount() throws IOException{      
        
        String url = APIRequests.GET_DEVICE_COUNT;
        String response = RemoteDB.getData(url);
        
        JSONObject o = new JSONObject(response.toString());
        JSONArray arr = o.getJSONArray("nums");
        counter = arr.getJSONObject(0).getInt("count");
        System.out.println("Device Recorded successfully");
        
     }

    public static int getCounter() {
        return counter;
    }
    
    
}
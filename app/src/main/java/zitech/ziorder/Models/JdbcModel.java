package zitech.ziorder.Models;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import zitech.ziorder.Activities.SettingActivity;
import zitech.ziorder.Objects.JdbcObject;

@SuppressLint("NewApi")
public class JdbcModel {
    public Connection getConnectionOf() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection objConn = null;
        String sConnURL = "";

        JdbcObject objEntity = new JdbcObject(SettingActivity.destinationIP, "ziorder", "1", "QLCP", "1433");
        try {
            Class.forName(objEntity.getsClass()).newInstance();
            //jdbc:jtds:sqlserver://192.168.1.104:1433/QLCP;user=ziorder;password=1
            sConnURL = "jdbc:jtds:sqlserver://"
                    + objEntity.getsServerName() + ":" + objEntity.getsPort() + "/" + objEntity.getsDatabase()
                    + ";user=" + objEntity.getsUserId()
                    + ";password=" + objEntity.getsPwd();
            objConn = DriverManager.getConnection(sConnURL);

        } catch (SQLException se) {
            Log.e("ERRO1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO2", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO3", e.getMessage());
        }
        return objConn;
    }
}

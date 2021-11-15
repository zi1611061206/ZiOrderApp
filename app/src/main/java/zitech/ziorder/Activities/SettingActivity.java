package zitech.ziorder.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import zitech.ziorder.R;

public class SettingActivity extends AppCompatActivity {

    EditText edtIpAdress, edtIpDestination;
    Button btnSetIP;

    public static String destinationIP = "192.168.1.106";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        AddViewById();
        AddEvent();
        ShowIpAddress();
    }

    private void AddEvent() {
        btnSetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDestinationIP();
            }

            private void SetDestinationIP() {
                String ipInput = edtIpDestination.getText().toString().trim();
                if (ipInput.equals("")){
                    Toast.makeText(SettingActivity.this, getString(R.string.full_fill), Toast.LENGTH_LONG).show();
                }
                else {
                    destinationIP = ipInput;
                    Toast.makeText(SettingActivity.this, getString(R.string.success), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ShowIpAddress() {
        String ip1 = getLocalIpAddress();
        String ip2 = getLocalIpAddress2();
        String ip3 = getLocalIpAddress3();

        if (ip2 != null){
            edtIpAdress.setText(ip2);
        }else if (ip3 != null){
            edtIpAdress.setText(ip3);
        }else if (ip1!=null) {
            edtIpAdress.setText(ip1);
        }else{
            edtIpAdress.setText(getString(R.string.connection_fail));
        }
    }

    private void AddViewById() {
        edtIpAdress = findViewById(R.id.edittext_ip);
        edtIpDestination = findViewById(R.id.edittext_set_destination_ip);
        btnSetIP = findViewById(R.id.button_set_ip);
    }

    @SuppressWarnings("deprecation")
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements();) {
                NetworkInterface intf = enumeration.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getLocalIpAddress2() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    public String getLocalIpAddress3(){
        WifiManager wifiMan = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiMan != null;
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
    }
}

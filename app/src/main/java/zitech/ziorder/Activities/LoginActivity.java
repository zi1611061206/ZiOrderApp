package zitech.ziorder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import zitech.ziorder.Models.AccountModel;
import zitech.ziorder.R;

public class LoginActivity extends AppCompatActivity {

    //khai bao bien toan cuc
    EditText edtUsername, edtPassword;
    Button btnLogin;
    ImageButton imbScanUser;
    public static String curentAccount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AddViewById();
        AddEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu_setting:
                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.item_menu_contact:
                Intent intentContact = new Intent(this, ContactActivity.class);
                startActivity(intentContact);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }

            private void Login() {
                String username = String.valueOf(edtUsername.getText());
                String password = String.valueOf(edtPassword.getText());
                if (username.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this, getString(R.string.full_fill), Toast.LENGTH_LONG).show();
                }else {
                    AccountModel accountModel = new AccountModel();
                    try {
                        if (accountModel.CheckLoginInfo(username, password)){
                            curentAccount = username;
                            Intent intent=new Intent(LoginActivity.this, TableActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_LONG).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        imbScanUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanUser();
            }

            private void ScanUser() {
                Intent intent = new Intent(LoginActivity.this, ScanAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AddViewById() {
        edtUsername=findViewById(R.id.edittext_username_login);
        edtPassword=findViewById(R.id.edittext_password_login);
        btnLogin=findViewById(R.id.button_login);
        imbScanUser = findViewById(R.id.imagebutton_scan_user);
    }
}

package zitech.ziorder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import zitech.ziorder.R;

public class ManagerActivity extends AppCompatActivity {

    ListView lvManager;
    List<String> managerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        AddViewById();
        LoadManager();
        AddEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu_account:
                Intent intentAccount = new Intent(this, AccountActivity.class);
                startActivity(intentAccount);
                break;
            case R.id.item_menu_setting:
                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.item_menu_contact:
                Intent intentContact = new Intent(this, ContactActivity.class);
                startActivity(intentContact);
                break;
            case R.id.item_menu_exit:
                Intent intentExit = new Intent(this, LoginActivity.class);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentExit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddViewById() {
        lvManager = findViewById(R.id.listview_manager);
    }

    private void AddEvent() {
        lvManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intentAddDishes = new Intent(ManagerActivity.this, AddDishesActivity.class);
                        startActivity(intentAddDishes);
                        break;
                    case 1:
                        Intent intentAddTable = new Intent(ManagerActivity.this, AddTableActivity.class);
                        startActivity(intentAddTable);
                        break;
                }
            }
        });
    }

    private void LoadManager() {
        managerList = new ArrayList<>();
        managerList.add(getString(R.string.add_dish));
        managerList.add(getString(R.string.add_table));
        ArrayAdapter managerAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, managerList);
        lvManager.setAdapter(managerAdapter);
    }
}

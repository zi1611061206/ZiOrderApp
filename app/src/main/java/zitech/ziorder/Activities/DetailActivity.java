package zitech.ziorder.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import zitech.ziorder.Models.MenuModel;
import zitech.ziorder.Objects.Menu;
import zitech.ziorder.R;

public class DetailActivity extends AppCompatActivity {

    //Khai bao bien toan cuc
    ImageView imgDishes;
    TextView tvDishName, tvDishesPrice, tvDishesDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        AddViewById();
        SetContent();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
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

    private void SetContent() {
        Intent intent=getIntent();
        int dishId = intent.getIntExtra("dishId_select", 0);
        MenuModel menuModel=new MenuModel();
        Menu dishDetail = null;
        try {
            dishDetail = menuModel.GetDishDetail(dishId);
            imgDishes.setImageBitmap(ConvertStringToImage(dishDetail));
            tvDishName.setText(dishDetail.getDishName());
            tvDishesPrice.setText(dishDetail.getPrice()+"");
            tvDishesDescription.setVisibility(View.GONE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Bitmap ConvertStringToImage(Menu menu) {
        byte[] imageBytes = menu.getImage();
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    private void AddViewById() {
        imgDishes = findViewById(R.id.image_dishes_detail);
        tvDishName = findViewById(R.id.textview_dishes_name_detail);
        tvDishesPrice = findViewById(R.id.textview_dishes_price_detail);
        tvDishesDescription = findViewById(R.id.textview_dishes_description_detail);
    }
}

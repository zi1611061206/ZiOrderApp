package zitech.ziorder.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

import zitech.ziorder.Adapters.CategorySpinnerAdapter;
import zitech.ziorder.Controller.BitmapResolver;
import zitech.ziorder.Models.CategoryModel;
import zitech.ziorder.Objects.CategorySpinner;
import zitech.ziorder.R;
import zitech.ziorder.Threads.AddDishesAsyncTask;

public class AddDishesActivity extends AppCompatActivity {
    EditText edtDishesName, edtPrice;
    Spinner spCategory;
    TextView tvError;
    Button btnCancel, btnAdd, btnChooseImage;
    ImageView imgPreview;

    ArrayList<CategorySpinner> categoryArrayList=new ArrayList<>();
    CategorySpinnerAdapter categoryAdapter;

    int categoryId;
    Bitmap dishesImage;
    int PICK_IMAGE = 2019;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_dish);

        categoryId = 0;
        dishesImage = BitmapFactory.decodeResource(getResources(), R.drawable.unnamed);
        AddViewById();
        try {
            LoadCategorySpinner();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AddEvent();
    }

    private void AddEvent() {
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = categoryArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }

            private void Back() {
                this.Back();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dishesName = edtDishesName.getText().toString().trim();
                String dishesPrice = edtPrice.getText().toString().trim();
                if (dishesName.equals("") | dishesPrice.equals("")){
                    tvError.setText(getString(R.string.full_fill));
                }else if (categoryId==0){
                    tvError.setText(getString(R.string.require_category));
                }else {
                    float price = Float.parseFloat(dishesPrice);
                    AddDishes(categoryId, dishesName, price, dishesImage);
                }
            }

            private void AddDishes(int categoryId, String dishesName, float price, Bitmap dishesImage) {
                new AddDishesAsyncTask(AddDishesActivity.this, categoryId,price, dishesImage, dishesName).execute();
            }
        });
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }

            private void ChooseImage() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Chọn ảnh "),PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            Bitmap photo = BitmapResolver.getBitmap(this.getContentResolver(), imageUri);
            imgPreview.setImageBitmap(photo);
            imgPreview.setVisibility(View.VISIBLE);
            dishesImage = photo;
        }
    }

    private void LoadCategorySpinner() throws SQLException {
        categoryArrayList.clear();
        categoryArrayList = new CategoryModel().GetCategorySpinnerList();
        categoryAdapter = new CategorySpinnerAdapter(AddDishesActivity.this,R.layout.item_category_spinner,categoryArrayList);
        this.spCategory.setAdapter(categoryAdapter);
    }

    private void AddViewById() {
        edtDishesName = findViewById(R.id.edittext_dishes_name_dialog);
        edtPrice = findViewById(R.id.edittext_dishes_price_dialog);
        spCategory = findViewById(R.id.spinner_Category);
        tvError = findViewById(R.id.textview_error_add_dishes);
        btnCancel = findViewById(R.id.button_cancel_add_dishes_dialog);
        btnAdd = findViewById(R.id.button_confrim_add_dishes_dialog);
        btnChooseImage = findViewById(R.id.button_choose_image_dishes_dialog);
        imgPreview = findViewById(R.id.imageview_dish_preview);
    }
}

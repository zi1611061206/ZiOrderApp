package zitech.ziorder.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import zitech.ziorder.Adapters.CartAdapter;
import zitech.ziorder.Adapters.CategoryAdapter;
import zitech.ziorder.Adapters.MenuAdapter;
import zitech.ziorder.Models.CategoryModel;
import zitech.ziorder.Models.MenuModel;
import zitech.ziorder.Models.SQLiteModel;
import zitech.ziorder.Objects.Cart;
import zitech.ziorder.Objects.Category;
import zitech.ziorder.Objects.Menu;
import zitech.ziorder.R;
import zitech.ziorder.Threads.CartAsyncTask1;
import zitech.ziorder.Threads.CartAsyncTask2;

public class MenuActivity extends AppCompatActivity {

    //khai bao bien toan cuc
    TabHost tabhost;
    RecyclerView rvMenu;
    ListView lvCategory;
    ImageButton imbReload, imbAddCart;

    List<Category> categoryList;
    ArrayList<Menu> menuList;
    int tableId = 0;
    int status = 0;
    public static SQLiteModel sqLiteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        AddViewById();
        AddEvent();
        SetTabHost();

        LoadCategory();
        LoadMenu(0);

        Intent intentGet = getIntent();
        tableId = intentGet.getIntExtra("tableId_select", 0);
        status = intentGet.getIntExtra("tableStatus_select", 0);

        sqLiteModel = new SQLiteModel(this);
//        sqLiteModel.DeleteCart();
        sqLiteModel.InitCart();
        sqLiteModel.ClearData();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu_search:
                try {
                    ShowSearchDialog();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
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

    private void ShowSearchDialog() throws SQLException {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_search_dishes);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setLayout(width*7/8, height*3/5);

        final EditText edtSearchBox = dialog.findViewById(R.id.edittext_search_dishes);
        Button btnSearch = dialog.findViewById(R.id.button_search_dishes);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    menuList = new MenuModel().SearchTableByName(edtSearchBox.getText().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    private void AddEvent() {
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectOneCategoryItem(position);
            }

            private void SelectOneCategoryItem(int position) {
                Category category = categoryList.get(position);
                int categoryID = category.getId();
                LoadMenu(categoryID);
                tabhost.setCurrentTab(1);
            }
        });
        imbReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMenu(0);
            }
        });
        imbAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCart();
            }

            private void AddCart() {
                ShowCartDialog(tableId);
            }
        });
    }

    private void LoadMenu(int id) {
        try {
            rvMenu.setHasFixedSize(true);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false);
            rvMenu.setLayoutManager(layoutManager);

            MenuModel menuModel = new MenuModel();
            if (id == 0) {
                menuList = menuModel.GetMenuList();
            } else {
                menuList = menuModel.GetMenuListByCategoryId(id);
            }

            MenuAdapter menuAdapter = new MenuAdapter(this, R.layout.item_menu, menuList);
            rvMenu.setAdapter(menuAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void LoadCategory() {
        try {
            CategoryModel categoryModel = new CategoryModel();
            categoryList = categoryModel.GetCategoryList();
            CategoryAdapter categoryAdapter = new CategoryAdapter(this, R.layout.item_category, categoryList);
            lvCategory.setAdapter(categoryAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void SetTabHost() {
        tabhost.setup();
        //tab danh muc
        TabHost.TabSpec tabCategory = tabhost.newTabSpec("tabCategory");
        tabCategory.setIndicator(getString(R.string.category));
        tabCategory.setContent(R.id.content_category);
        tabhost.addTab(tabCategory);
        //tab thuc don
        TabHost.TabSpec tabMenu = tabhost.newTabSpec("tabMenu");
        tabMenu.setIndicator(getString(R.string.dishes));
        tabMenu.setContent(R.id.content_menu);
        tabhost.addTab(tabMenu);
    }

    private void AddViewById() {
        tabhost = findViewById(R.id.tabhost_menu);
        lvCategory = findViewById(R.id.listview_category);
        rvMenu = findViewById(R.id.recyclerview_menu);
        imbReload = findViewById(R.id.imagebutton_reload_menu);
        imbAddCart = findViewById(R.id.imagebutton_add_cart);
    }

    private void ShowCartDialog(final int tableId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_view_cart);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setLayout(width * 7 / 8, height * 4 / 5);

        ListView lvCart = dialog.findViewById(R.id.listview_cart);
        Button btnCancel = dialog.findViewById(R.id.button_cancel_cart_dialog);
        Button btnConfirm = dialog.findViewById(R.id.button_confrim_cart_dialog);

        final List<Cart> cartList = new ArrayList<>();
        final Cursor cursor = sqLiteModel.GetCartData();
        while (cursor.moveToNext()) {
            cartList.add(new Cart(cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getFloat(3),
                    cursor.getInt(4),
                    cursor.getFloat(5)));
        }

        final CartAdapter cartAdapter = new CartAdapter(MenuActivity.this, R.layout.item_cart_detail, cartList);
        cartAdapter.notifyDataSetChanged();
        lvCart.setAdapter(cartAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dissmiss();
            }

            private void Dissmiss() {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }

            private void Save(){
                if (status==0){
                    new CartAsyncTask2(MenuActivity.this, cartList, tableId).execute(cartList.size());
                }else {
                    new CartAsyncTask1(MenuActivity.this, cartList, tableId).execute(cartList.size());
                }
                Intent intent = new Intent(MenuActivity.this, TableActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }
}

package zitech.ziorder.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import zitech.ziorder.Adapters.AreaAdapter;
import zitech.ziorder.Adapters.BillDetailAdapter;
import zitech.ziorder.Adapters.TableAdapter;
import zitech.ziorder.Models.AreaModel;
import zitech.ziorder.Models.BillDetailModel;
import zitech.ziorder.Models.TableModel;
import zitech.ziorder.Objects.Area;
import zitech.ziorder.Objects.BillDetail;
import zitech.ziorder.Objects.Table;
import zitech.ziorder.R;
import zitech.ziorder.Threads.PayAsyncTask;

public class TableActivity extends AppCompatActivity {

    //khai bao bien toan cuc
    TabHost tabhost;
    ListView lvArea;
    RecyclerView rvTable;
    ImageButton imbReload, imbQrCode;

    ArrayList<Area> areaList;
    ArrayList<Table> tableList;
    TableAdapter tableAdapter = null;

    public int fullTableAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        AddViewById();
        SetTabHost();
        AddEvent();

        LoadArea();
        LoadTable(0);
        tabhost.setCurrentTab(1);

        try {
            fullTableAmount = new TableModel().CountFullTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StartTimer();
    }

    private void StartTimer() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int currentFullTableAmount = new TableModel().CountFullTable();
                                    if (currentFullTableAmount != fullTableAmount){
                                        LoadTable(0);
                                        fullTableAmount = currentFullTableAmount;
                                        Toast.makeText(TableActivity.this, getString(R.string.update), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
                intentExit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        dialog.setContentView(R.layout.dialog_search_table);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setLayout(width*7/8, height*4/5);

        final EditText edtSearchBox = dialog.findViewById(R.id.edittext_search_table);
        Button btnSearch = dialog.findViewById(R.id.button_search_table);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tableList = new TableModel().SearchTableByName(edtSearchBox.getText().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    private void AddEvent() {
        lvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectOneAreaItem(position);
            }

            private void SelectOneAreaItem(int position) {
                Area area = areaList.get(position);
                int areaID = area.getId();
                LoadTable(areaID);
                tabhost.setCurrentTab(1);
            }
        });
        imbReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadTable(0);
            }
        });
        imbQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanTable();
            }

            private void ScanTable() {
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                startActivityForResult(intent, 0);

                Intent intent = new Intent(TableActivity.this, ScanTableActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Handle successful scan
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                try {
                    int tableId = Integer.parseInt(contents);
                    Intent intent2 = new Intent(TableActivity.this, OrderActivity.class);
                    intent2.putExtra("tableId_scan", tableId);
                    startActivity(intent2);
                }catch (NumberFormatException e){
                    Toast.makeText(TableActivity.this, getText(R.string.incorrect_format), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(TableActivity.this, getText(R.string.canceled), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void LoadTable(int id) {
        try {
            rvTable.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false);
            rvTable.setLayoutManager(layoutManager);

            TableModel tableModel = new TableModel();
            if (id == 0) {
                tableList = tableModel.GetTableList();
            } else {
                tableList = tableModel.GetTableListByAreaId(id);
            }

            tableAdapter = new TableAdapter(this, R.layout.item_table, tableList);
            rvTable.setAdapter(tableAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void LoadArea() {
        try {
            AreaModel areaModel = new AreaModel();
            areaList = areaModel.GetAreaList();
            AreaAdapter areaAdapter = new AreaAdapter(this, R.layout.item_area, areaList);
            lvArea.setAdapter(areaAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void SetTabHost() {
        tabhost.setup();
        //tab khu vuc
        TabHost.TabSpec tabArea = tabhost.newTabSpec("tabArea");
        tabArea.setIndicator(getString(R.string.area));
        tabArea.setContent(R.id.content_area);
        tabhost.addTab(tabArea);
        //tab ban
        TabHost.TabSpec tabTable = tabhost.newTabSpec("tabTable");
        tabTable.setIndicator(getString(R.string.table));
        tabTable.setContent(R.id.content_table);
        tabhost.addTab(tabTable);
    }

    private void AddViewById() {
        tabhost = findViewById(R.id.tabhost_table);
        lvArea = findViewById(R.id.listview_area);
        rvTable = findViewById(R.id.recycleriew_table);
        imbReload = findViewById(R.id.imagebutton_reload_table);
        imbQrCode = findViewById(R.id.imagebutton_scan_table);
    }

    public void ShowBillDialog(String tableName, final int billId, final int tableId) throws SQLException {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_view_bill);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setLayout(width * 7 / 8, height * 4 / 5);

        TextView tvTableName = dialog.findViewById(R.id.textview_tablename_bill_dialog);
        Button btnCancel = dialog.findViewById(R.id.button_cancel_bill_dialog);
        Button btnPay = dialog.findViewById(R.id.button_pay_bill_dialog);
        ListView lvBill = dialog.findViewById(R.id.listview_bill);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }

            private void Cancel() {
                dialog.dismiss();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pay();
            }

            private void Pay() {
                try {
                    PayAlertDialog(billId, tableId);
                    dialog.dismiss();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        tvTableName.setText(tableName);

        BillDetailModel billDetailModel = new BillDetailModel();
        List<BillDetail> billDetailList = billDetailModel.GetBillDetail(billId);
        BillDetailAdapter billDetailAdapter = new BillDetailAdapter(this, R.layout.item_bill_detail, billDetailList);
        lvBill.setAdapter(billDetailAdapter);

        dialog.show();
    }

    public void PayAlertDialog(final int billId, final int tableId) throws SQLException {
        BillDetailModel billDetailModel = new BillDetailModel();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.unnamed);
        dialog.setTitle(getString(R.string.pay_confirm));
        final float total = billDetailModel.GetTotal(billId);
        dialog.setMessage(getString(R.string.total) + ": " + total);
        dialog.setCancelable(false);
        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Pay();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private void Pay() throws SQLException {
                new PayAsyncTask(TableActivity.this, billId, tableId, total).execute();
                LoadTable(0);
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Dissmiss(dialogInterface);
            }

            private void Dissmiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import zitech.ziorder.Adapters.BillDetailAdapter;
import zitech.ziorder.Models.BillDetailModel;
import zitech.ziorder.Models.BillModel;
import zitech.ziorder.Models.TableModel;
import zitech.ziorder.Objects.Bill;
import zitech.ziorder.Objects.BillDetail;
import zitech.ziorder.R;
import zitech.ziorder.Threads.PayAsyncTask;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    //khai bao bien toan cuc
    TextView tvTableNameQr;
    Button btnOrder, btnCancel, btnPay;

    String tableName;
    int areaID, status, tableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AddViewById();
        Intent intent = getIntent();
        int tableIdScan = intent.getIntExtra("tableId_scan", 0);

        try {
            DisplayTableInfo(tableIdScan);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        btnOrder.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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

    private void DisplayTableInfo(int tableIdScan) throws SQLException {
        ResultSet rs = new TableModel().CheckTableStatus(tableIdScan);
        if (rs.next()) {
            tableName = rs.getString("tenban");
            areaID = rs.getInt("makhuvuc");
            status = rs.getInt("trangthaisudung");
            tableId = rs.getInt("maban");
            if (status == 0) {
                String contents = tableName + "\n KV" + areaID + "\n" + getString(R.string.ready);
                tvTableNameQr.setText(contents);
                tvTableNameQr.setTextColor(this.getResources().getColor(R.color.green_dark));
                btnOrder.setVisibility(View.VISIBLE);
            } else {
                String contents = tableName + "\n KV" + areaID + "\n" + getString(R.string.being_used);
                tvTableNameQr.setText(contents);
                tvTableNameQr.setTextColor(this.getResources().getColor(R.color.red));
                btnOrder.setVisibility(View.VISIBLE);
                btnPay.setVisibility(View.VISIBLE);
            }
        }
    }

    private void AddViewById() {
        tvTableNameQr = findViewById(R.id.textview_qrcode);
        btnOrder = findViewById(R.id.button_qrcode_order);
        btnCancel = findViewById(R.id.button_qrcode_cancel);
        btnPay = findViewById(R.id.button_qrcode_pay);

        btnPay.setVisibility(View.GONE);
        btnOrder.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_qrcode_cancel:
                this.finish();
                break;
            case R.id.button_qrcode_order:
                Order();
                break;
            case R.id.button_qrcode_pay:
                try {
                    Pay();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void Order() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("tableId_select", tableId);
        intent.putExtra("tableStatus_select", status);
        startActivity(intent);
    }

    private void Pay() throws SQLException{
        Bill bill = new BillModel().GetBill(tableId);
        int billId = bill.getId();
        ShowBillDialog(tableName, billId, tableId);
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
                new PayAsyncTask(OrderActivity.this, billId, tableId, total).execute();
                Intent intent = new Intent(OrderActivity.this, TableActivity.class);
                startActivity(intent);
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

package zitech.ziorder.Threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.SQLException;

import zitech.ziorder.Models.BillModel;
import zitech.ziorder.Models.TableModel;
import zitech.ziorder.R;

public class PayAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progressDialog;
    private Context context;
    private int billId, tableId;
    private float total;

    public PayAsyncTask(Context context, int billId, int tableId, float total) {
        this.context = context;
        this.billId = billId;
        this.tableId = tableId;
        this.total = total;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.progress));
        progressDialog.setMessage(context.getString(R.string.handling)+"...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            new BillModel().UpdatePay(billId, total);
            if (new TableModel().UpdateStatus(tableId)){
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean){
            Toast.makeText(context, context.getString(R.string.fail), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else {
            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
}

package zitech.ziorder.Threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import zitech.ziorder.Models.BillModel;
import zitech.ziorder.Objects.Cart;
import zitech.ziorder.R;

public class CartAsyncTask1 extends AsyncTask<Integer, String, Boolean> {

    private ProgressDialog progressDialog;
    private Context context;
    private List<Cart> cartList;
    private int tableId;

    public CartAsyncTask1(Context context, List<Cart> cartList, int tableId) {
        this.context = context;
        this.cartList = cartList;
        this.tableId = tableId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.update_bill));
        progressDialog.setMessage(context.getString(R.string.update_bill_loading)+"...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        try {
            long total = 0;
            for (int i = 0; i < integers[0]; i++) {
                Cart cart = cartList.get(i);
                new BillModel().HandlingUsingTable(tableId, cart.getDishesId(), cart.getAmount());
                total+=100/integers[0];
                publishProgress(total+"");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //cập nhật tiến trình chạy
        progressDialog.setProgress(Integer.parseInt(values[0]));
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean){
            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else {
            Toast.makeText(context, context.getString(R.string.fail), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
}

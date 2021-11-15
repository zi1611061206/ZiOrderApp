package zitech.ziorder.Threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.SQLException;

import zitech.ziorder.Models.MenuModel;
import zitech.ziorder.R;

public class AddDishesAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progressDialog;
    private Context context;
    private int categoryId;
    private float price;
    private Bitmap dishesImage;
    String dishesName;

    public AddDishesAsyncTask(Context context, int categoryId, float price, Bitmap dishesImage, String dishesName) {
        this.context = context;
        this.categoryId = categoryId;
        this.price = price;
        this.dishesImage = dishesImage;
        this.dishesName = dishesName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.add_dish));
        progressDialog.setMessage(context.getString(R.string.handling)+"...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            return new MenuModel().Insert(categoryId, dishesName, price, dishesImage);
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

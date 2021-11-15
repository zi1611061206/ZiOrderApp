package zitech.ziorder.Threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.SQLException;

import zitech.ziorder.Models.TableModel;
import zitech.ziorder.R;

public class AddTableAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progressDialog;
    private Context context;
    private int areaId;
    private String tableName;

    public AddTableAsyncTask(Context context, int areaId, String tableName) {
        this.context = context;
        this.areaId = areaId;
        this.tableName = tableName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.checking));
        progressDialog.setMessage(context.getString(R.string.checking_and_change)+"...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            return new TableModel().Insert(areaId, tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

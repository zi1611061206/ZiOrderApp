package zitech.ziorder.Threads;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.sql.SQLException;

import zitech.ziorder.Models.AccountModel;
import zitech.ziorder.R;

public class ChangePasswordAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progressDialog;
    private Context context;
    private String newPass, oldPass, username;

    public ChangePasswordAsyncTask(Context context, String newPass, String oldPass, String username) {
        this.context = context;
        this.newPass = newPass;
        this.oldPass = oldPass;
        this.username = username;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.checking));
        progressDialog.setMessage(context.getString(R.string.checking_and_change) + "...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            if (new AccountModel().CheckOldPass(username, oldPass)) {
                new AccountModel().UpdatePass(username, newPass);
                return true;
            } else {
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
        if (!aBoolean) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setIcon(R.drawable.unnamed);
            dialog.setTitle(R.string.result);
            dialog.setCancelable(true);
            dialog.setMessage(context.getString(R.string.old_pass_wrong));
            dialog.show();
            progressDialog.dismiss();
        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setIcon(R.drawable.unnamed);
            dialog.setTitle(R.string.result);
            dialog.setCancelable(true);
            dialog.setMessage(context.getString(R.string.change_pass_success));
            dialog.show();
            progressDialog.dismiss();
        }
    }
}

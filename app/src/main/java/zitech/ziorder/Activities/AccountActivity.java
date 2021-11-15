package zitech.ziorder.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

import zitech.ziorder.Models.AccountModel;
import zitech.ziorder.Objects.Account;
import zitech.ziorder.R;
import zitech.ziorder.Threads.ChangePasswordAsyncTask;

public class AccountActivity extends AppCompatActivity {

    ImageView imgAvatar;
    Button btnChangePass, btnManager;
    TextView tvUsername, tvFullname, tvPosition, tvIdCard, tvAddress, tvPhone;
    Account account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        AddViewById();
        try {
            LoadStaffInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AddEvent();
    }

    private void AddEvent() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePass();
            }

            private void ChangePass() {
                ShowChangePassDialog();
            }
        });
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manager();
            }

            private void Manager() {
                if (account.getPositionId()==1){
                    Intent intent = new Intent(AccountActivity.this, ManagerActivity.class);
                    startActivity(intent);
                }
                else {
                    ShowAccessDiniedDialog();
                }
            }
        });
    }

    private void ShowChangePassDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_change_password);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setLayout(width*7/8, height*4/5);

        final EditText edtOldPassword = dialog.findViewById(R.id.edittext_old_pass);
        final EditText edtNewPassword = dialog.findViewById(R.id.edittext_new_pass);
        final EditText edtRenewPassword = dialog.findViewById(R.id.edittext_renew_pass);
        final TextView tvError = dialog.findViewById(R.id.textview_error_change_password);
        Button btnCancel = dialog.findViewById(R.id.button_cancel_change_password_dialog);
        Button btnConfirm = dialog.findViewById(R.id.button_confrim_change_password_dialog);

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
                ChangePassword();
            }

            private void ChangePassword() {
                tvError.setText("");
                String oldPass = edtOldPassword.getText().toString();
                String newPass = edtNewPassword.getText().toString();
                String renewPass = edtRenewPassword.getText().toString();
                if (oldPass.equals("")|newPass.equals("")|renewPass.equals("")){
                    tvError.setText(getString(R.string.full_fill));
                }else {
                    if (!renewPass.equals(newPass)){
                        tvError.setText(getString(R.string.new_pass_not_match));
                    }
                    else {
                        new ChangePasswordAsyncTask(AccountActivity.this, newPass, oldPass, LoginActivity.curentAccount).execute();
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }

    private void ShowAccessDiniedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AccountActivity.this);
        dialog.setTitle(getString(R.string.message));
        dialog.setMessage(getString(R.string.access_denied));
        dialog.setIcon(R.drawable.unnamed);
        dialog.setCancelable(false);
        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void LoadStaffInfo() throws SQLException {
        AccountModel accountModel = new AccountModel();
        account = accountModel.GetAccount(LoginActivity.curentAccount);
        tvUsername.setText(LoginActivity.curentAccount);
        tvFullname.setText(account.getFullName());
        tvAddress.setText(account.getAddress());
        tvPhone.setText(account.getPhone());
        tvIdCard.setText(account.getIdCard());
        tvPosition.setText(new AccountModel().GetPositionName(account.getPositionId()));
        imgAvatar.setImageBitmap(ConvertStringToImage(account.getAvartar()));
    }

    private Bitmap ConvertStringToImage(byte[] avatar) {
        byte[] imageBytes = avatar;
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    private void AddViewById() {
        imgAvatar = findViewById(R.id.imageview_avartar);
        btnChangePass = findViewById(R.id.button_change_password);
        btnManager = findViewById(R.id.button_manager);
        tvUsername = findViewById(R.id.textview_username);
        tvFullname = findViewById(R.id.textview_fullname);
        tvPosition = findViewById(R.id.textview_position);
        tvIdCard = findViewById(R.id.textview_idcard);
        tvAddress = findViewById(R.id.textview_address);
        tvPhone = findViewById(R.id.textview_phone);
    }
}

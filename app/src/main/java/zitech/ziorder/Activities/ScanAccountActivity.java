package zitech.ziorder.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.Result;

import java.sql.SQLException;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import zitech.ziorder.Models.AccountModel;
import zitech.ziorder.R;

public class ScanAccountActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    private int STRING_ENCODE = 1998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(ScanAccountActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanAccountActivity.this, new String[] { Manifest.permission.CAMERA }, 1 );
            return;
        }
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        AddEvent();
        scannerView.setAutoFocus(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void AddEvent() {
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFlash();
            }

            private void OpenFlash() {
                boolean hasFlash = ScanAccountActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                if (hasFlash){
                    scannerView.setFlash(true);
                }else if (HasFlash()){
                    scannerView.setFlash(true);
                }else {
                    Toast.makeText(ScanAccountActivity.this, getString(R.string.flash_not_ready), Toast.LENGTH_SHORT).show();
                }
            }
        });
        scannerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return OpenFrontCamera();
            }

            private boolean OpenFrontCamera() {
                if (CheckCamera()==1){
                    scannerView.startCamera(1);
                    return true;
                }else {
                    Toast.makeText(ScanAccountActivity.this, getString(R.string.front_camera_not_ready), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }

    public boolean HasFlash() {
        Camera camera = Camera.open();
        if (camera == null) {
            return false;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getFlashMode() == null) {
            return false;
        }
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }
        return true;
    }

    private int CheckCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return 1;
            }else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();
        if (result != null) {
            String[] resultSplit = result.split(""+STRING_ENCODE+"");
            if (resultSplit.length == 2){
                String username = resultSplit[0];
                String password = resultSplit[1];
                try {
                    if (new AccountModel().CheckLoginInfo(username, password)){
                        LoginActivity.curentAccount = username;
                        Intent intent=new Intent(ScanAccountActivity.this, TableActivity.class);
                        startActivity(intent);
                    }else {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setIcon(R.drawable.unnamed);
                        dialog.setTitle(R.string.result);
                        dialog.setCancelable(true);
                        dialog.setMessage(getString(R.string.login_fail));
                        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ReLogin();
                            }

                            private void ReLogin() {
                                onBackPressed();
                            }
                        });
                        dialog.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setIcon(R.drawable.unnamed);
                dialog.setTitle(R.string.result);
                dialog.setCancelable(true);
                dialog.setMessage(getString(R.string.incorrect_format));
                dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReLogin();
                    }

                    private void ReLogin() {
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}

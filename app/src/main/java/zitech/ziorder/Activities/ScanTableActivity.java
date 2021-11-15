package zitech.ziorder.Activities;

import android.Manifest;
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

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import zitech.ziorder.R;

public class ScanTableActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(ScanTableActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanTableActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
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
                boolean hasFlash = ScanTableActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                if (hasFlash) {
                    scannerView.setFlash(true);
                } else if (HasFlash()) {
                    scannerView.setFlash(true);
                } else {
                    Toast.makeText(ScanTableActivity.this, getString(R.string.flash_not_ready), Toast.LENGTH_SHORT).show();
                }
            }
        });
        scannerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return OpenFrontCamera();
            }

            private boolean OpenFrontCamera() {
                if (CheckCamera() == 1) {
                    scannerView.startCamera(1);
                    return true;
                } else {
                    Toast.makeText(ScanTableActivity.this, getString(R.string.front_camera_not_ready), Toast.LENGTH_SHORT).show();
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
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();
        if (result != null) {
            try {
                int tableId = Integer.parseInt(result);
                Intent intent = new Intent(ScanTableActivity.this, OrderActivity.class);
                intent.putExtra("tableId_scan", tableId);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(ScanTableActivity.this, getText(R.string.incorrect_format), Toast.LENGTH_LONG).show();
            }
        }
        onBackPressed();
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

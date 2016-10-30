package com.example.abdelrahmansamir.sampleflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity {

    private Camera camera;
    private boolean hasFlash;
    private boolean flashOn;
    Camera.Parameters param;
    ToggleButton tB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Error");
            alert.setMessage("No flash on your device");
            alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();

        }

        getCamera();

        tB = (ToggleButton) findViewById(R.id.button_flash);
        tB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });


    }

    private void getCamera() {

        if (camera == null) {

            try {

                camera = Camera.open();
                param = camera.getParameters();

            } catch (RuntimeException e) {

                Log.e("Camera", e.getMessage());
            }

        }

    }


    private void turnOnFlash() {

        if (!flashOn) {

            if (camera == null || param == null) {
                return;
            } else {

                param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(param);
                camera.startPreview();
                flashOn = true;
                Log.v("flash", "turned on");
            }

        }

    }

    private void turnOffFlash() {

        if (flashOn) {

            if (camera == null || param == null) {
                return;
            } else {

                param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                camera.stopPreview();
                flashOn = false;
                Log.v("flash", "turned off");
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        turnOffFlash();
        if (camera != null) {

            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }
}

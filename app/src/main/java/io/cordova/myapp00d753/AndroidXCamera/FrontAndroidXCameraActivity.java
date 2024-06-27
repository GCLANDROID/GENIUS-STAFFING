package io.cordova.myapp00d753.AndroidXCamera;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.cordova.myapp00d753.R;

public class FrontAndroidXCameraActivity extends AppCompatActivity {
    private static final String TAG = "AndroidXCameraActivity";
    public static final String IMAGE_PATH_KEY = "imagePathKey";
    public static final int IMAGE_RESULT_CODE = 2000;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    CameraSelector cameraSelector;
    Camera camera;
    PreviewView previewFinder;
    Preview preview;
    private ImageButton captureButton;
    static int RESULT_CODE;
    public static final int LONG_IMAGE_RESULT_CODE = 2001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_xcamera);
        intiView();
        checkCameraPermission();
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(view);
            }
        });
    }

    private void intiView() {
        captureButton = findViewById(R.id.captureButton);
        previewFinder = findViewById(R.id.viewFinder);
    }


    private void checkCameraPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.e("onPermissionsGranted", "Called");
                            cameraProviderFuture = ProcessCameraProvider.getInstance(FrontAndroidXCameraActivity.this);
                            cameraProviderFuture.addListener(() -> {
                                try {
                                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                                    bindPreview(cameraProvider);
                                } catch (Exception e) {
                                    Log.e("CameraX", "Error binding camera", e);
                                }
                            }, ContextCompat.getMainExecutor(FrontAndroidXCameraActivity.this));
                        } else {
                            CameraPermissionDialog.showDialog(FrontAndroidXCameraActivity.this, new CameraPermissionDialog.OnResponseListeners() {
                                @Override
                                public void onResponse() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", FrontAndroidXCameraActivity.this.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 1);
                                }
                            });
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        preview = new Preview.Builder().build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        preview.setSurfaceProvider(previewFinder.getSurfaceProvider());

        camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    private void captureImage(View view) {
        File outputDirectory = getOutputDirectory();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
        File outputFile = new File(outputDirectory, fileName);

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(outputFile).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.e(TAG, "onImageSaved: " + outputFile.getPath());
                        String url = outputFile.getPath();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.putExtra("picture",outputFile);
                                intent.putExtra(IMAGE_PATH_KEY, outputFile.getPath());
                                setResult(RESULT_CODE, intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e("CameraX", "Error capturing image", exception);
                    }
                });
    }

    private File getOutputDirectory() {
        File mediaDir = new File(getApplicationContext().getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".png");
        if (!mediaDir.exists()) {
            if (!mediaDir.mkdirs()) {
                Log.e("CameraX", "Failed to create directory");
                return null;
            }
        }
        return mediaDir;
    }

    public static void launch(AppCompatActivity activity, int finalResultCode) {
        Intent ii = new Intent(activity, FrontAndroidXCameraActivity.class);
        activity.startActivityForResult(ii, IMAGE_RESULT_CODE);
        RESULT_CODE = finalResultCode;
    }
}
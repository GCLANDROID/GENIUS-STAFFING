package io.cordova.myapp00d753.activity;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;


import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.cameraview.CameraView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.FileUtils;

//import com.google.android.cameraview.demo.R;
//import junit.framework.Assert;

// This demo app uses dlib face recognition based on resnet
public class FaceRecognitation extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    static final int REQUEST_IMAGE_CAPTURE = 1;


    private static final String TAG = "FaceRecognitation";
    private static final int INPUT_SIZE = 500;

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;

    private CameraView mCameraView;

    private Handler mBackgroundHandler;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();

                    }
                    break;
//                case R.id.add_person:
//                    Intent i = new Intent(FaceRecognitation.this, AddPerson.class);
//                    startActivity(i);
//                    finish();
//                    break;
            }
        }
    };


    ArrayList<String> names;
    File folder;
    String address;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognitation);
        checkPermissions();
        address=getIntent().getStringExtra("address");

        mCameraView = (CameraView) findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        Button fab = (Button) findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
//        Button add_person = (Button) findViewById(R.id.add_person);
//        if (add_person != null) {
//            add_person.setOnClickListener(mOnClickListener);
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }
//Pass Capture Image between Preview Activity

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String msg = getResultMessage(names);

            Intent i = new Intent(FaceRecognitation.this, PreviewActivity.class);
            i.putExtra("BitmapImage", imageBitmap);
            i.putExtra("address",address);
            i.putExtra("info", msg);
            startActivity(i);
        }
    }*/

    //END
    //private FaceRec mFaceRec;

    private void changeProgressDialogMessage(final ProgressDialog pd, final String msg) {
        Runnable changeMessage = new Runnable() {
            @Override
            public void run() {
                pd.setMessage(msg);
            }
        };
        runOnUiThread(changeMessage);
    }

    private class initRecAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(FaceRecognitation.this);

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "initRecAsync onPreExecute called");
            dialog.setMessage("Initializing...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
// create dlib_rec_example directory in sd card and copy model files

                /*File image_folder = new File(Constants.getDLibImageDirectoryPath());
                image_folder.mkdirs();
                if (!new File(Constants.getFaceShapeModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(FaceRecognitation.this, R.raw.shape_predictor_5_face_landmarks, Constants.getFaceShapeModelPath());
                }
                if (!new File(Constants.getFaceDescriptorModelPath()).exists()) {
                    FileUtils.copyFileFromRawToOthers(FaceRecognitation.this, R.raw.dlib_face_recognition_resnet_model_v1, Constants.getFaceDescriptorModelPath());
                }

            mFaceRec = new FaceRec(Constants.getDLibDirectoryPath());
            changeProgressDialogMessage(dialog, "Adding people...");
            mFaceRec.train();*/
            return null;
        }

        protected void onPostExecute(Void result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            new initRecAsync().execute();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause called");
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        super.onDestroy();
     /*   if (mFaceRec != null) {
            mFaceRec.release();
        }*/
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// do something
            }
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private String getResultMessage(ArrayList<String> names) {
        String msg = new String();
        if (names.isEmpty()) {
            msg = "No face detected or Unknown person";

        } else {

                msg = names.get(0) .split(Pattern.quote("."))[0];





        }
        return msg;
    }


    private class recognizeAsync extends AsyncTask<Bitmap, Void, ArrayList<String>> {
        ProgressDialog dialog = new ProgressDialog(FaceRecognitation.this);
        private int mScreenRotation = 0;
        private boolean mIsComputing = false;
        private Bitmap mCroppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Recognizing...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected ArrayList<String> doInBackground(Bitmap... bp) {

            drawResizedBitmap(bp[0], mCroppedBitmap);
            Log.d(TAG, "byte to bitmap");

           /* long startTime = System.currentTimeMillis();
            List<VisionDetRet> results;
            results = mFaceRec.recognize(mCroppedBitmap);
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "Time cost: " + String.valueOf((endTime - startTime) / 1000f) + " sec");

            ArrayList<String> names = new ArrayList<>();
            for (VisionDetRet n : results) {
                names.add(n.getLabel());
            }*/
            return names;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(ArrayList<String> names) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mCroppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String message = getResultMessage(names);
                Intent in1 = new Intent(FaceRecognitation.this, PreviewActivity.class);
                in1.putExtra("image",byteArray);
                in1.putExtra("address",address);
                in1.putExtra("info",message);
                startActivity(in1);

                Log.d("messagees", message);
            }
        }

        private void drawResizedBitmap(final Bitmap src, final Bitmap dst) {

            Display getOrient = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int orientation = Configuration.ORIENTATION_PORTRAIT;
            Point point = new Point();
            getOrient.getSize(point);
            int screen_width = point.x;
            int screen_height = point.y;
            Log.d(TAG, String.format("screen size (%d,%d)", screen_width, screen_height));
            if (screen_width < screen_height) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
                if (screen_height<1500){
                    mScreenRotation= 0;
                }else {
                    mScreenRotation = 270;

                }

            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
                mScreenRotation = 0;
            }

//Assert.assertEquals(dst.getWidth(), dst.getHeight());
            final float minDim = Math.min(src.getWidth(), src.getHeight());

            final Matrix matrix = new Matrix();

// We only want the center square out of the original rectangle.
            final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
            final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
            matrix.preTranslate(translateX, translateY);

            final float scaleFactor = dst.getHeight() / minDim;
            matrix.postScale(scaleFactor, scaleFactor);

// Rotate around the center if necessary.
            if (mScreenRotation != 0) {
                matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
                matrix.postRotate(mScreenRotation);
                matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
            }else {
                matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
                matrix.postRotate(mScreenRotation);
                matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
            }

            final Canvas canvas = new Canvas(dst);
            canvas.drawBitmap(src, matrix, null);
        }
    }


    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), R.string.picture_taken, Toast.LENGTH_SHORT)
                    .show();
            Bitmap bp = BitmapFactory.decodeByteArray(data, 0, data.length);
            new recognizeAsync().execute(bp);
        }

    };

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }


    private int count = 0;
    @Override
    public void onBackPressed() {
        count++;
        if (count >=1) {
        /* If count is greater than 1 ,you can either move to the next
        activity or just quit. */
            Intent intent = new Intent(FaceRecognitation.this, FRDashboard.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();

            // resetting the counter in 2s
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 2000);
        }
        super.onBackPressed();
    }


}

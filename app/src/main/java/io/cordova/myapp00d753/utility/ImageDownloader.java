package io.cordova.myapp00d753.utility;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader {
    static File file;
    public static void downloadImageAndSaveToFile(Context context, String imageUrl, String fileName,ImageDownloader.SaveFileListener saveFileListener) {
        new DownloadImageTask(context, fileName,saveFileListener).execute(imageUrl);
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private Context context;
        private String fileName;
        ImageDownloader.SaveFileListener saveFileListener;
        public DownloadImageTask(Context context, String fileName,ImageDownloader.SaveFileListener saveFileListener) {
            this.context = context;
            this.fileName = fileName;
            this.saveFileListener = saveFileListener;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            Bitmap bitmap = null;

            try {
                // Open a connection to the image URL
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                // Get the input stream from the connection
                InputStream inputStream = connection.getInputStream();

                // Decode the input stream into a Bitmap
                bitmap = BitmapFactory.decodeStream(inputStream);

                // Close the input stream
                inputStream.close();
            } catch (IOException e) {
                Log.e("ImageDownloader", "Error downloading image: " + e.getMessage());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                // Save the bitmap to file
                file = saveBitmapToFile(context, bitmap, fileName);
                saveFileListener.onSaveFile(file);
            } else {
                Log.e("ImageDownloader", "Failed to download image");
                saveFileListener.onFileSaveFailure("Failed to download image");
            }
        }

        public static File saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {
            // Make sure the file path is valid
            File directory = context.getFilesDir(); // Or use getExternalFilesDir() for external storage
            File file = new File(directory, fileName);

            try (FileOutputStream outStream = new FileOutputStream(file)) {
                // Compress the bitmap and save to the file
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                Log.d("ImageDownloader", "Image saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                Log.e("ImageDownloader", "Error saving image: " + e.getMessage());
            }
            return file;
        }
    }

    public interface SaveFileListener{
        void onSaveFile(File file);
        void onFileSaveFailure(String error);
    }
}

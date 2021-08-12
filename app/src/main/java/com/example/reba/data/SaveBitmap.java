package com.example.reba.data;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveBitmap {
    public static void saveTempBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            Log.i("VID", "store");
            saveImage(bitmap);
        }else{
            //prompt the user or do something
            Log.i("VID", "can't store");
        }
    }

    private static void saveImage(Bitmap bitmap) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("VID", "Can't create directory to save the image");
            else
                Log.i("VID", "dir created/exists");
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        try {
            Log.i("VID", "done 000");
            boolean fileCreated = pictureFile.createNewFile();
            Log.i("VID", "done 008");
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            //Log.i("VID", "done 008");
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("VID", "There was an issue saving the image.");
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}

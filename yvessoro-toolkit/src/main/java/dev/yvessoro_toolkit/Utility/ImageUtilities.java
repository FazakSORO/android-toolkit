package dev.yvessoro_toolkit.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by YSORO on 04/10/2016.
 */

public class ImageUtilities {


    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static Bitmap downloadImageBitmap(String sUrl) {
        String TAG = "DownloadImage";
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
            bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
            inputStream.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception 1, Something went wrong in downloadImageBitmap!");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName + ".png", Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong in saveImage!");
            e.printStackTrace();
        }
    }

    public static Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream = context.openFileInput(imageName + ".png");
            bitmap = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong in loadImageBitmap!");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String imageFullPath(Context ctx, String imgName) {
        File file = ctx.getApplicationContext().getFileStreamPath(imgName + ".png");
        String imageFullPath = file.getAbsolutePath();

        return imageFullPath;
    }

    public static boolean isImageExists(Context ctx, String imgName) {
        File file = ctx.getApplicationContext().getFileStreamPath(imgName + ".png");
        if (file.exists()) {
            Log.d("file", imgName + " exists!");
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteImage(Context ctx, String imgName) {
        File file = ctx.getApplicationContext().getFileStreamPath(imgName + ".png");
        if (file.delete()) {
            Log.d("file", imgName + " deleted!");
            return true;
        } else {
            return false;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromUrl(String sUrl,
                                                    int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream inputStream = null;
        try {
            inputStream = new URL(sUrl).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.decodeStream(inputStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(inputStream, null, options);
    }
}

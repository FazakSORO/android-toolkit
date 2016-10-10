package dev.yvessoro_toolkit.Utility;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.concurrent.Executor;

import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * @author FOUNGNIGUE YVES SORO
 * @since 12/08/2016
 **/

public class Utilities {
    private static final int BUFFER_SIZE = 0x1000;
    public static NumberFormat formatter = null;

    public static void Dialog(Context context, String title, String message, String titleButton, int icon, final Runnable function) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setIcon(icon);
        adb.setPositiveButton(titleButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (function != null) {
                    function.run();
                }
            }
        });
        AlertDialog alertDialog = adb.create();
        alertDialog.show();
    }

    public static void DialogWithTwoButtons(Context context, String title, String message, String titleFirstButton, final Runnable funcFirstButton, String titleSecondButton, final Runnable funcSecondButton, int icon) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setIcon(icon);

        alertDialogBuilder.setPositiveButton(titleFirstButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (funcFirstButton != null) {
                    funcFirstButton.run();
                }
            }
        });

        alertDialogBuilder.setNegativeButton(titleSecondButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (funcSecondButton != null) {
                    funcSecondButton.run();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("Error", "Error checking internet connection", e);
            }
        } else {
            Log.d("Error", "No network available!");
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean IsCredentialsEntered(String login, String pwd) {
        return !login.equalsIgnoreCase("") && !pwd.equalsIgnoreCase("");
    }

    public static void VerifyFiles(Context context, String fileDirectory, String filename, String fileExtension) {
        // Recupere le dossier de stockage de l'application
        File ls = context.getFilesDir();
        // Création d'un repertoire
        File dataDos = new File(ls.getAbsolutePath() + "/" + fileDirectory);
        // Création d'un fichier dans le repertoire
        File dataFile = new File(dataDos.getAbsolutePath() + "/" + filename + "." + fileExtension);
        Log.i("path-->", "files path--> : " + dataDos.getAbsolutePath() + "/" + filename + "." + fileExtension);

        if (dataDos.exists() == false) {
            dataDos.mkdir();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (dataDos.exists() == true) {
            if (dataFile.exists() == false) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static String ReadFile(Context context, String fileDirectory, String filename, String fileExtension) {
        // The name of the file to open.
        // Recupere le dossier de stockage de l'application
        File ls = context.getFilesDir();
        // Création d'un repertoire
        File dataDos = new File(ls.getAbsolutePath() + "/" + fileDirectory);
        // Création d'un fichier dans le repertoire
        File dataFile = new File(dataDos.getAbsolutePath() + "/" + filename + "." + fileExtension);
        String fileName = filename + "." + fileExtension;
        // This will reference one line at a time
        String line = null;
        String result = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(dataFile);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                result = line;
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return result;
    }

    public static void WriteFile(Context context, String fileDirectory, String filename, String fileExtension, String content) {
        // Recupere le dossier de stockage de l'application
        File ls = context.getFilesDir();
        // Création d'un repertoire
        File dataDos = new File(ls.getAbsolutePath() + "/" + fileDirectory);
        // Création d'un fichier dans le repertoire
        File dataFile = new File(dataDos.getAbsolutePath() + "/" + filename + "." + fileExtension);
        String fileName = filename + "." + fileExtension;

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(dataFile);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(content);

            // Always close files.
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }

    public static String getBodyString(Response response) throws IOException {
        TypedInput body = response.getBody();

        if (body != null) {

            if (!(body instanceof TypedByteArray)) {
                // Read the entire response body to we can log it and replace the original response
                response = readBodyToBytesIfNecessary(response);
                body = response.getBody();
            }

            byte[] bodyBytes = ((TypedByteArray) body).getBytes();
            String bodyMime = body.mimeType();
            String bodyCharset = MimeUtil.parseCharset(bodyMime);
            return new String(bodyBytes, bodyCharset);
        }
        return null;
    }

    static Response readBodyToBytesIfNecessary(Response response) throws IOException {
        TypedInput body = response.getBody();
        if (body == null || body instanceof TypedByteArray) {
            return response;
        }
        String bodyMime = body.mimeType();
        byte[] bodyBytes = streamToBytes(body.in());
        body = new TypedByteArray(bodyMime, bodyBytes);

        return replaceResponseBody(response, body);
    }

    @SuppressWarnings("deprecation")
    static Response replaceResponseBody(Response response, TypedInput body) {
        return new Response(response.getStatus(), response.getReason(), response.getHeaders(), body);
    }

    static byte[] streamToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (stream != null) {
            byte[] buf = new byte[BUFFER_SIZE];
            int r;
            while ((r = stream.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
        }
        return baos.toByteArray();
    }

    public static void loadImageFromNetwork(final String url, final ImageView imageView) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Bitmap bitmap;
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static int indexOfString(String searchString, String[] domain) {
        for (int i = 0; i < domain.length; i++) {
            if (searchString.equals(domain[i]))
                return i;
        }
        return -1;
    }

    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    static class SynchronousExecutor implements Executor {
        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }
}

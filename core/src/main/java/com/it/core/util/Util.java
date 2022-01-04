package com.it.core.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.it.core.R;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public abstract class Util implements Closeable {

    public static SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm:ss a");
    public static SimpleDateFormat DateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

    public static boolean cannotAccessSDCard() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        return !mExternalStorageAvailable && !mExternalStorageWriteable;
    }

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
    public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    public static Document generateDocument() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.newDocument();
        } catch (Exception e) {
        }
        return null;
    }

    public static File getSDCard() {
        return Environment.getExternalStorageDirectory();
    }

    public static String getInnerXML(Node root) {
        StringBuilder buffer = new StringBuilder();

        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            buffer.append(getStringFromNode(nodeList.item(i)));
        }

        return buffer.toString();
    }

    public static String getStringFromNode(Node root) {

        StringBuilder result = new StringBuilder();

        if (root.getNodeType() == 3)
            result.append(root.getNodeValue());
        else {
            if (root.getNodeType() != 9) {
                StringBuffer attrs = new StringBuffer();
                for (int k = 0; k < root.getAttributes().getLength(); ++k) {
                    attrs.append(" ").append(
                            root.getAttributes().item(k).getNodeName()).append(
                            "=\"").append(
                            TextUtils.htmlEncode(root.getAttributes().item(k).getNodeValue()))
                            .append("\" ");
                }
                result.append("<").append(root.getNodeName()).append(" ")
                        .append(attrs).append(">");
            } else {
                result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            }

            NodeList nodes = root.getChildNodes();
            for (int i = 0, j = nodes.getLength(); i < j; i++) {
                Node node = nodes.item(i);
                result.append(getStringFromNode(node));
            }

            if (root.getNodeType() != 9) {
                result.append("</").append(root.getNodeName()).append(">");
            }
        }
        return result.toString();
    }

    public static Document LoadDocument(File path) {
        try {
            FileInputStream stream = new FileInputStream(path);
            return ReadDocument(stream);
        } catch (Exception e) {

        }
        return null;
    }

    public static Document ReadDocument(InputStream stream) {
        try {
            String xml = new String(ReadToEOF(stream), StandardCharsets.UTF_8);
            return ReadDocument(xml);
        } catch (Exception e) {

            Log.e("Error", "Error while Parsing Document", e);
            return null;
        }
    }

    public static Document ReadDocument(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            StringReader reader = new StringReader(xml);
            is.setCharacterStream(reader);
            doc = db.parse(is);
        } catch (Exception e) {
            Log.e("Error", "Error while Parsing Document", e);
            return null;
        }
        return doc;
    }

    public static byte[] ReadToEOF(InputStream stream) throws Exception {
        return ReadToEOF(stream, -1);
    }

    public static byte[] ReadToEOF(InputStream stream, long totalSize) throws Exception {
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            long totalReadBytes = 0;
            int read = 0;
            byte[] receivedData = new byte[5000];
            while ((read = stream.read(receivedData)) > 0) {
                array.write(receivedData, 0, read);
                totalReadBytes += read;

                if (totalSize > 0) {
                    if (totalReadBytes == totalSize)
                        break;
                } else {
                    if (!(stream instanceof FileInputStream) && stream.available() == 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                        if (stream.available() == 0)
                            break;
                    }
                }
            }
            return array.toByteArray();
        } catch (Exception e) {

            throw e;
        }
    }

    private static final int BUF_SIZE = 0x1000; // 4K

    public static long copyStream(InputStream from, OutputStream to)
            throws IOException {
        byte[] buf = new byte[BUF_SIZE];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    public static String XMLAppendDeclaration(String nodeString) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                nodeString;
    }

    public static String getMusicLocation() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    public static File getDCIMFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    public static File getPictureFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static String getStringFromByteArray(byte[] bytes) {
        try {
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                cursor.close();
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isValidEmail(String target) {
        try {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        } catch (NullPointerException exception) {
            return false;
        }
    }
	

    public static void copyFile(File source, File dest) {
        FileInputStream in = null;
        FileOutputStream out = null;

        try {

            in = new FileInputStream(source);
            out = new FileOutputStream(dest);

            byte[] buffer = new byte[4096];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();

        } catch (Exception e) {

        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e2) {

            }
            try {
                if (out != null)
                    out.close();
            } catch (Exception e2) {

            }
        }
    }

    public static void moveFile(File source, File dest) {
        source.renameTo(dest);
    }

    public static InputStream getInputStream(Context context, Uri uri) throws FileNotFoundException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            return context.getContentResolver().openInputStream(uri);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return new FileInputStream(uri.getPath());
        }

        return null;
    }

    public static void alterAddColumn(SQLiteDatabase db, String tableName, String columnSignature) {
        db.execSQL("ALTER TABLE " + tableName
                + " ADD " + columnSignature);
    }

    public static void showToast(Context context, String message, int length) {
        if (context != null) {
            Toast.makeText(context, message, length).show();
        }
    }

    public static void showConnectionToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
	
	/*public static File getCameraFolder()
	{
		File DCIMDir = getDCIMFolder();
		File CameraDir = new File(DCIMDir, "Camera");
		
		// If has DCIM/Camera Folder
		if(CameraDir.exists())
		{
			// Has Camera Folder
			return CameraDir;
		}
		else
		{
			// Using DCIM Standard
			return new File(DCIMHelper.getDirectoryForNewImage());
		}
	}*/


    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private static void huaweiProtectedApps(Context context) {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial(context);
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private static String getUserSerial(Context context) {
        //noinspection ResourceType
        Object userManager = context.getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }

    public static boolean appInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }

        return false;
    }


    public static interface OnTouchOnclickListener {
        void onClick();

        void onTouch();
    }

}

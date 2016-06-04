package com.qualcomm.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Version;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Analytics extends BroadcastReceiver {
    public static final String DATA_COLLECTION_PATH = ".ftcdc";
    public static final String DS_COMMAND_STRING = "update_ds";
    public static final String EXTERNAL_STORAGE_DIRECTORY_PATH;
    public static final String LAST_UPLOAD_DATE = "last_upload_date";
    public static final String MAX_DEVICES = "max_usb_devices";
    public static int MAX_ENTRIES_SIZE = 0;
    public static final String RC_COMMAND_STRING = "update_rc";
    public static int TRIMMED_SIZE = 0;
    public static final String UUID_PATH = ".analytics_id";
    static String f147a;
    static long f148b;
    static UUID f149c;
    static String f150d;
    static String f151f;
    static final HostnameVerifier f152l;
    private static final Charset f153m;
    String f154e;
    Context f155g;
    SharedPreferences f156h;
    boolean f157i;
    long f158j;
    int f159k;

    /* renamed from: com.qualcomm.analytics.Analytics.1 */
    static class C00171 implements HostnameVerifier {
        C00171() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /* renamed from: com.qualcomm.analytics.Analytics.2 */
    static class C00182 implements X509TrustManager {
        C00182() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    public static class DataInfo implements Serializable {
        private final String f145a;
        protected int numUsages;

        public DataInfo(String adate, int numUsages) {
            this.f145a = adate;
            this.numUsages = numUsages;
        }

        public String date() {
            return this.f145a;
        }

        public int numUsages() {
            return this.numUsages;
        }
    }

    /* renamed from: com.qualcomm.analytics.Analytics.a */
    private class C0019a extends AsyncTask {
        final /* synthetic */ Analytics f146a;

        private C0019a(Analytics analytics) {
            this.f146a = analytics;
        }

        protected Object doInBackground(Object[] params) {
            if (this.f146a.isConnected()) {
                try {
                    URL url = new URL(Analytics.f147a);
                    if (!Analytics.getDateFromTime(Analytics.f148b).equals(Analytics.getDateFromTime(this.f146a.f156h.getLong(Analytics.LAST_UPLOAD_DATE, this.f146a.f158j)))) {
                        String ping = Analytics.ping(url, this.f146a.m88a("cmd", "=", "ping"));
                        CharSequence charSequence = "\"rc\": \"OK\"";
                        if (ping == null || !ping.contains(charSequence)) {
                            RobotLog.m328e("Analytics: Ping failed.");
                        } else {
                            RobotLog.m329i("Analytics ping succeeded.");
                            ping = Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + Analytics.DATA_COLLECTION_PATH;
                            ArrayList readObjectsFromFile = this.f146a.readObjectsFromFile(ping);
                            if (readObjectsFromFile.size() >= Analytics.MAX_ENTRIES_SIZE) {
                                this.f146a.trimEntries(readObjectsFromFile);
                            }
                            String call = Analytics.call(url, this.f146a.updateStats(Analytics.f149c.toString(), readObjectsFromFile, this.f146a.f154e));
                            if (call == null || !call.contains(charSequence)) {
                                RobotLog.m328e("Analytics: Upload failed.");
                            } else {
                                RobotLog.m329i("Analytics: Upload succeeded.");
                                Editor edit = this.f146a.f156h.edit();
                                edit.putLong(Analytics.LAST_UPLOAD_DATE, Analytics.f148b);
                                edit.apply();
                                edit.putInt(Analytics.MAX_DEVICES, 0);
                                edit.apply();
                                new File(ping).delete();
                            }
                        }
                    }
                } catch (MalformedURLException e) {
                    RobotLog.m328e("Analytics encountered a malformed URL exception");
                } catch (Exception e2) {
                    this.f146a.f157i = true;
                }
                if (this.f146a.f157i) {
                    RobotLog.m329i("Analytics encountered a problem during communication");
                    this.f146a.m89a();
                    this.f146a.f157i = false;
                }
            }
            return null;
        }
    }

    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("networkInfo") && ((NetworkInfo) extras.get("networkInfo")).getState().equals(State.CONNECTED)) {
            RobotLog.m329i("Analytics detected NetworkInfo.State.CONNECTED");
            communicateWithServer();
        }
    }

    static {
        f147a = "https://ftcdc.qualcomm.com/DataApi";
        EXTERNAL_STORAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/";
        MAX_ENTRIES_SIZE = 100;
        TRIMMED_SIZE = 90;
        f153m = Charset.forName("UTF-8");
        f149c = null;
        f151f = BuildConfig.VERSION_NAME;
        f152l = new C00171();
    }

    public void unregister() {
        this.f155g.unregisterReceiver(this);
    }

    public void register() {
        this.f155g.registerReceiver(this, new IntentFilter("android.net.wifi.STATE_CHANGE"));
    }

    public Analytics(Context context, String commandString, HardwareMap map) {
        this.f157i = false;
        this.f158j = 0;
        this.f159k = 0;
        this.f155g = context;
        this.f154e = commandString;
        try {
            this.f156h = PreferenceManager.getDefaultSharedPreferences(context);
            f148b = System.currentTimeMillis();
            f151f = Version.getLibraryVersion();
            handleUUID(UUID_PATH);
            int calculateUsbDevices = calculateUsbDevices(map);
            if (this.f156h.getInt(MAX_DEVICES, this.f159k) < calculateUsbDevices) {
                Editor edit = this.f156h.edit();
                edit.putInt(MAX_DEVICES, calculateUsbDevices);
                edit.apply();
            }
            setApplicationName(context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());
            handleData();
            register();
            RobotLog.m329i("Analytics has completed initialization.");
        } catch (Exception e) {
            this.f157i = true;
        }
        try {
            if (this.f157i) {
                m89a();
                this.f157i = false;
            }
        } catch (Exception e2) {
            RobotLog.m329i("Analytics encountered a problem during initialization");
            RobotLog.logStacktrace(e2);
        }
    }

    protected int calculateUsbDevices(HardwareMap map) {
        int size = (0 + map.legacyModule.size()) + map.deviceInterfaceModule.size();
        Iterator it = map.servoController.iterator();
        int i = size;
        while (it.hasNext()) {
            if (Pattern.compile("(?i)usb").matcher(((ServoController) it.next()).getDeviceName()).matches()) {
                size = i + 1;
            } else {
                size = i;
            }
            i = size;
        }
        it = map.dcMotorController.iterator();
        while (it.hasNext()) {
            if (Pattern.compile("(?i)usb").matcher(((DcMotorController) it.next()).getDeviceName()).matches()) {
                i++;
            }
        }
        return i;
    }

    protected void handleData() throws IOException, ClassNotFoundException {
        String str = EXTERNAL_STORAGE_DIRECTORY_PATH + DATA_COLLECTION_PATH;
        if (new File(str).exists()) {
            ArrayList updateExistingFile = updateExistingFile(str, getDateFromTime(f148b));
            if (updateExistingFile.size() >= MAX_ENTRIES_SIZE) {
                trimEntries(updateExistingFile);
            }
            writeObjectsToFile(str, updateExistingFile);
            return;
        }
        createInitialFile(str);
    }

    protected void trimEntries(ArrayList<DataInfo> dataInfoArrayList) {
        dataInfoArrayList.subList(TRIMMED_SIZE, dataInfoArrayList.size()).clear();
    }

    protected ArrayList<DataInfo> updateExistingFile(String filepath, String date) throws ClassNotFoundException, IOException {
        ArrayList<DataInfo> readObjectsFromFile = readObjectsFromFile(filepath);
        DataInfo dataInfo = (DataInfo) readObjectsFromFile.get(readObjectsFromFile.size() - 1);
        if (dataInfo.f145a.equalsIgnoreCase(date)) {
            dataInfo.numUsages++;
        } else {
            readObjectsFromFile.add(new DataInfo(date, 1));
        }
        return readObjectsFromFile;
    }

    protected ArrayList<DataInfo> readObjectsFromFile(String filepath) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(filepath)));
        ArrayList<DataInfo> arrayList = new ArrayList();
        Object obj = 1;
        while (obj != null) {
            try {
                arrayList.add((DataInfo) objectInputStream.readObject());
            } catch (EOFException e) {
                obj = null;
            }
        }
        objectInputStream.close();
        return arrayList;
    }

    protected void createInitialFile(String filepath) throws IOException {
        DataInfo dataInfo = new DataInfo(getDateFromTime(f148b), 1);
        ArrayList arrayList = new ArrayList();
        arrayList.add(dataInfo);
        writeObjectsToFile(filepath, arrayList);
    }

    private void m89a() {
        RobotLog.m329i("Analytics is starting with a clean slate.");
        Editor edit = this.f156h.edit();
        edit.putLong(LAST_UPLOAD_DATE, this.f158j);
        edit.apply();
        edit.putInt(MAX_DEVICES, 0);
        edit.apply();
        new File(EXTERNAL_STORAGE_DIRECTORY_PATH + DATA_COLLECTION_PATH).delete();
        new File(EXTERNAL_STORAGE_DIRECTORY_PATH + UUID_PATH).delete();
        this.f157i = false;
    }

    public void communicateWithServer() {
        new C0019a().execute((Object[]) new String[]{f147a});
    }

    public static void setApplicationName(String name) {
        f150d = name;
    }

    public void handleUUID(String filename) {
        File file = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + filename);
        if (!file.exists()) {
            f149c = UUID.randomUUID();
            handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + filename, f149c.toString());
        }
        try {
            f149c = UUID.fromString(readFromFile(file));
        } catch (IllegalArgumentException e) {
            RobotLog.m329i("Analytics encountered an IllegalArgumentException");
            f149c = UUID.randomUUID();
            handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + filename, f149c.toString());
        }
    }

    protected String readFromFile(File file) {
        try {
            char[] cArr = new char[4096];
            FileReader fileReader = new FileReader(file);
            int read = fileReader.read(cArr);
            fileReader.close();
            return new String(cArr, 0, read).trim();
        } catch (FileNotFoundException e) {
            RobotLog.m329i("Analytics encountered a FileNotFoundException while trying to read a file.");
            return BuildConfig.VERSION_NAME;
        } catch (IOException e2) {
            RobotLog.m329i("Analytics encountered an IOException while trying to read.");
            return BuildConfig.VERSION_NAME;
        }
    }

    protected void writeObjectsToFile(String filepath, ArrayList<DataInfo> info) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filepath));
        Iterator it = info.iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject((DataInfo) it.next());
        }
        objectOutputStream.close();
    }

    protected void handleCreateNewFile(String filepath, String data) {
        IOException e;
        Throwable th;
        Writer bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filepath)), "utf-8"));
            try {
                bufferedWriter.write(data);
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    RobotLog.m329i("Analytics encountered an IOException: " + e.toString());
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e4) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e6) {
            e = e6;
            bufferedWriter = null;
            RobotLog.m329i("Analytics encountered an IOException: " + e.toString());
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedWriter = null;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw th;
        }
    }

    public static String getDateFromTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(time));
    }

    protected static UUID getUuid() {
        return f149c;
    }

    public String updateStats(String user, ArrayList<DataInfo> dateInfo, String commandString) {
        String str = m88a("cmd", "=", commandString) + "&" + m88a("uuid", "=", user) + "&" + m88a("device_hw", "=", Build.MANUFACTURER) + "&" + m88a("device_ver", "=", Build.MODEL) + "&" + m88a("chip_type", "=", m91b()) + "&" + m88a("sw_ver", "=", f151f) + "&" + m88a("max_dev", "=", String.valueOf(this.f156h.getInt(MAX_DEVICES, this.f159k))) + "&";
        String str2 = BuildConfig.VERSION_NAME;
        int i = 0;
        while (i < dateInfo.size()) {
            if (i > 0) {
                str2 = str2 + ",";
            }
            String str3 = str2 + m88a(((DataInfo) dateInfo.get(i)).date(), ",", String.valueOf(((DataInfo) dateInfo.get(i)).numUsages()));
            i++;
            str2 = str3;
        }
        return (str + m88a("dc", "=", BuildConfig.VERSION_NAME)) + str2;
    }

    private String m88a(String str, String str2, String str3) {
        String str4 = BuildConfig.VERSION_NAME;
        try {
            str4 = URLEncoder.encode(str, f153m.name()) + str2 + URLEncoder.encode(str3, f153m.name());
        } catch (UnsupportedEncodingException e) {
            RobotLog.m329i("Analytics caught an UnsupportedEncodingException");
        }
        return str4;
    }

    private String m91b() {
        String str = "UNKNOWN";
        String str2 = "/proc/cpuinfo";
        String[] strArr = new String[]{"CPU implementer", "Hardware"};
        Map hashMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            for (str2 = bufferedReader.readLine(); str2 != null; str2 = bufferedReader.readLine()) {
                String[] split = str2.toLowerCase().split(":");
                if (split.length >= 2) {
                    hashMap.put(split[0].trim(), split[1].trim());
                }
            }
            bufferedReader.close();
            str2 = BuildConfig.VERSION_NAME;
            int length = strArr.length;
            String str3 = str2;
            int i = 0;
            while (i < length) {
                i++;
                str3 = str3 + ((String) hashMap.get(strArr[i].toLowerCase())) + " ";
            }
            str3 = str3.trim();
            if (str3.isEmpty()) {
                return str;
            }
            return str3;
        } catch (FileNotFoundException e) {
            RobotLog.m329i("Analytics encountered a FileNotFoundException while looking for CPU info");
            return str;
        } catch (IOException e2) {
            RobotLog.m329i("Analytics encountered an IOException while looking for CPU info");
            return str;
        }
    }

    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.f155g.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String ping(URL baseUrl, String data) {
        return call(baseUrl, data);
    }

    public static String call(URL url, String data) {
        if (url == null || data == null) {
            return null;
        }
        String str;
        try {
            HttpURLConnection httpURLConnection;
            long currentTimeMillis = System.currentTimeMillis();
            if (url.getProtocol().toLowerCase().equals("https")) {
                m92c();
                httpURLConnection = (HttpsURLConnection) url.openConnection();
                httpURLConnection.setHostnameVerifier(f152l);
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
            httpURLConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            str = new String();
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        str = str + readLine;
                    } else {
                        bufferedReader.close();
                        RobotLog.m329i("Analytics took: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
                        return str;
                    }
                } catch (IOException e) {
                }
            }
        } catch (IOException e2) {
            str = null;
            RobotLog.m329i("Analytics Failed to process command.");
            return str;
        }
    }

    private static void m92c() {
        TrustManager[] trustManagerArr = new TrustManager[]{new C00182()};
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, trustManagerArr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

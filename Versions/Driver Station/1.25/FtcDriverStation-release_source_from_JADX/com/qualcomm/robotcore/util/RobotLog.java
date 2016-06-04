package com.qualcomm.robotcore.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.File;

public class RobotLog {
    public static final String TAG = "RobotCore";
    private static String f571a;
    private static boolean f572b;

    /* renamed from: com.qualcomm.robotcore.util.RobotLog.1 */
    static class C01231 extends Thread {
        final /* synthetic */ String f566a;
        final /* synthetic */ String f567b;
        final /* synthetic */ int f568c;

        C01231(String str, String str2, String str3, int i) {
            this.f566a = str2;
            this.f567b = str3;
            this.f568c = i;
            super(str);
        }

        public void run() {
            try {
                String str = "UsbRequestJNI:S UsbRequest:S *:V";
                RobotLog.m330v("saving logcat to " + this.f566a);
                RunShellCommand runShellCommand = new RunShellCommand();
                RunShellCommand.killSpawnedProcess("logcat", this.f567b, runShellCommand);
                runShellCommand.run(String.format("logcat -f %s -r%d -n%d -v time %s", new Object[]{this.f566a, Integer.valueOf(this.f568c), Integer.valueOf(1), "UsbRequestJNI:S UsbRequest:S *:V"}));
            } catch (RobotCoreException e) {
                RobotLog.m330v("Error while writing log file to disk: " + e.toString());
            } finally {
                RobotLog.f572b = false;
            }
        }
    }

    /* renamed from: com.qualcomm.robotcore.util.RobotLog.2 */
    static class C01242 extends Thread {
        final /* synthetic */ String f569a;
        final /* synthetic */ String f570b;

        C01242(String str, String str2) {
            this.f569a = str;
            this.f570b = str2;
        }

        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            try {
                RobotLog.m330v("closing logcat file " + this.f569a);
                RunShellCommand.killSpawnedProcess("logcat", this.f570b, new RunShellCommand());
            } catch (RobotCoreException e2) {
                RobotLog.m330v("Unable to cancel writing log file to disk: " + e2.toString());
            }
        }
    }

    private RobotLog() {
    }

    static {
        f571a = BuildConfig.VERSION_NAME;
        f572b = false;
    }

    public static void m330v(String message) {
        Log.v(TAG, message);
    }

    public static void m327d(String message) {
        Log.d(TAG, message);
    }

    public static void m329i(String message) {
        Log.i(TAG, message);
    }

    public static void m331w(String message) {
        Log.w(TAG, message);
    }

    public static void m328e(String message) {
        Log.e(TAG, message);
    }

    public static void logStacktrace(Exception e) {
        m328e(e.toString());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            m328e(stackTraceElement.toString());
        }
    }

    public static void logStacktrace(RobotCoreException e) {
        m328e(e.toString());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            m328e(stackTraceElement.toString());
        }
        if (e.isChainedException()) {
            m328e("Exception chained from:");
            if (e.getChainedException() instanceof RobotCoreException) {
                logStacktrace((RobotCoreException) e.getChainedException());
            } else {
                logStacktrace(e.getChainedException());
            }
        }
    }

    public static void setGlobalErrorMsg(String message) {
        if (f571a.isEmpty()) {
            f571a += message;
        }
    }

    public static void setGlobalErrorMsgAndThrow(String message, RobotCoreException e) throws RobotCoreException {
        setGlobalErrorMsg(message + "\n" + e.getMessage());
        throw e;
    }

    public static String getGlobalErrorMsg() {
        return f571a;
    }

    public static boolean hasGlobalErrorMsg() {
        return !f571a.isEmpty();
    }

    public static void clearGlobalErrorMsg() {
        f571a = BuildConfig.VERSION_NAME;
    }

    public static void logAndThrow(String errMsg) throws RobotCoreException {
        m331w(errMsg);
        throw new RobotCoreException(errMsg);
    }

    public static void writeLogcatToDisk(Context context, int fileSizeKb) {
        if (!f572b) {
            f572b = true;
            String str = "Logging Thread";
            new C01231(str, new File(getLogFilename(context)).getAbsolutePath(), context.getPackageName(), fileSizeKb).start();
        }
    }

    public static String getLogFilename(Context context) {
        return (Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName()) + ".logcat";
    }

    public static void cancelWriteLogcatToDisk(Context context) {
        String packageName = context.getPackageName();
        String absolutePath = new File(Environment.getExternalStorageDirectory(), packageName).getAbsolutePath();
        f572b = false;
        new C01242(absolutePath, packageName).start();
    }
}

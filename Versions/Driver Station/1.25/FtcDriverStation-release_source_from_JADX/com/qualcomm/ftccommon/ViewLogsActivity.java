package com.qualcomm.ftccommon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ScrollView;
import android.widget.TextView;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.SPI_SLAVE_CMD;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class ViewLogsActivity extends Activity {
    public static final String FILENAME = "Filename";
    TextView f211a;
    int f212b;
    String f213c;

    /* renamed from: com.qualcomm.ftccommon.ViewLogsActivity.1 */
    class C00441 implements Runnable {
        final /* synthetic */ ScrollView f208a;
        final /* synthetic */ ViewLogsActivity f209b;

        C00441(ViewLogsActivity viewLogsActivity, ScrollView scrollView) {
            this.f209b = viewLogsActivity;
            this.f208a = scrollView;
        }

        public void run() {
            this.f208a.fullScroll(SPI_SLAVE_CMD.SPI_SHORT_MASTER_TRANSFER);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.ViewLogsActivity.2 */
    class C00452 implements Runnable {
        final /* synthetic */ ViewLogsActivity f210a;

        C00452(ViewLogsActivity viewLogsActivity) {
            this.f210a = viewLogsActivity;
        }

        public void run() {
            try {
                this.f210a.f211a.setText(this.f210a.m111a(this.f210a.readNLines(this.f210a.f212b)));
            } catch (IOException e) {
                RobotLog.m328e(e.toString());
                this.f210a.f211a.setText("File not found: " + this.f210a.f213c);
            }
        }
    }

    public ViewLogsActivity() {
        this.f212b = 300;
        this.f213c = " ";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.activity_view_logs);
        this.f211a = (TextView) findViewById(C0035R.id.textAdbLogs);
        ScrollView scrollView = (ScrollView) findViewById(C0035R.id.scrollView);
        scrollView.post(new C00441(this, scrollView));
    }

    protected void onStart() {
        super.onStart();
        Serializable serializableExtra = getIntent().getSerializableExtra(FILENAME);
        if (serializableExtra != null) {
            this.f213c = (String) serializableExtra;
        }
        runOnUiThread(new C00452(this));
    }

    public String readNLines(int n) throws IOException {
        int i = 0;
        Environment.getExternalStorageDirectory();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(this.f213c)));
        String[] strArr = new String[n];
        int i2 = 0;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            strArr[i2 % strArr.length] = readLine;
            i2++;
        }
        int i3 = i2 - n;
        if (i3 >= 0) {
            i = i3;
        }
        int i4 = i;
        String str = BuildConfig.VERSION_NAME;
        i3 = i4;
        while (i3 < i2) {
            i3++;
            str = str + strArr[i3 % strArr.length] + "\n";
        }
        i2 = str.lastIndexOf("--------- beginning");
        if (i2 < 0) {
            return str;
        }
        return str.substring(i2);
    }

    private Spannable m111a(String str) {
        int i = 0;
        Spannable spannableString = new SpannableString(str);
        String[] split = str.split("\\n");
        int length = split.length;
        int i2 = 0;
        while (i < length) {
            String str2 = split[i];
            if (str2.contains("E/RobotCore") || str2.contains(DbgLog.ERROR_PREPEND)) {
                spannableString.setSpan(new ForegroundColorSpan(-65536), i2, str2.length() + i2, 33);
            }
            i2 = (i2 + str2.length()) + 1;
            i++;
        }
        return spannableString;
    }
}

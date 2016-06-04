package com.qualcomm.ftccommon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.CHIPTOP_CMD;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftcdriverstation.BuildConfig;
import com.qualcomm.wirelessp2p.WifiDirectChannelSelection;
import java.io.IOException;

public class FtcWifiChannelSelectorActivity extends Activity implements OnClickListener, OnItemSelectedListener {
    private static int f181a;
    private Button f182b;
    private Button f183c;
    private Spinner f184d;
    private ProgressDialog f185e;
    private WifiDirectChannelSelection f186f;
    private int f187g;
    private int f188h;
    private Context f189i;

    /* renamed from: com.qualcomm.ftccommon.FtcWifiChannelSelectorActivity.1 */
    class C00331 implements Runnable {
        final /* synthetic */ FtcWifiChannelSelectorActivity f177a;

        /* renamed from: com.qualcomm.ftccommon.FtcWifiChannelSelectorActivity.1.1 */
        class C00321 implements Runnable {
            final /* synthetic */ C00331 f176a;

            C00321(C00331 c00331) {
                this.f176a = c00331;
            }

            public void run() {
                this.f176a.f177a.setResult(-1);
                this.f176a.f177a.f185e.dismiss();
                this.f176a.f177a.finish();
            }
        }

        C00331(FtcWifiChannelSelectorActivity ftcWifiChannelSelectorActivity) {
            this.f177a = ftcWifiChannelSelectorActivity;
        }

        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            this.f177a.runOnUiThread(new C00321(this));
        }
    }

    /* renamed from: com.qualcomm.ftccommon.FtcWifiChannelSelectorActivity.2 */
    class C00342 implements Runnable {
        final /* synthetic */ String f178a;
        final /* synthetic */ int f179b;
        final /* synthetic */ FtcWifiChannelSelectorActivity f180c;

        C00342(FtcWifiChannelSelectorActivity ftcWifiChannelSelectorActivity, String str, int i) {
            this.f180c = ftcWifiChannelSelectorActivity;
            this.f178a = str;
            this.f179b = i;
        }

        public void run() {
            Toast.makeText(this.f180c.f189i, this.f178a, this.f179b).show();
        }
    }

    public FtcWifiChannelSelectorActivity() {
        this.f187g = -1;
        this.f188h = -1;
    }

    static {
        f181a = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.activity_ftc_wifi_channel_selector);
        this.f189i = this;
        this.f184d = (Spinner) findViewById(C0035R.id.spinnerChannelSelect);
        SpinnerAdapter createFromResource = ArrayAdapter.createFromResource(this, C0035R.array.wifi_direct_channels, 17367048);
        createFromResource.setDropDownViewResource(17367049);
        this.f184d.setAdapter(createFromResource);
        this.f184d.setOnItemSelectedListener(this);
        this.f182b = (Button) findViewById(C0035R.id.buttonConfigure);
        this.f182b.setOnClickListener(this);
        this.f183c = (Button) findViewById(C0035R.id.buttonWifiSettings);
        this.f183c.setOnClickListener(this);
        this.f186f = new WifiDirectChannelSelection(this, (WifiManager) getSystemService("wifi"));
    }

    protected void onStart() {
        super.onStart();
        this.f184d.setSelection(f181a);
    }

    public void onItemSelected(AdapterView<?> adapterView, View v, int item, long l) {
        switch (item) {
            case SpiSlaveResponseEvent.OK /*0*/:
                this.f187g = -1;
                this.f188h = -1;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                this.f187g = 81;
                this.f188h = 1;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                this.f187g = 81;
                this.f188h = 6;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                this.f187g = 81;
                this.f188h = 11;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                this.f187g = 124;
                this.f188h = 149;
            case BuildConfig.VERSION_CODE /*5*/:
                this.f187g = 124;
                this.f188h = 153;
            case FT4222_STATUS.FT4222_INVALID_PARAMETER /*6*/:
                this.f187g = 124;
                this.f188h = 157;
            case FT4222_STATUS.FT4222_INVALID_BAUD_RATE /*7*/:
                this.f187g = 124;
                this.f188h = CHIPTOP_CMD.CHIPTOP_SET_DS_CTL1_REG;
            default:
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onClick(View v) {
        if (v.getId() == C0035R.id.buttonConfigure) {
            f181a = this.f184d.getSelectedItemPosition();
            m101a();
        } else if (v.getId() == C0035R.id.buttonWifiSettings) {
            DbgLog.msg("launch wifi settings");
            startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
        }
    }

    private void m101a() {
        DbgLog.msg(String.format("configure p2p channel - class %d channel %d", new Object[]{Integer.valueOf(this.f187g), Integer.valueOf(this.f188h)}));
        try {
            this.f185e = ProgressDialog.show(this, "Configuring Channel", "Please Wait", true);
            this.f186f.config(this.f187g, this.f188h);
            new Thread(new C00331(this)).start();
        } catch (IOException e) {
            m102a("Failed - root is required", 0);
            e.printStackTrace();
        }
    }

    private void m102a(String str, int i) {
        runOnUiThread(new C00342(this, str, i));
    }
}

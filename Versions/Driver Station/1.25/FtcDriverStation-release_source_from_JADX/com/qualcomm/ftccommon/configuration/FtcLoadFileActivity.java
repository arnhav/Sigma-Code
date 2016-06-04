package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qualcomm.ftccommon.C0035R;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class FtcLoadFileActivity extends Activity {
    public static final String CONFIGURE_FILENAME = "CONFIGURE_FILENAME";
    OnClickListener f386a;
    private ArrayList<String> f387b;
    private Context f388c;
    private Utility f389d;

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcLoadFileActivity.1 */
    class C00831 implements View.OnClickListener {
        final /* synthetic */ FtcLoadFileActivity f383a;

        C00831(FtcLoadFileActivity ftcLoadFileActivity) {
            this.f383a = ftcLoadFileActivity;
        }

        public void onClick(View view) {
            Builder buildBuilder = this.f383a.f389d.buildBuilder("Available files", "These are the files the Hardware Wizard was able to find. You can edit each file by clicking the edit button. The 'Activate' button will set that file as the current configuration file, which will be used to start the robot.");
            buildBuilder.setPositiveButton("Ok", this.f383a.f386a);
            AlertDialog create = buildBuilder.create();
            create.show();
            ((TextView) create.findViewById(16908299)).setTextSize(14.0f);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcLoadFileActivity.2 */
    class C00842 implements View.OnClickListener {
        final /* synthetic */ FtcLoadFileActivity f384a;

        C00842(FtcLoadFileActivity ftcLoadFileActivity) {
            this.f384a = ftcLoadFileActivity;
        }

        public void onClick(View view) {
            Builder buildBuilder = this.f384a.f389d.buildBuilder("AutoConfigure", "This is the fastest way to get a new machine up and running. The AutoConfigure tool will automatically enter some default names for devices. AutoConfigure expects certain devices.  If there are other devices attached, the AutoConfigure tool will not name them.");
            buildBuilder.setPositiveButton("Ok", this.f384a.f386a);
            AlertDialog create = buildBuilder.create();
            create.show();
            ((TextView) create.findViewById(16908299)).setTextSize(14.0f);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.configuration.FtcLoadFileActivity.3 */
    class C00853 implements OnClickListener {
        final /* synthetic */ FtcLoadFileActivity f385a;

        C00853(FtcLoadFileActivity ftcLoadFileActivity) {
            this.f385a = ftcLoadFileActivity;
        }

        public void onClick(DialogInterface dialog, int button) {
        }
    }

    public FtcLoadFileActivity() {
        this.f387b = new ArrayList();
        this.f386a = new C00853(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.activity_load);
        this.f388c = this;
        this.f389d = new Utility(this);
        this.f389d.createConfigFolder();
        m258a();
    }

    protected void onStart() {
        super.onStart();
        this.f387b = this.f389d.getXMLFiles();
        m259b();
        this.f389d.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        m260c();
    }

    private void m258a() {
        ((Button) findViewById(C0035R.id.files_holder).findViewById(C0035R.id.info_btn)).setOnClickListener(new C00831(this));
        ((Button) findViewById(C0035R.id.autoconfigure_holder).findViewById(C0035R.id.info_btn)).setOnClickListener(new C00842(this));
    }

    private void m259b() {
        if (this.f387b.size() == 0) {
            this.f389d.setOrangeText("No files found!", "In order to proceed, you must create a new file", C0035R.id.empty_filelist, C0035R.layout.orange_warning, C0035R.id.orangetext0, C0035R.id.orangetext1);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) findViewById(C0035R.id.empty_filelist);
        viewGroup.removeAllViews();
        viewGroup.setVisibility(8);
    }

    private void m260c() {
        ViewGroup viewGroup = (ViewGroup) findViewById(C0035R.id.inclusionlayout);
        viewGroup.removeAllViews();
        Iterator it = this.f387b.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            View inflate = LayoutInflater.from(this).inflate(C0035R.layout.file_info, null);
            viewGroup.addView(inflate);
            ((TextView) inflate.findViewById(C0035R.id.filename_editText)).setText(str);
        }
    }

    public void new_button(View v) {
        this.f389d.saveToPreferences(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename);
        startActivity(new Intent(this.f388c, FtcConfigurationActivity.class));
    }

    public void file_edit_button(View v) {
        this.f389d.saveToPreferences(m257a(v, true), C0035R.string.pref_hardware_config_filename);
        startActivity(new Intent(this.f388c, FtcConfigurationActivity.class));
    }

    public void file_activate_button(View v) {
        this.f389d.saveToPreferences(m257a(v, false), C0035R.string.pref_hardware_config_filename);
        this.f389d.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
    }

    public void file_delete_button(View v) {
        String a = m257a(v, true);
        File file = new File(Utility.CONFIG_FILES_DIR + a);
        if (file.exists()) {
            file.delete();
        } else {
            this.f389d.complainToast("That file does not exist: " + a, this.f388c);
            DbgLog.error("Tried to delete a file that does not exist: " + a);
        }
        this.f387b = this.f389d.getXMLFiles();
        this.f389d.saveToPreferences(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename);
        this.f389d.updateHeader(Utility.NO_FILE, C0035R.string.pref_hardware_config_filename, C0035R.id.active_filename, C0035R.id.included_header);
        m260c();
    }

    private String m257a(View view, boolean z) {
        String charSequence = ((TextView) ((LinearLayout) ((LinearLayout) view.getParent()).getParent()).findViewById(C0035R.id.filename_editText)).getText().toString();
        if (z) {
            return charSequence + Utility.FILE_EXT;
        }
        return charSequence;
    }

    public void launch_autoConfigure(View v) {
        startActivity(new Intent(getBaseContext(), AutoConfigureActivity.class));
    }

    public void onBackPressed() {
        String filenameFromPrefs = this.f389d.getFilenameFromPrefs(C0035R.string.pref_hardware_config_filename, Utility.NO_FILE);
        Intent intent = new Intent();
        intent.putExtra(CONFIGURE_FILENAME, filenameFromPrefs);
        setResult(-1, intent);
        finish();
    }
}

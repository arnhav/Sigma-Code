package com.qualcomm.ftcdriverstation;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class OpModeSelectionDialogFragment extends DialogFragment {
    private OpModeSelectionDialogListener listener;
    private String[] opModes;

    /* renamed from: com.qualcomm.ftcdriverstation.OpModeSelectionDialogFragment.1 */
    class C00981 implements OnClickListener {
        C00981() {
        }

        public void onClick(DialogInterface dialogInterface, int selectionIndex) {
            if (OpModeSelectionDialogFragment.this.listener != null) {
                OpModeSelectionDialogFragment.this.listener.onSelectionClick(OpModeSelectionDialogFragment.this.opModes[selectionIndex]);
            }
        }
    }

    public interface OpModeSelectionDialogListener {
        void onSelectionClick(String str);
    }

    public OpModeSelectionDialogFragment() {
        this.opModes = new String[0];
        this.listener = null;
    }

    public void setOpModes(String[] opModes) {
        this.opModes = opModes;
    }

    public void setOnSelectionDialogListener(OpModeSelectionDialogListener listener) {
        this.listener = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View customTitle = LayoutInflater.from(getActivity()).inflate(C0099R.layout.custom_dialog_title_bar, null);
        Builder builder = new Builder(getActivity());
        builder.setCustomTitle(customTitle);
        builder.setTitle(C0099R.string.dialog_title_select_op_mode);
        builder.setItems(this.opModes, new C00981());
        return builder.create();
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.findViewById(dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null)).setBackgroundColor(getResources().getColor(C0099R.color.very_bright_red));
    }
}

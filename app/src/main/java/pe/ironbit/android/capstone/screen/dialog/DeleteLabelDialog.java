package pe.ironbit.android.capstone.screen.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.screen.fragment.ManagerLabelFragment;

public class DeleteLabelDialog extends DialogFragment {
    public static final String DELETE_LABEL_DIALOG_ITEM = "DELETE_LABEL_DIALOG_ITEM";

    public static DeleteLabelDialog newInstance(Integer index) {
        Bundle bundle = new Bundle();
        bundle.putInt(DELETE_LABEL_DIALOG_ITEM, index);

        DeleteLabelDialog dialog = new DeleteLabelDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    public DeleteLabelDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Integer index = getArguments().getInt(DELETE_LABEL_DIALOG_ITEM);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.delete_label_dialog_message));

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
                ((ManagerLabelFragment)fragment).doOnEraseLabelAcceptAction(index);
            }
        });

        return builder.create();
    }
}

package pe.ironbit.android.capstone.screen.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.screen.fragment.ManagerLabelFragment;

public class EditLabelDialog extends DialogFragment {
    private static final String LABEL_INDEX = "LABEL_ID";

    private static final String LABEL_NAME = "LABEL_NAME";

    @BindView(R.id.dialog_create_label_name)
    EditText editText;

    public static EditLabelDialog newInstance(int index, String labelName) {
        Bundle bundle = new Bundle();
        bundle.putInt(LABEL_INDEX, index);
        bundle.putString(LABEL_NAME, labelName);

        EditLabelDialog dialog = new EditLabelDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    public EditLabelDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final int index = bundle.getInt(LABEL_INDEX);
        final String labelName = bundle.getString(LABEL_NAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_label, null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        builder.setTitle(R.string.manager_label_edit_label_title);
        editText.setText(labelName);

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
                ((ManagerLabelFragment)fragment).doOnEditLabelCancelAction();
            }
        });

        builder.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String labelName = editText.getText().toString();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
                ((ManagerLabelFragment)fragment).doOnEditLabelAcceptAction(index, labelName);
            }
        });

        return builder.create();
    }
}

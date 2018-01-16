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

public class CreateLabelDialog extends DialogFragment {
    private static final String LABEL_IDENTIFIER = "LABEL_IDENTIFIER";

    @BindView(R.id.dialog_create_label_name)
    EditText editText;

    public static CreateLabelDialog newInstance(Integer labelId) {
        Bundle bundle = new Bundle();
        bundle.putInt(LABEL_IDENTIFIER, labelId);

        CreateLabelDialog dialog = new CreateLabelDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Integer labelId = getArguments().getInt(LABEL_IDENTIFIER);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_label, null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        builder.setTitle(R.string.manager_label_create_label_title);

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
                ((ManagerLabelFragment)fragment).doOnCreateLabelCancelAction();
            }
        });

        builder.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String labelName = editText.getText().toString();
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
                ((ManagerLabelFragment)fragment).doOnCreateLabelAcceptAction(labelId, labelName);
            }
        });

        return builder.create();
    }
}

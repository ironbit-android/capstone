package pe.ironbit.android.capstone.screen.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.screen.fragment.BookMenuFragment;

public class DeleteBookDialog extends DialogFragment {
    public DeleteBookDialog() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.fragment_book_menu_erase_book_message));

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
                ((BookMenuFragment)fragment).doOnEraseBookCancelled();
            }
        });

        builder.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
                ((BookMenuFragment)fragment).doOnEraseBookAccepted();
            }
        });

        return builder.create();
    }
}

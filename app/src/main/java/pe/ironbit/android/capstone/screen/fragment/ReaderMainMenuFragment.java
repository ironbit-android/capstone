package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.ironbit.android.capstone.R;

public class ReaderMainMenuFragment extends Fragment {
    public ReaderMainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_reader_main_menu, container, false);
    }
}

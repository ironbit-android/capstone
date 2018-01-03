package pe.ironbit.android.capstone.screen.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.ironbit.android.capstone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookMenuFragment extends Fragment {

    public static BookMenuFragment newInstance() {
        BookMenuFragment fragment = new BookMenuFragment();
        return fragment;
    }

    public BookMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_menu, container, false);
    }

}

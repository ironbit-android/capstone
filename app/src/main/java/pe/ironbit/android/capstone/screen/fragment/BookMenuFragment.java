package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;

public class BookMenuFragment extends Fragment {
    public static final String BOOK_MENU_LIST_KEY = "BOOK_MENU_LIST_KEY";

    private List<BookPrimeParcelable> books;

    public static BookMenuFragment newInstance(ArrayList<BookPrimeParcelable> list) {
        Bundle argument = new Bundle();
        argument.putParcelableArrayList(BOOK_MENU_LIST_KEY, list);

        BookMenuFragment fragment = new BookMenuFragment();
        fragment.setArguments(argument);

        return fragment;
    }

    public BookMenuFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            books = bundle.getParcelableArrayList(BOOK_MENU_LIST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_menu, container, false);
    }

    private void loadBookDataIntoBookMenuScreen() {
    }
}

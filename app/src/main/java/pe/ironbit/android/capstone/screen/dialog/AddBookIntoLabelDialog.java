package pe.ironbit.android.capstone.screen.dialog;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookData;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookFactory;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookMapper;
import pe.ironbit.android.capstone.screen.activity.LibraryActivity;
import pe.ironbit.android.capstone.screen.fragment.BookMenuFragment;
import pe.ironbit.android.capstone.storage.contract.LabelBookContract;
import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.LabelBookLoader;
import pe.ironbit.android.capstone.storage.loader.LabelPrimeLoader;
import pe.ironbit.android.capstone.view.menulabel.MenuLabelAdapter;

public class AddBookIntoLabelDialog extends DialogFragment {
    private static final String BOOK_PRIME_LIST = "BOOK_PRIME_LIST";

    private List<BookPrimeData> selectedBooks;

    private MenuLabelAdapter adapter;

    @BindView(R.id.dialog_add_book_into_label_cancel_button)
    Button cancelButton;

    @BindView(R.id.dialog_add_book_into_label_collection)
    RecyclerView recyclerView;

    public static AddBookIntoLabelDialog newInstance(List<BookPrimeData> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BOOK_PRIME_LIST, (ArrayList)BookPrimeFactory.createBookPrimeParcelableList(list));

        AddBookIntoLabelDialog dialog = new AddBookIntoLabelDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    public AddBookIntoLabelDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        selectedBooks = BookPrimeFactory.createBookPrimeDataList(bundle.<BookPrimeParcelable>getParcelableArrayList(BOOK_PRIME_LIST));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_book_into_label, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        configure();
        loadLabelPrimeData();
    }

    private void configure() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOnAddBookIntoLabelDialogCancel();
            }
        });

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new MenuLabelAdapter(new BaseListener<Integer>() {
            @Override
            public void update(Integer index) {
                loadLabelBookData(adapter.get(index).getLabelId());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void performAddBookAction(final int labelId, final List<LabelBookData> labelBookDataList) {
        getView().post(new Runnable() {
            @Override
            public void run() {
                for (BookPrimeData select : selectedBooks) {
                    boolean found = false;
                    for (LabelBookData labelBookData : labelBookDataList) {
                        if (select.getBookId() == labelBookData.getBookId()) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        ContentResolver resolver = getActivity().getContentResolver();
                        LabelBookMapper.insert(resolver, LabelBookFactory.create(labelId, select.getBookId()));
                    }
                }

                doOnAddBookIntoLabelDialogAccept();
            }
        });

    }

    private void update(List list) {
        adapter.update(list);
    }

    private void loadLabelPrimeData() {
        LabelPrimeLoader loader = new LabelPrimeLoader(getContext());
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(List list) {
                getLoaderManager().destroyLoader(LabelPrimeContract.LOADER_IDENTIFIER);
                update(list);
            }
        });
        getLoaderManager().initLoader(LabelPrimeContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadLabelBookData(final int labelId) {
        getView().post(new Runnable() {
            @Override
            public void run() {
                LabelBookLoader loader = new LabelBookLoader(getContext());
                loader.LoadLabelList(labelId);
                loader.setListener(new OnStorageListener() {
                    @Override
                    public void onEvent(List list) {
                        getLoaderManager().destroyLoader(LabelBookContract.LOADER_IDENTIFIER);
                        performAddBookAction(labelId, list);
                    }
                });
                getLoaderManager().initLoader(LabelBookContract.LOADER_IDENTIFIER, null, loader);
            }
        });
    }

    private void doOnAddBookIntoLabelDialogCancel() {
        View view = ((LibraryActivity)getActivity()).getPrimeView();
        String message = getString(R.string.menu_selection_book_add_failure);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();

        doOnFinish();
    }

    private void doOnAddBookIntoLabelDialogAccept() {
        View view = ((LibraryActivity)getActivity()).getPrimeView();
        String message = getString(R.string.menu_selection_book_add_success);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();

        doOnFinish();
    }

    private void doOnFinish() {
        dismiss();

        Fragment fragment = getFragmentManager().findFragmentByTag(BookMenuFragment.class.getSimpleName());
        ((BookMenuFragment)fragment).performOnFinishAction();
    }
}

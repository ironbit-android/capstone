package pe.ironbit.android.capstone.screen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.screen.fragment.BookMenuFragment;
import pe.ironbit.android.capstone.screen.fragment.MainMenuFragment;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        initializeScreen();
    }

    protected void initializeScreen() {
        FragmentManager manager = getSupportFragmentManager();

        Fragment mainMenuFragment = MainMenuFragment.newInstance();
        manager.beginTransaction()
               .add(R.id.layout_library_main_menu, mainMenuFragment, MainMenuFragment.class.getSimpleName())
               .commit();

        Fragment bookMenuFragment = BookMenuFragment.newInstance();
        manager.beginTransaction()
               .add(R.id.layout_library_book_menu, bookMenuFragment, BookMenuFragment.class.getSimpleName())
               .commit();
    }
}

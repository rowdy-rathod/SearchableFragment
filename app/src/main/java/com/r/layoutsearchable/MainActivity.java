package com.r.layoutsearchable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    private static final String ACTIVITY_NAME = MainActivity.class.getSimpleName();
    private int entryCount = 0;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    Toolbar toolbar;
    SearchView searchView = null;

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find the toolbar from activity_main
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        // if you want to show the fragment onCreate
//        SearchableFragment mainFragment = new SearchableFragment();
//        addFragment(mainFragment, "MainActivity");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setFitsSystemWindows(true);
            setSearchView(searchView);


            // traverse the view to the widget containing the hint text
            LinearLayout ll = (LinearLayout) searchView.getChildAt(0);
            LinearLayout ll2 = (LinearLayout) ll.getChildAt(2);
            LinearLayout ll3 = (LinearLayout) ll2.getChildAt(1);
            SearchView.SearchAutoComplete autoComplete = (SearchView.SearchAutoComplete) ll3.getChildAt(0);
            // set the hint text color
            autoComplete.setHintTextColor(getResources().getColor(R.color.white));
            // set the text color
            autoComplete.setTextColor(getResources().getColor(R.color.white));
            // set the cursor background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                autoComplete.setTextCursorDrawable(R.drawable.custume_cursor);
            }
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    Log.e("Action==>", "Expand");
                    // attach the fragment if expand the searchView
                    SearchableFragment mainFragment = new SearchableFragment();
                    addFragment(mainFragment, "MainActivity");
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    Log.e("Action==>", "Collapse");
                    // remove the fragment or dettach the fragment if click on collapse(Back) button
                    // top left button
                    // onBackPressed button handle the count of fragment attach in the container
                    //
                    onBackPressed();
                    return true;
                }
            });

            // change the close(clear) button
            ImageView closeIcon = searchView.findViewById(R.id.search_close_btn);
            closeIcon.setImageResource(R.drawable.ic_close_black_24dp);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addFragment(Fragment fragment, String callingFrom) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "demoFragment");
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        entryCount = fragmentManager.getBackStackEntryCount();
        if (entryCount == 1) {
            super.onBackPressed();
        } else {
            finish();
        }

    }

    @Override
    public void onBackStackChanged() {
        entryCount = fragmentManager.getBackStackEntryCount();
        if (entryCount > 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    // if want to handle backpress from the fragment then call this method for backpress
    public void onBackPressButton() {
        entryCount = fragmentManager.getBackStackEntryCount();
//        if (searchView.isSearchOpen()) {
//            searchView.closeSearch();
//        } else {
//            if (entryCount == 1) {
//                setToolbarTitle("MainFragment");
//                finish();
//            } else {
//                super.onBackPressed();
//            }
//        }
    }
}

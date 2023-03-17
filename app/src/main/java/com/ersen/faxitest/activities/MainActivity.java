package com.ersen.faxitest.activities;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.ersen.faxitest.R;
import com.ersen.faxitest.adapters.ToolbarTitle;
import com.ersen.faxitest.adapters.BookDetailLauncher;
import com.ersen.faxitest.adapters.BookSearchingLauncher;

import de.greenrobot.event.EventBus;

public class MainActivity extends ToolbarActivity implements FragmentManager.OnBackStackChangedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        EventBus.getDefault().register(this);
        replaceFragment(new BookSearchingActivity(), R.id.fragmentContainer, false, BookSearchingActivity.class.getName(), true);
    }

    private void showBookSearchResultsFragment(String searchQuery){
        BookSearchingResultActivity bookSearchingResultActivity = BookSearchingResultActivity.newInstance(searchQuery);
        replaceFragment(bookSearchingResultActivity, R.id.fragmentContainer, true, BookSearchingResultActivity.class.getName(), true);
    }

    private void showBookDetailFragment(String linkToBookDetails){
        BookDetailActivity bookDetailActivity = BookDetailActivity.newInstance(linkToBookDetails);
        replaceFragment(bookDetailActivity, R.id.fragmentContainer, true, BookDetailActivity.class.getName(), true);
    }

    /** Event Bus subscribers*/

    public void onEvent(BookSearchingLauncher bookSearchingLauncher){
        showBookSearchResultsFragment(bookSearchingLauncher.getSearchQuery());
    }

    public void onEvent(BookDetailLauncher bookDetailLauncher){
        showBookDetailFragment(bookDetailLauncher.getLinkToBookDetails());
    }

    public void onEvent(ToolbarTitle toolbarTitle){
        setToolbarTitle(toolbarTitle.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackStackChanged() {
        displayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() != 0);
    }
}

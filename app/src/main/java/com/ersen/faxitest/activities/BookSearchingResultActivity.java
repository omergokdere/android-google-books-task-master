package com.ersen.faxitest.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ersen.faxitest.R;
import com.ersen.faxitest.application.Moblib;
import com.ersen.faxitest.application.MobLibConstant;
import com.ersen.faxitest.adapters.BookAdapter;
import com.ersen.faxitest.adapters.BookListAdapter;
import com.ersen.faxitest.adapters.ToolbarTitle;
import com.ersen.faxitest.connection.ErrorAPI;
import com.ersen.faxitest.utilities.RecyclerViewEndlessOnScrollListener;
import com.ersen.faxitest.utilities.VisibilityManager;
import com.ersen.faxitest.views.SpaceBetweenView;
import com.ersen.faxitest.adapters.BookSearchResultsAdapter;
import com.ersen.faxitest.views.PlaceholderView;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BookSearchingResultActivity extends Fragment implements PlaceholderView.OnRetryButtonPressedListener {

    private RecyclerView mListOfBooks;
    private VisibilityManager mVisibilityManager;
    private BookSearchResultsAdapter mAdapter;
    private String mSearchQuery;
    private int mStartIndex; //Used for paging. 0 is default. Google Books API starts at 0 for the first element in the collection.
    private boolean mIsDoingSearch;

    public static BookSearchingResultActivity newInstance(String searchQueryParams) {
        BookSearchingResultActivity bookSearchingResultActivity = new BookSearchingResultActivity();
        Bundle args = new Bundle();
        args.putString(MobLibConstant.BundleConstants.SEARCH_QUERY_PARAMS, searchQueryParams);
        bookSearchingResultActivity.setArguments(args);
        return bookSearchingResultActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchQuery = getArguments().getString(MobLibConstant.BundleConstants.SEARCH_QUERY_PARAMS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book_searching_result, container, false);
        mListOfBooks = (RecyclerView) view.findViewById(R.id.list);
        mListOfBooks.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mListOfBooks.setLayoutManager(linearLayoutManager);
        mListOfBooks.addItemDecoration(new SpaceBetweenView(10));
        mListOfBooks.addOnScrollListener(new RecyclerViewEndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                carryOutSearch();
            }
        });

        PlaceholderView placeholderView = (PlaceholderView) view.findViewById(R.id.placeholderView);
        placeholderView.setOnRetryButtonPressedListener(this);
        mVisibilityManager = new VisibilityManager(placeholderView, mListOfBooks);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().post(new ToolbarTitle(Moblib.getInstance().getResources().getString(R.string.toolbarBookSearchingResult)));
        handleInitialContent();
    }

    private void handleInitialContent() {
        if (mStartIndex == 0) { //If we are coming back from the back stack, only the view is destroyed but not the members.
            mAdapter = new BookSearchResultsAdapter(new ArrayList<BookAdapter>());
            mListOfBooks.setAdapter(mAdapter);
            mVisibilityManager.showLoading(Moblib.getInstance().getResources().getString(R.string.bookSearchingLoading));
            carryOutSearch();
        } else {
            mListOfBooks.setAdapter(mAdapter);
            mVisibilityManager.showMainContent();
        }
    }

    /** Checking mStartIndex is equal to 0 is a way to check if we have not on the first page already so that I can display failure or no books placeholder if it is the first request. This is important because if the user is scrolling
     *  to get the next page of results, I do not want to show these placeholder if something goes wrong thus
     * */
    private void carryOutSearch(){
        if(!mIsDoingSearch){
            if(mStartIndex > 0){ /** Show a progress item if the user is getting the next page of results */
                mAdapter.addProgressItem();
            }
            mIsDoingSearch = true;
            Call<BookListAdapter> call = Moblib.getInstance().getNetworkAPI().searchForBooks(mSearchQuery,mStartIndex);
            call.enqueue(new Callback<BookListAdapter>() {
                @Override
                public void onResponse(Response<BookListAdapter> response, Retrofit retrofit) {
                    if(isAdded()){
                        mIsDoingSearch = false;
                        mAdapter.removeProgressItem(); /** Remove anyway, it is safe*/
                        if(response.isSuccess()){
                            ArrayList<BookAdapter> books = response.body().getBookItems();
                            if(books != null){ /** If the search yielded no books, the array is null so  */
                                if(mStartIndex == 0){
                                    mVisibilityManager.showMainContent();
                                }
                                mAdapter.addItems(books);
                                mStartIndex = mAdapter.getItemCount() + 1; /** Used for pagination. We must tell the API where we want to start in the collection of books. Simple calculation of our current adapter array size + 1 to start from the next book   */
                            }else{
                                if(mStartIndex == 0){
                                    mVisibilityManager.showFailure(Moblib.getInstance().getResources().getString(R.string.noBooks));
                                }
                            }
                        }else{
                            if(mStartIndex == 0){
                                mVisibilityManager.showFailure(ErrorAPI.getUnsuccessfulRequestMessage(response));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mIsDoingSearch = false;
                    mAdapter.removeProgressItem();
                    if(mStartIndex == 0){
                        mVisibilityManager.showFailure(ErrorAPI.getFailedRequestMessage(t));
                    }
                }
            });
        }

    }

    @Override
    public void onRetryPressed() {
        handleInitialContent();
    }
}

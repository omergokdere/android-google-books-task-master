package com.ersen.faxitest.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ersen.faxitest.R;
import com.ersen.faxitest.application.Moblib;
import com.ersen.faxitest.application.MobLibConstant;
import com.ersen.faxitest.adapters.BookAdapter;
import com.ersen.faxitest.adapters.ToolbarTitle;
import com.ersen.faxitest.connection.ErrorAPI;
import com.ersen.faxitest.utilities.IntentUtils;
import com.ersen.faxitest.utilities.VisibilityManager;
import com.ersen.faxitest.views.PlaceholderView;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BookDetailActivity extends Fragment implements PlaceholderView.OnRetryButtonPressedListener, View.OnClickListener {

    private BookAdapter mBookFull;
    private VisibilityManager mVisibilityManager;

    public static BookDetailActivity newInstance(String linkToBookDetails) {
        BookDetailActivity bookDetailActivity = new BookDetailActivity();
        Bundle args = new Bundle();
        args.putString(MobLibConstant.BundleConstants.BOOK_SELF_LINK, linkToBookDetails);
        bookDetailActivity.setArguments(args);
        return bookDetailActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book_detail, container, false);
        PlaceholderView placeholderView = (PlaceholderView) view.findViewById(R.id.placeholderView);
        placeholderView.setOnRetryButtonPressedListener(this);
/*        View viewOnGoogleBooks = view.findViewById(R.id.viewOnGoogleBooks);
        viewOnGoogleBooks.setOnClickListener(this);*/
        mVisibilityManager = new VisibilityManager(placeholderView,/*viewOnGoogleBooks, */view.findViewById(R.id.mainContent));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().post(new ToolbarTitle(Moblib.getInstance().getResources().getString(R.string.toolbarBookDetail)));
        handleInitialContent();
    }

    private void handleInitialContent() {
        if (mBookFull == null) {
            mVisibilityManager.showLoading(Moblib.getInstance().getResources().getString(R.string.bookDetailLoading));
            getFullDetailsOfBook();
        } else {
            showBookDetails();
        }
    }

    private void getFullDetailsOfBook() {
        String urlToSelfLink = getArguments().getString(MobLibConstant.BundleConstants.BOOK_SELF_LINK);
        Call<BookAdapter> call = Moblib.getInstance().getNetworkAPI().getDetailsOfBook(urlToSelfLink);
        call.enqueue(new Callback<BookAdapter>() {
            @Override
            public void onResponse(Response<BookAdapter> response, Retrofit retrofit) {
                if (isAdded()) {
                    if (response.isSuccess()) {
                        mBookFull = response.body();
                        showBookDetails();
                    } else {
                        mVisibilityManager.showFailure(ErrorAPI.getUnsuccessfulRequestMessage(response));
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                mVisibilityManager.showFailure(ErrorAPI.getFailedRequestMessage(t));
            }
        });

    }

    private void showBookDetails() {
        final View view = getView();
        if(view != null){
            ImageView bookImage = (ImageView)view.findViewById(R.id.bookImage);
            TextView bookTitle = (TextView)view.findViewById(R.id.bookTitle);
            TextView bookAuthor = (TextView)view.findViewById(R.id.bookAuthor);
            TextView description = (TextView)view.findViewById(R.id.description);
            TextView categories = (TextView)view.findViewById(R.id.categories);
            TextView publisher = (TextView)view.findViewById(R.id.publisher);
            TextView miscellaneous = (TextView)view.findViewById(R.id.miscellaneous);
            TextView identifier = (TextView)view.findViewById(R.id.identifier);

            if(mBookFull.getBookInfo().getBookImages() != null){
                Glide.with(getActivity()).load(mBookFull.getBookInfo().getBookImages().getLargestPossibleImage()).fitCenter().into(bookImage);
            }

            bookTitle.setText(mBookFull.getBookInfo().getTitle());
            bookAuthor.setText(mBookFull.getBookInfo().getAuthors());
            description.setText(Html.fromHtml(mBookFull.getBookInfo().getDescription()));
            categories.setText(mBookFull.getBookInfo().getCategory());
            publisher.setText(Moblib.getInstance().getResources().getString(R.string.bookDetailPublisherInfo, mBookFull.getBookInfo().getPublisher(), mBookFull.getBookInfo().getPublishedDate()));
            miscellaneous.setText(Moblib.getInstance().getResources().getString(R.string.bookDetailMiscellaneousInfo, mBookFull.getBookInfo().getLanguage(), mBookFull.getBookInfo().getPageCount()));
            identifier.setText(mBookFull.getBookInfo().getBookIdentifiers());
        }
        mVisibilityManager.showMainContent();
    }


    @Override
    public void onRetryPressed() {
        handleInitialContent();
    }

    @Override
    public void onClick(View view) {
/*        switch (view.getId()){
            case R.id.viewOnGoogleBooks:
                IntentUtils.startInternetBrowser(getActivity(),mBookFull.getBookInfo().getUrlToBookOnGoogleBooks());
                break;
        }*/
    }
}

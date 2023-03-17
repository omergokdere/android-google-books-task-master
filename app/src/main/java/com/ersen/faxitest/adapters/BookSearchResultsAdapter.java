package com.ersen.faxitest.adapters;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ersen.faxitest.R;
import com.ersen.faxitest.views.ProgressBarView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class BookSearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int VIEW_BOOK_ITEM = 1, VIEW_PROGRESS_ITEM = 2;
    private ArrayList<BookAdapter> mBooks;
    private long mLastClickTime = 0;

    public BookSearchResultsAdapter(ArrayList<BookAdapter> mItems) {
        this.mBooks = mItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == VIEW_BOOK_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_book, parent, false);
            viewHolder = new BooksSearchResultViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more_scrolling_book_list, parent, false);
            viewHolder = new ProgressBarView(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  BooksSearchResultViewHolder){
            BooksSearchResultViewHolder booksSearchResultViewHolder = (BooksSearchResultViewHolder)holder;
            booksSearchResultViewHolder.setContent(mBooks.get(position));
        }else{
            ((ProgressBarView)holder).getProgressBar().setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        /** If the item is not null its an event item. If its null its progress*/
        return mBooks.get(position)!= null ? VIEW_BOOK_ITEM : VIEW_PROGRESS_ITEM;
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public void addItems(ArrayList<BookAdapter> bookItems){
        int positionStart = mBooks.size() + 1;
        mBooks.addAll(bookItems);
        notifyItemRangeInserted(positionStart,bookItems.size());
    }

    public void addProgressItem(){
        mBooks.add(null);
        notifyItemInserted(mBooks.size());
    }

    public void removeProgressItem(){
        if(mBooks.size() > 0){
            BookAdapter book = mBooks.get(mBooks.size() - 1);
            if(book == null){ ///Check if its a progress item
                mBooks.remove(mBooks.size() - 1);
                notifyItemRemoved(mBooks.size());
            }
        }
    }

    private class BooksSearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mBookImage;
        private TextView mBookTitle, mBookAuthor;

        public BooksSearchResultViewHolder(View itemView) {
            super(itemView);
            mBookImage = (ImageView)itemView.findViewById(R.id.bookImage);
            mBookTitle = (TextView)itemView.findViewById(R.id.bookTitle);
            mBookAuthor = (TextView)itemView.findViewById(R.id.bookAuthor);
            itemView.setOnClickListener(this);
        }

        public void setContent(BookAdapter book){
            mBookTitle.setText(book.getBookInfo().getTitle());
            String authors = book.getBookInfo().getAuthors();
            if(TextUtils.isEmpty(authors)){
                mBookAuthor.setVisibility(View.GONE);
            }else{
                mBookAuthor.setVisibility(View.VISIBLE);
                mBookAuthor.setText(authors);
            }

            if(book.getBookInfo().getBookImages() != null){
                Glide.with(mBookImage.getContext()).load(book.getBookInfo().getBookImages().getLargestPossibleImage()).fitCenter().into(mBookImage);
            }
        }

        @Override
        public void onClick(View view) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 100) { //Another click can only happen even 100ms from each other. Note this does not affect performance of the first click but to prevent simultaneous clicks across multiple items
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            EventBus.getDefault().post(new BookDetailLauncher(mBooks.get(getLayoutPosition()).getLinkToFullDetail()));
        }
    }
}

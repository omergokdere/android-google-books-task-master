package com.ersen.faxitest.adapters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/** Used for deserialization purposes*/
public class BookListAdapter {

    @SerializedName("items")
    private ArrayList<BookAdapter> mBookItems;

    public ArrayList<BookAdapter> getBookItems() {
        return mBookItems;
    }
}

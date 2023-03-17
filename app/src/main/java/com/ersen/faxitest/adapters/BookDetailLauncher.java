package com.ersen.faxitest.adapters;

public class BookDetailLauncher {
    private String mLinkToBookDetails;

    public BookDetailLauncher(String mLinkToBookDetails) {
        this.mLinkToBookDetails = mLinkToBookDetails;
    }

    public String getLinkToBookDetails() {
        return mLinkToBookDetails;
    }
}

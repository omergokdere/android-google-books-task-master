package com.ersen.faxitest.connection;

import com.ersen.faxitest.application.MobLibConstant;
import com.ersen.faxitest.adapters.BookAdapter;
import com.ersen.faxitest.adapters.BookListAdapter;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.Url;

public interface GoogleAPI {

    @GET(MobLibConstant.URLConstants.GOOGLE_BOOKS_API_SEARCH_VOLUMES)
    Call<BookListAdapter> searchForBooks(@Query(MobLibConstant.URLParamConstants.GOOGLE_BOOKS_API_SEARCH_PARAM) String searchQuery, @Query(MobLibConstant.URLParamConstants.GOOGLE_BOOKS_API_START_INDEX) int startIndex);
    @GET
    Call<BookAdapter> getDetailsOfBook(@Url String urlToSelfLink);
}

package com.ersen.faxitest.connection;

import com.ersen.faxitest.R;
import com.ersen.faxitest.application.Moblib;
import java.io.IOException;


import retrofit.Response;

public class ErrorAPI {
    /**
     * This is when the server responds with a non http 200 status request and has some kind of message to display
     *
     * @param response : the entire response obtained from the call
     * @return A string that contains the response to display
     */
    public static String getUnsuccessfulRequestMessage(Response response) {
        //TODO Find out what Google Books API error body looks like so that it can be deserialize and displayed
        return Moblib.getInstance().getResources().getString(R.string.unknownError,response.code());
    }

    /**
     * This is when we get failures from our side rather than the server i.e. our network connection dropped etc.
     */
    public static String getFailedRequestMessage(Throwable throwable) {
        if(throwable instanceof IOException){ //Java SocketTimeoutException is a sub class of IOException so IOException covers all the possible network communication errors
            return Moblib.getInstance().getResources().getString(R.string.noConnection);
        }
       //Unknown cause
        return Moblib.getInstance().getResources().getString(R.string.unknownError);
    }

}

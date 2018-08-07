package com.example.trieveoncooper.ucbook.infrastructure;

/**
 * Created by trieveoncooper on 11/13/17.
 */

import android.app.Application;
import com.squareup.otto.Bus;

public class BookApplication extends Application {
    private Bus bus;

    public BookApplication(){
        bus = new Bus();
    }

    public Bus getBus(){
        return bus;
    }

}


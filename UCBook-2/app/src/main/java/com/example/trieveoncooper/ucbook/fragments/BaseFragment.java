package com.example.trieveoncooper.ucbook.fragments;

import android.os.Bundle;
import com.squareup.otto.Bus;
import android.support.annotation.Nullable;
import com.example.trieveoncooper.ucbook.infrastructure.BookApplication;
import android.app.Fragment;

/**
 * Created by trieveoncooper on 11/13/17.
 */

public class BaseFragment extends Fragment {
    private BookApplication application;
    protected Bus bus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        application = (BookApplication) getActivity().getApplication();
        bus.register(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        bus.unregister(this);
    }
}

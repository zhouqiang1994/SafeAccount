package com.hyl.safeaccount;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by POI on 2017/5/29.
 */

public class HomeFragment extends Fragment {
    private String context ="Home";
    private TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // context = getArguments().getString("context");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        mTextView = (TextView) view.findViewById(R.id.textVIew);
        mTextView.setText(this.context);
        return view;
    }
}

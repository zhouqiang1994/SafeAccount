package com.hyl.safeaccount;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by HYL on 17/5/25.
 */

public class MyFragment extends Fragment {
    private String context = "有东西";
    private TextView mTextView;

    public  static MyFragment newInstance(String context) {
        MyFragment myFragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("context", context);
        myFragment.setArguments(bundle);
        return myFragment;

    }
    public MyFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // context = getArguments().getString("context");


    }

//    public MyFragment(String context){
//        this.context = context;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        mTextView = (TextView) view.findViewById(R.id.textVIew);
        mTextView.setText(this.context);
        return view;
    }
}

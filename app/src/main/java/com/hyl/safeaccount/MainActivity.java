package com.hyl.safeaccount;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioButton rb_home, rb_account, rb_add_account;
    private RadioGroup radioGroup;
    private Fragment fg1, fg2, fg3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();


    }

    private void bindView() {
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_account = (RadioButton) findViewById(R.id.rb_account);
        rb_add_account = (RadioButton) findViewById(R.id.rb_add_account);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(this);
        rb_home.setChecked(true);

    }

    public void hideAllFragment(FragmentTransaction transaction) {
        if (fg1 != null) {
            transaction.hide(fg1);
        }
        if (fg2 != null) {
            transaction.hide(fg2);
        }
        if (fg3 != null) {
            transaction.hide(fg3);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (checkedId) {
            case R.id.rb_home:
                if (fg1 == null) {
                    fg1 = new HomeFragment();
                    transaction.add(R.id.fragment_container, fg1);
                } else {
                    transaction.show(fg1);
                }
                break;
            case R.id.rb_account:
                if (fg2 == null) {
                    fg2 =new AccountFragment();
                    transaction.add(R.id.fragment_container, fg2);
                } else {
                    transaction.show(fg2);
                }
                break;
            case R.id.rb_add_account:
                if (fg3 == null) {
                    fg3 = new AddAccountFragment();
                    transaction.add(R.id.fragment_container, fg3);
                } else {
                    transaction.show(fg3);
                }
                break;
        }
        transaction.commit();
    }
}

package com.hyl.safeaccount;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioButton rb_home, rb_account, rb_add_account;
    private RadioGroup radioGroup;
    private Fragment fg1, fg2, fg3;

    private EditText type,account,password,remark;
    private SQLiteDatabase DB;

    private ListView values;

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


        type=(EditText) findViewById(R.id.type);
        account=(EditText) findViewById(R.id.account);
        password=(EditText) findViewById(R.id.password);
        remark=(EditText) findViewById(R.id.remark);

        values = (ListView) findViewById(R.id.values_list);
        // 获取SQLiteDatabase以操作SQL语句
        DB = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + "/info.db",null);
        // 长按删除
        values.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // 获取所点击项的_id
                TextView tv = (TextView) arg1.findViewById(R.id.tv_id);
                final String id = tv.getText().toString();
                // 通过Dialog提示是否删除
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setMessage("确定要删除吗？");
                // 确定按钮点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(id);
                        replaceList();// 删除后刷新列表
                    }
                });
                // 取消按钮点击事件
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                return true;
            }
        });

        // 点击更新
        values.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 获取_id,username,password项
                TextView tvId = (TextView) arg1.findViewById(R.id.tv_id);

                TextView tvType = (TextView) arg1
                        .findViewById(R.id.tv_type);
                TextView tvAccount = (TextView) arg1
                        .findViewById(R.id.tv_account);
                TextView tvPassword = (TextView) arg1
                        .findViewById(R.id.tv_password);
                TextView tvRemark = (TextView) arg1
                        .findViewById(R.id.tv_remark);
                final String id = tvId.getText().toString();
                String type=tvType.getText().toString();
                String account = tvAccount.getText().toString();
                String password = tvPassword.getText().toString();
                String remark=tvRemark.getText().toString();
                // 通过Dialog弹出修改界面
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("修改");
                // 自定义界面包括两个文本输入框
                View v = View.inflate(MainActivity.this, R.layout.alertdialog,
                        null);
                final EditText etType = (EditText) v
                        .findViewById(R.id.alert_type);
                final EditText etAccount = (EditText) v
                        .findViewById(R.id.alert_account);
                final EditText etPassowrd = (EditText) v
                        .findViewById(R.id.alert_password);
                final EditText etRemark = (EditText) v
                        .findViewById(R.id.alert_remark);
                // Dialog弹出就显示原内容
                etType.setText(type);
                etAccount.setText(account);
                etPassowrd.setText(password);
                etRemark.setText(remark);

                builder.setView(v);
                // 确定按钮点击事件
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newType = etType.getText().toString();
                        String newAccount = etAccount.getText().toString();
                        String newPassword=etPassowrd.getText().toString();
                        String newRemark=etRemark.getText().toString();
                        updata(newType, newAccount,newPassword,newRemark, id);
                        replaceList();// 更新后刷新列表
                    }
                });
                // 取消按钮点击事件
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }
    /**
     * 关闭程序关闭数据库
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.close();
    }
    /**
     * 保存按钮点击事件，首次插入由于没有表必然报错，简化程序利用try-catch在catch中创建表
     */
    public void save(View v) {
        String _type =type.getText().toString();
        String _account=account.getText().toString();
        String _password = password.getText().toString();
        String _remark=remark.getText().toString();
        try {
            insert(_type, _account,_password,_type);
        } catch (Exception e) {
            create();
            insert(_type, _account,_password,_type);
        }
        Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
        type.setText("");
        account.setText("");
        password.setText("");
        remark.setText("");
    }
    /**
     * 读取按钮点击事件，以列表的形式显示所有内容
     */
    public void read(View v) {
        Log.e("read","read");
        replaceList();
    }
    /**
     * ListView的适配器
     */
    public void replaceList() {
        Cursor cursor = select();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.values_item, cursor, new String[] { "_id", "type","account","password",
                "remark" }, new int[] { R.id.tv_id, R.id.tv_type,R.id.tv_account,
                R.id.tv_password,R.id.tv_remark },
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        values.setAdapter(adapter);

        Log.e("replaceList","");
    }

    /**
     * 建表
     */
    public void create() {
        String createSql = "create table account(_id integer primary key autoincrement,account,password,remark)";
        DB.execSQL(createSql);
        Log.e("create","create");
    }

    /**
     * 插入
     */
    public void insert(String type,String account, String password ,String remark) {
        String insertSql = "insert into account(type,account,password,remark) values(?,?,?,?)";
        DB.execSQL(insertSql, new String[] { type,account, password,remark });
    }

    /**
     * 查询
     */
    public Cursor select() {
        Log.e("select","select1");
        String selectSql = "select _id,type,account,password,remark from account";
        Log.e("select","select2");
        Cursor cursor = DB.rawQuery(selectSql, null);// 我们需要查处所有项故不需要查询条件
        Log.e("select","select3");
        return cursor;
    }

    /**
     * 删除
     */
    public void delete(String id) {
        String deleteSql = "delete from account where _id=?";
        DB.execSQL(deleteSql, new String[] { id });
    }

    /**
     * 更新
     */
    public void updata(String type,String account, String password,String remark, String id) {
        String updataSql = "update account set type=?,account=?,password=?,remark=? where _id=?";
        DB.execSQL(updataSql, new String[] { type,account, password,remark, id });
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

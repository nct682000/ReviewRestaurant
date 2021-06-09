package com.example.foodreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

    Button buttons[] = new Button[4];
    Button btn_all, btn_hcm, btn_hn;
    TextView tv_all, tv_hcm, tv_hn;
    List<WishRes> arrayWishRes;
    WishAdapter wishResAdapter;
    ListView lv_wishRes;
    List<Integer> wishList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
            Init();

        MainActivity.database.queryData("CREATE TABLE IF NOT EXISTS Wish(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " accId INT," +
                " resId INT )");

        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_all.setTextColor(Color.parseColor("#6c5ce7"));
                btn_hcm.setTextColor(Color.parseColor("#636e72"));
                btn_hn.setTextColor(Color.parseColor("#636e72"));

                tv_all.setBackgroundColor(Color.parseColor("#6c5ce7"));
                tv_hcm.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_hn.setBackgroundColor(Color.parseColor("#FFFFFF"));

                if(MainActivity3.isLogin){
                    loadWishRes();
                }else
                    dialogConfirmLogin();
            }
        });
        btn_hcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hcm.setTextColor(Color.parseColor("#6c5ce7"));
                btn_all.setTextColor(Color.parseColor("#636e72"));
                btn_hn.setTextColor(Color.parseColor("#636e72"));

                tv_hcm.setBackgroundColor(Color.parseColor("#6c5ce7"));
                tv_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_hn.setBackgroundColor(Color.parseColor("#FFFFFF"));

                if(MainActivity3.isLogin){
                    loadWishId();
                    arrayWishRes.clear();
                    for(int i = 0; i < wishList.size(); i++){
                        Cursor data = MainActivity.database.getData("SELECT * FROM Res WHERE id = '"+ wishList.get(i) +"' AND resProvince = 'Hồ Chí Minh'");
                        while (data.moveToNext()){
                            arrayWishRes.add(new WishRes(
                                    data.getInt(0),
                                    data.getString(1),
                                    data.getString(2),
                                    data.getString(3),
                                    data.getString(4),
                                    data.getFloat(6),
                                    data.getBlob(5)
                            ));
                        }
                    }
                    wishResAdapter.notifyDataSetChanged();

                }else
                    dialogConfirmLogin();
            }
        });
        btn_hn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hn.setTextColor(Color.parseColor("#6c5ce7"));
                btn_hcm.setTextColor(Color.parseColor("#636e72"));
                btn_all.setTextColor(Color.parseColor("#636e72"));

                tv_hn.setBackgroundColor(Color.parseColor("#6c5ce7"));
                tv_hcm.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_all.setBackgroundColor(Color.parseColor("#FFFFFF"));

                if(MainActivity3.isLogin){
                    loadWishId();
                    arrayWishRes.clear();
                    for(int i = 0; i < wishList.size(); i++){
                        Cursor data = MainActivity.database.getData("SELECT * FROM Res WHERE id = '"+ wishList.get(i) +"' AND resProvince = 'Hà Nội'");
                        while (data.moveToNext()){
                            arrayWishRes.add(new WishRes(
                                    data.getInt(0),
                                    data.getString(1),
                                    data.getString(2),
                                    data.getString(3),
                                    data.getString(4),
                                    data.getFloat(6),
                                    data.getBlob(5)
                            ));
                        }
                    }
                    wishResAdapter.notifyDataSetChanged();
                }else
                    dialogConfirmLogin();
            }
        });

        //ListView
        arrayWishRes = new ArrayList<>();
        wishResAdapter = new WishAdapter(this, R.layout.wish_str, arrayWishRes);

        if(MainActivity3.isLogin){
            loadWishRes();
        }else
            dialogConfirmLogin();

        wishResAdapter.notifyDataSetChanged();
        lv_wishRes.setAdapter(wishResAdapter);
        lv_wishRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WishRes selected = arrayWishRes.get(position);
                MainActivity.isSelected = selected.getIndex();
                startActivity(new Intent(MainActivity1.this, ResItemActivity.class));
            }
        });

    }

    private void loadWishId(){
        wishList.clear();
        Cursor data = MainActivity.database.getData("SELECT * FROM Wish WHERE accId = '"+ MainActivity3.idAccount +"'");
        while (data.moveToNext()){
            wishList.add(data.getInt(2));
        }
    }

    public void loadWishRes() {
        loadWishId();
        arrayWishRes.clear();
        for(int i = 0; i < wishList.size(); i++){
           Cursor data = MainActivity.database.getData("SELECT * FROM Res WHERE id = '"+ wishList.get(i) +"'");
           while (data.moveToNext()){
               arrayWishRes.add(new WishRes(
                       data.getInt(0),
                       data.getString(1),
                       data.getString(2),
                       data.getString(3),
                       data.getString(4),
                       data.getFloat(6),
                       data.getBlob(5)
               ));
           }
        }
        wishResAdapter.notifyDataSetChanged();
    }

    private void dialogConfirmLogin(){
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(this);
        dialogConfirm.setMessage("Bạn chưa đăng nhập");
        dialogConfirm.setPositiveButton("Đăng nhập ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity1.this, MainActivity3.class));
            }
        });
        dialogConfirm.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogConfirm.show();
    }

    private void Init() {
        for (int i = 0; i < buttons.length; i++) {
            String btn_id = "btn_" + i;
            int res_id = getResources().getIdentifier(btn_id, "id", getPackageName());
            buttons[i] = findViewById(res_id);
            buttons[i].setOnClickListener(this);
        }

        btn_all = findViewById(R.id.btn_all);
        btn_hcm = findViewById(R.id.btn_hcm);
        btn_hn = findViewById(R.id.btn_hn);
        tv_all = findViewById(R.id.tv_all);
        tv_hcm = findViewById(R.id.tv_hcm);
        tv_hn = findViewById(R.id.tv_hn);
        lv_wishRes = findViewById(R.id.lv_wishRes);
    }

    @Override
    public void onClick(View v) {
        String id = v.getResources().getResourceEntryName(v.getId());
        if( id.equals("btn_0")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if(id.equals("btn_1")) {
            return;
        } else if(id.equals("btn_2")){
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
        }
//        String temp = "MainActivity" + id;

    }
}
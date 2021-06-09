package com.example.foodreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    Button buttons[] = new Button[4];
    Button btn_order, btn_social, btn_news;
    TextView tv_order, tv_social, tv_news, tv_1;
    LinearLayout ll_act21, ll_act22, ll_act23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Init();

        String test = getIntent().getStringExtra("test");
        tv_1.setText(test);

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_order.setTextColor(Color.parseColor("#00b894"));
                btn_social.setTextColor(Color.parseColor("#636e72"));
                btn_news.setTextColor(Color.parseColor("#636e72"));

                tv_order.setBackgroundColor(Color.parseColor("#00b894"));
                tv_social.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_news.setBackgroundColor(Color.parseColor("#FFFFFF"));

                ll_act21.setVisibility(View.VISIBLE);
                ll_act22.setVisibility(View.GONE);
                ll_act23.setVisibility(View.GONE);
            }
        });
        btn_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_social.setTextColor(Color.parseColor("#00b894"));
                btn_order.setTextColor(Color.parseColor("#636e72"));
                btn_news.setTextColor(Color.parseColor("#636e72"));

                tv_social.setBackgroundColor(Color.parseColor("#00b894"));
                tv_order.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_news.setBackgroundColor(Color.parseColor("#FFFFFF"));

                ll_act21.setVisibility(View.GONE);
                ll_act22.setVisibility(View.VISIBLE);
                ll_act23.setVisibility(View.GONE);
            }
        });
        btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_news.setTextColor(Color.parseColor("#00b894"));
                btn_order.setTextColor(Color.parseColor("#636e72"));
                btn_social.setTextColor(Color.parseColor("#636e72"));

                tv_news.setBackgroundColor(Color.parseColor("#00b894"));
                tv_order.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_social.setBackgroundColor(Color.parseColor("#FFFFFF"));

                ll_act21.setVisibility(View.GONE);
                ll_act22.setVisibility(View.GONE);
                ll_act23.setVisibility(View.VISIBLE);
            }
        });
    }

    private void Init() {
        for (int i = 0; i < buttons.length; i++) {
            String btn_id = "btn_" + i;
            int res_id = getResources().getIdentifier(btn_id, "id", getPackageName());
            buttons[i] = findViewById(res_id);
            buttons[i].setOnClickListener(this);
        }

        btn_order = findViewById(R.id.btn_order);
        btn_social = findViewById(R.id.btn_social);
        btn_news = findViewById(R.id.btn_news);
        tv_order = findViewById(R.id.tv_order);
        tv_social = findViewById(R.id.tv_social);
        tv_news = findViewById(R.id.tv_news);
        ll_act21 = findViewById(R.id.ll_act21);
        ll_act22 = findViewById(R.id.ll_act22);
        ll_act23 = findViewById(R.id.ll_act23);
        tv_1 = findViewById(R.id.tv_1);
    }


    @Override
    public void onClick(View v) {
        String id = v.getResources().getResourceEntryName(v.getId());
        if( id.equals("btn_0")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if(id.equals("btn_1")) {
            Intent intent = new Intent(this, MainActivity1.class);
            startActivity(intent);
        } else if(id.equals("btn_2")){
            return;
        } else {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
        }
//        String temp = "MainActivity" + id;

    }
}
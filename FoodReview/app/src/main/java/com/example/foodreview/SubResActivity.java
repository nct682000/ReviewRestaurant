package com.example.foodreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SubResActivity extends AppCompatActivity {

    ListView lv_subRes;
    Button btn_subReturn;
    List<WishRes> subArray;
    WishAdapter subAdapter;

    MainActivity1 context;
    int subId = 0;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_res);

        lv_subRes = findViewById(R.id.lv_subRes);
        btn_subReturn = findViewById(R.id.btn_subReturn);

        subArray = new ArrayList<>();
        subAdapter = new WishAdapter(this, R.layout.wish_str, subArray);

        loadSubRes();
        lv_subRes.setAdapter(subAdapter);

        lv_subRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WishRes selected = subArray.get(position);
                subId = selected.getIndex();
                name = selected.getName();
                dialogSubConfirm();
            }
        });
        btn_subReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubResActivity.this, MainActivity.class));
            }
        });
    }

    private void loadSubRes() {
        subArray.clear();
        Cursor data = MainActivity.database.getData("SELECT * FROM Res");
        while (data.moveToNext()){
            subArray.add(new WishRes(
                    data.getInt(0),
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getFloat(6),
                    data.getBlob(5)
            ));
        }
        subAdapter.notifyDataSetChanged();
    }

    public void dialogSubConfirm(){
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(this);
        dialogConfirm.setMessage("Bạn có chắc chắn muốn XÓA \""+ name +"\"?");
        dialogConfirm.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.database.queryData("DELETE FROM Res WHERE id = '"+ subId +"'");
                MainActivity.database.queryData("DELETE FROM Wish WHERE resId = '"+ subId +"'");
                loadSubRes();
                Toast.makeText(SubResActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
        dialogConfirm.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogConfirm.show();
    }
}
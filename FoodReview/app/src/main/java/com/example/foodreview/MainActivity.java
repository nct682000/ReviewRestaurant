package com.example.foodreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

import static android.R.layout.simple_spinner_dropdown_item;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner sp_province;
    GridView gv_res;
    ArrayList<Res> arrayRes;
    ResAdapter resAdapter;
    EditText edt_search;
    Button[] buttons = new Button[4];
    ViewFlipper vf;
    public static int isSelected;
    String sql = "SELECT * FROM Res";

    public static Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();

        database = new Database(this, "data.sqlite", null, 1);

        database.queryData("CREATE TABLE IF NOT EXISTS Res(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " resName VARCHAR(200)," +
                " resAddress VARCHAR(200)," +
                " resProvince VARCHAR(100)," +
                " description VARCHAR(300)," +
                " image BLOB," +
                " resRating REAL," +
                " resRatingAmount INT )");

        //flipper
        int[] vfArray = {R.drawable.f1, R.drawable.f2, R.drawable.f3
                , R.drawable.f4};
        for(int i = 0; i < vfArray.length; i++){
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(vfArray[i]);
            vf.addView(iv);
        }
        vf.setFlipInterval(3000);
        vf.setAutoStart(true);

        //search
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sp_province.getSelectedItem().toString() == "Tất cả") {
                    sql = "SELECT * FROM Res WHERE resName LIKE '%"+ edt_search.getText().toString().trim() +"%'";
                }else
                    sql = "SELECT * FROM Res WHERE resName LIKE '%"+ edt_search.getText().toString().trim() +"%' AND resProvince = '"+ sp_province.getSelectedItem().toString() +"'";
                loadRes();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //spinner
        ArrayList<String> province = new ArrayList<>();
        province.add("Tất cả");
        province.add("Hồ Chí Minh");
        province.add("Hà Nội");
        ArrayAdapter provinceAdapter = new ArrayAdapter(MainActivity.this
                , simple_spinner_dropdown_item
                , province);
        sp_province.setAdapter(provinceAdapter);
        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    sql = "SELECT * FROM Res WHERE resName LIKE '%"+ edt_search.getText().toString().trim() +"%'";
                }
                else{
                    sql = "SELECT * FROM Res WHERE resName LIKE '%"+ edt_search.getText().toString().trim() +"%' AND resProvince = '"+ sp_province.getSelectedItem().toString() +"'";
                }
                loadRes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //grid
        arrayRes = new ArrayList<>();
        resAdapter = new ResAdapter(this, R.layout.res_str, arrayRes);
            //getData
        loadRes();

        resAdapter.notifyDataSetChanged();
        gv_res.setAdapter(resAdapter);
        gv_res.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = database.getData(sql);
                data.move(position + 1);
                isSelected = data.getInt(0);
                startActivity(new Intent(MainActivity.this, ResItemActivity.class));
            }
        });

    }

    public void loadRes() {
        arrayRes.clear();
        Cursor data = database.getData(sql);
//        Cursor data = database.SELECT_BY_KEYWORD(edt_search.getText().toString());
        while (data.moveToNext()){
            arrayRes.add(new Res(
                    data.getInt(0),
                    data.getString(1),
                    data.getBlob(5)
            ));
        }
        resAdapter.notifyDataSetChanged();
    }

    private void Init() {
        sp_province = findViewById(R.id.sp_province);
        gv_res = findViewById(R.id.gv_res);
        edt_search = findViewById(R.id.edt_search);
        vf = findViewById(R.id.vf_vf);

        for (int i = 0; i < buttons.length; i++) {
            String btn_id = "btn_" + i;
            int res_id = getResources().getIdentifier(btn_id, "id", getPackageName());
            buttons[i] = findViewById(res_id);
            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.res_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.item_add){
            if(MainActivity3.idAccount == 1)
                startActivity(new Intent(MainActivity.this, AddResActivity.class));
            else
                Toast.makeText(this, "Bạn không phải là admin", Toast.LENGTH_SHORT).show();
        }

        if(item.getItemId() == R.id.item_sub){
            if(MainActivity3.idAccount == 1)
                startActivity(new Intent(MainActivity.this, SubResActivity.class));
            else
                Toast.makeText(this, "Bạn không phải là admin", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String id = v.getResources().getResourceEntryName(v.getId());
                String temp = "MainActivity" + id;
        if( id.equals("btn_0")){
            return;
        } else if(id.equals("btn_1")) {
            Intent intent = new Intent(this, MainActivity1.class);
            startActivity(intent);
        } else if(id.equals("btn_2")){
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
        }
    }

//    public void loadRes(){
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://192.168.0.109/foodreview/loadRes.php", null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        for(int i = 0; i < response.length(); i++){
//                            try {
//                                JSONObject object = response.getJSONObject(i);
//                                int id = object.getInt("id");
//                                String name = object.getString("name");
//                                String image = object.getString("image");
//
//                                arrayRes.add(new Res(id, name, image));
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Toast.makeText(MainActivity.this,"load thành công", Toast.LENGTH_SHORT).show();
//                        resAdapter.notifyDataSetChanged();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        System.out.println(error.toString());
//                    }
//                }
//        );
//        requestQueue.add(jsonArrayRequest);
//    }

}
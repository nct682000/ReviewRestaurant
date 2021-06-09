package com.example.foodreview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.layout.simple_spinner_dropdown_item;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    Button buttons[] = new Button[4];
    Button btn_login, btn_register, btn_logout, btn_update;
    LinearLayout ll_act31, ll_act32;
    TextView tv_name, tv_sex, tv_birthday, tv_address, tv_phone, tv_mail;
    public  static int idAccount;
    public  static String usernameAccount = "";
    boolean nameExits = false;
    static boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Init();

        if(isLogin == false){
            ll_act31.setVisibility(View.VISIBLE);
            ll_act32.setVisibility(View.GONE);
        }else {
            ll_act31.setVisibility(View.GONE);
            ll_act32.setVisibility(View.VISIBLE);
            getDataAcc();
        }

        //Tạo bảng account
        MainActivity.database.queryData("CREATE TABLE IF NOT EXISTS Acc(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " username VARCHAR(50)," +
                " password VARCHAR(200)," +
                " name VARCHAR(50)," +
                " sex VARCHAR(20)," +
                " birthday VARCHAR(20)," +
                " address VARCHAR(200)," +
                " phone VARCHAR(20)," +
                " mail VARCHAR(100) )");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogin();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRegister();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpdate();
            }
        });
    }

    public void dialogRegister(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_register);
        dialog.show();

        EditText edt_userReg = dialog.findViewById(R.id.edt_userReg);
        EditText edt_passReg = dialog.findViewById(R.id.edt_passReg);
        EditText edt_confirmReg = dialog.findViewById(R.id.edt_confirmReg);
        Button btn_reg = dialog.findViewById(R.id.btn_reg);
        Button btn_cancelReg = dialog.findViewById(R.id.btn_cancelReg);


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edt_userReg.getText().toString().trim();
                String pass = edt_passReg.getText().toString().trim();
                String confirm = edt_confirmReg.getText().toString().trim();
                if(user.equals("")){
                    Toast.makeText(MainActivity3.this,"Tên đăng nhập không được để trống",Toast.LENGTH_SHORT).show();
                }else{
                    Cursor dataAcc = MainActivity.database.getData("SELECT * FROM Acc");
                    while (dataAcc.moveToNext()) {
                        String username = dataAcc.getString(1);
                        if (user.equals(username)) {
                            Toast.makeText(MainActivity3.this, "Tên đăng nhập của bạn đã tồn tại", Toast.LENGTH_SHORT).show();
                            nameExits = true;
                            break;
                        }
                        // xampp
//                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3.this);
//                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://192.168.0.109/foodreview/loadAcc.php", null,
//                            new Response.Listener<JSONArray>() {
//                                @Override
//                                public void onResponse(JSONArray response) {
//                                    for(int i = 0; i < response.length(); i++){
//                                        try {
//                                            JSONObject object = response.getJSONObject(i);
//                                            String userName = object.getString("userName");
//
//                                            if(user.equals(userName)){
//                                                Toast.makeText(MainActivity3.this,"Tên đăng nhập của bạn đã tồn tại",Toast.LENGTH_SHORT).show();
//                                                nameExits = true;
//                                                break;
//                                            }
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                    if(nameExits==false){
//                                        if (pass.equals(confirm)) {
//                                            insertAcc("http://192.168.0.109/foodreview/insertAcc.php", user, pass);
//                                            dialog.dismiss();
//                                        }else{
//                                            Toast.makeText(MainActivity3.this,"Vui lòng kiểm tra lại mật khẩu",Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Toast.makeText(MainActivity3.this, error.toString(), Toast.LENGTH_SHORT).show();
//                                    System.out.println(error.toString());
//                                }
//                            }
//                    );
//                    requestQueue.add(jsonArrayRequest);
                    }
                    if (nameExits == false) {
                        if (pass.equals(confirm)) {
                            MainActivity.database.INSERT_ACC(user, pass);
                            Toast.makeText(MainActivity3.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity3.this, "Vui lòng kiểm tra lại mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        btn_cancelReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    };

    public void dialogLogin(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_login);
        dialog.show();

        EditText edt_userLog = dialog.findViewById(R.id.edt_userLog);
        EditText edt_passLog = dialog.findViewById(R.id.edt_passLog);
        Button btn_log = dialog.findViewById(R.id.btn_log);
        Button btn_regLog = dialog.findViewById(R.id.btn_regLog);


        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edt_userLog.getText().toString();
                String password = edt_passLog.getText().toString();
                if(user.equals("")){
                    Toast.makeText(MainActivity3.this, "Bạn chưa nhập tên đăng nhập",Toast.LENGTH_SHORT).show();
                }else{
                    Cursor dataAcc = MainActivity.database.getData("SELECT * FROM Acc");
                    boolean succ = false;
                    while (dataAcc.moveToNext()) {
                        int id = dataAcc.getInt(0);
                        String username = dataAcc.getString(1);
                        String pass = dataAcc.getString(2);
                        String name = dataAcc.getString(3);
                        String sex = dataAcc.getString(4);
                        String birthday = dataAcc.getString(5);
                        String address = dataAcc.getString(6);
                        String phone = dataAcc.getString(7);
                        String mail = dataAcc.getString(8);

                        if(user.equals(username) && pass.equals(password)){
                            Toast.makeText(MainActivity3.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            tv_name.setText(name);
                            tv_sex.setText(sex);
                            tv_birthday.setText(birthday);
                            tv_address.setText(address);
                            tv_phone.setText(phone);
                            tv_mail.setText(mail);
                            idAccount = id;
                            usernameAccount = username;

                            ll_act31.setVisibility(View.GONE);
                            ll_act32.setVisibility(View.VISIBLE);

                            succ = true;
                            isLogin = true;
                            break;
                        }
                    }
                    if(succ==false)
                        Toast.makeText(MainActivity3.this,"Tên đăng nhập hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show();
                }
                        // xampp
//                        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3.this);
//                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://192.168.0.109/foodreview/loadAcc.php", null,
//                                new Response.Listener<JSONArray>() {
//                                    @Override
//                                    public void onResponse(JSONArray response) {
//                                        for(int i = 0; i < response.length(); i++){
//                                            try {
//                                                JSONObject object = response.getJSONObject(i);
//                                                int id = object.getInt("id");
//                                                String userName = object.getString("userName");
//                                                String pass = object.getString("pass");
//                                                String name = object.getString("name");
//                                                String sex = object.getString("sex");
//                                                String dateOfBirth = object.getString("dateOfBirth");
//                                                String address = object.getString("address");
//                                                String phone = object.getString("phone");
//                                                String mail = object.getString("mail");
//
//                                                if(user.equals(userName) && password.equals(pass)){
//                                                    Toast.makeText(MainActivity3.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
//                                                    dialog.dismiss();
//                                                    tv_name.setText(name);
//                                                    tv_sex.setText(sex);
//                                                    tv_birthday.setText(dateOfBirth);
//                                                    tv_address.setText(address);
//                                                    tv_phone.setText(phone);
//                                                    tv_mail.setText(mail);
//                                                    idAccount = id;
//
//                                                    ll_act31.setVisibility(View.GONE);
//                                                    ll_act32.setVisibility(View.VISIBLE);
//
//                                                    succ = true;
//                                                    isLogin = true;
//
//                                                    break;
//
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                        }
//                                        if(succ == false) {
//                                            Toast.makeText(MainActivity3.this,"Tên đăng nhập hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                },
//                                new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        Toast.makeText(MainActivity3.this, error.toString(), Toast.LENGTH_SHORT).show();
//                                        System.out.println(error.toString());
//                                    }
//                                }
//                        );
//                        requestQueue.add(jsonArrayRequest);

//                        if(user.equals(userName) && pass.equals(pass)){
//                            Toast.makeText(MainActivity3.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                            tv_name.setText(name);
//                            tv_sex.setText(sex);
//                            tv_birthday.setText(birthday);
//                            tv_address.setText(address);
//                            tv_phone.setText(phone);
//                            tv_mail.setText(mail);
//                            idAccount = id;
//
//                            ll_act31.setVisibility(View.GONE);
//                            ll_act32.setVisibility(View.VISIBLE);
//
//                            succ = true;
//                            break;
//                        }
//                    }
//                    if(succ==false)
//                        Toast.makeText(MainActivity3.this,"Tên đăng nhập hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show();

            }
        });

        btn_regLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRegister();
            }
        });
    }

    public void dialogUpdate(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update);
        dialog.show();

        EditText edt_name = dialog.findViewById(R.id.edt_name);
        Spinner sp_sex = dialog.findViewById(R.id.sp_sex);
        EditText edt_birthday = dialog.findViewById(R.id.edt_birthday);
        EditText edt_address = dialog.findViewById(R.id.edt_address);
        EditText edt_phone = dialog.findViewById(R.id.edt_phone);
        EditText edt_mail = dialog.findViewById(R.id.edt_mail);
        Button btn_upd = dialog.findViewById(R.id.btn_upd);
        Button btn_cancelUpd = dialog.findViewById(R.id.btn_cancelUpd);

        //spinner
        ArrayList<String> sex = new ArrayList<>();
        sex.add("Nam");
        sex.add("Nữ");
        sex.add("Khác");
        ArrayAdapter sexAdapter = new ArrayAdapter(MainActivity3.this
                , simple_spinner_dropdown_item
                , sex);
        sp_sex.setAdapter(sexAdapter);

        edt_name.setText(tv_name.getText());
        if(tv_sex.getText().toString().trim().equals("Nam"))
            sp_sex.setSelection(0);
        else if(tv_sex.getText().toString().trim().equals("Nữ"))
            sp_sex.setSelection(1);
        else
            sp_sex.setSelection(2);
        edt_birthday.setText(tv_birthday.getText());
        edt_address.setText(tv_address.getText());
        edt_phone.setText(tv_phone.getText());
        edt_mail.setText(tv_mail.getText());

        btn_upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                String sex = sp_sex.getSelectedItem().toString();
                String dateOfBirth = edt_birthday.getText().toString().trim();
                String address = edt_address.getText().toString().trim();
                String phone = edt_phone.getText().toString().trim();
                String mail = edt_mail.getText().toString().trim();

                String idAcc = String.valueOf(idAccount);
//                updateAcc("http://192.168.0.109/foodreview/updateAcc.php", name, sex, dateOfBirth, address, phone, mail, idAcc);

                MainActivity.database.queryData("UPDATE Acc SET name = '"+ name +"' WHERE id = '"+ idAccount +"'");
                MainActivity.database.queryData("UPDATE Acc SET sex = '"+ sex +"' WHERE id = '"+ idAccount +"'");
                MainActivity.database.queryData("UPDATE Acc SET birthday = '"+ dateOfBirth +"' WHERE id = '"+ idAccount +"'");
                MainActivity.database.queryData("UPDATE Acc SET address = '"+ address +"' WHERE id = '"+ idAccount +"'");
                MainActivity.database.queryData("UPDATE Acc SET phone = '"+ phone +"' WHERE id = '"+ idAccount +"'");
                MainActivity.database.queryData("UPDATE Acc SET mail = '"+ mail +"' WHERE id = '"+ idAccount +"'");


                Cursor data = MainActivity.database.getData("SELECT * FROM Acc WHERE id = '"+ idAcc +"'");
                while (data.moveToNext()){
                    tv_name.setText(data.getString(3));
                    tv_sex.setText(data.getString(4));
                    tv_birthday.setText(data.getString(5));
                    tv_address.setText(data.getString(6));
                    tv_phone.setText(data.getString(7));
                    tv_mail.setText(data.getString(8));
                }
                dialog.dismiss();
                }
        });

        btn_cancelUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    };

    //Hàm lấy dữ liệu
    private void getDataAcc() {
        Cursor dataAcc = MainActivity.database.getData("SELECT * FROM Acc WHERE id = '" + idAccount + "'");
        while (dataAcc.moveToNext()) {
            tv_name.setText(dataAcc.getString(3));
            tv_sex.setText(dataAcc.getString(4));
            tv_birthday.setText(dataAcc.getString(5));
            tv_address.setText(dataAcc.getString(6));
            tv_phone.setText(dataAcc.getString(7));
            tv_mail.setText(dataAcc.getString(8));
        }
    }

    public void dialogConfirm(){
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(this);
        dialogConfirm.setMessage("Bạn có chắc chắn muốn ĐĂNG XUẤT?");
        dialogConfirm.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isLogin = false;
                ll_act31.setVisibility(View.VISIBLE);
                ll_act32.setVisibility(View.GONE);
            }
        });
        dialogConfirm.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_logout = findViewById(R.id.btn_logout);
        btn_update = findViewById(R.id.btn_update);

        ll_act31 = findViewById(R.id.ll_act31);
        ll_act32 =findViewById(R.id.ll_act32);

        tv_name = findViewById(R.id.tv_name);
        tv_sex = findViewById(R.id.tv_sex);
        tv_birthday = findViewById(R.id.tv_birthday);
        tv_address = findViewById(R.id.tv_address);
        tv_phone = findViewById(R.id.tv_phone);
        tv_mail = findViewById(R.id.tv_mail);

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
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        } else {
            return;
        }
//        String temp = "MainActivity" + id;
    }

//    private void insertAcc(String url, String userName, String pass){
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(response.trim().equals("success")){
//                            Toast.makeText(MainActivity3.this, "Đã thêm thành công", Toast.LENGTH_SHORT).show();
//                        }else
//                            Toast.makeText(MainActivity3.this, "Thêm KHÔNG thành công!", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity3.this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("_userName", userName);
//                params.put("_pass", pass);
//
//                return params;
//            }
//        };
//        requestQueue.add(stringRequest);
//    }

//    private void updateAcc(String url, String name, String sex, String dateOfBirth, String address, String phone, String mail, String id){
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(response.trim().equals("success")){
//                            Toast.makeText(MainActivity3.this, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
//                        }else
//                            Toast.makeText(MainActivity3.this, "Cập nhật KHÔNG thành công!", Toast.LENGTH_SHORT).show();
//                        loadInfo();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity3.this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("_name", name);
//                params.put("_sex", sex);
//                params.put("_dateOfBirth", dateOfBirth);
//                params.put("_address", address);
//                params.put("_phone", phone);
//                params.put("_mail", mail);
//                params.put("_id", id);
//
//                return params;
//            }
//        };
//        requestQueue.add(stringRequest);
//    }

//    private void loadInfo(){
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3.this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, "http://192.168.0.109/foodreview/loadAcc.php", null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) { {
//                        int id = idAccount - 1;
//                        try {
//                            JSONObject object = response.getJSONObject(id);
//                            String name_ = object.getString("name");
//                            String sex_ = object.getString("sex");
//                            String dateOfBirth_ = object.getString("dateOfBirth");
//                            String address_ = object.getString("address");
//                            String phone_ = object.getString("phone");
//                            String mail_ = object.getString("mail");
//
//                            tv_name.setText(name_);
//                            tv_sex.setText(sex_);
//                            tv_birthday.setText(dateOfBirth_);
//                            tv_address.setText(address_);
//                            tv_phone.setText(phone_);
//                            tv_mail.setText(mail_);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity3.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        System.out.println(error.toString());
//                    }
//                }
//        );
//        requestQueue.add(jsonArrayRequest);
//    }
}
package com.example.foodreview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResItemActivity extends AppCompatActivity {

    ImageView iv_resItemImg;
    TextView tv_resItemName, tv_resItemAddress, tv_resItemProvince, tv_resItemDescription, tv_resItemRating;

    RadioButton rb_1, rb_2, rb_3, rb_4, rb_5, rb_wish;
    Button btn_returnHome, btn_rating, btn_comment;
    ListView lv_comment;
    RatingBar ratingBar;
    EditText edt_comment;

    ArrayList<Comment> commentArray;
    CommentAdapter commentAdapter;

    int ratingAmount = 0;
    float rating = 0;
    int resId = MainActivity.isSelected;
    String resNameSelected = "";

    MainActivity1 context1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_item);
        Init();

        loadResItem();

        MainActivity.database.queryData("CREATE TABLE IF NOT EXISTS Com(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " text VARCHAR(255)," +
                " image BLOB," +
                " resId INT," +
                " accName VARCHAR(255) )");

        // listView
        commentArray = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, R.layout.comment_str, commentArray);
        loadComment();
        lv_comment.setAdapter(commentAdapter);

        btn_returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ResItemActivity.this, MainActivity.class));
                finish();
            }
        });
        edt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComment();
            }
        });

        // rb_wishRes
        if(MainActivity3.isLogin){
            if(checkWish())
                rb_wish.setChecked(true);
            else
                rb_wish.setChecked(false);
        }else
            rb_wish.setChecked(false);
        rb_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity3.isLogin){
                    if(checkWish()){
                        MainActivity.database.queryData("DELETE FROM Wish WHERE accId = '"+ MainActivity3.idAccount +"' AND resId = '"+ resId +"'");
                        rb_wish.setChecked(false);
                    }else {
                        MainActivity.database.queryData("INSERT INTO Wish VALUES(null, '"+ MainActivity3.idAccount +"', '"+ resId +"') ");
                        rb_wish.setChecked(true);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ResItemActivity.this)
                                .setSmallIcon(R.drawable.ic_notifi)
                                .setContentTitle("Thông báo")
                                .setContentText("Quán ăn '"+ resNameSelected +"' đã được thêm vào danh sách yêu thích")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);
                        Intent intent = new Intent(ResItemActivity.this, MainActivity2.class);
                        intent.putExtra("test", "Thông báo thành công");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        PendingIntent pendingIntent = PendingIntent.getActivity(ResItemActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, builder.build());
                    }

                }else {
                    rb_wish.setChecked(false);
                    dialogConfirmLogin();
                }
            }
        });

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity3.isLogin){
                    ratingAmount++;
                    int rate = 0;
                    if(rb_1.isChecked())
                        rate = 1;
                    else if(rb_2.isChecked())
                        rate = 2;
                    else if(rb_3.isChecked())
                        rate = 3;
                    else if(rb_4.isChecked())
                        rate = 4;
                    else
                        rate = 5;
                    float tongRate = rating * (ratingAmount-1) + rate;
                    rating = tongRate / ratingAmount;

//                    updateResRate(String.valueOf(rating), String.valueOf(ratingAmount), String.valueOf(resId));
                MainActivity.database.queryData("UPDATE Res SET resRatingAmount = '"+ ratingAmount +"' WHERE id = '"+ resId +"'");
                MainActivity.database.queryData("UPDATE Res SET resRating = '"+ rating +"' WHERE id = '"+ resId +"'");
                    Toast.makeText(ResItemActivity.this, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                    loadResItem();
                }else {
                    dialogConfirmLogin();
                }

            }
        });

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity3.isLogin){
                    String text = edt_comment.getText().toString().trim();
                    if(text.equals("")){
                        return;
                    }else {
                        MainActivity.database.INSERT_COMMENT(text, resId, MainActivity3.usernameAccount);
//                        insertComment(text,"", String.valueOf(resId),String.valueOf(MainActivity3.idAccount));
                        edt_comment.setText("");
                        loadComment();
                    }
                }else{
                    dialogConfirmLogin();
                }
            }
        });
    }

    public void dialogComment(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_comment);
        dialog.show();

        Button btn_comImg = dialog.findViewById(R.id.btn_comImg);
        Button btn_comFinish = dialog.findViewById(R.id.btn_comFinish);
        EditText edt_comComment = dialog.findViewById(R.id.edt_comComment);
        ImageView iv_comImg = dialog.findViewById(R.id.iv_comImage);

        edt_comComment.setText(edt_comment.getText().toString());

        btn_comFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_comment.setText(edt_comComment.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void dialogConfirmLogin(){
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(this);
        dialogConfirm.setMessage("Bạn chưa đăng nhập");
        dialogConfirm.setPositiveButton("Đăng nhập ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ResItemActivity.this, MainActivity3.class));
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

    private void loadComment() {
        commentArray.clear();
        Cursor data = MainActivity.database.getData("SELECT * FROM Com WHERE resId = '"+ resId +"'");
        while (data.moveToNext()){
            commentArray.add(new Comment(
                    data.getInt(0),
                    data.getString(4),
                    data.getString(1),
                    data.getBlob(2)
            ));
        }
        commentAdapter.notifyDataSetChanged();
        lv_comment.setSelection(commentAdapter.getCount() - 1);
    }

    private void loadResItem() {
        Cursor data = MainActivity.database.getData("SELECT * FROM Res WHERE id = '"+ resId +"'");
        while (data.moveToNext()){
            byte[] img = data.getBlob(5);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            iv_resItemImg.setImageBitmap(bitmap);
            tv_resItemName.setText(data.getString(1));
            tv_resItemAddress.setText(data.getString(2));
            tv_resItemProvince.setText(data.getString(3));
            tv_resItemDescription.setText(data.getString(4));
            rating = data.getFloat(6);
            tv_resItemRating.setText(data.getString(6));
            ratingAmount = data.getInt(7);
            ratingBar.setRating(rating);
            resNameSelected = data.getString(1);
        }
    }

    private boolean checkWish(){
        Cursor data = MainActivity.database.getData("SELECT * FROM Wish WHERE resId = '"+ resId +"' AND accId = '"+ MainActivity3.idAccount +"'");
        if(data.getCount() == 0)
            return false;
        else
            return true;
    }

    private void Init() {
        iv_resItemImg = findViewById(R.id.iv_resItemImg);
        tv_resItemName = findViewById(R.id.tv_resItemName);
        tv_resItemAddress = findViewById(R.id.tv_resItemAddress);
        tv_resItemProvince = findViewById(R.id.tv_resItemProvince);
        tv_resItemDescription = findViewById(R.id.tv_resItemDescription);
        tv_resItemRating = findViewById(R.id.tv_resItemRating);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        rb_5 = findViewById(R.id.rb_5);
        btn_returnHome = findViewById(R.id.btn_returnHome);
        btn_rating = findViewById(R.id.btn_rating);
        ratingBar = findViewById(R.id.ratingBar_rating);
        lv_comment = findViewById(R.id.lv_comment);
        btn_comment = findViewById(R.id.btn_comment);
        edt_comment = findViewById(R.id.edt_comment);
        rb_wish = findViewById(R.id.rb_wish);
    }

//    public void loadResWithId(){
//        RequestQueue requestQueue = Volley.newRequestQueue(ResItemActivity.this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://192.168.0.109/foodreview/loadRes.php", null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            JSONObject object = response.getJSONObject(MainActivity.isSelected);
//                            int id = object.getInt("id");
//                            String name = object.getString("name");
//                            String address = object.getString("address");
//                            String province = object.getString("province");
//                            String description = object.getString("description");
//                            String image = object.getString("image");
//                            Double rate = object.getDouble("rate");
//                            int rateAmount = object.getInt("rateAmount");
//
//                            tv_resItemName.setText(name);
//                            tv_resItemAddress.setText(address);
//                            tv_resItemProvince.setText(province);
//                            tv_resItemDescription.setText(description);
//                            tv_resItemRating.setText(String.valueOf(rate));
//                            ratingAmount = rateAmount;
//                            rating = rate;
//                            ratingBar.setRating(rate.floatValue());
//                            resId = id;
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Toast.makeText(ResItemActivity.this,"load thành công", Toast.LENGTH_SHORT).show();
//                        //loadComment
//                        loadComment(String.valueOf(9));
//                        System.out.println(resId);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ResItemActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        System.out.println(error.toString());
//                    }
//                }
//        );
//        requestQueue.add(jsonArrayRequest);
//    }

//    private void updateResRate(String rate, String rateAmount, String id){
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.109/foodreview/updateResRate.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        loadResWithId();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ResItemActivity.this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("_rate", rate);
//                params.put("_rateAmount", rateAmount);
//                params.put("_id", id);
//
//                return params;
//            }
//        };
//        requestQueue.add(stringRequest);
//    }

//    private void insertComment(String text, String image, String resId, String accId){
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.109/foodreview/insertComment.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ResItemActivity.this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("_text", text);
//                params.put("_image", image);
//                params.put("_resId", resId);
//                params.put("_accId", accId);
//
//                return params;
//            }
//        };
//        requestQueue.add(stringRequest);
//    }

//    private void loadComment(String resId) {
//        RequestQueue requestQueue = Volley.newRequestQueue(ResItemActivity.this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://192.168.0.109/foodreview/loadComment.php", null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        for(int i = 0; i < response.length(); i++){
//                            try {
//                                JSONObject object = response.getJSONObject(i);
//                                int id = object.getInt("id");
//                                int accId = object.getInt("accId");
//                                String text = object.getString("text");
//                                String image = object.getString("image");
//
//                                commentArray.add(new Comment(id, String.valueOf(accId), text, image));
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Toast.makeText(ResItemActivity.this,"load thành công", Toast.LENGTH_SHORT).show();
//                        commentAdapter.notifyDataSetChanged();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ResItemActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
//                        System.out.println(error.toString());
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("_resId", resId);
//
//                return params;
//            }
//        };
//        requestQueue.add(jsonArrayRequest);
//    }
}
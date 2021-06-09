package com.example.foodreview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.R.layout.simple_spinner_dropdown_item;

public class AddResActivity extends AppCompatActivity {
    EditText edt_resName, edt_resAddress, edt_description;
    Spinner sp_resProvince;
    ImageButton ibtn_camera, ibtn_folder;
    ImageView iv_imageDefault;
    Button btn_addConfirm, btn_addCancel;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_res);
        Init();

        ArrayList<String> resProvince = new ArrayList<>();
        resProvince.add("Hồ Chí Minh");
        resProvince.add("Hà Nội");
        ArrayAdapter resProvinceAdapter = new ArrayAdapter(this
                , simple_spinner_dropdown_item
                , resProvince);
        sp_resProvince.setAdapter(resProvinceAdapter);

        btn_addConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resName = edt_resName.getText().toString().trim();
                String resAddress = edt_resAddress.getText().toString().trim();
                String resProvince = sp_resProvince.getSelectedItem().toString();
                String description = edt_description.getText().toString().trim();

                // chuyển dữ liệu hình ảnh (ImageView) thành mảng byte[]
                BitmapDrawable bitmapDrawable = (BitmapDrawable) iv_imageDefault.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                byte[] img = byteArray.toByteArray();

                MainActivity.database.INSERT_RES(resName, resAddress, resProvince, description, img);
                Toast.makeText(AddResActivity.this,"Đã thêm thành công",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddResActivity.this, MainActivity.class));
            }
        });

        btn_addCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddResActivity.this, MainActivity.class));
            }
        });

        ibtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        ibtn_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            iv_imageDefault.setImageBitmap(bitmap);
        }
        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                iv_imageDefault.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Init() {
        edt_resName = findViewById(R.id.edt_resName);
        edt_resAddress = findViewById(R.id.edt_resAddress);
        edt_description = findViewById(R.id.edt_description);
        sp_resProvince = findViewById(R.id.sp_resProvince);
        ibtn_camera = findViewById(R.id.ibtn_camera);
        ibtn_folder = findViewById(R.id.ibtn_folder);
        iv_imageDefault = findViewById(R.id.iv_imgageDefault);
        btn_addConfirm = findViewById(R.id.btn_addConfirm);
        btn_addCancel = findViewById(R.id.btn_addCancel);
    }
}
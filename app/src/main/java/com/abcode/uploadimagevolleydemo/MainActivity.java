package com.abcode.uploadimagevolleydemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static android.util.Base64.encodeToString;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    ImageView imageView;
    EditText editText;
    Button button_choose, button_upload;

    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    String test = "test";


    private String uploadURL = "http://10.0.2.2:8000/upload_images_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.editText);
        button_choose= findViewById(R.id.button_choose);
        button_upload= findViewById(R.id.button_upload);

        button_choose.setOnClickListener(this);
        button_upload.setOnClickListener(this);




    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_choose:
                selectImage();
                break;

            case R.id.button_upload:
                uploadImage();
                break;
        }
    }
    public void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uploadURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("test", "we received response : "+response);

//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            String Response = jsonObject.getString("response");
//                            Toast.makeText(MainActivity.this, Response, Toast.LENGTH_LONG).show();
//                            imageView.setImageResource(0);
//                            imageView.setVisibility(View.GONE);
//                            editText.setText("");
//                            editText.setVisibility(View.GONE);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                      //  Log.i("test", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test", "there was an error");
                Log.i("test", error.getMessage());
            }
        })
        {

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("name",  editText.getText().toString().trim());
//                params.put("image", imageToString(bitmap));
//
//                return params;
//            }
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String>  params = new HashMap<String, String>();
//                    params.put("Content-Type", "application/json");
//                    //params.put("Accept", "application/json");
//                    //params.put("name",  editText.getText().toString().trim());//
//                    //params.put("image", imageToString(bitmap));//
//
//                    return params;
//                }
        };

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest );

    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        String base64 = encodeToString(imgBytes, android.util.Base64.DEFAULT);
        return base64;

    }
}

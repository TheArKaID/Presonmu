package id.ac.umy.unires.mh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.Utils.ServerAPI.UPDATEPROFILE_URL;

public class EditProfile extends AppCompatActivity {

    ImageView iv_editFoto;
    EditText et_nama, et_status, et_password, et_newPass, et_newRePass;
    Button btn_simpan;

    String nama, status, password, newPass, newRePass, email;

    private final int REQUEST_IMAGE_FROM_GALLERY = 200;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final Bundle bundle = getIntent().getExtras();

        iv_editFoto = findViewById(R.id.editProfileFoto);
        et_nama = findViewById(R.id.editProfileNama);
        et_status = findViewById(R.id.editProfileStatus);
        et_password = findViewById(R.id.editPassword);
        et_newPass = findViewById(R.id.editNewPassword);
        et_newRePass = findViewById(R.id.editNewRePassword);
        btn_simpan = findViewById(R.id.editSimpan);

        et_nama.setText(bundle.getString("nama"));
        et_status.setText(bundle.getString("status"));

        iv_editFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihGambar();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = et_nama.getText().toString();
                status = et_status.getText().toString();
                password = et_password.getText().toString();
                newPass = et_newPass.getText().toString();
                newRePass = et_newRePass.getText().toString();
                email = bundle.getString("email");

                saveMyData();
            }
        });
    }

    private void pilihGambar() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_FROM_GALLERY && data != null){
            Uri image = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                iv_editFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveMyData() {
        password = Login.md5(password);
        newPass = Login.md5(newPass);
        newRePass = Login.md5(newRePass);
        StringRequest request = new StringRequest(Request.Method.POST, UPDATEPROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditProfile.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ErrorSaveProfile = > ", error.getMessage());
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();

                    params.put("email", email);
                    params.put("nama", nama);
                    params.put("status", status);
                    params.put("pass", password);
                    params.put("newpass", newPass);
                    params.put("newrepass", newRePass);
                    params.put("foto", image2string(bitmap));

                    return params;
                }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private String image2string(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}

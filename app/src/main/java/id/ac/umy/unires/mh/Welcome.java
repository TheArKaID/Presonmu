package id.ac.umy.unires.mh;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.Utils.ServerAPI.LOGIN_URL;
import static id.ac.umy.unires.mh.login.md5;

public class Welcome extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog checkBar;
    Button checkingBtn;
    AlertDialog.Builder showAskPermission;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        checkingBtn = findViewById(R.id.CheckingBtn);
        checkBar = new ProgressDialog(this);
        showAskPermission = new AlertDialog.Builder(this);

        checkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLocationPermission();

            }
        });
    }

    private void CheckLocationPermission() {
        if (ActivityCompat.checkSelfPermission(Welcome.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Welcome.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAskPermission.setTitle("Izin dibutuhkan")
                    .setMessage("Aplikasi ini harus memiliki izin untuk mengakses lokasi anda.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Welcome.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }).show();
        } else {
            intenter();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intenter();
                return;
            }
        }
    }

    private void LoadingBarCheck() {
        checkBar.setTitle("Please Wait...");
        checkBar.setMessage("While We're Checking your Data");
        checkBar.setCanceledOnTouchOutside(false);
        checkBar.setCancelable(false);
        checkBar.show();
    }

    private void intenter() {
        LoadingBarCheck();
        if (isInternetWorking()) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("id.ac.umy.unires.mh.DATA_DIRI", MODE_PRIVATE);
            SharedPreferences.Editor prefEdit = pref.edit();
            if (pref.getString("email", null) != null && pref.getString("pass", null) != null) {
                email = pref.getString("email", null);
                password = pref.getString("pass", null);
                LoginandIntent(email, password);
                // TODO : Intent ke halaman awal melalui login dengan data yang disimpan
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("email", email);

                Intent loginIntent = new Intent(getApplicationContext(), login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
            }

            prefEdit.apply();
            checkBar.dismiss();
        } else {
            checkBar.dismiss();
            Toast.makeText(Welcome.this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void LoginandIntent(final String email, final String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            checkBar.dismiss();
            Toast.makeText(this, "Harap Masukkan email dan password anda", Toast.LENGTH_LONG).show();
        } else {
            if (isInternetWorking()) {
                StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.contains("Data Matched")){
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("id.ac.umy.unires.mh.DATA_DIRI", MODE_PRIVATE);
                                    SharedPreferences.Editor prefEdit = pref.edit();

                                    prefEdit.putString("email", email);
                                    prefEdit.putString("pass", password);

                                    prefEdit.apply();
                                    checkBar.dismiss();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", email);
//
                                    Intent mainIntent = new Intent(Welcome.this, MainActivity.class);
                                    mainIntent.putExtras(bundle);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mainIntent);
                                } else{
                                    Toast.makeText(Welcome.this, response, Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Welcome.this, "Error Response => "+error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }

//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String,String> params = new HashMap<>();
//                        params.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
//                        params.put("Cookie", "__test=bdcdab978ace2da77de09294366a1ddd; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
//                        return params;
//                    }
                };
                Volley.newRequestQueue(this).add(request);

            } else {
                checkBar.dismiss();
                Toast.makeText(Welcome.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}

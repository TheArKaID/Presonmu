package id.ac.umy.unires.mh.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import id.ac.umy.unires.mh.R;

import static id.ac.umy.unires.mh.Utils.ServerAPI.LOGIN_URL;

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
                new newWelcome().execute();
            }
        });
    }

    private void CheckLocationPermission() {
        if (ActivityCompat.checkSelfPermission(Welcome.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Welcome.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Welcome.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Welcome.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)  {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showAskPermission.setTitle("Izin dibutuhkan")
                        .setMessage("Aplikasi ini harus memiliki izin untuk mengakses lokasi anda.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Welcome.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                toaster("Maaf, anda harus memberi aplikasi ini izin. Dismiss");
                            }
                        }).show();
                }
            });
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
            } else{
                toaster("Maaf, anda harus memberi aplikasi ini izin");
            }
        }
        checkBar.dismiss();
    }

    private void intenter() {
        if (isInternetWorking()) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("id.ac.umy.unires.mh.DATA_DIRI", MODE_PRIVATE);
            SharedPreferences.Editor prefEdit = pref.edit();
            if (pref.getString("email", null) != null && pref.getString("pass", null) != null) {
                email = pref.getString("email", null);
                password = pref.getString("pass", null);
                LoginandIntent(email, password);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("email", email);

                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                checkBar.dismiss();
                startActivity(loginIntent);
            }

            prefEdit.apply();
        } else {
            toaster("Check your internet connection");
        }
    }

    private void LoginandIntent(final String email, final String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            toaster("Harap Masukkan email dan password anda");
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
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", email);
//
                                    Intent mainIntent = new Intent(Welcome.this, MainActivity.class);
                                    mainIntent.putExtras(bundle);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    checkBar.dismiss();
                                    startActivity(mainIntent);
                                } else{
                                    toaster(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                toaster("Error Response => "+error.toString());
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        params.put("logged", "true");
                        return params;
                    }
                };
                Volley.newRequestQueue(this).add(request);

            } else {
                toaster("Check your internet connection");
            }
        }

    }

    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.co.id");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    private void LoadingBarCheck() {
        checkBar.setTitle("Please Wait...");
        checkBar.setMessage("While We're Checking your Data");
        checkBar.setCanceledOnTouchOutside(false);
        checkBar.setCancelable(false);
        checkBar.show();
    }

    private class newWelcome extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void...voids) {
            CheckLocationPermission();
            return null;
        }

        @Override
        protected void onPreExecute() {
            LoadingBarCheck();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    private void toaster(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Welcome.this, message, Toast.LENGTH_LONG).show();
                checkBar.dismiss();
            }
        });
    }
}

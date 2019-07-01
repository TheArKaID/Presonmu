package id.ac.umy.unires.mh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.Utils.ServerAPI.LOGIN_URL;

public class Login extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    Button login;

    String email;
    String password;


    ProgressDialog checkBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        emailET = findViewById(R.id.EmailET);
        passwordET = findViewById(R.id.PassET);
        login = findViewById(R.id.LoginBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new newLogin().execute();
            }
        });
    }

    public void login(final String email, String password) {
        final String pass =  md5(password);
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
                                prefEdit.putString("pass", pass);

                                prefEdit.apply();

                                Bundle bundle = new Bundle();
                                bundle.putString("email", email);
//
                                Intent mainIntent = new Intent(Login.this, MainActivity.class);
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
                            toaster("Error Response =>"+error.toString());
                        }
                    }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", pass);
                        return params;
                    }
                };
                Volley.newRequestQueue(this).add(request);

            } else {
                toaster("Check your internet connection");
            }
        }
    }

    private void LoadingBarCheck() {
        checkBar = new ProgressDialog(this);
        checkBar.setTitle("Please Wait...");
        checkBar.setMessage("While We're Checking your Data");
        checkBar.setCanceledOnTouchOutside(false);
        checkBar.setCancelable(false);
        checkBar.show();
    }

    public static final String md5(final String toEncrypt) {
        String result = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            result = sb.toString().toLowerCase();
            return result;
        } catch (Exception exc) {
            return result; // Impossibru!
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

    private class newLogin extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            LoadingBarCheck();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            email = emailET.getText().toString();
            password = passwordET.getText().toString();

            login(email, password);
            return null;
        }
    }

    private void toaster(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                checkBar.dismiss();
            }
        });
    }
}

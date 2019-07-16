package id.ac.umy.unires.mh;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.Utils.ServerAPI.VERSIONCHECK_URL;

public class SplashActivity extends AppCompatActivity {

    ProgressDialog checkBar;

    String version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkBar = new ProgressDialog(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("id.ac.umy.unires.mh.DATA_DIRI", MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = pref.edit();
        if (pref.getString("version", null) != null) {
            version = pref.getString("version", "1.0.0");
        } else{
            version = "1.0.0";
            prefEdit.putString("version", version);
        }
        prefEdit.apply();
        new versionCheck().execute();
    }

    private class versionCheck extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            askVersion();
            return null;
        }

        @Override
        protected void onPreExecute() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingBarCheck();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            checkBar.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    private void askVersion(){
        StringRequest request = new StringRequest(Request.Method.POST, VERSIONCHECK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if(response.equals("Versi Baru")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    exit();
                                }
                            });
                        } else{
                            letsIntent();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.d("Error Splash Request=> ", error.getMessage() != null ? error.getMessage() : "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SplashActivity.this, "Gagal Terhubung. Restart Aplikasi."+ (error.getMessage() != null ? error.getMessage() : ""), Toast.LENGTH_LONG).show();
                                Log.d("AboutErrorRequest =>", error.getMessage() != null ? error.getMessage() : "");
                            }
                        });
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("version", version);
                    return params;
                }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void loadingBarCheck() {
        checkBar.setTitle("Version Checking");
        checkBar.setMessage("Please Wait...");
        checkBar.setCanceledOnTouchOutside(false);
        checkBar.setCancelable(false);
        checkBar.show();
    }

    private void letsIntent(){
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try{
                        sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        Intent welcomeIntent = new Intent(SplashActivity.this, Welcome.class);
                        startActivity(welcomeIntent);
                    }
                }
            };
            thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    private void exit() {
        AlertDialog exitDialog = new AlertDialog.Builder(SplashActivity.this).create();
        exitDialog.setTitle("Versi Baru Tersedia");
        exitDialog.setMessage("Silahkan Hubungi Pengembang Untuk Mendapatkan Versi Baru.");
        exitDialog.setCancelable(false);
        exitDialog.setCanceledOnTouchOutside(false);
        exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                });
        exitDialog.show();
    }
}

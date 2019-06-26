package id.ac.umy.unires.mh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import static id.ac.umy.unires.mh.Utils.ServerAPI.VERSIONCHECK_URL;

public class SplashActivity extends AppCompatActivity {

    ProgressDialog checkBar;

    String isContinuable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkBar = new ProgressDialog(this);

        new versionCheck().execute();
    }

    private class versionCheck extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            askVersion();
            return "";
        }

        @Override
        protected void onPreExecute() {
            loadingBarCheck();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void askVersion(){
        StringRequest request = new StringRequest(Request.Method.POST, VERSIONCHECK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        continueOrNot(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
                }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void loadingBarCheck() {
        checkBar.setTitle("Please Wait...");
        checkBar.setMessage("While We're Checking your Data");
        checkBar.setCanceledOnTouchOutside(false);
        checkBar.setCancelable(false);
        checkBar.show();
    }

    private void continueOrNot(String result){
        if(result.equals("Versi Baru")){

        } else if(result.equals("MH Selesai")){

        } else{
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try{
                        sleep(5000);
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
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}

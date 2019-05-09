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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog checkBar;
    Button checkingBtn;
    AlertDialog.Builder showAskPermission;
    FirebaseFirestore db;

    String email;
    String password;

    Map<String, Object> dataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        dataUser = new HashMap();

        checkingBtn = findViewById(R.id.CheckingBtn);
        checkBar = new ProgressDialog(this);
        showAskPermission = new AlertDialog.Builder(this);
        db = FirebaseFirestore.getInstance();

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

    private void LoginandIntent(String email, String password) {
        db.collection("users").whereEqualTo("email", email).whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dataUser.putAll(document.getData());
                            }
                            checkBar.dismiss();

                            final Bundle bundle = new Bundle();

                            for (Map.Entry<String, Object> entry : dataUser.entrySet()) {
                                bundle.putString(entry.getKey(), entry.getValue().toString());
                            }

                            Intent mainIntent = new Intent(Welcome.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);


                        } else {
                            checkBar.dismiss();
                            Log.w("FragmentActivity", "Error getting documents.", task.getException());
                        }
                    }
                });

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

package id.ac.umy.unires.mh;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog checkBar;
    Button checkingBtn;
    AlertDialog.Builder showAskPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        checkingBtn = findViewById(R.id.CheckingBtn);
        checkBar = new ProgressDialog(this);
        showAskPermission = new AlertDialog.Builder(this);

        checkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLocationPermission();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("id.ac.umy.unires.mh.DATA_DIRI", MODE_PRIVATE);
                SharedPreferences.Editor prefEdit = pref.edit();
                if (pref.getString("uid", null) != null && pref.getString("pass", null) != null) {
                    // TODO : Intent ke halaman awal melalui login dengan data yang disimpan
                } else {
                    // TODO : Intent ke halaman login
                }

                prefEdit.apply();
                checkBar.dismiss();
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
                            ActivityCompat.requestPermissions(Welcome.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            ActivityCompat.requestPermissions(Welcome.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }).show();
        }
        else {
            LoadingBarCheck();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            }
            else{
                //CheckLocationPermission();
            }
        }
    }

    private void LoadingBarCheck(){
        checkBar.setTitle("Please Wait...");
        checkBar.setMessage("While We're Checking your Data");
        checkBar.show();
    }
}

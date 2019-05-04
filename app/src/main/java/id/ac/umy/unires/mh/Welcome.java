package id.ac.umy.unires.mh;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

    Button checkingBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        checkingBtn = findViewById(R.id.CheckingBtn);

        checkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("id.ac.umy.unires.mh.DATA_DIRI", MODE_PRIVATE);
                SharedPreferences.Editor prefEdit = pref.edit();
                if(pref.getString("uid", null) != null && pref.getString("pass", null) != null){
                    //TODO : Intent ke halaman awal melalui login dengan data yang disimpan
                } else{
                    // TODO : Intent ke halaman login
                }

                prefEdit.apply();
            }
        });
    }
}

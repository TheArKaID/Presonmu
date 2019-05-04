package id.ac.umy.unires.mh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

    Button checkingBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        checkingBtn = findViewById(R.id.CheckingBtn);
    }
}

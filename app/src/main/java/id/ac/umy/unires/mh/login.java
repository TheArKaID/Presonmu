package id.ac.umy.unires.mh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    Button login;

    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.EmailET);
        passwordET = findViewById(R.id.PassET);
        login = findViewById(R.id.LoginBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                Login(email, password);
            }
        });
    }

    private void Login(String email, String password) {

    }
}

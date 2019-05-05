package id.ac.umy.unires.mh;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class login extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    Button login;

    String email;
    String password;

    Map dataUser;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.EmailET);
        passwordET = findViewById(R.id.PassET);
        login = findViewById(R.id.LoginBtn);
        db = FirebaseFirestore.getInstance();
        dataUser = new HashMap();

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
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Harap Masukkan email dan password anda", Toast.LENGTH_LONG).show();
        }
        else {
            db.collection("users").whereEqualTo("email", email).whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(!task.getResult().isEmpty()){
                                    QuerySnapshot qs = task.getResult();
                                    List<DocumentSnapshot> qds = qs.getDocuments();
                                    dataUser.putAll(qds.get(0).getData());
                                }
                            } else {
                                Log.w("FragmentActivity", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
    }
}

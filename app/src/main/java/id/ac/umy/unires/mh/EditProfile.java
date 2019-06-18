package id.ac.umy.unires.mh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditProfile extends AppCompatActivity {

    ImageView iv_editFoto;
    EditText et_nama, et_status, et_password, et_newPass, et_newRePass;
    Button btn_simpan;

    String nama, status, password, newPass, newRePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Bundle bundle = getIntent().getExtras();

        iv_editFoto = findViewById(R.id.editProfileFoto);
        et_nama = findViewById(R.id.editProfileNama);
        et_status = findViewById(R.id.editProfileStatus);
        et_password = findViewById(R.id.editPassword);
        et_newPass = findViewById(R.id.editNewPassword);
        et_newRePass = findViewById(R.id.editNewRePassword);
        btn_simpan = findViewById(R.id.editSimpan);

        et_nama.setText(bundle.getString("nama"));
        et_status.setText(bundle.getString("status"));;
    }
}

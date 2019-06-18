package id.ac.umy.unires.mh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    }
}

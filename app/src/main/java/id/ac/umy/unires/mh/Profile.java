package id.ac.umy.unires.mh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.MainActivity.email;
import static id.ac.umy.unires.mh.Utils.ServerAPI.CEKPROFILE_URL;

public class Profile extends Fragment {

    TextView tvnama, tvjkelamin, tvemail, tvmasjid, tvstatus;
    Button btnedit;
    ImageView ivprofile;

    ProgressDialog checkBar;

    String newFoto;

    private static int REQUEST_EDIT_PROFILE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        tvnama = v.findViewById(R.id.profileNama);
        tvjkelamin = v.findViewById(R.id.profileJenisKelamin);
        tvemail = v.findViewById(R.id.profileEmail);
        tvmasjid = v.findViewById(R.id.profileMasjid);
        tvstatus = v.findViewById(R.id.profileStatus);
        btnedit = v.findViewById(R.id.profileEditButton);
        ivprofile = v.findViewById(R.id.profileFotoProfile);

        LoadingBarCheck();
        loadMyProfile(email);

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("nama", tvnama.getText().toString());
                bundle.putString("status", tvstatus.getText().toString());
                bundle.putString("foto", newFoto);

                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_EDIT_PROFILE);
            }
        });

        return v;
    }

    private void loadMyProfile(final String email) {
        StringRequest request = new StringRequest(Request.Method.POST, CEKPROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);

                            tvnama.setText(data.getString("nama"));
                            tvjkelamin.setText(data.getString("jenis_kelamin"));
                            tvemail.setText(email);
                            tvmasjid.setText(data.getString("masjid"));
                            tvstatus.setText(data.getString("status"));
                            newFoto = data.getString("foto");
                            Glide.with(getContext())
                                    .load(newFoto)
                                    .fitCenter()
                                    .error(R.drawable.icon_profile)
                                    .into(ivprofile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        checkBar.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage() != null ? error.getMessage() : "", Toast.LENGTH_SHORT).show();
                        Log.d("CekProfileErrorResponse", error.getMessage() != null ? error.getMessage() : "");
                        checkBar.dismiss();
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();

                    params.put("email", email);

                    return params;
                }
            };
        Volley.newRequestQueue(getContext()).add(request);
    }

    private void LoadingBarCheck() {
        checkBar = new ProgressDialog(getActivity());
        checkBar.setTitle("Please Wait...");
        checkBar.setMessage("While We're Checking your Data");
        checkBar.setCanceledOnTouchOutside(false);
        checkBar.setCancelable(false);
        checkBar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadingBarCheck();
        loadMyProfile(email);
    }
}

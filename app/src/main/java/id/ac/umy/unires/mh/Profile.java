package id.ac.umy.unires.mh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.MainActivity.email;
import static id.ac.umy.unires.mh.Utils.ServerAPI.CEKPROFILE_URL;

public class Profile extends Fragment {

    TextView tvnama, tvjkelamin, tvemail, tvmasjid, tvstatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        tvnama = v.findViewById(R.id.profileNama);
        tvjkelamin = v.findViewById(R.id.profileJenisKelamin);
        tvemail = v.findViewById(R.id.profileEmail);
        tvmasjid = v.findViewById(R.id.profileMasjid);
        tvstatus = v.findViewById(R.id.profileStatus);

        loadMyProfile(email);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CekProfileErrorResponse", error.getMessage());
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
}

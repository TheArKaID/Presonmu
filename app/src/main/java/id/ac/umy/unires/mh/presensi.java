package id.ac.umy.unires.mh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.MainActivity.email;
import static id.ac.umy.unires.mh.Utils.ServerAPI.*;

public class presensi extends Fragment {

    TextView haridantanggal, shiftke;
    Button absen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_presensi, container, false);

        haridantanggal = v.findViewById(R.id.HaridanTanggal);
        shiftke = v.findViewById(R.id.ShiftKe);
        absen = v.findViewById(R.id.Absen);

        loadData();

        absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAbsen(email);
            }
        });
        return v;
    }

    private void doAbsen(String email) {
        StringRequest cekAbsen = new StringRequest(Request.Method.POST, CEKABSEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(cekAbsen);
    }

    private void loadData(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DATETIME_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            haridantanggal.setText(response.getString("tanggal"));
                            shiftke.setText(String.format("Shift ke %s", response.getString("shift")));

                            if(!response.getBoolean("absenable")){
                                absen.setClickable(response.getBoolean("absenable"));
                                absen.setBackgroundColor(getResources().getColor(R.color.colorFalse));
                            }
                            Log.d("Log, ", "Response => "+response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Volley, ", "onErrorResponse => "+error.getMessage());
                    }
                });
        Volley.newRequestQueue(getContext()).add(request);
    }
}

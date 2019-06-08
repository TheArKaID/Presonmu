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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static id.ac.umy.unires.mh.Utils.ServerAPI.DATETIME_URL;

public class presensi extends Fragment {

    TextView haridantanggal, shiftke;
    Button absen;

    String haridantanggalnya, jam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_presensi, container, false);

        haridantanggal = v.findViewById(R.id.HaridanTanggal);
        shiftke = v.findViewById(R.id.ShiftKe);
        absen = v.findViewById(R.id.Absen);

        loadData();
        return v;
    }

    private void loadData(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DATETIME_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            haridantanggal.setText(response.getString("tanggal"));
                            shiftke.setText(String.format("Shift ke %s", response.getString("shift")));
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

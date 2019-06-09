package id.ac.umy.unires.mh;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    String haridantanggalnya, shift;

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
                doAbsen();
            }
        });
        return v;
    }

    private void loadData(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DATETIME_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            haridantanggalnya = response.getString("tanggal");
                            shift = response.getString("shift");
                            haridantanggal.setText(haridantanggalnya);
                            shiftke.setText(String.format("Shift ke %s", shift));

                            if(!response.getBoolean("absenable")){
                                absen.setClickable(response.getBoolean("absenable"));
                                absen.setBackgroundColor(getResources().getColor(R.color.colorFalse));
                            } else{
                                cekAbsen(email);
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

    private void cekAbsen(final String email) {
        StringRequest CekAbsen = new StringRequest(Request.Method.POST, CEKABSEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Done")){
                            didAbsen();
                            Log.d("Response cek Absen => ", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error =>", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("tanggal", haridantanggalnya);
                params.put("shift", shift);

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(CekAbsen);
    }

    private void doAbsen() {
        //TODO: Progress Bar When Absen
        final AlertDialog.Builder notif = new AlertDialog.Builder(getContext());
        StringRequest absen = new StringRequest(Request.Method.POST, ABSEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Sukses")){
                            notif.setTitle("Sukses")
                                    .setMessage("Anda Telah Absen")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //TODO: Re call or Re Draw or anything to refresh the page
                                        }
                                    }).show();
                        } else{
                            notif.setTitle("Gagal")
                                    .setMessage("Coba Lagi")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //TODO: Re call or Re Draw or anything to refresh the page
                                        }
                                    }).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("tanggal", haridantanggalnya);
                params.put("shift", shift);

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(absen);
    }

    private void didAbsen() {
        absen.setClickable(false);
        absen.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        absen.setText("✓");
    }

}

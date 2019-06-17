package id.ac.umy.unires.mh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static id.ac.umy.unires.mh.MainActivity.email;
import static id.ac.umy.unires.mh.Utils.ServerAPI.CEKRIWAYAT_URL;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Riwayat extends Fragment {

    BarChart barChart;

    ArrayList<BarEntry> shift1;
    ArrayList<BarEntry> shift2;
    String[] days;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_riwayat, container, false);

        barChart = v.findViewById(R.id.mp_barchart);

        shift1 = new ArrayList<>();
        shift2 = new ArrayList<>();

        loadData(email);

        return v;
    }

    private void loadData(final String email) {
        StringRequest request = new StringRequest(Request.Method.POST, CEKRIWAYAT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            days = new String[array.length()];

                            for (int pos = 0; pos<array.length(); pos++){
                                String object = array.getString(pos);
                                JSONObject data = new JSONObject(object);
                                int isPresent1 = data.getInt("shift1");
                                int isPresent2 = data.getInt("shift2");

                                days[pos] = data.getString("hari");
                                shift1.add(new BarEntry(pos+1, isPresent1));
                                shift2.add(new BarEntry(pos+1, isPresent2));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showMyBarChart(days, shift1, shift2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ErrorCekRiwayat => ", error.getMessage());
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

    private void showMyBarChart(String[] days, ArrayList<BarEntry> shift1, ArrayList<BarEntry> shift2) {
        BarDataSet dataSet1 = new BarDataSet(shift1, "Shift 1");
        dataSet1.setColor(Color.YELLOW);
        BarDataSet dataSet2 = new BarDataSet(shift2, "Shift 2");
        dataSet2.setColor(Color.BLUE);

        BarData data = new BarData(dataSet1, dataSet2);
        barChart.setData(data);
    }
}

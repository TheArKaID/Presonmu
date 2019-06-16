package id.ac.umy.unires.mh;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.ac.umy.unires.mh.adapters.StatusAdapter;
import id.ac.umy.unires.mh.model.StatusModel;

import static id.ac.umy.unires.mh.MainActivity.email;
import static id.ac.umy.unires.mh.Utils.ServerAPI.*;

public class Home extends Fragment {

    TextView nama, masjid, status;
    ImageView foto;

    ProgressDialog checkBar;
    RecyclerView recyclerView;

    ArrayList<StatusModel> modelList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);

        nama = v.findViewById(R.id.myNamaPeserta);
        masjid = v.findViewById(R.id.myMasjidPeserta);
        status = v.findViewById(R.id.myStatusPeserta);
        foto = v.findViewById(R.id.myProfilePicture);

        recyclerView = v.findViewById(R.id.rv_status);
        recyclerView.setHasFixedSize(true);

        LoadingBarCheck();
        loadStatusData(email);
        loadMyData(email);

        return v;
    }

    private void loadStatusData(final String email) {
        StringRequest request = new StringRequest(Request.Method.POST, STATUSHOME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int pos = 0; pos<array.length(); pos++){
                                String object = array.getString(pos);
                                JSONObject data = new JSONObject(object);

                                StatusModel status = new StatusModel();
                                status.setNama(data.getString("nama"));
                                status.setMasjid(data.getString("masjid"));
                                status.setStatus(data.getString("status"));
                                modelList.add(status);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadStatus();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LoadStatusDataError", error.getMessage());
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

    private void loadStatus() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        StatusAdapter statusAdapter = new StatusAdapter(getContext());
        statusAdapter.setStatusModels(modelList);
        recyclerView.setAdapter(statusAdapter);
    }

    private void loadMyData(final String email) {
        StringRequest request = new StringRequest(Request.Method.POST, MYHOME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       try {
                           JSONObject jsonObject= new JSONObject(response);
                           nama.setText(jsonObject.getString("nama"));
                           masjid.setText(jsonObject.getString("masjid"));
                           status.setText(jsonObject.getString("status"));
                       }catch (Exception e){
                           Log.d("onErrorRequest=> ", e.toString());
                       }
                       checkBar.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("loadMyDataError =>", error.getMessage());
                        checkBar.dismiss();
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
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
}

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static id.ac.umy.unires.mh.Utils.ServerAPI.ABOUT_URL;

public class About extends Fragment {

    TextView about, version;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_about, container, false);

        about = v.findViewById(R.id.aboutText);
        version = v.findViewById(R.id.versionText);

        loadAbout();

        return v;
    }

    private void loadAbout() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ABOUT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            about.setText(response.getString("informasi"));
                            version.setText(String.format("Version %s", response.getString("version")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AboutErrorRequest =>", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getContext()).add(request);
    }
}

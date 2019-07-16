package id.ac.umy.unires.mh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static id.ac.umy.unires.mh.MainActivity.*;
import static id.ac.umy.unires.mh.Utils.ServerAPI.*;

public class Presensi extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    TextView haridantanggal, shiftke;
    Button absen;

    String haridantanggalnya, shift;

    ProgressDialog checkBar;

    MapView mapView;
    GoogleMap map;
    Location lastLocation;
    Marker currentLocationMarker;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_presensi, container, false);

        haridantanggal = v.findViewById(R.id.HaridanTanggal);
        shiftke = v.findViewById(R.id.ShiftKe);
        absen = v.findViewById(R.id.Absen);

        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        loadData();

        absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastLocation==null){
                    AlertDialog exitDialog = new AlertDialog.Builder(getContext()).create();
                    exitDialog.setTitle("Tidak bisa presensi");
                    exitDialog.setMessage("Data Lokasi tidak ditemukan. Pastikan service Lokasi anda aktif dan mengizinkan Presonmu mengaksesnya");
                    exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    exitDialog.show();
                } else{
                    LoadingBarCheck();
                    if(lastLocation.isFromMockProvider()) {
                        exit(lastLocation.getProvider());
                        checkBar.dismiss();
                    }else
                        doAbsen();
                }
            }
        });
        return v;
    }

    private void loadData() {
        LoadingBarCheck();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DATETIME_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            haridantanggalnya = response.getString("tanggal");
                            shift = response.getString("shift");
                            haridantanggal.setText(haridantanggalnya);
                            shiftke.setText(String.format("Shift ke %s", shift));

                            if (!response.getBoolean("absenable")) {
                                absen.setClickable(response.getBoolean("absenable"));
                                absen.setBackgroundColor(getResources().getColor(R.color.colorFalse));
                                checkBar.dismiss();
                            } else {
                                cekAbsen(email);
                            }
                            Log.d("Log, ", "Response => " + response.toString());

                        } catch (JSONException e) {
                            checkBar.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkBar.dismiss();
                        Toast.makeText(getContext(), error.getMessage() != null ? error.getMessage() : "", Toast.LENGTH_SHORT).show();
                        Log.d("Error Volley, ", "onErrorResponse => " + (error.getMessage() != null ? error.getMessage() : ""));
                    }
                });
        Volley.newRequestQueue(getContext()).add(request);
    }

    private void cekAbsen(final String email) {
        StringRequest CekAbsen = new StringRequest(Request.Method.POST, CEKABSEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Done")) {
                            didAbsen();
                            Log.d("Response cek Absen => ", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkBar.dismiss();
                        Log.d("Error =>", error.getMessage() != null ? error.getMessage() : "");
                    }
                }) {
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
        final AlertDialog.Builder notif = new AlertDialog.Builder(getContext());
        StringRequest absen = new StringRequest(Request.Method.POST, ABSEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Sukses")) {
                            notif.setTitle("Sukses")
                                    .setCancelable(false)
                                    .setMessage("Anda Telah Absen")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loadData();
                                        }
                                    }).show();
                        } else {
                            notif.setTitle("Gagal")
                                    .setMessage("Coba Lagi")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loadData();
                                        }
                                    }).show();
                        }
                        checkBar.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkBar.dismiss();
                        Log.d("onErrorResponse", error.getMessage() != null ? error.getMessage() : "");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("tanggal", haridantanggalnya);
                params.put("shift", shift);
                params.put("latlng", lastLocation.getLatitude() + ", " + lastLocation.getLongitude());

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(absen);
    }

    private void didAbsen() {
        absen.setClickable(false);
        absen.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        absen.setText("✓");
        checkBar.dismiss();
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, Welcome.MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        map.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(-7.81012, 110.3220527);

        buildGoogleApiClient();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, Welcome.MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if(currentLocationMarker != null)
            currentLocationMarker.remove();

        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posisi Anda");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void exit(String mocker) {
        AlertDialog exitDialog = new AlertDialog.Builder(getContext()).create();
        exitDialog.setTitle("Fake GPS Terdeteksi : " + mocker);
        exitDialog.setMessage("Matikan Fake GPS dan coba lagi");
        exitDialog.setCancelable(false);
        exitDialog.setCanceledOnTouchOutside(false);
        exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finishAndRemoveTask();
                    }
                });
        exitDialog.show();
    }
}

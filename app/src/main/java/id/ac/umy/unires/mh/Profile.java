package id.ac.umy.unires.mh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Profile extends Fragment {

    TextView nama, jkelamin, email, masjid, status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        nama = v.findViewById(R.id.profileNama);
        jkelamin = v.findViewById(R.id.profileJenisKelamin);
        email = v.findViewById(R.id.profileEmail);
        masjid = v.findViewById(R.id.profileMasjid);
        status = v.findViewById(R.id.profileStatus);

        return v;
    }
}

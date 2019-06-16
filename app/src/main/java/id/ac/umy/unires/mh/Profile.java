package id.ac.umy.unires.mh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static id.ac.umy.unires.mh.MainActivity.email;

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

    }
}

package id.ac.umy.unires.mh;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class home extends Fragment {

    TextView nama, masjid, status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);

        nama = v.findViewById(R.id.namaPeserta);
        masjid = v.findViewById(R.id.masjidPeserta);
        status = v.findViewById(R.id.statusPeserta);

        return v;
    }
}

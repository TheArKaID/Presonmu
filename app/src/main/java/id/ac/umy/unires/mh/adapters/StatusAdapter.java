package id.ac.umy.unires.mh.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import id.ac.umy.unires.mh.R;
import id.ac.umy.unires.mh.model.StatusModel;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StatusModel> statusModels;

    public StatusAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.ViewHolder viewHolder, int i) {
        viewHolder.nama.setText(getStatusModels().get(i).getNama());
        viewHolder.masjid.setText(getStatusModels().get(i).getMasjid());
        viewHolder.status.setText(getStatusModels().get(i).getStatus());
        Glide.with(context)
                .load(getStatusModels().get(i).getFoto())
                .fitCenter()
                .error(R.drawable.icon_profile)
                .into(viewHolder.foto);
    }

    @Override
    public int getItemCount() {
        return getStatusModels().size();
    }

    public ArrayList<StatusModel> getStatusModels() {
        return statusModels;
    }

    public void setStatusModels(ArrayList<StatusModel> statusModels) {
        this.statusModels = statusModels;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama, masjid, status;
        ImageView foto;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaPeserta);
            masjid = itemView.findViewById(R.id.masjidPeserta);
            status = itemView.findViewById(R.id.statusPeserta);
            foto = itemView.findViewById(R.id.profilePicture);
        }
    }
}

package id.ac.umy.unires.mh.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.ViewHolder viewHolder, int i) {

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

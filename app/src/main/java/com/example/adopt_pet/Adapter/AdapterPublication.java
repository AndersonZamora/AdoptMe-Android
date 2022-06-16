package com.example.adopt_pet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopt_pet.R;
import com.example.adopt_pet.models.mascota;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class AdapterPublication extends RecyclerView.Adapter<AdapterPublication.PublicationViewHolder> {

    List<mascota> mascotaList;
    receiveId acction;

    public AdapterPublication(List<mascota> mascotaList, receiveId acction) {
        this.mascotaList = mascotaList;
        this.acction = acction;
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublicationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_publication, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {

        mascota mas = mascotaList.get(position);
        holder.nombreVP.setText(mas.getNombre());
        holder.fechaVP.setText(mas.getFechaPublication());
        holder.estadoVP.setText(mas.getEstado());

        if (mas.getFoto() != null && !Objects.equals(mas.getFoto(), "")) {
            Picasso.get()
                    .load(mas.getFoto())
                    .placeholder(R.color.black)
                    .error(R.color.purple_200)
                    .into(holder.imagePublication, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.imagePublication.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.imagePublication.setVisibility(View.VISIBLE);
                            holder.imagePublication.setImageResource(R.drawable.portada);
                        }
                    });
        }

        holder.setMenuClickListener((view, adapterPosition) -> viewDetail(mascotaList.get(adapterPosition)));
    }

    void viewDetail(@NonNull mascota mascota) {
        acction.receivePublication(mascota.conId);
    }

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    static class PublicationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imagePublication;
        TextView nombreVP;
        TextView estadoVP;
        TextView fechaVP;

        IRecyclerViewMenuClickListener clickListener;

        public void setMenuClickListener(IRecyclerViewMenuClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePublication = itemView.findViewById(R.id.image_publication);
            nombreVP = itemView.findViewById(R.id.nombreVP);
            estadoVP = itemView.findViewById(R.id.estadoVP);
            fechaVP = itemView.findViewById(R.id.fechaVP);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onRecyclerMenuClick(view, getLayoutPosition());
        }
    }
}

package com.example.adopt_pet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopt_pet.R;
import com.example.adopt_pet.models.adopciones;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class AdapterAdopciones extends RecyclerView.Adapter<AdapterAdopciones.PublicationViewHolder> {

    List<adopciones> adopcionesList;
    receiveId acction;

    public AdapterAdopciones(List<adopciones> adopcionesList, receiveId acction) {
        this.adopcionesList = adopcionesList;
        this.acction = acction;
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublicationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_adopcion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {

        adopciones mas = adopcionesList.get(position);
        holder.nombres.setText(mas.getNombre());
        holder.fecha.setText(mas.getFecha());
        holder.tipo.setText(mas.getInfo());

        if (mas.getFoto() != null && !Objects.equals(mas.getFoto(), "")) {
            Picasso.get()
                    .load(mas.getFoto())
                    .placeholder(R.color.black)
                    .error(R.color.purple_200)
                    .into(holder.imagen_mascota, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.imagen_mascota.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.imagen_mascota.setVisibility(View.VISIBLE);
                            holder.imagen_mascota.setImageResource(R.drawable.portada);
                        }
                    });
        }

        holder.setMenuClickListener((view, adapterPosition) -> viewDetail(adopcionesList.get(adapterPosition)));
    }

    void viewDetail(@NonNull adopciones adopciones) {
        acction.receivePublication(adopciones.conId);
    }

    @Override
    public int getItemCount() {
        return adopcionesList.size();
    }

    static class PublicationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imagen_mascota;
        TextView nombres;
        TextView fecha;
        TextView tipo;

        IRecyclerViewMenuClickListener clickListener;

        public void setMenuClickListener(IRecyclerViewMenuClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);

            imagen_mascota = itemView.findViewById(R.id.imagen_mascota);
            nombres = itemView.findViewById(R.id.nombres);
            fecha = itemView.findViewById(R.id.fecha);
            tipo = itemView.findViewById(R.id.tipo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onRecyclerMenuClick(view, getLayoutPosition());
        }
    }
}

package com.example.firechat.controlador;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firechat.R;
import com.example.firechat.modelo.Mensaje;
import com.example.firechat.modelo.Usuario;

import java.util.ArrayList;

public class AdaptadorChats extends RecyclerView.Adapter<AdaptadorChats.AdaptadorViewHolder>  {


    ArrayList<Mensaje> lei;
    Usuario logue, rece;
    Activity ac;


    public AdaptadorChats(ArrayList<Mensaje> listaLe, Usuario logueado, Usuario receptor, Activity activity){
        this.lei = listaLe;
        this.logue = logueado;
        this.rece = receptor;
        this.ac = activity;
    }

    @NonNull
    @Override
    public AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje, parent, false);
        AdaptadorViewHolder adaptador = new AdaptadorViewHolder(itemView);
        return adaptador;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorViewHolder holder, int position) {
        Mensaje mensaje = lei.get(position);
        holder.mensaje.setText(mensaje.getEmisor()+": "+mensaje.getTexto());
        holder.hora.setText(mensaje.getHora());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder{

        TextView mensaje, hora;


        public AdaptadorViewHolder(@NonNull View itemView){
            super(itemView);
            mensaje = itemView.findViewById(R.id.textViewMensaje);
            hora = itemView.findViewById(R.id.textViewHora);
        }
    }
}

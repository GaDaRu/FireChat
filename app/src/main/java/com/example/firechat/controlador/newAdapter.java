package com.example.firechat.controlador;

import android.app.Activity;
import android.content.Context;
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
import java.util.List;

public class newAdapter  extends RecyclerView.Adapter<newAdapter.AdaptadorViewHolder> {
    ArrayList<Mensaje> lei;
    Usuario logue, rece;
    Activity ac;

    public newAdapter(ArrayList<Mensaje> listaLe, Usuario logueado, Usuario receptor, Activity activity) {
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
        //Obtenemos el mensaje correspondiente
        Mensaje mensaje = lei.get(position);
        //Si el mensaje tiene como texto x nos lo pasamos y no hacemos nada(Por el mensaje de prueva para evitar ArrayList vacios)
        if(mensaje.getTexto().equalsIgnoreCase("x")){

        }else{
            if(mensaje.getEmisor().equalsIgnoreCase(rece.getEmail())){
                holder.mensaje.setText(mensaje.getEmisor()+": "+mensaje.getTexto());
            }else{
                holder.mensaje.setText("Yo: "+": "+mensaje.getTexto());
            }

            holder.hora.setText(mensaje.getHora());
        }
    }

    @Override
    public int getItemCount() {
        return lei.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        TextView mensaje, hora;

        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.textViewMensaje);
            hora = itemView.findViewById(R.id.textViewHora);

        }
    }
}

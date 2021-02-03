package com.example.firechat.controlador;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.firechat.Chats;
import com.example.firechat.R;
import com.example.firechat.modelo.Mensaje;
import com.example.firechat.modelo.Usuario;

import java.util.ArrayList;

public class AdaptadorUsers extends RecyclerView.Adapter<AdaptadorUsers.AdaptadorViewHolder> {
    private ArrayList<Usuario> lista;
    String correo;
    TextView usua, leido;
    Activity ac;
    Usuario user;


    public class AdaptadorViewHolder extends RecyclerView.ViewHolder{

        public AdaptadorViewHolder(View itemView){
            super(itemView);

            usua = itemView.findViewById(R.id.textViewUsuario);
            leido = itemView.findViewById(R.id.textViewSinLeer);
        }
    }

    public AdaptadorUsers(ArrayList<Usuario> lista, String correo, Activity activity){
        this.lista = lista;
        this.correo = correo;
        this.ac = activity;
    }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.usu, parent, false);
        AdaptadorViewHolder avh = new AdaptadorViewHolder(itemView);
        return avh;
    }

    @Override
    public void onBindViewHolder(AdaptadorUsers.AdaptadorViewHolder holder, int position) {
        //Añadimos el nombre de la persona en el recyclerView

        usua.setText(lista.get(position).getNombre());

        ArrayList<Mensaje> listaMensajes = new ArrayList<>();

        //Obtenemos los mensajes del usuario iniciado y los añadimos a una lista
        /*for(int i = 0; i < user.getMensajesRecibidos().size(); i++){
            listaMensajes.add(user.getMensajesRecibidos().get(i));
        }

        //Comprobamos de quien es cada mensaje, para poder poner en el recyclerView si tiene mensajes nuevos de ese usuario o no
        for (int i = 0; i < listaMensajes.size(); i++){
            if(lista.get(position).getEmail().equalsIgnoreCase(listaMensajes.get(i).getEmisor())){
                leido.setText("Sin leer");
            }else{
                leido.setText("Al día");
            }
        }*/

        //Al hacer click en el recyclerView que queremos nos manda al Chat de esa persona
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inicio = new Intent(ac, Chats.class);
                inicio.putExtra("correo", correo);
                inicio.putExtra("nombre", lista.get(position).getNombre());
                inicio.putExtra("correoReceptor", lista.get(position).getEmail());
                ac.startActivity(inicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

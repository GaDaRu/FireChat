package com.example.firechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.firechat.controlador.AdaptadorChats;
import com.example.firechat.controlador.AdaptadorUsers;
import com.example.firechat.controlador.newAdapter;
import com.example.firechat.modelo.Mensaje;
import com.example.firechat.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Chats extends AppCompatActivity {

    private String nombre, correoLogin, correorecep;

    private TextView nom, mensaje;

    private Button salir, enviar;

    private RecyclerView rvMensajes;

    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseId;

    private ValueEventListener eventListener;

    private Usuario emisor, receptor, user;

    int me;

    ArrayList<Mensaje> mr, ml;

    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView.Adapter adaptador;

    Activity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //Inicializamos las variables con sus objetos
        nom = findViewById(R.id.textViewPersona);
        mensaje = findViewById(R.id.editTextMensaje);
        salir = findViewById(R.id.buttonSalirChats);
        enviar = findViewById(R.id.buttonEnviarMensaje);
        rvMensajes = findViewById(R.id.rvMensajes);
        mr = new ArrayList<>();
        ml = new ArrayList<>();
        ac = this;

        //Rescatamos la informacion mandada por la Activity anterior
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            correoLogin = extras.getString("correo");
            nombre = extras.getString("nombre");
            correorecep = extras.getString("correoReceptor");
        }

        //Le damos forma al RecyclerView
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMensajes.setLayoutManager(layoutManager);

        //Obtenemos todos los mensajes de lus usuarios
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabaseId = FirebaseDatabase.getInstance().getReference().child("usuarios");

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 1; i <= snapshot.getChildrenCount(); i++){
                    user = snapshot.child(""+i).getValue(Usuario.class);

                    if(user.getEmail().equalsIgnoreCase(correoLogin)){
                        emisor = user;
                        Mensaje men = new Mensaje();
                        for(int j = 0; j < snapshot.child(""+i).child("mensajesRecibidos").getChildrenCount(); j++){
                            men = snapshot.child(""+i).child("mensajesRecibidos").child(""+j).getValue(Mensaje.class);
                            if(men.getEmisor().equalsIgnoreCase(correorecep)){
                                mr.add(men);
                            }
                        }
                        for(int j = 0; j < snapshot.child(""+i).child("mensajesLeidos").getChildrenCount(); j++){
                            men = snapshot.child(""+i).child("mensajesLeidos").child(""+j).getValue(Mensaje.class);
                            if(men.getEmisor().equalsIgnoreCase(correorecep)){
                                ml.add(men);
                            }
                        }
                    }else if(user.getNombre().equalsIgnoreCase(nombre)){
                        receptor = user;
                        Mensaje men = new Mensaje();
                        for(int j = 0; j < snapshot.child(""+i).child("mensajesRecibidos").getChildrenCount(); j++){
                            me = j+1;
                            men = snapshot.child(""+i).child("mensajesRecibidos").child(""+j).getValue(Mensaje.class);
                            mr.add(men);
                        }
                        for(int j = 0; j < snapshot.child(""+i).child("mensajesLeidos").getChildrenCount(); j++){
                            men = snapshot.child(""+i).child("mensajesLeidos").child(""+j).getValue(Mensaje.class);
                            ml.add(men);
                        }
                    }
                }

                //Añadimos los mensajes de mr(Mensajes recibidos) a ml(mensajes leidos)
                for(int i = 0; i < mr.size(); i++){
                    ml.add(mr.get(i));
                }
                //Pasamos los valores al adaptador y se lo pasamos al RacyclerView
                adaptador = new newAdapter(ml, emisor, receptor, ac);
                rvMensajes.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabaseId.addValueEventListener(eventListener);

        enviar.setOnClickListener(v -> {enviarMensaje();});

        nom.setText(nombre);
        salir.setOnClickListener(v -> {onBackPressed();});
    }

    //Metodo que controla enviar mensajes
    private void enviarMensaje() {
        Mensaje men = new Mensaje();

        //Recogemos el texto
        men.setTexto(mensaje.getText().toString());
        mensaje.setText("");

        //Sacamos la hora del sistema
        Calendar calen = Calendar.getInstance();
        men.setHora(calen.get(Calendar.HOUR_OF_DAY) + ":" + calen.get(Calendar.MINUTE));

        //Le damos los valores de leido, el emisor y el receptor
        men.setLeido(0);
        men.setEmisor(emisor.getEmail());
        men.setReceptor(receptor.getEmail());

        //Añadimos el mensaje en el usuario receptor
        mDatabase.child("usuarios").child(receptor.getId()).child("mensajesRecibidos").child(""+me).setValue(men);
    }
}
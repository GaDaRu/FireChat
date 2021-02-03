package com.example.firechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.firechat.controlador.AdaptadorUsers;
import com.example.firechat.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity{

    //Iniciamos las variables
    private Button salir, bt;

    private RecyclerView rv;

    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView.Adapter adaptador;

    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseId;

    private ValueEventListener eventListener;

    private ArrayList<Usuario> correos = new ArrayList<>();

    private Usuario user = new Usuario();

    private Usuario personal = new Usuario();

    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Rescatamos el Email
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            correo = extras.getString("email");
        }

        //Aqui accedemos a los usuarios
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabaseId = FirebaseDatabase.getInstance().getReference().child("usuarios");

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 1; i <= snapshot.getChildrenCount(); i++){
                    user = snapshot.child(""+i).getValue(Usuario.class);
                    //Cogemos el usuario con el que estamos logueados
                    if(user.getEmail().equalsIgnoreCase(correo)){
                        personal = user;
                    }else{
                        //El resto de usuarios los añadimos a una lista
                        correos.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabaseId.addValueEventListener(eventListener);

        //Iniciamos el recyclerView
        rv = findViewById(R.id.recyclerView);

        //Le damos formato vertical
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        //Llamamos al adaptador
       /* adaptador = new AdaptadorUsers(correos, correo, this);
        rv.setAdapter(adaptador);*/

        salir = findViewById(R.id.buttonSalir);
        salir.setOnClickListener(v -> {salir();});

        bt = findViewById(R.id.button);
        bt.setOnClickListener(v -> {reload();});
        reload();
    }

    //Recarga la lista de usuarios registrados
    private void reload() {
        adaptador = new AdaptadorUsers(correos, correo, this);
        rv.setAdapter(adaptador);
    }

    //Boton para salir de la APP, cierra sesion
    private void salir() {
        SharedPreferences.Editor pref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit();
        pref.clear();
        pref.apply();

        FirebaseAuth.getInstance().signOut();
        onBackPressed();
    }
}
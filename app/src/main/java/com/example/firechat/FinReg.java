package com.example.firechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firechat.modelo.Mensaje;
import com.example.firechat.modelo.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FinReg extends AppCompatActivity{

    private DatabaseReference mDatabase;

    TextView id, correo, nombre, telefono;
    Button terminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_reg);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        id = findViewById(R.id.editTextId);
        correo = findViewById(R.id.editTextEma);
        nombre = findViewById(R.id.editTextNombre);
        telefono = findViewById(R.id.editTextTelefono);

        terminar = findViewById(R.id.buttonTerminar);
        terminar.setOnClickListener(v -> {terminarLogin();});

        //Rescatamos los datos pasados del MainActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            //Añadimos los datos de id y correo, no se podran modificar
            id.setText(extras.getString("id"));
            correo.setText(extras.getString("email"));
        }
    }

    private void terminarLogin() {
        //Creamos dos listas
        List<Mensaje> mLeidos = new ArrayList<>();
        List<Mensaje> mEnviados = new ArrayList<>();

        //Creamos dos mensajes de prueba, para evitar un ArrayList vacio
        Mensaje men = new Mensaje("x", "12:05", 0, "x", "x");
        Mensaje men2 = new Mensaje("x", "12:05", 0, "x", "x");

        mLeidos.add(men);
        mEnviados.add(men2);

        //Creamos el usuario con todos sus valores
        Usuario user = new Usuario();
        user.setId(id.getText().toString());
        user.setEmail(correo.getText().toString());
        user.setNombre(nombre.getText().toString());
        user.setTelefono(telefono.getText().toString());
        user.setMensajesLeidos(mLeidos);
        user.setMensajesRecibidos(mEnviados);

        //Comprobados que los dos campos a rellenar esten con datos correctos
        if(TextUtils.isEmpty(nombre.getText().toString())){nombre.setError("Requerido"); return;}
        if(TextUtils.isEmpty(telefono.getText().toString())){telefono.setError("Requerido"); return;}

        String key = user.getEmail();
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        //Añadimos el usuario a firebase
        mDatabase.child("usuarios").child(user.getId()).setValue(user);

        //Enviamos datos al siguiente Activity
        Intent inicio = new Intent(this, HomeActivity.class);
        inicio.putExtra("email", user.getEmail());
        startActivity(inicio);
    }
}
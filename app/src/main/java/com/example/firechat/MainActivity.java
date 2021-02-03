package com.example.firechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firechat.modelo.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    //Inicializamos las variables
    TextView usuario, contrasena;
    Button registrar, iniciar, google;

    private int GOOGLE_SIGN_IN = 100;

    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseId;

    Usuario user;

    private ValueEventListener eventListener;

    ArrayList<String> correos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buscamos su id
        usuario = findViewById(R.id.editTextCorreo);
        contrasena = findViewById(R.id.editTextContrasena);

        registrar = findViewById(R.id.buttonRegistrar);
        registrar.setOnClickListener(v -> {registrar();});
        iniciar = findViewById(R.id.buttonIniciar);
        iniciar.setOnClickListener(v -> {iniciar();});
        google = findViewById(R.id.buttonGoogle);
        google.setOnClickListener(v -> {google();});

        //Funcion para comprobar si tenemos la sesion ya iniciada en el dispositivo movil
        iniciada();

        FirebaseAnalytics analisis = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("message", "Entro");
        analisis.logEvent("PantallaIniciar", bundle);

        //Iniciamos la lectura de la DB de Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabaseId = FirebaseDatabase.getInstance().getReference().child("usuarios");

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 1; i <= snapshot.getChildrenCount(); i++){
//                    contador = snapshot.child("id").getValue().toString();
//                    user = snapshot.getValue(Usuario.class);
                    //Sacamos todos los usuarios que tenemos y los guardamos en una lista
                    user = snapshot.child(""+i).getValue(Usuario.class);
                    correos.add(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabaseId.addValueEventListener(eventListener);
    }

    //Clase que comprueba si tenemos sesion iniciada
    private void iniciada() {
        //Comprobamos en el directorio si hay sesion iniciada
        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        //Sacamos el "email" del usuario q hay iniciado
        String email = prefs.getString("email", null);
        if(email != null){
            //Lo mandamos a la pantalla de los chats
            showLogin(email);
        }
    }

    //Iniciamos sesion con Google
    private void google() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient signInIntent = GoogleSignIn.getClient(this, gso);

        signInIntent.signOut();

        startActivityForResult(signInIntent.getSignInIntent(), GOOGLE_SIGN_IN);
    }

    //Iniciar con Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account != null){
                    AuthCredential gp = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                    FirebaseAuth.getInstance().signInWithCredential(gp).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Comprobamos que el usuario no este registrado.
                                String email = account.getEmail();
                                boolean existe = false;
                                for(int i = 0; i < correos.size(); i++){
                                    if(correos.get(i).equalsIgnoreCase(email)){
                                        existe = true;
                                    }
                                }
                                if(existe == false){
                                    //Si no existe lo mandamos a la ventana para terminar el registro.
                                    showRegister(email, user.getId());
                                }else if(existe == true){
                                    //En el caso de que ya se haya registrado lo mandamos a la pantalla de los chats
                                    showLogin(email);
                                    existe = false;
                                }
                            }
                        }
                    });
                }
            } catch (ApiException e) {
                System.out.println(e);
            }
        }
    }

    //Iniciar sesion con correo
    private void iniciar() {
        if(usuario.getText().toString().isEmpty() && contrasena.getText().toString().isEmpty()){
            System.out.println("Error");
        }else{
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    usuario.getText().toString(),
                    contrasena.getText().toString()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        //Al iniciar sesion mandamos al usuario a la pantalla de Chats
                        showLogin(usuario.getText().toString());
                    }else{
                    }
                }
            });
        }
    }

    //Registramos con correo
    private void registrar() {
        if(usuario.getText().toString().isEmpty() && contrasena.getText().toString().isEmpty()){
            System.out.println("Error");
        }else{
            System.out.println(usuario.getText().toString());
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    usuario.getText().toString(),
                    contrasena.getText().toString()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Al registrarse el usuario lo mandamos a la ventana de registro para completar los datos restantes
                    showRegister(usuario.getText().toString(), user.getId());
                }
            });
        }
    }

    //En este metodo redirigimos a terminar de registrar
    private void showRegister(String email, String id){
        //Toast.makeText(this, user.getNombre(), Toast.LENGTH_SHORT).show();
        //Guardamos el inicio de sesion en el movil
        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit();
        prefs.putString("email", email);
        prefs.apply();

        //Lo mandamos a FinReg para terminar su registro, pasandole el email y el id
        int i = Integer.parseInt(id);
        i = i + 1;
        Intent inicio = new Intent(this, FinReg.class);
        inicio.putExtra("email", email);
        inicio.putExtra("id", ""+i);
        startActivity(inicio);
    }

    //En este metodo redirigimos a Chats
    private void showLogin(String email){
        //Guardamos el inicio de sesion del usuario
        SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit();
        prefs.putString("email", email);
        prefs.apply();

        //Lo mandamos a HomeActivity
        Intent inicio = new Intent(this, HomeActivity.class);
        inicio.putExtra("email", email);
        startActivity(inicio);
    }
}
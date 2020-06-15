package com.example.lab09_10.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lab09_10.Data.AsyncTaskManager;
import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.Model.Usuario;
import com.example.lab09_10.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddEstudianteActivity extends AppCompatActivity {
    private EditText cedula;
    private EditText nombre;
    private EditText apellidos;
    private EditText edad;
    private ImageView sendButton;
    private ImageView edit;
    //private DBAdapterSQL db;
    private Estudiante estudiante;
    private Boolean admin;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_estudiante);
        //db = DBAdapterSQL.getInstance(this);
        this.cedula = findViewById(R.id.descripcion);
        this.nombre = findViewById(R.id.creditos);
        this.apellidos = findViewById(R.id.apellidosEstudiante);
        this.edad = findViewById(R.id.edadEstudiante);
        this.sendButton = findViewById(R.id.sendEstudiante);
        this.edit = findViewById(R.id.edit);
        loadingProgressBar = findViewById(R.id.barra);
        Bundle extras = getIntent().getExtras();
        final Boolean editable;
        if (extras != null) {
            editable = extras.getBoolean("editable");
            if (editable) {
                admin = extras.getBoolean("actual");
                estudiante = (Estudiante) getIntent().getSerializableExtra("estudiante");
                if (admin) {
                    this.cedula.setText(estudiante.getCedula());
                    this.cedula.setFocusable(false);
                    this.nombre.setText(estudiante.getNombre());
                    this.nombre.setFocusable(false);
                    this.apellidos.setText(estudiante.getApellidos());
                    this.apellidos.setFocusable(false);
                    this.edad.setText(Integer.toString(estudiante.getEdad()));
                    this.edad.setFocusable(false);
                    this.sendButton.setVisibility(View.GONE);
                    this.edit.setVisibility(View.VISIBLE);

                    this.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enableInputs();
                        }
                    });
                } else {
                    this.edit.setVisibility(View.GONE);
                    this.sendButton.setVisibility(View.VISIBLE);
                    display();

                }
                this.sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        showProgress(true);
                        editEstudiante();
                        // editable();
                    }
                });
            } else {
                this.edit.setVisibility(View.GONE);
                this.sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildEstudiante();
                    }
                });
            }
        }


    }

    public void display() {
        this.cedula.setText(estudiante.getCedula());
        this.nombre.setText(estudiante.getNombre());
        this.apellidos.setText(estudiante.getApellidos());
        this.edad.setText(Integer.toString(estudiante.getEdad()));
    }

    public void enableInputs() {
        this.cedula.setFocusableInTouchMode(true);
        this.nombre.setFocusableInTouchMode(true);
        this.apellidos.setFocusableInTouchMode(true);
        this.edad.setFocusableInTouchMode(true);
        this.edit.setVisibility(View.GONE);
        this.sendButton.setVisibility(View.VISIBLE);
    }

    public void editable() {
        this.sendButton.setVisibility(View.GONE);
        this.edit.setVisibility(View.VISIBLE);
    }

    public void buildEstudiante() {
        if (this.validateForm()) {
            Estudiante estudiante = new Estudiante();
            estudiante.setCedula(this.cedula.getText().toString());
            estudiante.setNombre(this.nombre.getText().toString());
            estudiante.setApellidos(this.apellidos.getText().toString());
            estudiante.setEdad(Integer.parseInt(this.edad.getText().toString()));
            Usuario user = new Usuario();
            String credentials = estudiante.getNombre().toLowerCase();
            user.setUsuario(credentials);
            user.setPassword(credentials);
            user.setRol("estandar");

            insertarUsuarioYest(user,estudiante);
            //estudiante.setUser(db.getCountUsuario());
            //insertarEstudiante(estudiante);

            listarEstudiantes();
        }

    }

    public void listarEstudiantes() {
        finish();
        Intent intent = new Intent(AddEstudianteActivity.this, EstudianteListActivity.class);
        startActivity(intent);
    }

    public void editEstudiante() {
        if (this.validateForm()) {
            this.estudiante.setCedula(this.cedula.getText().toString());
            this.estudiante.setNombre(this.nombre.getText().toString());
            this.estudiante.setApellidos(this.apellidos.getText().toString());
            this.estudiante.setEdad(Integer.parseInt(this.edad.getText().toString()));
            updateEstudiante(this.estudiante);
            if (admin) {
                showProgress(true);
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Se han guardado los cambios satisfactoriamente", Toast.LENGTH_LONG).show();
            } else {
                listarEstudiantes();
            }
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.cedula.getText())) {
            cedula.setError("Cédula requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.nombre.getText())) {
            nombre.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.apellidos.getText())) {
            apellidos.setError("Apellidos requeridos");
            error++;
        }
        if (TextUtils.isEmpty(this.edad.getText())) {
            edad.setError("Edad requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "El formulario contiene errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void homeUser() {
        finish();
        Intent intent = new Intent(AddEstudianteActivity.this, NavDrawerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, NavDrawerActivity.class);
        startActivity(intent);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

            loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            loadingProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void insertarUsuarioYest(Usuario user,Estudiante estudiante){
        JsonObject jsonObject = new JsonObject();

        JsonParser parser = new JsonParser();
        jsonObject.add("cedula",parser.parse(estudiante.getCedula()));
        jsonObject.add("nombre",parser.parse(estudiante.getNombre()));
        jsonObject.add("apellidos",parser.parse(estudiante.getApellidos()));
        jsonObject.add("edad",parser.parse(String.valueOf(estudiante.getEdad())));

        String url = "http://192.168.1.8:14715/QuizWeb/servletEstudiantes?" +
                "estudiante="+jsonObject.getAsString();
        AsyncTaskManager net = new AsyncTaskManager(url, new AsyncTaskManager.AsyncResponse() {
            @Override
            public void processFinish(String output) {

            }
        });
        net.execute(AsyncTaskManager.POST);
    }
    private void insertarEstudiante(Estudiante estudiante){

    }
    private void updateEstudiante(Estudiante estudiante){}
}
package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiz.LogicaDeNegocio.Estudiante;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class AddUpdEstudianteActivity extends AppCompatActivity {

    private FloatingActionButton finishBtn;
    private boolean editable = true;
    private EditText nombreField;
    private EditText cedulaField;
    private EditText emailField;
    private EditText telefonoField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_estudiante);
       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.profesor));*/
        nombreField = findViewById(R.id.nombreProfesor);
        cedulaField = findViewById(R.id.cedulaProfesor);
        emailField = findViewById(R.id.emailProfesor);
        telefonoField=findViewById(R.id.telefonoProfesor);
        nombreField.setText("");
        cedulaField.setText("");
        emailField.setText("");
        telefonoField.setText("");

        finishBtn = findViewById(R.id.finishBtn);
        Bundle extras = getIntent().getExtras(); // para extraer el objeto extras producido en el intent
        if(extras!= null){
            editable = extras.getBoolean("editable");
            if(editable){ // EDITAR
                Estudiante aux = (Estudiante) getIntent().getSerializableExtra("profesor");
                nombreField.setText(aux.getNombre());
                cedulaField.setText(aux.getCedula());
                cedulaField.setEnabled(false);
                emailField.setText(aux.getApellidos());
                telefonoField.setText(Integer.toString(aux.getEdad()));
                finishBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editProfesor();
                    }
                });
            } else{ // agregar profesor
                finishBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addProfesor();
                    }
                });
            }
        }

    }
    private void editProfesor(){
        if(this.validateForm()){
            Estudiante profesor = new Estudiante();
            profesor.setNombre(this.nombreField.getText().toString());
            profesor.setCedula(this.cedulaField.getText().toString());
            profesor.setApellidos(this.emailField.getText().toString());
            profesor.setEdad(Integer.parseInt(this.telefonoField.getText().toString()));
            Intent intentProfesor =  new Intent(getBaseContext(), MainActivity.class);
           // intentProfesor.putExtra("editProfesor", profesor);
            startActivity(intentProfesor);
            finish(); //liberar recursos

        }

    }
    public void addProfesor(){
        if(this.validateForm()){
            Estudiante profesor = new Estudiante();
            profesor.setNombre(this.nombreField.getText().toString());
            profesor.setCedula(this.cedulaField.getText().toString());
            profesor.setApellidos(this.emailField.getText().toString());
            profesor.setEdad(Integer.parseInt(this.telefonoField.getText().toString()));
            JSONObject p = new JSONObject();
            try {
                p.put("cedula", profesor.getCedula());
                p.put("nombreProfesor", profesor.getNombre());
                p.put("telefonoProfesor", profesor.getApellidos());
                p.put("emailProfesor", profesor.getEdad());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsyncTaskManager net = new AsyncTaskManager("http://10.0.2.2:36083/frontend_web/servletProfesores", new AsyncTaskManager.AsyncResponse() {
                @Override
                public void processFinish(String output) {

                }
            });
            net.execute(AsyncTaskManager.POST, p.toString());
            Intent intentProfesor =  new Intent(getBaseContext(), MainActivity.class);
            //intentProfesor.putExtra("addProfesor", profesor);
            startActivity(intentProfesor);
            finish(); //liberar recursos
        }
    }
    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.nombreField.getText())) {
            nombreField.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.cedulaField.getText())) {
            cedulaField.setError("Cedula requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.emailField.getText())) {
            emailField.setError("Email requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.telefonoField.getText())) {
            telefonoField.setError("Telefono requerido");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Hay errores en el formulario", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

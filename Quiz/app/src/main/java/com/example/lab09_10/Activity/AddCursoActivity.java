package com.example.lab09_10.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.Model.Curso;
import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.R;

public class AddCursoActivity extends AppCompatActivity {

    private EditText descripcion;
    private EditText creditos;
    private ImageView sendButton;
    //private DBAdapterSQL db;
    private Curso curso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_curso);
        //db = DBAdapterSQL.getInstance(this);
        descripcion = findViewById(R.id.descripcion);
        creditos = findViewById(R.id.creditos);
        sendButton = findViewById(R.id.sendCurso);
        Bundle extras = getIntent().getExtras();

        Boolean editable;
        if (extras != null) {
            editable = extras.getBoolean("editable");
            if (editable) {
                curso = (Curso) getIntent().getSerializableExtra("curso");
                this.descripcion.setText(curso.getDescripcion());
                this.creditos.setText(Integer.toString(curso.getCreditos()));
                this.sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editCurso();
                    }
                });
            }
            else{
                this.sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildCurso();
                    }
                });
            }
        }
    }
    public void editCurso(){
        if(validateForm()){
            this.curso.setDescripcion(this.descripcion.getText().toString());
            this.curso.setCreditos(Integer.parseInt(this.creditos.getText().toString()));
            db.updateCurso(this.curso);
            //db.close();
            listarCursos();
        }
    }
    public void buildCurso(){
        if(validateForm()){
            Curso curso  = new Curso();
            curso.setDescripcion(this.descripcion.getText().toString());
            curso.setCreditos(Integer.parseInt(this.creditos.getText().toString()));
            db.insertarCurso(curso);
            db.close();
            listarCursos();
        }
    }
    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.descripcion.getText())) {
            descripcion.setError("Descripción requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.creditos.getText())) {
            creditos.setError("Créditos requeridos");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "El formulario contiene errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void listarCursos(){
        finish();
        Intent intent = new Intent(AddCursoActivity.this, CursoListActivity.class);
        startActivity(intent);
    }
}
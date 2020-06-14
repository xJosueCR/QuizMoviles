package com.example.lab09_10.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.Model.Curso;
import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBAdapterSQL db;
    private LinearLayout linearLayout;
    private CheckBox checkBox;
    private List<CheckBox> checkBoxList = new ArrayList<>();
    private ImageButton matricular;
    private List<Curso> cursosDisponibles;
    private List<Integer> cursosMatriculados;
    private Estudiante estudiante;
    private List<Curso> cursoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linear_layout);
        intentInformation();
        db = DBAdapterSQL.getInstance(this);
        this.cursosMatriculados = new ArrayList<>();
        cursosDisponibles = db.listCurso();
        cursoList = db.cursosEstudiante(this.estudiante.getId());
        for(int i =0; i<cursosDisponibles.size(); i++){
            checkBox = new CheckBox(this);
            checkBox.setId(cursosDisponibles.get(i).getId());
            checkBox.setText(cursosDisponibles.get(i).getDescripcion());
            checkBox.setTextSize(18);
            linearLayout.addView(checkBox);
            checkBoxList.add(checkBox);
            if(checkCursos(cursosDisponibles.get(i).getId())){
                checkBox.setChecked(true);
            }
            //checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
        }
        matricular = findViewById(R.id.matricular_cursos);
        matricular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matricularCursos();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, CursosEstudianteActivity.class);
        startActivity(intent);

    }
    public boolean checkCursos(int id){
        for (Curso c:this.cursoList) {
            if(c.getId() == id){
                return true;
            }
        }
        return false;
    }
    public void matricularCursos(){
        for (CheckBox check: this.checkBoxList) {
            if(check.isChecked()){
                cursosMatriculados.add(check.getId());
            }
        }
        db.eliminarCursosEstudiante(this.estudiante.getId());
        if(db.matricularCursos(cursosMatriculados, this.estudiante.getId())){
            Toast.makeText(getApplicationContext(), "Cursos guardados con éxito ", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Se ha producido un error. Intentelo nuevamente", Toast.LENGTH_LONG).show();
        }
        goMisCursos();

    }
    public void intentInformation(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            estudiante = (Estudiante) getIntent().getSerializableExtra("estudiante");
        }
    }
    public void goMisCursos(){
        finish();
        Intent intent = new Intent(this, CursosEstudianteActivity.class);
        intent.putExtra("estudiante", estudiante);
        startActivity(intent);
    }

}

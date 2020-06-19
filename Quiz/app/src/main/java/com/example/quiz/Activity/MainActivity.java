package com.example.quiz.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.quiz.Data.AsyncTaskManager;
import com.example.quiz.Model.Curso;
import com.example.quiz.Model.Estudiante;
import com.example.quiz.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //DBAdapterSQL db;
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
      ;
        this.cursosMatriculados = new ArrayList<>();
        prepareListas();

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

        matricular(cursosMatriculados, this.estudiante.getId());



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

    public void prepareListas(){
        listCurso(this.estudiante.getId());
    }

    public void preparedView(){
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
    }
    public void listCurso(final int id){
        cursosDisponibles = new ArrayList<>();
        AsyncTaskManager net = new AsyncTaskManager("http://192.168.1.8:14715/QuizWeb/servletCursos?opcion=1", new AsyncTaskManager.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray array = new JSONArray(output);
                    //carreraList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        Curso c = new Curso(
                                array.getJSONObject(i).getInt("id"),
                                array.getJSONObject(i).getString("descripcion"),
                                array.getJSONObject(i).getInt("creditos")
                        );

                        cursosDisponibles.add(c);
                    }
                    cursosEstudiante(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net.execute(AsyncTaskManager.GET);
    }
    public void cursosEstudiante(int id){
        cursoList = new ArrayList<>();
        AsyncTaskManager net = new AsyncTaskManager("http://192.168.1.8:14715/QuizWeb/servletCursos?opcion=2&estudiante="+id, new AsyncTaskManager.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    if(!output.equals("null")) {
                        JSONArray array = new JSONArray(output);
                        //carreraList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            Curso c = new Curso(
                                    array.getJSONObject(i).getInt("id"),
                                    array.getJSONObject(i).getString("descripcion"),
                                    array.getJSONObject(i).getInt("creditos")
                            );

                            cursoList.add(c);
                        }
                    }
                    preparedView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net.execute(AsyncTaskManager.GET);
    }

    public void matricular(List<Integer> L, int id ){
        /*int[] vector = new int[L.size()];
        for ( int i = 0;i<L.size();i++) {
            vector[i]=L.get(i);
        }*/
        String vector = "[";
        for ( int i = 0;i<L.size();i++) {
            vector+=L.get(i);
            if(i+1< L.size()){
                vector += ",";
            }

        }
        vector += "]";
        //String array = Arrays.toString(vector);
        String URL = "http://192.168.1.8:14715/QuizWeb/servletCursos?estudiante="+id+"&values="+vector;
        AsyncTaskManager net = new AsyncTaskManager(URL, new AsyncTaskManager.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                goMisCursos();
            }
        });
        net.execute(AsyncTaskManager.POST);
    }
}

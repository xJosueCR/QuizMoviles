package com.example.lab09_10.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.lab09_10.Adapter.CursoAdapter;
import com.example.lab09_10.Adapter.EstudianteAdapter;
import com.example.lab09_10.Data.AsyncTaskManager;
import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.Helper.RecyclerItemTouchHelper;
import com.example.lab09_10.Model.Curso;
import com.example.lab09_10.Model.Estudiante;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab09_10.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CursosEstudianteActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        CursoAdapter.CursoListener{
    private RecyclerView mRecyclerView;
    private CursoAdapter mAdapter;
    private List<Curso> cursoList;
    private SearchView searchView;
    private CoordinatorLayout coordinatorLayout;
   // private DBAdapterSQL db;
    private ImageButton matricular;
    private Estudiante estudiante;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos_estudiante);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intentInformation();
        this.coordinatorLayout = findViewById(R.id.coordinator_layout_curso_estudiante);
        mRecyclerView = findViewById(R.id.recycler_cursoListEstudiante);

        cursosEstudiante(this.estudiante.getId());

        mAdapter = new CursoAdapter(cursoList, this);
        whiteNotificationBar(mRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        matricular = findViewById(R.id.matricular);
        matricular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matricularCursos();
            }
        });
    }
    public void matricularCursos(){
        Intent a = new Intent(this, MainActivity.class);
        a.putExtra("estudiante", estudiante);
        startActivity(a);
    }
    public void intentInformation(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            estudiante = (Estudiante) getIntent().getSerializableExtra("estudiante");
        }
    }
    @Override
    public void onContactSelected(Curso curso) {
        Toast.makeText(getApplicationContext(), "Selected: " + curso.getDescripcion() ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }

    @Override
    public void onItemMove(int source, int target) {
        mAdapter.onItemMove(source, target);
    }
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds profesorList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent a = new Intent(this, NavDrawerActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        super.onBackPressed();
    }

    public void cursosEstudiante(int id){
        cursoList = new ArrayList<>();
        AsyncTaskManager net = new AsyncTaskManager("http://192.168.1.8:14715/QuizWeb/servletCursos?opcion=2&estudiante="+id, new AsyncTaskManager.AsyncResponse() {
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

                        cursoList.add(c);
                    }
                    if(cursoList.size() == 0){
                        textView = findViewById(R.id.noCursos);
                        textView.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net.execute(AsyncTaskManager.GET);
    }
}
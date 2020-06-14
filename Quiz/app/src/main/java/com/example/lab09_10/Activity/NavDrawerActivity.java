package com.example.lab09_10.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.Model.Usuario;
import com.example.lab09_10.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private Usuario currentUser;
    private DBAdapterSQL db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Bienvenido");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.openDrawer(GravityCompat.START);
        this.intentInformation();
        this.userPrivileges();
        db = DBAdapterSQL.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_estudiante) {
            abrirMantenimientoEstudiante();
        }
        if (id == R.id.nav_curso) {
            abrirMantenimientoCurso();
        } else {
            if (id == R.id.nav_logout) {
                this.abrirLogin();
            } else{
                if(id == R.id.nav_persona){
                    this.openMisDatos();
                } else{
                    if(id == R.id.nav_mis_cursos) {
                        this.openMisCursos();
                    }
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void userPrivileges() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem holder;
        switch (currentUser.getRol()) {
            case "admin":
                holder = menu.findItem(R.id.nav_estudiante);
                holder.setEnabled(true);
                holder = menu.findItem(R.id.nav_curso);
                holder.setEnabled(true);
                holder = menu.findItem(R.id.nav_persona);
                holder.setVisible(false);
                holder = menu.findItem(R.id.nav_mis_cursos);
                holder.setVisible(false);
                holder = menu.findItem(R.id.nav_logout);
                holder.setEnabled(true);
                break;
            case "estandar":
                holder = menu.findItem(R.id.nav_estudiante);
                holder.setEnabled(false);
                holder.setVisible(false);
                holder = menu.findItem(R.id.nav_curso);
                holder.setVisible(false);
                holder.setEnabled(false);
                holder = menu.findItem(R.id.nav_persona);
                holder.setVisible(true);
                holder.setEnabled(true);
                holder = menu.findItem(R.id.nav_mis_cursos);
                holder.setVisible(true);
                holder.setEnabled(true);
                holder = menu.findItem(R.id.nav_logout);
                holder.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void intentInformation() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("usuario", "");
        Usuario obj = gson.fromJson(json, Usuario.class);
        currentUser = obj;
    }

    public void abrirLogin() {
        finish();
        Intent a = new Intent(this, LoginActivity.class);
        startActivity(a);
    }

    public void abrirMantenimientoCurso() {
        finish();
        Intent a = new Intent(this, CursoListActivity.class);
        startActivity(a);
    }

    public void abrirMantenimientoEstudiante() {
        finish();
        Intent a = new Intent(this, EstudianteListActivity.class);
        startActivity(a);
    }
    public void openMisDatos(){
        Estudiante estudiante = db.getEstudiante(this.currentUser.getId());
        finish();
        Intent a = new Intent(this, AddEstudianteActivity.class);
        a.putExtra("estudiante", estudiante);
        a.putExtra("editable", true);
        a.putExtra("actual", true);
        startActivity(a);
    }
    public void openMisCursos(){
        Estudiante estudiante = db.getEstudiante(this.currentUser.getId());
        finish();
        Intent a = new Intent(this, CursosEstudianteActivity.class);
        a.putExtra("estudiante", estudiante);
        startActivity(a);
    }
}

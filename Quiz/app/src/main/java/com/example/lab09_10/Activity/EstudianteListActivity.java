package com.example.lab09_10.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.lab09_10.Adapter.EstudianteAdapter;
import com.example.lab09_10.Data.DBAdapterSQL;
import com.example.lab09_10.Helper.RecyclerItemTouchHelper;
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

import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.lab09_10.R;

import java.util.List;

public class EstudianteListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        EstudianteAdapter.EstudianteListener {

    private RecyclerView mRecyclerView;
    private EstudianteAdapter mAdapter;
    private List<Estudiante> estudianteList;
    private SearchView searchView;
    private CoordinatorLayout coordinatorLayout;
    private DBAdapterSQL db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(getString(R.string.appList));
        this.coordinatorLayout = findViewById(R.id.coordinator_layout_estudiante);
        mRecyclerView = findViewById(R.id.recycler_jobAppList);
        db = DBAdapterSQL.getInstance(this);
        estudianteList = db.listEstudiantes();
        mAdapter = new EstudianteAdapter(estudianteList, this);
        whiteNotificationBar(mRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addEstudiante();
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContactSelected(Estudiante estudiante) {
        Toast.makeText(getApplicationContext(), "Selected: " + estudiante.getNombre() + ", " + estudiante.getApellidos(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof EstudianteAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String name = estudianteList.get(viewHolder.getAdapterPosition()).getNombre();

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                int id = estudianteList.get(deletedIndex).getId();
                db.deleteEstudiante(id);
                db.deleteUsuario(id);
                //db.close();
                // remove the item from recyclerView
                mAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " removido!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // undo is selected, restore the deleted item from adapter
                        mAdapter.restoreItem(deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        } else {
            Estudiante aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(EstudianteListActivity.this, AddEstudianteActivity.class);
            intent.putExtra("estudiante", aux);
            intent.putExtra("editable", true);
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
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
        finish();
        Intent a = new Intent(this, NavDrawerActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        super.onBackPressed();
    }
    public void addEstudiante(){
        //finish();
        Intent add = new Intent(EstudianteListActivity.this, AddEstudianteActivity.class);
        add.putExtra("editable", false);
        startActivity(add);
    }

}
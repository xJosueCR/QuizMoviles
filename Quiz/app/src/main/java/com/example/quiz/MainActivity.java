package com.example.quiz;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.quiz.Helper.RecyclerItemTouchHelper;
import com.example.quiz.LogicaDeNegocio.Estudiante;
import com.example.quiz.adapters.EstudianteAdapter;
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

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, EstudianteAdapter.ProfesorAdapterListener{

    private RecyclerView mRecyclerView;
    private EstudianteAdapter mAdapter;
    private List<Estudiante> profesorList;
    private SearchView searchView;
    private FloatingActionButton addProfesor;
    //private Data model;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.coordinatorLayout = findViewById(R.id.coordinator_layout_profesor);

        getSupportActionBar().setTitle(getString(R.string.profesor));

        mRecyclerView = findViewById(R.id.recycler_cursosFld);
        profesorList = new ArrayList<>();
       // model = new Data();
        /*profesorList = model.getProfesorList();
        mAdapter = new ProfesorAdapter(profesorList, this);*/

        AsyncTaskManager net = new AsyncTaskManager("http://10.0.2.2:36083/frontend_web/servletProfesores", new AsyncTaskManager.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray array = new JSONArray(output);
                    //carreraList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        Estudiante p = new Estudiante();
                        p.setCedula(array.getJSONObject(i).getString("cedula"));
                        p.setNombre(array.getJSONObject(i).getString("nombre"));
                        p.setApellidos(array.getJSONObject(i).getString("apellidos"));
                        p.setEdad(array.getJSONObject(i).getInt("email"));
                        profesorList.add(p);
                    }
                    mAdapter = new EstudianteAdapter(profesorList, MainActivity.this);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net.execute(AsyncTaskManager.GET);

        whiteNotificationBar(mRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        addProfesor = findViewById(R.id.addBtn);
        addProfesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddUpdProfesor();
            }
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        intentInformation(); //revisar si se edita o se agrega un profesor
        // mAdapter.notifyDataSetChanged(); //Refrescar la lista del reciclerView
    }
    public void intentInformation(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){ // si extras recibio algun objeto
            Estudiante auxiliar;
            auxiliar = (Estudiante)getIntent().getSerializableExtra("addProfesor");
            if(auxiliar != null){ // add profesor trae algun elemento, agregar nuevo
                //this.model.getProfesorList().add(auxiliar);
                profesorList.add(auxiliar);
                Toast.makeText(getApplicationContext(), auxiliar.getNombre() + " agregado correctamente", Toast.LENGTH_LONG).show();
            } else{ // se esta editando un profesor
                auxiliar = (Estudiante)getIntent().getSerializableExtra("editProfesor");
                boolean founded = false;
                for (Estudiante profesor : profesorList) {
                    if (profesor.getCedula().equals(auxiliar.getCedula())) {
                        profesor.setNombre(auxiliar.getNombre());
                        profesor.setApellidos(auxiliar.getApellidos());
                        profesor.setEdad(auxiliar.getEdad());
                        founded = true;
                        break;
                    }
                }
                //check if exist
                if (founded) {
                    Toast.makeText(getApplicationContext(), auxiliar.getNombre() + " editado correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), auxiliar.getNombre() + " no encontrado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void goToAddUpdProfesor(){
        Intent intent = new Intent(this, AddUpdEstudianteActivity.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }
    @Override
    public void onContactSelected(Estudiante profesor) {
        Toast.makeText(getApplicationContext(), "Selected: " + profesor.getCedula() + ", " + profesor.getNombre(), Toast.LENGTH_LONG).show();
    }
    @Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds profesorList to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
     //   SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
     //   searchView = (SearchView) menu.findItem(R.id.action_search)
     //           .getActionView();
    //    searchView.setSearchableInfo(searchManager
     //           .getSearchableInfo(getComponentName()));
    //    searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
     //   searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
     //       @Override
    //        public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
    //            mAdapter.getFilter().filter(query);
     //           return false;
    //        }

     //       @Override
    //        public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
    //            mAdapter.getFilter().filter(query);
      //          return false;
    //        }
    //    });
    //    return true;
  //  }


    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof EstudianteAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String name = profesorList.get(viewHolder.getAdapterPosition()).getNombre();

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
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
        } else { //edicion de un profesor
            Estudiante aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, AddUpdEstudianteActivity.class);
            intent.putExtra("editable", true);
            //intent.putExtra("profesor", aux);
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
       // Intent a = new Intent(this, NavDrawerActivity.class);
        //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(a);
       // super.onBackPressed();
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
}

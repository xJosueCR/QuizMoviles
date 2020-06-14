package com.example.lab09_10.Adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EstudianteAdapter extends RecyclerView.Adapter<EstudianteAdapter.MyViewHolder> implements Filterable {
    private List<Estudiante> estudianteList;
    private List<Estudiante> estudianteListFiltered;
    private EstudianteListener listener;
    private Estudiante deletedItem;
    public EstudianteAdapter(List<Estudiante> jobAppList, EstudianteListener listener) {
        this.estudianteList = jobAppList;
        this.listener = listener;
        //init filter
        this.estudianteListFiltered = jobAppList;
    }
    @Override
    public EstudianteAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(EstudianteAdapter.MyViewHolder holder, int position) {
        final Estudiante estudiante = estudianteListFiltered.get(position);
        holder.titulo1.setText("Nombre: "+ estudiante.getNombre());
        holder.titulo2.setText(estudiante.getApellidos());
        holder.description.setText("ID: "+estudiante.getCedula());
        Random random = new Random();
        boolean x = random.nextBoolean();
        if(x) {
            holder.imageView.setImageResource(R.drawable.student);
        }
        else{
            holder.imageView.setImageResource(R.drawable.teacher);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    estudianteListFiltered = estudianteList;
                } else {
                    List<Estudiante> filteredList = new ArrayList<>();
                    for (Estudiante row : estudianteList) {
                        // filter use two parameters
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase()) || row.getApellidos().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    estudianteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = estudianteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                estudianteListFiltered = (ArrayList<Estudiante>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return estudianteListFiltered.size();
    }
    public void removeItem(int position) {
        deletedItem = estudianteListFiltered.remove(position);
        Iterator<Estudiante> iter = estudianteList.iterator();
        while (iter.hasNext()) {
            Estudiante aux = iter.next();
            if (deletedItem.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }
    public void restoreItem(int position) {

        if (estudianteListFiltered.size() == estudianteList.size()) {
            estudianteListFiltered.add(position, deletedItem);
        } else {
            estudianteListFiltered.add(position, deletedItem);
            estudianteList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }
    public Estudiante getSwipedItem(int index) {
        if (this.estudianteList.size() == this.estudianteListFiltered.size()) { //not filtered yet
            return estudianteList.get(index);
        } else {
            return estudianteListFiltered.get(index);
        }
    }
    public void onItemMove(int fromPosition, int toPosition) {
        if (estudianteList.size() == estudianteListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(estudianteList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(estudianteList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(estudianteListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(estudianteListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo1, titulo2, description;
        public ImageView imageView;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;


        public MyViewHolder(View view) {
            super(view);
            titulo1 = view.findViewById(R.id.titleFirstLbl);
            titulo2 = view.findViewById(R.id.titleSecLbl);
            description = view.findViewById(R.id.descriptionLbl);
            imageView = view.findViewById(R.id.item_image_view);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(estudianteListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface EstudianteListener {
        void onContactSelected(Estudiante estudiante);
    }
}

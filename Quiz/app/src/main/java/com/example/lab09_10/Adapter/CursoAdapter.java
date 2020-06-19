package com.example.lab09_10.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab09_10.Model.Curso;
import com.example.lab09_10.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.MyViewHolder> implements Filterable {
    private List<Curso> cursoList;
    private List<Curso> cursoListFiltered;
    private CursoListener listener;
    private Curso deletedItem;
    public CursoAdapter(List<Curso> cursoList, CursoListener listener) {
        this.cursoList = cursoList;
        this.listener = listener;
        //init filter
        this.cursoListFiltered = cursoList;
    }
    @Override
    public CursoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Curso curso = cursoListFiltered.get(position);
        holder.titulo1.setText("Nombre: "+ curso.getDescripcion());
        holder.description.setText("Creditos: "+curso.getCreditos());
        Random random = new Random();
        boolean x = random.nextBoolean();
        if(x) {
            x = random.nextBoolean();
            if(x){
                //holder.imageView.setImageResource(R.drawable.database);
            }else{
                //holder.imageView.setImageResource(R.drawable.server);
            }
        }
        else{
            //holder.imageView.setImageResource(R.drawable.typing);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cursoListFiltered = cursoList;
                } else {
                    List<Curso> filteredList = new ArrayList<>();
                    for (Curso row : cursoList) {
                        // filter use two parameters
                        if (row.getDescripcion().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    cursoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cursoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cursoListFiltered = (ArrayList<Curso>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return cursoListFiltered.size();
    }
    public void removeItem(int position) {
        deletedItem = cursoListFiltered.remove(position);
        Iterator<Curso> iter = cursoList.iterator();
        while (iter.hasNext()) {
            Curso aux = iter.next();
            if (deletedItem.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }
    public void restoreItem(int position) {

        if (cursoListFiltered.size() == cursoList.size()) {
            cursoListFiltered.add(position, deletedItem);
        } else {
            cursoListFiltered.add(position, deletedItem);
            cursoList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }
    public Curso getSwipedItem(int index) {
        if (this.cursoList.size() == this.cursoListFiltered.size()) { //not filtered yet
            return cursoList.get(index);
        } else {
            return cursoListFiltered.get(index);
        }
    }
    public void onItemMove(int fromPosition, int toPosition) {
        if (cursoList.size() == cursoListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(cursoList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(cursoList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(cursoListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(cursoListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo1, titulo2, description;
       // public ImageView imageView;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;


        public MyViewHolder(View view) {
            super(view);
            titulo1 = view.findViewById(R.id.titleFirstLbl);
            titulo2 = view.findViewById(R.id.titleSecLbl);
            description = view.findViewById(R.id.descriptionLbl);
            //imageView = view.findViewById(R.id.item_image_view);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(cursoListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface CursoListener {
        void onContactSelected(Curso curso);
    }
}
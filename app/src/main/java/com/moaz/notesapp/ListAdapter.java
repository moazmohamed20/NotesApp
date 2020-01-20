package com.moaz.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder> {

    private Context context;
    private List<TableItem> list;

    public ListAdapter(Context context, List<TableItem> list) {
        this.context = context;
        this.list = list;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView title, description, writtenBy;
        ImageView finished;

        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            writtenBy = itemView.findViewById(R.id.written_by);
            finished = itemView.findViewById(R.id.finished);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapter.Holder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
        if (!list.get(position).getWrittenBy().isEmpty()) {
            holder.writtenBy.setText("Written By: " + list.get(position).getWrittenBy());
        }
        if (list.get(position).isFinished()) {
            holder.finished.setVisibility(View.VISIBLE);
        } else {
            holder.finished.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            CustomDialog D = new CustomDialog(context, list.get(position));
            D.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

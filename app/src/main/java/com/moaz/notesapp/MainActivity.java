package com.moaz.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static Activity thisActivity;

    private RecyclerView mRecyclerView;
    private ArrayList<TableItem> mTasksList;
    private ListAdapter mListAdapter;

    private TaskDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisActivity = this;
        mTasksList = new ArrayList<>();
        mListAdapter = new ListAdapter(this, mTasksList);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        dao = Database.getDatabase(this).taskDao();

        getAllData();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteItem(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
        }
    };

    public void add_new_task(View v) {
        CustomDialog D = new CustomDialog(MainActivity.this);
        D.show();
    }

    @SuppressLint("CheckResult")
    private void getAllData() {
        Single.fromCallable(() -> dao.getAll()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(value -> {
            mTasksList.addAll(value);
            mListAdapter = new ListAdapter(this, mTasksList);
            mRecyclerView.setAdapter(mListAdapter);
        });
    }

    @SuppressLint("CheckResult")
    private void deleteItem(int position) {
        Single.fromCallable(() -> {
            dao.delete(mTasksList.get(position));
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(value -> {
            recreate();
            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
        });

    }

}

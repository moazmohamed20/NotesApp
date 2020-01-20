package com.moaz.notesapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CustomDialog extends Dialog {

    private TableItem editItem;

    private EditText title, description, writtenBy;
    private ExtendedFloatingActionButton add, cancel;
    private CheckBox finished;

    private TaskDao dao;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, TableItem editItem) {
        super(context);
        this.editItem = editItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        title = findViewById(R.id.title_field);
        description = findViewById(R.id.description_field);
        writtenBy = findViewById(R.id.written_by_field);
        finished = findViewById(R.id.finished);
        add = findViewById(R.id.add_btn);
        cancel = findViewById(R.id.cancel_btn);

        dao = Database.getDatabase(getContext()).taskDao();

        if (editItem != null) {
            title.setText(editItem.getTitle());
            description.setText(editItem.getDescription());
            writtenBy.setText(editItem.getWrittenBy());
            finished.setChecked(editItem.isFinished());
            add.setText("Update");
            add.setIcon(getContext().getDrawable(R.drawable.ic_arrow));
        }

        add.setOnClickListener(view -> {
            if (title.getText().toString().isEmpty()) {
                title.setError("Title is required");
                return;
            }
            if (add.getText().equals("Add")) {
                insertItem();
            } else {
                updateItem();
            }
            dismiss();
        });
        cancel.setOnClickListener(view -> dismiss());
    }

    @SuppressLint("CheckResult")
    private void insertItem() {
        TableItem myItem = new TableItem();
        myItem.setTitle(title.getText().toString().trim());
        myItem.setDescription(description.getText().toString().trim());
        myItem.setWrittenBy(writtenBy.getText().toString().trim());
        myItem.setFinished(finished.isChecked());

        Single.fromCallable(() -> {
            dao.insert(myItem);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(value -> {
            MainActivity.thisActivity.recreate();
            Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_SHORT).show();
        });

    }

    @SuppressLint("CheckResult")
    private void updateItem() {
        TableItem myItem = new TableItem();
        myItem.setId(editItem.getId());
        myItem.setTitle(title.getText().toString().trim());
        myItem.setDescription(description.getText().toString().trim());
        myItem.setWrittenBy(writtenBy.getText().toString().trim());
        myItem.setFinished(finished.isChecked());

        Single.fromCallable(() -> {
            dao.update(myItem);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(value -> {
            MainActivity.thisActivity.recreate();
            Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
        });

    }

}

package com.example.ghichu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.ghichu.Adapter.AdapterToDo;
import com.example.ghichu.Model.ToDoModel;
import com.example.ghichu.Utils.DataProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView recyclerView;
    private AdapterToDo taskAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;
    private DataProvider db;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = -1;
        try {
            position = ((AdapterToDo) recyclerView.getAdapter()).getPosition();
        }catch (Exception e){
            Log.d("Error", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()){
            case R.id.menuUpdate:
                taskAdapter.editItem(position);
                break;
            case R.id.menuDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(taskAdapter.getContext());
                builder.setTitle("Xóa ghi chú");
                builder.setMessage("Bạn có muốn xóa ghi chú này không");
                builder.setPositiveButton("Đồng ý",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskAdapter.deleteItem(((AdapterToDo) recyclerView.getAdapter()).getPosition());
                            }
                        });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.menuDetails:
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

                intent.putExtra("task", taskAdapter.getItemIndex(position).getTask());
                intent.putExtra("content", taskAdapter.getItemIndex(position).getContent());

                startActivity(intent);

                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DataProvider(this);
        db.openDatabase();


        recyclerView = findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new AdapterToDo(db, MainActivity.this);
        recyclerView.setAdapter(taskAdapter);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        taskAdapter.setTasks(taskList);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }

        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}



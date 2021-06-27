package com.example.ghichu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghichu.AddNewTask;
import com.example.ghichu.MainActivity;
import com.example.ghichu.Model.ToDoModel;
import com.example.ghichu.R;
import com.example.ghichu.Utils.DataProvider;

import java.util.List;

public class AdapterToDo extends RecyclerView.Adapter<AdapterToDo.ViewHolder> {
    private List<ToDoModel> toDoModelList;
    private MainActivity activity;
    private DataProvider db;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AdapterToDo(DataProvider db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void setTasks (List<ToDoModel> toDoModelList){
        this.toDoModelList = toDoModelList;
        notifyDataSetChanged();//Thong bao data da thay doi
    }
    public Context getContext() { return activity; }

    public ToDoModel getItemIndex(int position){
        ToDoModel item = toDoModelList.get(position);
        return item ;
    }

    public void deleteItem(int position){
        ToDoModel item = toDoModelList.get(position);
        db.deleteTask(item.getId());
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onViewRecycled(@NonNull AdapterToDo.ViewHolder holder) {
        holder.task.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        ToDoModel item = toDoModelList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                }
                else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }
    public int getItemCount(){
        return toDoModelList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }


    public void editItem(int position){
        ToDoModel item = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        bundle.putString("content",item.getContent());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        notifyItemChanged(position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task =view.findViewById(R.id.todoCheckBox);
            view.setOnCreateContextMenuListener(this);

        }

        @SuppressLint("ResourceType")
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE,R.id.menuUpdate,Menu.NONE,R.string.update);
            menu.add(Menu.NONE,R.id.menuDelete,Menu.NONE,R.string.delete);
            menu.add(Menu.NONE,R.id.menuDetails,Menu.NONE,R.string.details);
        }
    }
}

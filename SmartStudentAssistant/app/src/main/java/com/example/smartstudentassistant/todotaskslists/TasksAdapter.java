package com.example.smartstudentassistant.todotaskslists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentassistant.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    /* Creating variable for our array list for books */
    private final ArrayList<TasksModel> tasksArrayList;

    /* Creating variable for a context of our class */
    Context context;

    /* Creating Variable for interface of items click */
    private OnTaskItemSelectedListener taskListener;

    /* Creating Interface for click on task card item */
    public interface OnTaskItemSelectedListener{
        void onTaskItemSelectedListener(TasksModel tasksModel);
    }

    /* Setting Listener on selection of task card item */
    public void setOnTaskItemSelectedListener(OnTaskItemSelectedListener taskListener){

        this.taskListener = taskListener;

    }

    /* Creating variable for interface of checkbox */
    private OnTaskStatusChangeListener listener;

    /* Creating Interface for status of checkbox */
    public interface OnTaskStatusChangeListener{
        void onStatusChanged(TasksModel tasksModel);

    }


    /* Setting listener for our checkbox interface */
    public void setOnTaskStatusChangeListener(OnTaskStatusChangeListener listener){

        this.listener = listener; /* This is our listener for task item clicks */
    }

    /* Creating a default constructor for our class */
    public TasksAdapter(ArrayList<TasksModel> tasksArrayList, Context context){

        this.tasksArrayList = tasksArrayList; /* Here we initialize tasks array lists */

        this.context = context; /* Here we will initialize our reminder list context */

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /* Here we are inflating our layout file for items of our recycler view */
        View Myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_rv_items,parent,false);

        /* Returning our view layout by using ViewHolder class for data items */
        return new ViewHolder(Myview);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /* Here we are setting data to items of our recycler view */
        TasksModel tasksModel = tasksArrayList.get(position);

        /* Getting data from class and setting it on our views */
        holder.txtTaskTitle.setText(tasksModel.getTaskTitle());
        holder.userTask.setText(tasksModel.getTaskDetail());


        /* This is used to avoid all non wanted calls for status */
        holder.userTask.setOnCheckedChangeListener(null);

        /* Here we will check if one check box is checked or not */
        holder.userTask.setChecked(tasksModel.isChecked());

        /* Here we will set listener for checking checkbox status */
        holder.userTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tasksModel.setTaskStatus(isChecked ? "Finished" : "Pending");

            /* Here we will notify our activity for listener */
            if (listener != null){
                listener.onStatusChanged(tasksModel);
            }
        });

        /* Here we will set listener for clicking on task card item */
        holder.itemView.setOnClickListener(v -> {

            if (taskListener != null){ /* When it is not null then */

                /* We set it for click to start a change activity */
                taskListener.onTaskItemSelectedListener(tasksModel);
            }

        });
    }

    @Override
    public int getItemCount() {
        /* Here we are returning size of our task array list */
        return tasksArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        /* Here we will bind our views of task items */
        MaterialCheckBox userTask;
        TextView txtTaskTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /* Here we will hook our layout views */
            txtTaskTitle = itemView.findViewById(R.id.txt_activity_name);
            userTask = itemView.findViewById(R.id.cbox_task_data);
        }
    }
}

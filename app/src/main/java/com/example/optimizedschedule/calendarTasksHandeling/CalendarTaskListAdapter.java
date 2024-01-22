package com.example.optimizedschedule.calendarTasksHandeling;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.optimizedschedule.R;
import com.example.optimizedschedule.activities.TasksActivity;
import com.example.optimizedschedule.taskListHandeling.Task;
import com.example.optimizedschedule.taskListHandeling.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class CalendarTaskListAdapter extends RecyclerView.Adapter<CalendarTaskListAdapter.ViewHolder> {
    private List<Task> tasks;
    private Context context;

    public CalendarTaskListAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context.getApplicationContext();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CalendarTaskListAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.taskDueDate.setText(task.getTaskDueDate());
        holder.taskTimeHours.setText(task.getTaskTimeHours());
        holder.taskTimeMinutes.setText(task.getTaskTimeMinutes());
        holder.doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Context context = v.getContext();

                // Check if the context is an instance of TasksActivity
                if (context instanceof TasksActivity) {
                    String taskId = task.getId();
                    ((TasksActivity) context).markTaskAsDone(taskId);
                } else {
                    // Handle the case where the context is not TasksActivity
                    Log.e("TaskAdapter", "Context is not an instance of TasksActivity");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button doneButton;
        public TextView taskName;
        public TextView taskDueDate;
        public TextView taskTimeHours;
        public TextView taskTimeMinutes;

        public ViewHolder(View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.taskName);
            taskDueDate = (TextView) itemView.findViewById(R.id.taskDueDate);
            taskTimeHours = (TextView) itemView.findViewById(R.id.taskTimeHours);
            taskTimeMinutes = (TextView) itemView.findViewById(R.id.taskTimeMinutes);
            doneButton = (Button) itemView.findViewById(R.id.taskFinishedButton);
        }
    }
    }

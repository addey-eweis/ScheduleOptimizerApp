package com.example.optimizedschedule.taskListHandeling;

// TaskAdapter.java
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.optimizedschedule.R;
import com.example.optimizedschedule.activities.TasksActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context context;

    public TaskAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View taskView = inflater.inflate(R.layout.taskitem, parent, false);

        return new ViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
        return tasks.size();
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

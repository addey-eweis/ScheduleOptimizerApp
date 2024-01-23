package com.example.optimizedschedule.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.optimizedschedule.R;
import com.example.optimizedschedule.TaskComparator;
import com.example.optimizedschedule.TaskDoneListener;
import com.example.optimizedschedule.taskListHandeling.Task;
import com.example.optimizedschedule.taskListHandeling.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OptimizedScheduleActivity extends AppCompatActivity implements TaskDoneListener {
    private List<Task> OptimizedTasks;
    private TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimized_schedule);

        OptimizedTasks = new ArrayList<>();
        CollectionReference transactionsCollectionRef = FirebaseFirestore.getInstance().collection("data");

        RecyclerView recyclerView = findViewById(R.id.optimizedTasksList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(OptimizedTasks, OptimizedScheduleActivity.this, this);
        recyclerView.setAdapter(taskAdapter);

        transactionsCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    return;
                }

                if (querySnapshot != null) {
                    // Process the query snapshot and update the tasks list
                    OptimizedTasks.clear(); // Clear existing tasks
                    for (DocumentSnapshot document : querySnapshot) {
                        if (document.exists()) {
                            String taskId = document.getId();
                            String taskName = document.getString("taskName");
                            String taskDueDate = document.getString("taskDueDate");
                            String taskTimeHours = document.getString("taskTimeHours");
                            String taskTimeMinute = document.getString("taskTimeMinutes");
                            String taskPriority = document.getString("taskPriority");

                            Task task = new Task(taskId, taskName, taskDueDate, taskTimeHours, taskTimeMinute, taskPriority);
                            int calculatedPriority = TaskPriorityAlgorithm(task.getTaskDueDate(), task.getTaskPriority(), task.getTaskTimeHours(), task.getTaskTimeMinutes());
                            task.setCumulativeTaskPriority(calculatedPriority); // Assuming Task has a setPriority method
                            OptimizedTasks.add(task);
                        }
                    }
                    Collections.sort(OptimizedTasks, new TaskComparator());

                }
            }
        });
    }


    public int TaskPriorityAlgorithm(String taskDueDate, String taskPriority, String taskTimeHours, String taskTimeMinutes) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = dateFormat.parse(taskDueDate);
            Date currentDate = new Date();

            // Calculate time difference in days
            long timeDiff = dueDate.getTime() - currentDate.getTime();
            int daysUntilDue = (int) (timeDiff / (1000 * 60 * 60 * 24));

            // Calculate total task time in minutes
            int totalTimeInMinutes = Integer.parseInt(taskTimeHours) * 60 + Integer.parseInt(taskTimeMinutes);

            // Assign numerical values to task priorities
            int priorityValue;
            switch (taskPriority.toLowerCase()) {
                case "high":
                    priorityValue = 3;
                    break;
                case "medium":
                    priorityValue = 2;
                    break;
                case "low":
                default:
                    priorityValue = 1;
                    break;
            }

            // Composite priority calculation
            int compositePriority = (priorityValue * 100) - totalTimeInMinutes - daysUntilDue;
             Toast.makeText(this, compositePriority, Toast.LENGTH_SHORT).show();
            return compositePriority;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 in case of an error
        }
    }

    @Override
    public void onTaskMarkedAsDone(String taskId) {

    }
}
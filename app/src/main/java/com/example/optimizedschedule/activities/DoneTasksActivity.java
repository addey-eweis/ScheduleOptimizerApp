package com.example.optimizedschedule.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.optimizedschedule.R;
import com.example.optimizedschedule.TaskDoneListener;
import com.example.optimizedschedule.taskListHandeling.Task;
import com.example.optimizedschedule.taskListHandeling.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoneTasksActivity extends AppCompatActivity implements TaskDoneListener {
    private List<Task> doneTasks;
    private TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_tasks);

        doneTasks = new ArrayList<>();
        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
        CollectionReference doneTasksCollectionRef = firestoreDb.collection("doneTasks");


        RecyclerView recyclerView = findViewById(R.id.doneTasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(doneTasks, DoneTasksActivity.this, this);
        recyclerView.setAdapter(taskAdapter);

        doneTasksCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    return;
                }

                if (querySnapshot != null) {
                    // Process the query snapshot and update the tasks list
                    doneTasks.clear(); // Clear existing tasks
                    for (DocumentSnapshot document : querySnapshot) {
                        if (document.exists()) {
                            String taskId = document.getId();
                            String taskName = document.getString("taskName");
                            String taskDueDate = document.getString("taskDueDate");
                            String taskTimeHours = document.getString("taskTimeHours");
                            String taskTimeMinute = document.getString("taskTimeMinutes");
                            String taskPriority = document.getString("taskPriority");

                            Task task = new Task(taskId, taskName, taskDueDate, taskTimeHours, taskTimeMinute, taskPriority);
                            doneTasks.add(task);
                            Collections.sort(doneTasks, new TasksActivity.TaskPriorityComparator());
                            taskAdapter.notifyDataSetChanged();


                        }
                    }

                }

            }


        });

    }

    @Override
    public void onTaskMarkedAsDone(String taskId) {

    }
}
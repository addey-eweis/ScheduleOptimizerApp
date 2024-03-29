//package com.example.optimizedschedule.activites;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.optimizedschedule.R;
//import com.example.optimizedschedule.taskListHandeling.Task;
//import com.example.optimizedschedule.taskListHandeling.TaskAdapter;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class TasksActivity extends AppCompatActivity {
//
//    private List<Task> tasks;
//    private TaskAdapter taskAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tasks);
//
//        tasks = new ArrayList<>();
//        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
//        CollectionReference transactionsCollectionRef = firestoreDb.collection("data");
//
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        taskAdapter = new TaskAdapter(tasks);
//        recyclerView.setAdapter(taskAdapter);
//
//
//        transactionsCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    // Handle error
//                    return;
//                }
//
//                if (querySnapshot != null) {
//
//                    // Process the query snapshot and update the recentItemsArrayList
//                    for (DocumentSnapshot document : querySnapshot) {
//                        if (document.exists()) {
////                            Map<String, Object> taskArray = document.getData();
//                            String taskId = document.getId().toString();
//                            String taskName = document.getString("taskName");
//                            String taskDueDate = document.getString("taskDueDate");
//                            String taskTimeHours = document.getString("taskTimeHours");
//                            String taskTimeMinute = document.getString("taskTimeMinutes");
//
//                            recentItem = new RecentItem(transactionName, transactionCatagory, transactionAmount, "+", transactionCurrency, transactionDate);
//
////                        Toast.makeText(getContext(), recentItemsArrayList.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                        recentItemsArrayList.add(recentItem);
//                    }
//
//                    // Now, recentItemsArrayList contains the RecentItem objects with data from the documents
//                    // You can use this ArrayList for further processing
////                    sortByDate(recentItemsArrayList);
//                    // Update the UI with the new recentItemsArrayList
////                    rvAdapter = new RecentRvAdapter(getContext(), recentItemsArrayList);
////                    recyclerView = myView.findViewById(R.id.recent_activity_recycler_view);
////                    recyclerView.setHasFixedSize(false);
////                    rvLayoutManager = new LinearLayoutManager(getContext());
////                    recyclerView.setLayoutManager(rvLayoutManager);
////                    recyclerView.setAdapter(rvAdapter);
//                }
//            }
//        });
//
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    }

package com.example.optimizedschedule.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.optimizedschedule.R;
import com.example.optimizedschedule.TaskDoneListener;
import com.example.optimizedschedule.taskListHandeling.Task;
import com.example.optimizedschedule.taskListHandeling.TaskAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TasksActivity extends AppCompatActivity implements TaskDoneListener {

    private List<Task> tasks;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        tasks = new ArrayList<>();
        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
        CollectionReference transactionsCollectionRef = firestoreDb.collection("data");

        RecyclerView recyclerView = findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(tasks, TasksActivity.this, this);
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
                    tasks.clear(); // Clear existing tasks
                    for (DocumentSnapshot document : querySnapshot) {
                        if (document.exists()) {
                            String taskId = document.getId();
                            String taskName = document.getString("taskName");
                            String taskDueDate = document.getString("taskDueDate");
                            String taskTimeHours = document.getString("taskTimeHours");
                            String taskTimeMinute = document.getString("taskTimeMinutes");
                            String taskPriority = document.getString("taskPriority");

                            Task task = new Task(taskId, taskName, taskDueDate, taskTimeHours, taskTimeMinute, taskPriority);
                            tasks.add(task);
                            Collections.sort(tasks, new TaskPriorityComparator());
                           taskAdapter.notifyDataSetChanged();


                        }
                    }

                }

            }


        });




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
                startActivity(intent);
            }
        });

        Button doneTasksButton = findViewById(R.id.doneTasksButton);
        doneTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DoneTasksActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onTaskMarkedAsDone(String taskId) {
        markTaskAsDone(taskId);
    }
    public void markTaskAsDone(String taskId) {
        Toast.makeText(TasksActivity.this, taskId, Toast.LENGTH_SHORT).show();
        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
        CollectionReference doneCollectionRef = firestoreDb.collection("doneTasks");
        DocumentReference doneTaskDocumentRef = firestoreDb.collection("data").document(taskId);

        doneTaskDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot taskResult = task.getResult();
                    if(taskResult != null){
                        doneCollectionRef.document(taskId + "doneTask").set(taskResult.getData()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                doneTaskDocumentRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(TasksActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }



    // Custom comparator for sorting tasks in descending order based on priority
    static class TaskPriorityComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            String priority1 = task1.getTaskPriority();
            String priority2 = task2.getTaskPriority();

            // Handle null priorities
            if (priority1 == null) {
                return (priority2 == null) ? 0 : 1;
            }
            if (priority2 == null) {
                return -1;
            }

            // Rest of your comparison logic
            if (priority1.equals("high")) {
                return priority2.equals("high") ? 0 : -1;
            } else if (priority1.equals("medium")) {
                return priority2.equals("high") ? -1 : (priority2.equals("medium") ? 0 : 1);
            } else {
                return priority2.equals("low") ? 0 : 1;
            }
        }
    }
}


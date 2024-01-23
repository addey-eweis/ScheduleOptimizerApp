package com.example.optimizedschedule.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.optimizedschedule.R;
import com.example.optimizedschedule.TaskDoneListener;
import com.example.optimizedschedule.taskListHandeling.Task;
import com.example.optimizedschedule.taskListHandeling.TaskAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class CalendarActivity extends AppCompatActivity implements TaskDoneListener {
    private TaskAdapter taskCalendarAdapter;
    private List<Task> allTasks = new ArrayList<>();
    private List<Task> tasksForDate = new ArrayList<>();

    private CalendarView calendarView;
    private RecyclerView recyclerViewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.tasksCalendar);
        recyclerViewTasks = findViewById(R.id.calendarTasksList);

        CollectionReference transactionsCollectionRef = FirebaseFirestore.getInstance().collection("data");

        transactionsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> taskSnapshot) {
                if (taskSnapshot.isSuccessful()) {
                    for (QueryDocumentSnapshot document : taskSnapshot.getResult()) {
                        if (document.exists()) {
                            String taskId = document.getId();
                            String taskName = document.getString("taskName");
                            String taskDueDate = document.getString("taskDueDate");
                            String taskTimeHours = document.getString("taskTimeHours");
                            String taskTimeMinute = document.getString("taskTimeMinutes");
                            String taskPriority = document.getString("taskPriority");


                            Task task = new Task(taskId, taskName, taskDueDate, taskTimeHours, taskTimeMinute, taskPriority);
                            allTasks.add(task);
                        }
                    }

//                    // Set up RecyclerView and Adapter after data is retrieved
//                    taskCalendarAdapter = new CalendarTaskListAdapter(allTasks, CalendarActivity.this);
//                    recyclerViewTasks.setLayoutManager(new LinearLayoutManager(CalendarActivity.this));
//                    recyclerViewTasks.setAdapter(taskCalendarAdapter);
                } else {
                    Log.d("TAG", "Error getting documents: ", taskSnapshot.getException());
                }
            }
        });

        // Set up RecyclerView and Adapter after data is retrieved
        taskCalendarAdapter = new TaskAdapter(allTasks, CalendarActivity.this, this);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(CalendarActivity.this));
        recyclerViewTasks.setAdapter(taskCalendarAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String updatedMonth = (month + 1 < 10) ? "0" + (month + 1) : String.valueOf(month + 1);
                String selectedDate = dayOfMonth + "/" + updatedMonth + "/" + year;

                // Clear previous tasks and filter based on the selected date
                tasksForDate.clear();
                for (Task task : allTasks) {
                    if (selectedDate.equals(task.getTaskDueDate())) {
                        tasksForDate.add(task);
                    }
                }

                // Update the adapter with the filtered tasks
                taskCalendarAdapter.setTasks(tasksForDate);
                taskCalendarAdapter.notifyDataSetChanged();
            }
        });


    }
    @Override
    public void onTaskMarkedAsDone(String taskId) {
        markTaskAsDone(taskId);
    }

    public void markTaskAsDone(String taskId) {
        Toast.makeText(CalendarActivity.this, taskId, Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(CalendarActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }
}

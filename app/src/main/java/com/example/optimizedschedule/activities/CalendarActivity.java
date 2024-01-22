package com.example.optimizedschedule.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.optimizedschedule.FirebaseOperationsManager;
import com.example.optimizedschedule.R;
import com.example.optimizedschedule.calendarTasksHandeling.CalendarTaskListAdapter;
import com.example.optimizedschedule.taskListHandeling.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//public class CalendarActivity extends AppCompatActivity {
//    private CalendarTaskListAdapter taskCalendarAdapter;
//    private List<Task> allTasks = new ArrayList<>();;
//    List<Task> tasksForDate = new ArrayList<>();
//
//    private CalendarView calendarView;
//    private RecyclerView recyclerViewTasks;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//
//        calendarView = findViewById(R.id.tasksCalendar);
//        recyclerViewTasks = findViewById(R.id.calendarTasksList);
//
//        CollectionReference transactionsCollectionRef = FirebaseFirestore.getInstance().collection("data");
//
//        transactionsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> taskSnapshot) {
//                if (taskSnapshot.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : taskSnapshot.getResult()) {
//                        if (document.exists()) {
//                            String taskId = document.getId();
//                            String taskName = document.getString("taskName");
//                            String taskDueDate = document.getString("taskDueDate");
//                            String taskTimeHours = document.getString("taskTimeHours");
//                            String taskTimeMinute = document.getString("taskTimeMinutes");
//
//                            Task task = new Task(taskId, taskName, taskDueDate, taskTimeHours, taskTimeMinute);
//                            allTasks.add(task);
//
////                            for (Task taskShit : allTasks) {
////                                Toast.makeText(CalendarActivity.this, taskShit.getTaskName(), Toast.LENGTH_SHORT).show();
////                            }
//
//                        }
//                    }
//                } else {
//                    Log.d("TAG", "Error getting documents: ", taskSnapshot.getException());
//                }
//
//            }
//        });
//
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                // Update the RecyclerView with tasks for the selected date
//                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
//                updateTaskListForDate(selectedDate);
//
//            }
//        });
//
//        taskCalendarAdapter = new CalendarTaskListAdapter(allTasks, CalendarActivity.this);
//        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewTasks.setAdapter(taskCalendarAdapter);
//
//    }
////        transactionsCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
////            @Override
////            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
////                if (e != null) {
////                    // Handle error
////                    return;
////                }
////
////                if (querySnapshot != null) {
////                    // Process the query snapshot and update the tasks list
////                    allTasks.clear(); // Clear existing tasks
////                    for (DocumentSnapshot document : querySnapshot) {
////                        if (document.exists()) {
////                            String taskId = document.getId();
////                            String taskName = document.getString("taskName");
////                            String taskDueDate = document.getString("taskDueDate");
////                            String taskTimeHours = document.getString("taskTimeHours");
////                            String taskTimeMinute = document.getString("taskTimeMinutes");
////
////                            Task task = new Task(taskId, taskName, taskDueDate, taskTimeHours, taskTimeMinute);
//////                            Toast.makeText(getApplicationContext(), task.getTaskDueDate(), Toast.LENGTH_SHORT).show();
////                            allTasks.add(task);
////                        }
////                    }
////                }
////
////
////            }
////        });
//
//
//    private void updateTaskListForDate(String selectedDate) {
//        for (Task task : allTasks) {
//            if (selectedDate.toString().equals(task.getTaskDueDate())) {
//                tasksForDate.add(task);
//            }
//        }
//        taskCalendarAdapter.setTasks(tasksForDate);
//        taskCalendarAdapter.notifyDataSetChanged();
//    }
//}


public class CalendarActivity extends AppCompatActivity {
    private CalendarTaskListAdapter taskCalendarAdapter;
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
        taskCalendarAdapter = new CalendarTaskListAdapter(allTasks, CalendarActivity.this);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(CalendarActivity.this));
        recyclerViewTasks.setAdapter(taskCalendarAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Update the RecyclerView with tasks for the selected date
                String updatedMonth;
                if(month < 10){
                    updatedMonth = "0" + (month + 1);
                }
                else{
                    updatedMonth = String.valueOf(month + 1);
                }
                String selectedDate = dayOfMonth + "/" + updatedMonth + "/" + year;
//                updateTaskListForDate(selectedDate);
//                Toast.makeText(getApplicationContext(), "OnSelectedDayChange", Toast.LENGTH_SHORT).show();


                tasksForDate.clear();

                for (Task task : allTasks) {
                    Toast.makeText(CalendarActivity.this, selectedDate + " " + task.getTaskDueDate(), Toast.LENGTH_SHORT).show();

                    if (selectedDate.equals(task.getTaskDueDate())) {
                        tasksForDate.add(task);
                        Toast.makeText(getApplicationContext(), task.getTaskName(), Toast.LENGTH_SHORT).show();
                    }

                }
                taskCalendarAdapter.setTasks(tasksForDate);
                taskCalendarAdapter.notifyDataSetChanged();

            }
        });
    }
}

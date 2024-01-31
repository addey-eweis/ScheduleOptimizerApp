package com.example.optimizedschedule.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
    private List<Task> optimizedTasks;
    private TaskAdapter taskAdapter;
    private ScheduleAdapter scheduleAdapter;
    private static final int MAX_TASK_DURATION_PER_SLOT = 120; // 2 hours in minutes

    private static final String BREAKFAST_TIME = "08:00 AM - 09:00 AM";
    private static final String LUNCH_TIME = "12:00 PM - 01:00 PM";
    private static final String DINNER_TIME = "06:00 PM - 07:00 PM";
// Add more breaks as needed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimized_schedule);

        optimizedTasks = new ArrayList<>();
        CollectionReference transactionsCollectionRef = FirebaseFirestore.getInstance().collection("data");

//        RecyclerView recyclerView = findViewById(R.id.optimizedTasksList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        taskAdapter = new TaskAdapter(optimizedTasks, OptimizedScheduleActivity.this, this);
//        recyclerView.setAdapter(taskAdapter);
//

        RecyclerView recyclerView = findViewById(R.id.optimizedTasksList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(scheduleAdapter);

        transactionsCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    return;
                }

                if (querySnapshot != null) {
                    // Process the query snapshot and update the tasks list
                    optimizedTasks.clear(); // Clear existing tasks
                    for (DocumentSnapshot document : querySnapshot) {
                        if (document.exists()) {
//                            Toast.makeText(OptimizedScheduleActivity.this, document.getString("taskName"), Toast.LENGTH_SHORT).show();
                            String taskId = document.getId();
                            String taskName = document.getString("taskName");
                            String taskDueDate = document.getString("taskDueDate");
                            String taskTimeHours = document.getString("taskTimeHours");
                            String taskTimeMinute = document.getString("taskTimeMinutes");
                            String taskPriority = document.getString("taskPriority");

                            Task task = new Task(taskId, taskName, taskDueDate, taskTimeHours, taskTimeMinute, taskPriority);
                            int calculatedPriority = TaskPriorityAlgorithm(task.getTaskDueDate(), task.getTaskPriority(), task.getTaskTimeHours(), task.getTaskTimeMinutes());
                            int totalTimeInMinutes = Integer.parseInt(taskTimeHours) * 60 + Integer.parseInt(taskTimeMinute);
                            task.setTimeConsumed(totalTimeInMinutes);
                            task.setCumulativeTaskPriority(calculatedPriority); // Assuming Task has a setPriority method
                            optimizedTasks.add(task);

//                         Toast.makeText(OptimizedScheduleActivity.this, String.valueOf(calculatedPriority), Toast.LENGTH_SHORT).show();
                        }
                    }
                    Collections.sort(optimizedTasks, new TaskComparator());
                    List<ScheduleItem> scheduleItems = createSchedule(optimizedTasks);
                    scheduleAdapter.updateData(scheduleItems); // Update adapter data

                }
            }
        });
    }


    public int TaskPriorityAlgorithm(String taskDueDate, String taskPriority, String taskTimeHours, String taskTimeMinutes) {
        try {
            // Adjust the date format to match the input date string
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
            int compositePriority = (totalTimeInMinutes * 10) - (priorityValue * 15) - (daysUntilDue * 20);
//            Toast.makeText(this, String.valueOf(compositePriority), Toast.LENGTH_SHORT).show();

            return compositePriority;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TaskPriorityAlgorithm Error", e.toString());
            return -1; // Return -1 in case of an error
        }
    }


    @Override
    public void onTaskMarkedAsDone(String taskId) {

    }

    public List<ScheduleItem> createSchedule(List<Task> tasks) {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        // Sort tasks by priority or other criteria
//        Collections.sort(optimizedTasks, new TaskComparator());

        // Insert fixed breaks
        scheduleItems.add(new ScheduleItem(ScheduleItem.ItemType.BREAK, "Breakfast", "1 Hour", BREAKFAST_TIME));
        scheduleItems.add(new ScheduleItem(ScheduleItem.ItemType.BREAK, "Lunch", "1 Hour", LUNCH_TIME));
        scheduleItems.add(new ScheduleItem(ScheduleItem.ItemType.BREAK, "Dinner", "1 Hour", DINNER_TIME));


        int currentHour = 9; // Starting after breakfast
        for (Task task : tasks) {
            int remainingDuration = Integer.parseInt(task.getTaskTimeHours()) * 60 + Integer.parseInt(task.getTaskTimeMinutes());



            while (remainingDuration > 0) {
                int slotDuration = Math.min(remainingDuration, MAX_TASK_DURATION_PER_SLOT);
                String timeSlot = calculateTimeSlot(currentHour, slotDuration);
                String itemDuration = formatDuration(slotDuration);

                scheduleItems.add(new ScheduleItem(ScheduleItem.ItemType.TASK, task.getTaskName(), itemDuration, timeSlot));

                currentHour += slotDuration / 60;
                remainingDuration -= slotDuration;

                // Check and insert breaks, adjust currentHour accordingly
                // Also, consider the end of the workday and continue the task the next day if needed
//            scheduleAdapter.updateData();
            }
        }

        // Insert short breaks between tasks or at specific intervals, adjusting currentTimeSlot as needed

        return scheduleItems;
    }
    private String calculateTimeSlot(int startHour, int durationInMinutes) {
        // Calculate end hour based on duration
        int endHour = startHour + durationInMinutes / 60;
        // Format the time slot as a string, e.g., "09:00 AM - 10:00 AM"
        // Note: You'll need to write this formatting logic
        return formatTimeSlot(startHour, endHour);
    }
    private String formatTimeSlot(int startHour, int endHour) {
        // Format and return the time slot string
        // Implement formatting based on your requirements
        return startHour + ":00 AM - " + endHour + ":00 AM";
    }

    private String formatDuration(int durationInMinutes) {
        int hours = durationInMinutes / 60;
        int minutes = durationInMinutes % 60;
        return String.format("%02d:%02d", hours, minutes); // Formats time as HH:mm
    }

}
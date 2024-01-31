package com.example.optimizedschedule.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.optimizedschedule.R;
import com.example.optimizedschedule.taskListHandeling.Task;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChartsActivity extends AppCompatActivity {
    HashMap<String, Integer> taskDurations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        prepareChartData();
    }
    private void prepareChartData() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("data");
        List<Task> tasks = new ArrayList<>();
        HashMap<String, Integer> taskDurations = new HashMap<>();


        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    HashMap<String, Integer> taskDurations = new HashMap<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            String taskName = document.getString("taskName");
                            String taskTimeHours = document.getString("taskTimeHours");
                            String taskTimeMinute = document.getString("taskTimeMinutes");

                            int duration = Integer.parseInt(taskTimeHours) * 60 + Integer.parseInt(taskTimeMinute);
                            taskDurations.put(taskName, taskDurations.getOrDefault(taskName, 0) + duration);
                        }
                    }
                    setupPieChart(taskDurations);
                }
            }
        });
    }

    private void setupPieChart(HashMap<String, Integer> taskDurations) {
        // Convert and sort the map
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(taskDurations.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Create a new sorted map to return
        LinkedHashMap<String, Integer> sortedTaskDurations = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            sortedTaskDurations.put(entry.getKey(), entry.getValue());
        }

        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : sortedTaskDurations.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Task Durations (Minutes)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // or any other color scheme
        PieData pieData = new PieData(dataSet);

        Description description = new Description();
        description.setText("Pie Chart of Task Duration"); // Set the description text
        description.setTextColor(Color.BLACK); // Optional: Set the text color
        description.setTextSize(12f); // Optional: Set the text size
        pieChart.setDescription(description);

        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }



}

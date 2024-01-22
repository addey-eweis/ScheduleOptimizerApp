package com.example.optimizedschedule.taskListHandeling;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.optimizedschedule.FirebaseOperationsManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private String taskName;
    private String taskTimeHours;
    private String taskTimeMinutes;
    private String taskDate;
    private String taskCategory;
    private FirebaseFirestore firestoreDb;
    private String taskPriority;
    private Context context;
    public Transaction() {
    }

    public Transaction(String taskName, String taskTimeHours, String taskTimeMinutes, String taskDate, String taskCategory, String taskPriority, Context context) {
        this.taskName = taskName;
        this.taskTimeHours = taskTimeHours;
        this.taskTimeMinutes = taskTimeMinutes;
        this.taskDate = taskDate;
        this.taskCategory = taskCategory;
        this.firestoreDb = FirebaseFirestore.getInstance();
        this.taskPriority = taskPriority;
        this.context = context;
    }

    @NonNull
    private Map<String, Object> firebaseTaskHashmap() {
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("taskName", taskName);
        transactionData.put("taskTimeHours", taskTimeHours);
        transactionData.put("taskTimeMinutes", taskTimeMinutes);
        transactionData.put("taskDueDate", taskDate);
        transactionData.put("taskCategory", taskCategory);
        transactionData.put("taskPriority", taskPriority);

        return transactionData;
    }

    public String firebaseTaskSubmission() {
        CollectionReference transactionsRef = firestoreDb.collection("data");
        DocumentReference newTransactionRef = transactionsRef.document();
        String transactionId = newTransactionRef.getId();

        FirebaseOperationsManager.getInstance().submitToFirebase(context, newTransactionRef, firebaseTaskHashmap(), new FirebaseOperationsManager.FirebaseSubmitCallback() {
            @Override
            public void onSubmitSuccess() {
                Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSubmitFailure(String errorMessage) {
                Toast.makeText(context, "Processing Failed, Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        return transactionId;
    }
}

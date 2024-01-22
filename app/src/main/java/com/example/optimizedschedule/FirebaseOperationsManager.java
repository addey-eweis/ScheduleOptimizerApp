package com.example.optimizedschedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirebaseOperationsManager extends Activity {
    private static FirebaseOperationsManager instance;
    private final Handler handler;
    private final String TAG = "Authentication_SignIn";

    private FirebaseOperationsManager() {
        handler = new Handler(Looper.getMainLooper());

    }

    public static synchronized FirebaseOperationsManager getInstance() {
        if (instance == null) {
            instance = new FirebaseOperationsManager();
        }
        return instance;
    }



    public void throwException(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



    public void submitToFirebase(Context context, DocumentReference documentReference, Map<String, Object> dataMap, FirebaseSubmitCallback callback){
        if (dataMap == null || documentReference == null) {
            throwException(context, "Invalid input data.");
            return;
        }

        handler.post(() -> {
            documentReference.set(dataMap, SetOptions.merge())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Data successfully submitted to Firebase
                            if (callback != null) {
                                callback.onSubmitSuccess();
                            }
                        } else {
                            // Error occurred while submitting data to Firebase
                            if (task.getException() != null) {
                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";


                                // Handle the error or display an error message to the user
                                 Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

                                if (callback != null) {
                                    callback.onSubmitFailure(errorMessage);
                                }
                            }
                        }
                    });
        });
    }


    public interface FirebaseSubmitCallback {
        void onSubmitSuccess();
        void onSubmitFailure(String errorMessage);
    }

    public void readFromFirebase(Context context, DocumentReference documentReference, FirebaseReadCallback callback) {
        if (documentReference == null) {
            throwException(context, "Invalid input data.");
            return;
        }

        handler.post(() -> {
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Data successfully read from Firebase
                        // You can extract the data from the document using document.getData() or specific field access methods
                        // For example, if you have a field named "name":
                        // String name = document.getString("name");
                        // or
                        // Map<String, Object> dataMap = document.getData();
//                        Map<String, Object> dataMap = new HashMap<>();
//                        callback.onDataRead(dataMap); // Provide the data to the callback
                        if (callback != null) {
                            callback.onDataRead(document.getData());
                        }
                    } else {
                        // The document doesn't exist or is empty
                        // Handle the situation accordingly
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error occurred while reading data from Firebase
                    if (task.getException() != null) {
                        String errorMessage = task.getException().getMessage();
                        // Handle the error or display an error message to the user
                        // For example:
                         Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    public interface FirebaseReadCallback {
        void onDataRead(Map<String, Object> dataMap);
    }

}

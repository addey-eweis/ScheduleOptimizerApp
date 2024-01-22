package com.example.optimizedschedule.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.optimizedschedule.R;
import com.example.optimizedschedule.taskListHandeling.Transaction;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskAddActivity extends AppCompatActivity {
    String categoryName;
    String taskPriority;

    EditText date;
    
    String taskTimeHours;
    String taskTimeMinutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        MaterialButton doneButton = findViewById(R.id.done_button_add);
        doneButton.setOnClickListener(doneButtonListener);


        date = (EditText)findViewById(R.id.taskDateInput);
        date.addTextChangedListener(tw);
        date.setText(formattedDate);

        Button timePickerButton = findViewById(R.id.taskTimeInput);
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        Spinner spinner = findViewById(R.id.taskCategoryInput);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryName = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Spinner taskPriorityspinner = findViewById(R.id.taskPriorityInput);
        ArrayAdapter<CharSequence> taskPrioritySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.taskPriority, android.R.layout.simple_spinner_item);
        taskPrioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskPriorityspinner.setAdapter(taskPrioritySpinnerAdapter);
        taskPriorityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taskPriority = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private final View.OnClickListener doneButtonListener =
            view -> {
        
                EditText taskName = findViewById(R.id.taskNameInput);
                EditText taskDate = findViewById(R.id.taskDateInput);
                Context context = getApplicationContext();
                
                if(!taskName.toString().equals("") | taskTimeHours != null | taskTimeMinutes != null){ //TODO: Validate that the user entered all fields before submitting to firebase
                    
                    Transaction transaction = new Transaction(taskName.getText().toString(), taskTimeHours, taskTimeMinutes, taskDate.getText().toString(), categoryName.toLowerCase(), taskPriority.toLowerCase(), context);
                    String transactionID = transaction.firebaseTaskSubmission();

                    Toast.makeText(context, transactionID, Toast.LENGTH_SHORT).show();

                    finish();
                }
                else {
                    Toast.makeText(context, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            };

    public void showTimePickerDialog() {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        TextView timeSelected = findViewById(R.id.timeSelected);
                        // Handle the selected time
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        timeSelected.setText("Hours: " + selectedHour + " Minutes: " + selectedMinute);
                        Toast.makeText(getApplicationContext(), "Selected Time: " + time, Toast.LENGTH_SHORT).show();
                        taskTimeHours = String.valueOf(selectedHour);
                        taskTimeMinutes = String.valueOf(selectedMinute);
                    }
                }, hour, minute, true);

        
        // Show the dialog
        timePickerDialog.show();
    }

    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private final Calendar cal = Calendar.getInstance();

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int mon = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 2000) ? 2000 : (year > 2030) ? 2030 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                date.setText(current);
                date.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
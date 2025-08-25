package com.enamnotes.enamapppartsix;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private EditText participantNameEdit;
    private Button addparticipantBtn;
    private DBHandler dbHandler;

    private TextView tvDob;

    private String selectedDob = "";

    private Spinner occupationSpinner;
    private RadioGroup rgGender;

    private CheckBox cbWebDev, cbAppDev, cbFigma, cbSEO, cbDataAnalysis, cbMarketing;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button uploadFileBtn;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        participantNameEdit = findViewById(R.id.idParticipantName);
        addparticipantBtn = findViewById(R.id.idParticipantBtn);
        // Step 3: Reference RadioGroup and get selected gender
        rgGender = findViewById(R.id.rgGender);
        tvDob = findViewById(R.id.tvDob);
        occupationSpinner = findViewById(R.id.spinnerOccupation);

        //Initialize having interest

        cbWebDev = findViewById(R.id.cbWebDev);
        cbAppDev = findViewById(R.id.cbAppDev);
        cbFigma = findViewById(R.id.cbFigma);
        cbSEO = findViewById(R.id.cbSEO);
        cbDataAnalysis = findViewById(R.id.cbDataAnalysis);
        cbMarketing = findViewById(R.id.cbMarketing);
        uploadFileBtn = findViewById(R.id.btnUploadFile);


        dbHandler = new DBHandler(MainActivity.this);

        // Set up Occupation spinner
        ArrayAdapter<CharSequence> occAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.occupations_array,
                android.R.layout.simple_spinner_item
        );
        occAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupationSpinner.setAdapter(occAdapter);

        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show DatePickerDialog
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        (view, year1, month1, dayOfMonth) -> {
                            // Format date as yyyy-MM-dd
                            selectedDob = year1 + "-" + String.format("%02d", month1 + 1) + "-" + String.format("%02d", dayOfMonth);
                            tvDob.setText(selectedDob);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Upload File button click listener
        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        addparticipantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1️⃣ Get input from EditText
                String participantName = participantNameEdit.getText().toString();
                int selectedId = rgGender.getCheckedRadioButtonId();

                String gender = null;
                if (selectedId == R.id.rbMale) gender = "Male";
                else if (selectedId == R.id.rbFemale) gender = "Female";

                String occupation = occupationSpinner.getSelectedItem() != null
                        ? occupationSpinner.getSelectedItem().toString()
                        : "";
                //Collect interest data
                ArrayList<String> selectedInterests = new ArrayList<>();
                if (cbWebDev.isChecked()) selectedInterests.add("Web Dev");
                if (cbAppDev.isChecked()) selectedInterests.add("App Dev");
                if (cbFigma.isChecked()) selectedInterests.add("Figma");
                if (cbSEO.isChecked()) selectedInterests.add("SEO");
                if (cbDataAnalysis.isChecked()) selectedInterests.add("Data Analysis");
                if (cbMarketing.isChecked()) selectedInterests.add("Marketing");

                StringBuilder interestBuilder = new StringBuilder();
                for (int i = 0 ; i < selectedInterests.size(); i++) {
                    interestBuilder.append(selectedInterests.get(i));
                    if (i < selectedInterests.size() - 1) {
                        interestBuilder.append(", ");
                    }
                }

                String havingInterest = interestBuilder.toString();


                //Validate input
                if (participantName.isEmpty() || gender == null  || selectedDob.isEmpty()|| occupation.isEmpty() || selectedInterests.size() == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Do DB or action call
                String imagePath = selectedImageUri != null ? selectedImageUri.toString() : null;

                // Do DB or action call
                dbHandler.addParticipant(participantName, gender,  selectedDob, occupation, havingInterest, imagePath);

                // Show success message to user
                Toast.makeText(MainActivity.this,"Participant Added Successfully", Toast.LENGTH_SHORT).show();

                participantNameEdit.setText("");
                rgGender.clearCheck();
                tvDob.setText("Select Date");
                selectedDob = "";
                occupationSpinner.setSelection(0);

                //Clear checkbox
                cbWebDev.setChecked(false);
                cbAppDev.setChecked(false);
                cbFigma.setChecked(false);
                cbSEO.setChecked(false);
                cbDataAnalysis.setChecked(false);
                cbMarketing.setChecked(false);
                selectedImageUri = null; // Clear selected image


            }
        });
        }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show();
        }
    }




    }

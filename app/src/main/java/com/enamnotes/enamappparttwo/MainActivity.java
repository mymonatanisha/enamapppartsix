package com.enamnotes.enamappparttwo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    
    private EditText participantNameEdit;
    private Button addparticipantBtn;
    private Button uploadFileBtn;
    private DBHandler dbHandler;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        participantNameEdit = findViewById(R.id.idParticipantName);
        addparticipantBtn = findViewById(R.id.idParticipantBtn);
        uploadFileBtn = findViewById(R.id.btnUploadFile);
        // Step 3: Reference RadioGroup and get selected gender
        RadioGroup rgGender = findViewById(R.id.rgGender);

        dbHandler = new DBHandler(MainActivity.this);

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

                //Validate input
                if (participantName.isEmpty() || gender == null) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Do DB or action call
                String imagePath = selectedImageUri != null ? selectedImageUri.toString() : null;
                dbHandler.addParticipant(participantName, gender, imagePath);

                // Show success message to user
                Toast.makeText(MainActivity.this,"Participant Added Successfully", Toast.LENGTH_SHORT).show();

                participantNameEdit.setText("");
                rgGender.clearCheck();
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

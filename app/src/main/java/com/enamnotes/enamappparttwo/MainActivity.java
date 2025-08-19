package com.enamnotes.enamappparttwo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private EditText participantNameEdit;
    private Button addparticipantBtn;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        participantNameEdit = findViewById(R.id.idParticipantName);
        addparticipantBtn = findViewById(R.id.idParticipantBtn);

        dbHandler = new DBHandler(MainActivity.this);

        addparticipantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1️⃣ Get input from EditText
                String participantName = participantNameEdit.getText().toString();

                //Validate input
                if (participantName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Do DB or action call
                dbHandler.addParticipant(participantName);

                // Show success message to user
                Toast.makeText(MainActivity.this,"Participant Added Successfully", Toast.LENGTH_SHORT).show();

                participantNameEdit.setText("");


            }
        });
        }




    }

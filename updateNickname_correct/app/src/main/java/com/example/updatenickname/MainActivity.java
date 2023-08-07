package com.example.updatenickname;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText Fname, Email, DOB, Age;
    Spinner Gender;
    Button Submit, Next;

    DatabaseReference MainPost;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fname = findViewById(R.id.fname);
        Email = findViewById(R.id.email);
        DOB = findViewById(R.id.dob);
        Age = findViewById(R.id.age);

        Gender = findViewById(R.id.gender);
        Submit = findViewById(R.id.submit);
        Next = findViewById(R.id.next);

        MainPost = FirebaseDatabase.getInstance().getReference().child("UserData");

        String[] spinnertxt = getResources().getStringArray(R.array.spinner_item);
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnertxt);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gender.setAdapter(arrayAdapter);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();


                HashMap<String, Object> map = new HashMap<>();
                map.put("FullName", Fname.getText().toString());
                map.put("Email", Email.getText().toString());
                map.put("Date-OF-Birth", DOB.getText().toString());
                map.put("Age", Age.getText().toString());
                map.put("Gender", Gender.getSelectedItem().toString());

                MainPost.push()
                        .setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Fname.setText("");
                                Email.setText("");
                                DOB.setText("");
                                Age.setText("");

                                mDialog.dismiss();
                            }
                        });
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RetrieveData.class);
                startActivity(intent);
            }
        });
    }
}
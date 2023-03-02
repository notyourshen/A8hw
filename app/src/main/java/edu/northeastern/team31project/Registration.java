package edu.northeastern.team31project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Registration extends AppCompatActivity {

    Button signupButton;
    EditText signUpUsername, signUpUserEmail, signUpUserPassword;
    TextView signInText;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        signUpUsername = findViewById(R.id.signUp_username);
        signUpUserEmail = findViewById(R.id.signUp_userEmail);
        signUpUserPassword = findViewById(R.id.signUp_userPassword);
        signInText = findViewById(R.id.signIn_text);

        progressDialog = new ProgressDialog(Registration.this);
        progressDialog.setTitle("Waiting...");
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        signupButton = findViewById(R.id.signUpButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = signUpUsername.getText().toString();
                String UserEmail = signUpUserEmail.getText().toString();
                String UserPassword = signUpUserPassword.getText().toString();
                signUp(Username, UserEmail, UserPassword);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void signUp(String username, String userEmail, String userPassword) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (username.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(Registration.this,"Empty line is invalid", Toast.LENGTH_SHORT).show();
        } else if (!userEmail.matches(emailPattern)) {
            signUpUserEmail.setError("Invalid Email");
            Toast.makeText(Registration.this, "Invalid email", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DatabaseReference databaseReference = firebaseDatabase.getReference()
                                    .child("users")
                                    .child(Objects.requireNonNull(firebaseAuth.getUid()));
                            User user = new User(firebaseAuth.getUid(), username, userEmail);
                            databaseReference.setValue(user).addOnCompleteListener(newTask -> {
                                if (newTask.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Utils.updateToken(firebaseAuth.getUid());
                                    startActivity(new Intent(Registration.this, Contact.class));
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Registration.this, "Can't Create", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this,"Can't create",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
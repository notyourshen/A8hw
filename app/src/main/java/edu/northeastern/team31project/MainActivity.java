package edu.northeastern.team31project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.northeastern.team31project.User_Auth.Login;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button weather = (Button) findViewById(R.id.weatherButton);
        weather.setOnClickListener(this);

        Button about = (Button) findViewById(R.id.aboutButton);
        about.setOnClickListener(this);

        Button stickItToEm = (Button) findViewById(R.id.stickItbutton);
        stickItToEm.setOnClickListener(this);
    }

    public void onClick(View view){
        if (view.getId() == R.id.weatherButton) {
            Intent intent = new Intent(MainActivity.this, weather.class);
            startActivity(intent);
        } else if (view.getId() == R.id.aboutButton) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        } else if (view.getId() == R.id.stickItbutton) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }
}
package ca.georgiancollege.assignment_02;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.georgiancollege.assignment_02.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
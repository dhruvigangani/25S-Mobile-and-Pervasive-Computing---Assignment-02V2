package ca.georgiancollege.assignment_02;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import ca.georgiancollege.assignment_02.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.registerButton.setOnClickListener(v -> {
            String fullName = binding.enterFullName.getText().toString().trim();
            String email = binding.enterEmail.getText().toString().trim();
            String password = binding.enterPassword.getText().toString().trim();
            String confirmPassword = binding.confirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(fullName)) {
                binding.enterFullName.setError("Full name is required");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                binding.enterEmail.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                binding.enterPassword.setError("Password is required");
                return;
            }

            if (!password.equals(confirmPassword)) {
                binding.confirmPassword.setError("Passwords do not match");
                return;
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, Login.class));
                            finish();

                            } else {
                            Toast.makeText(Register.this, "Registration Failed:!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }
}

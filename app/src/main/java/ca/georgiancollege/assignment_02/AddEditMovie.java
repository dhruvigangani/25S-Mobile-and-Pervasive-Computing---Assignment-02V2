package ca.georgiancollege.assignment_02;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.georgiancollege.assignment_02.databinding.ActivityAddEditMovieBinding;

public class AddEditMovie extends AppCompatActivity {

    private ActivityAddEditMovieBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String movieId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        movieId = getIntent().getStringExtra("movieId");
        if (movieId != null) {
            binding.titleEditText.setText(getIntent().getStringExtra("title"));
            binding.directorEditText.setText(getIntent().getStringExtra("director"));
            binding.yearEditText.setText(getIntent().getStringExtra("year"));
        }

        binding.addEditButton.setOnClickListener(v -> saveMovie());
    }

    private void saveMovie() {
        String title = binding.titleEditText.getText().toString().trim();
        String director = binding.directorEditText.getText().toString().trim();
        String year = binding.yearEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(director) || TextUtils.isEmpty(year)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Movie movie = new Movie(title, director, year, userId);

        if (movieId == null) {
            db.collection("movies").add(movie)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to add movie", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("movies").document(movieId).set(movie)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Movie updated", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to update movie", Toast.LENGTH_SHORT).show());
        }
    }
}


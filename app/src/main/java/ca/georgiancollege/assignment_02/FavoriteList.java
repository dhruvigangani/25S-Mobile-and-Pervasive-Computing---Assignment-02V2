package ca.georgiancollege.assignment_02;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ca.georgiancollege.assignment_02.databinding.ActivityFavoriteListBinding;

public class FavoriteList extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    ActivityFavoriteListBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<Movie> movieList;
    private MovieAdapter adapter;

    private static final int ADD_EDIT_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList, this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditMovie.class);
            startActivityForResult(intent, ADD_EDIT_REQUEST_CODE);
        });

        loadMovies();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMovies() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("movies")
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    movieList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Movie movie = doc.toObject(Movie.class);
                        if (movie != null) {
                            movie.setId(doc.getId());
                            movieList.add(movie);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load movies", Toast.LENGTH_SHORT).show();
                    Log.e("FavoriteList", "Error loading movies", e);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            loadMovies(); // Reload movies after add/edit
        }
    }

    @Override
    public void onEditClick(int position) {
        Movie movie = movieList.get(position);
        Intent intent = new Intent(this, AddEditMovie.class);
        intent.putExtra("movieId", movie.getId());
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("director", movie.getDirector());
        intent.putExtra("year", movie.getYear());
        startActivityForResult(intent, ADD_EDIT_REQUEST_CODE);
    }

    @Override
    public void onDeleteClick(int position) {
        Movie movie = movieList.get(position);
        db.collection("movies").document(movie.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show();
                    loadMovies();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                    Log.e("FavoriteList", "Delete failed", e);
                });
    }
}


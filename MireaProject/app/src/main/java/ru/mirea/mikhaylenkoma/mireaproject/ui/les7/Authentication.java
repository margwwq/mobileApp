package ru.mirea.mikhaylenkoma.mireaproject.ui.les7;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.mikhaylenkoma.mireaproject.MainActivity;
import ru.mirea.mikhaylenkoma.mireaproject.databinding.ActivityAuthenticationBinding;

public class Authentication extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private ActivityAuthenticationBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmailField.getText().toString();
                String password = binding.editTextPasswordField.getText().toString();
                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Authentication.this, "Заполните все поля",
                            Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(email, password);
                }
            }
        });

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmailField.getText().toString();
                String password = binding.editTextPasswordField.getText().toString();
                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Authentication.this, "Заполните все поля",
                            Toast.LENGTH_SHORT).show();
                } else {
                    signIn(email, password);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        String keyIntent = (String) getIntent().getSerializableExtra("key");
        String emailLogOut = (String) getIntent().getSerializableExtra("email");
        if(keyIntent != null && emailLogOut != null && keyIntent.equals("logout")) {
            logOut();
            binding.editTextEmailField.setText(emailLogOut);
        } else {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            openApp(currentUser);
        }
    }

    private void openApp(FirebaseUser currentUser) {
        if(currentUser != null) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("UserEmail", currentUser.getEmail());
            startActivity(intent);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "create account: " + email + " : " + password);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            openApp(user);
                        } else {
                            Log.d(TAG, "createUserWithEmail:failed");
                            Toast.makeText(Authentication.this, "Ошибка регистрации",
                                    Toast.LENGTH_SHORT).show();
                            binding.editTextPasswordField.setText("");
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "SignIn: " + email + " : " + password);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmailAndPassword:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            openApp(user);
                        } else {
                            Log.d(TAG, "signInWithEmailAndPassword:failed");
                            Toast.makeText(Authentication.this, "Ошибка входа",
                                    Toast.LENGTH_SHORT).show();
                            binding.editTextPasswordField.setText("");
                        }
                    }
                });
    }

    private void logOut() {
        firebaseAuth.signOut();
    }
}
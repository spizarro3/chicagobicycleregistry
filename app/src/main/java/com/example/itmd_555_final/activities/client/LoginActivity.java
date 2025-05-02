package com.example.itmd_555_final.activities.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.itmd_555_final.R;
import com.example.itmd_555_final.activities.MainActivity;
import com.example.itmd_555_final.models.Client;
import com.example.itmd_555_final.util.SessionManager;
import com.example.itmd_555_final.data.dao.ClientDao;

public class LoginActivity extends AppCompatActivity {

    private EditText userName, password;
    private Button loginBtn;
    private int counter = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword);
        loginBtn = findViewById(R.id.btnLogin);

        loginBtn.setOnClickListener(v -> checkCredentials());

        Button signUpBtn = findViewById(R.id.btnSignUp);
        signUpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterClientActivity.class);
            startActivity(intent);
        });
    }

    private void checkCredentials() {
        String inputUsername = userName.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

        ClientDao clientDao = new ClientDao(this);

        if (clientDao.validateLogin(inputUsername, inputPassword)) {
            Client currentClient = clientDao.getClientByUsername(inputUsername);

            if (currentClient != null) {
                SessionManager.setCurrentClient(currentClient); // Save logged-in client

                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error: Client record not found.", Toast.LENGTH_SHORT).show();
            }

        } else {
            handleFailedLogin();
        }
    }

    private void handleFailedLogin() {
        switch (counter++) {
            case 1:
                Toast.makeText(this, "Wrong Credentials - 2 attempts left", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(this, "Wrong Credentials - 1 attempt left", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(this, "Wrong Credentials - 0 attempts left - Locked out!", Toast.LENGTH_LONG).show();
                loginBtn.setEnabled(false);
                Toast.makeText(this, "Locked out. Please restart the app.", Toast.LENGTH_LONG).show();

                break;
            default:
                Toast.makeText(this, "Invalid login attempt.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}

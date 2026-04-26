package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button btnLogin;
    private DatabaseReference dbRef;
    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Employees");

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String id = editUsername.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();

        if (id.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter both ID and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Employee employee = snapshot.getValue(Employee.class);
                    if (employee != null && employee.getPassword().equals(pass)) {
                        // Login Success
                        if ("Admin".equalsIgnoreCase(employee.getRole())) {
                            startActivity(new Intent(MainActivity.this, AdminDashboard.class));
                        } else {
                            // Start Driver/Conductor Dashboard
                            Intent intent = new Intent(MainActivity.this, DriverDashboard.class);
                            intent.putExtra("EMPLOYEE_ID", employee.getId());
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Employee ID not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

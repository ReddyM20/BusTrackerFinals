package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverDashboard extends AppCompatActivity {

    private TextView tvRoute, tvBus, tvDeparture, tvDriver, tvConductor;
    private Button btnStartTrip;
    private LinearLayout layoutHome, layoutTrips, layoutSettings;
    private String employeeId;
    private DatabaseReference dbRef;
    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        employeeId = getIntent().getStringExtra("EMPLOYEE_ID");
        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Employees").child(employeeId);

        initViews();
        loadDriverData();
        setupNavigation();

        btnStartTrip.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboard.this, TripViewActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        tvRoute = findViewById(R.id.tvRoute);
        tvBus = findViewById(R.id.tvBus);
        tvDeparture = findViewById(R.id.tvDeparture);
        tvDriver = findViewById(R.id.tvDriver);
        tvConductor = findViewById(R.id.tvConductor);
        btnStartTrip = findViewById(R.id.btnStartTrip);

        layoutHome = findViewById(R.id.layout_driver_home);
        layoutTrips = findViewById(R.id.layout_driver_trips);
        layoutSettings = findViewById(R.id.layout_driver_settings);

        layoutHome.setSelected(true);
    }

    private void loadDriverData() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Employee emp = snapshot.getValue(Employee.class);
                    if (emp != null) {
                        tvDriver.setText(emp.getName());
                        tvRoute.setText(emp.getUnit()); // Assuming unit stores route for now or similar
                        // You can expand this to fetch real Route data if needed
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverDashboard.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigation() {
        layoutTrips.setOnClickListener(v -> {
            startActivity(new Intent(this, TripListActivity.class));
        });
        layoutSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, DriverSettingsActivity.class));
        });
    }
}

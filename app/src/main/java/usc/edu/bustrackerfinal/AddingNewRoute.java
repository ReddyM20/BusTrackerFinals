package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddingNewRoute extends AppCompatActivity {

    private EditText editCode, editDistance;
    private Spinner spinnerStart, spinnerEnd, spinnerStatus;
    private Button btnAdd;
    private LinearLayout layoutDashboard, layoutAddRoute, layoutAssignedDriver, layoutBack;

    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_new_route);

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Routes");

        initViews();
        setupSpinners();
        setupNavClickListeners();

        // Highlight the current page in the nav bar
        if (layoutAddRoute != null) layoutAddRoute.setSelected(true);

        btnAdd.setOnClickListener(v -> saveRoute());
    }

    private void initViews() {
        editCode = findViewById(R.id.editRouteCode);
        editDistance = findViewById(R.id.editDistance);
        spinnerStart = findViewById(R.id.spinnerTerminalStart);
        spinnerEnd = findViewById(R.id.spinnerTerminalEnd);
        spinnerStatus = findViewById(R.id.spinnerRouteStatus);
        btnAdd = findViewById(R.id.btnAddRoute);

        // Navigation IDs from route_navbottons.xml
        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutAddRoute = findViewById(R.id.layout_addroute);
        layoutAssignedDriver = findViewById(R.id.layout_assigneddriver);
        layoutBack = findViewById(R.id.layout_back);
    }

    private void setupSpinners() {
        String[] statusOptions = {"Active", "Inactive"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions));

        String[] locations = {"Terminal A", "Terminal B", "Terminal C", "Mactan", "Cebu City"};
        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        spinnerStart.setAdapter(locAdapter);
        spinnerEnd.setAdapter(locAdapter);
    }

    private void saveRoute() {
        String code = editCode.getText().toString().trim();
        String distance = editDistance.getText().toString().trim();
        String startPoint = spinnerStart.getSelectedItem().toString();
        String endPoint = spinnerEnd.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if (code.isEmpty() || distance.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Using your 10-parameter Route model
        Route newRoute = new Route(code, startPoint, endPoint, distance, status, "", "", "", "", "");

        dbRef.child(code).setValue(newRoute).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Route Added!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupNavClickListeners() {
        if (layoutDashboard != null) {
            layoutDashboard.setOnClickListener(v -> {
                startActivity(new Intent(this, RouteManagement.class));
                finish();
            });
        }

        if (layoutAssignedDriver != null) {
            layoutAssignedDriver.setOnClickListener(v -> {
                startActivity(new Intent(this, AssigningDriver.class));
                finish();
            });
        }

        if (layoutBack != null) {
            layoutBack.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminDashboard.class));
                finish();
            });
        }
    }
}
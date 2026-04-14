package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AssigningDriver extends AppCompatActivity {

    private Spinner spinnerRouteCode, spinnerDeparture;
    private EditText editTerminal1, editTerminal2, editPlate, editDriver, editConductor, editContact;
    private Button btnAddTrip;

    // Navigation bar layouts
    private LinearLayout layoutDashboard, layoutAddRoute, layoutAssignedDriver, layoutBack;

    private DatabaseReference routesRef, tripsRef;
    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigning_driver);

        // Initialize Firebase
        routesRef = FirebaseDatabase.getInstance(DB_URL).getReference("Routes");
        tripsRef = FirebaseDatabase.getInstance(DB_URL).getReference("AssignedTrips");

        initViews();
        setupStaticSpinners();
        loadRouteCodes();
        setupNavClickListeners(); // Added Nav Listeners

        // Highlight current page in navigation bar
        if (layoutAssignedDriver != null) {
            layoutAssignedDriver.setSelected(true);
        }

        btnAddTrip.setOnClickListener(v -> saveAssignedTrip());
    }

    private void initViews() {
        spinnerRouteCode = findViewById(R.id.spinnerRouteCode);
        spinnerDeparture = findViewById(R.id.spinnerDeparture);
        editTerminal1 = findViewById(R.id.editTerminal1);
        editTerminal2 = findViewById(R.id.editTerminal2);
        editPlate = findViewById(R.id.editPlate);
        editDriver = findViewById(R.id.editDriver);
        editConductor = findViewById(R.id.editConductor);
        editContact = findViewById(R.id.editContact);
        btnAddTrip = findViewById(R.id.btnAddTrip);

        // Navigation IDs from route_navbottons.xml
        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutAddRoute = findViewById(R.id.layout_addroute);
        layoutAssignedDriver = findViewById(R.id.layout_assigneddriver);
        layoutBack = findViewById(R.id.layout_back);
    }

    private void setupNavClickListeners() {
        if (layoutDashboard != null) {
            layoutDashboard.setOnClickListener(v -> {
                Intent intent = new Intent(this, RouteManagement.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            });
        }

        if (layoutAddRoute != null) {
            layoutAddRoute.setOnClickListener(v -> {
                startActivity(new Intent(this, AddingNewRoute.class));
                finish();
            });
        }

        // layoutAssignedDriver is this activity, so we don't need a listener or can just scroll to top

        if (layoutBack != null) {
            layoutBack.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminDashboard.class));
                finish();
            });
        }
    }

    private void setupStaticSpinners() {
        String[] times = {"06:00 AM", "08:00 AM", "10:00 AM", "12:00 PM", "02:00 PM", "04:00 PM", "06:00 PM"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times);
        spinnerDeparture.setAdapter(timeAdapter);
    }

    private void loadRouteCodes() {
        routesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> codes = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    codes.add(ds.getKey());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AssigningDriver.this,
                        android.R.layout.simple_spinner_item, codes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRouteCode.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AssigningDriver.this, "Failed to load routes", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerRouteCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCode = parent.getItemAtPosition(position).toString();
                fetchRouteDetails(selectedCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchRouteDetails(String code) {
        routesRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Route route = snapshot.getValue(Route.class);
                if (route != null) {
                    editTerminal1.setText(route.getTerminalStart());
                    editTerminal2.setText(route.getTerminalEnd());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void saveAssignedTrip() {
        String code = spinnerRouteCode.getSelectedItem().toString();
        String plate = editPlate.getText().toString().trim();
        String driver = editDriver.getText().toString().trim();
        String conductor = editConductor.getText().toString().trim();
        String contact = editContact.getText().toString().trim();
        String time = spinnerDeparture.getSelectedItem().toString();

        if (plate.isEmpty() || driver.isEmpty()) {
            Toast.makeText(this, "Please fill in Plate Number and Driver", Toast.LENGTH_SHORT).show();
            return;
        }

        Route assignedTrip = new Route(
                code,
                editTerminal1.getText().toString(),
                editTerminal2.getText().toString(),
                "", "Active", time, plate, driver, conductor, contact
        );

        tripsRef.child(plate).setValue(assignedTrip)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Trip Assigned Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
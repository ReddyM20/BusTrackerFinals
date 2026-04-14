package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class AssigningDriver extends AppCompatActivity {
    private Spinner spinnerRouteCode, spinnerDeparture, spinnerDriver, spinnerConductor;
    private EditText editTerminal1, editTerminal2, editPlate, editContact;
    private DatabaseReference routesRef, employeesRef;
    private LinearLayout layoutDashboard, layoutAddRoute, layoutBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigning_driver);

        routesRef = FirebaseDatabase.getInstance("https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Routes");
        employeesRef = FirebaseDatabase.getInstance("https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Employees");

        initViews();
        loadEmployees();
        loadRouteCodes();
        setupDepartureSpinner();
        setupNavClickListeners();

        findViewById(R.id.btnAddTrip).setOnClickListener(v -> saveTrip());
    }

    private void initViews() {
        spinnerRouteCode = findViewById(R.id.spinnerRouteCode);
        spinnerDeparture = findViewById(R.id.spinnerDeparture);
        spinnerDriver = findViewById(R.id.spinnerDriver);
        spinnerConductor = findViewById(R.id.spinnerConductor);
        editTerminal1 = findViewById(R.id.editTerminal1);
        editTerminal2 = findViewById(R.id.editTerminal2);
        editPlate = findViewById(R.id.editPlate);
        editContact = findViewById(R.id.editContact);
        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutAddRoute = findViewById(R.id.layout_addroute);
        layoutBack = findViewById(R.id.layout_back);

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

    private void setupDepartureSpinner() {
        String[] times = {"06:00 AM", "07:00 AM", "08:00 AM", "09:00 AM", "10:00 AM", 
                         "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", 
                         "04:00 PM", "05:00 PM", "06:00 PM"};
        spinnerDeparture.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times));
    }

    private void fetchRouteDetails(String code) {
        routesRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Route route = snapshot.getValue(Route.class);
                if (route != null) {
                    editTerminal1.setText(route.getTerminalStart());
                    editTerminal2.setText(route.getTerminalEnd());
                    if (route.getPlateNumber() != null) editPlate.setText(route.getPlateNumber());
                    if (route.getContactInfo() != null) editContact.setText(route.getContactInfo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadEmployees() {
        employeesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> drivers = new ArrayList<>(), conductors = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Employee emp = ds.getValue(Employee.class);
                    if (emp != null) {
                        if ("Driver".equalsIgnoreCase(emp.getRole())) drivers.add(emp.getName());
                        else if ("Conductor".equalsIgnoreCase(emp.getRole())) conductors.add(emp.getName());
                    }
                }
                spinnerDriver.setAdapter(new ArrayAdapter<>(AssigningDriver.this, android.R.layout.simple_spinner_dropdown_item, drivers));
                spinnerConductor.setAdapter(new ArrayAdapter<>(AssigningDriver.this, android.R.layout.simple_spinner_dropdown_item, conductors));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadRouteCodes() {
        routesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> codes = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) codes.add(ds.getKey());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AssigningDriver.this, android.R.layout.simple_spinner_dropdown_item, codes);
                spinnerRouteCode.setAdapter(adapter);

                String preSelectedRoute = getIntent().getStringExtra("ROUTE_CODE");
                if (preSelectedRoute != null) {
                    int pos = adapter.getPosition(preSelectedRoute);
                    if (pos >= 0) spinnerRouteCode.setSelection(pos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void saveTrip() {
        if (spinnerRouteCode.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a route code", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerDriver.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a driver", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerConductor.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a conductor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerDeparture.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a departure time", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = spinnerRouteCode.getSelectedItem().toString();
        // Update the main route node with driver/plate info
        routesRef.child(code).child("plateNumber").setValue(editPlate.getText().toString());
        routesRef.child(code).child("assignedDriver").setValue(spinnerDriver.getSelectedItem().toString());
        routesRef.child(code).child("assignedConductor").setValue(spinnerConductor.getSelectedItem().toString());
        routesRef.child(code).child("departureTime").setValue(spinnerDeparture.getSelectedItem().toString());
        routesRef.child(code).child("contactInfo").setValue(editContact.getText().toString());

        Toast.makeText(this, "Trip Assigned!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupNavClickListeners() {
        if (layoutDashboard != null) {
            layoutDashboard.setOnClickListener(v -> {
                startActivity(new Intent(this, RouteManagement.class));
                finish();
            });
        }

        if (layoutAddRoute != null) {
            layoutAddRoute.setOnClickListener(v -> {
                startActivity(new Intent(this, AddingNewRoute.class));
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

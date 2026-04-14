package usc.edu.bustrackerfinal;

import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class EditRoute extends AppCompatActivity {

    private EditText editCode, editT1, editT2, editDist, editPlate, editContact;
    private Spinner spinnerStatus, spinnerTime, spinnerDriver, spinnerConductor;
    private DatabaseReference dbRef, empRef;
    private String routeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);

        routeCode = getIntent().getStringExtra("ROUTE_CODE");
        dbRef = FirebaseDatabase.getInstance("https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Routes").child(routeCode);
        empRef = FirebaseDatabase.getInstance("https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Employees");

        initViews();
        setupStaticSpinners();
        loadEmployeeSpinners(); // To populate Driver/Conductor from DB
        loadRouteData();

        findViewById(R.id.btnBackArrow).setOnClickListener(v -> finish());
        findViewById(R.id.btnSaveEdit).setOnClickListener(v -> saveChanges());
    }

    private void initViews() {
        editCode = findViewById(R.id.editRouteCode);
        editT1 = findViewById(R.id.editTerminal1);
        editT2 = findViewById(R.id.editTerminal2);
        editDist = findViewById(R.id.editDistance);
        editPlate = findViewById(R.id.editPlate);
        editContact = findViewById(R.id.editContact);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerTime = findViewById(R.id.spinnerTime);
        spinnerDriver = findViewById(R.id.spinnerDriver);
        spinnerConductor = findViewById(R.id.spinnerConductor);
    }

    private void loadRouteData() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Route route = snapshot.getValue(Route.class);
                if (route != null) {
                    editCode.setText(route.getRouteCode());
                    editT1.setText(route.getTerminalStart());
                    editT2.setText(route.getTerminalEnd());
                    editDist.setText(route.getDistance());
                    editPlate.setText(route.getPlateNumber());
                    editContact.setText(route.getContactInfo());

                    setSpinnerValue(spinnerStatus, route.getStatus());
                    setSpinnerValue(spinnerTime, route.getDepartureTime());
                    // Driver/Conductor spinners set after loadEmployeeSpinners finishes
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void saveChanges() {
        Route updatedRoute = new Route(
                routeCode,
                editT1.getText().toString().trim(),
                editT2.getText().toString().trim(),
                editDist.getText().toString().trim(),
                spinnerStatus.getSelectedItem().toString(),
                spinnerTime.getSelectedItem().toString(),
                editPlate.getText().toString().trim(),
                spinnerDriver.getSelectedItem().toString(),
                spinnerConductor.getSelectedItem().toString(),
                editContact.getText().toString().trim()
        );

        dbRef.setValue(updatedRoute).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Route Updated!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupStaticSpinners() {
        String[] status = {"Active", "Inactive"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, status));

        String[] times = {"06:00 AM", "08:00 AM", "10:00 AM", "12:00 PM", "02:00 PM", "04:00 PM"};
        spinnerTime.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times));
    }

    private void loadEmployeeSpinners() {
        empRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                spinnerDriver.setAdapter(new ArrayAdapter<>(EditRoute.this, android.R.layout.simple_spinner_dropdown_item, drivers));
                spinnerConductor.setAdapter(new ArrayAdapter<>(EditRoute.this, android.R.layout.simple_spinner_dropdown_item, conductors));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        if (adapter != null) {
            int pos = adapter.getPosition(value);
            spinner.setSelection(pos);
        }
    }
}
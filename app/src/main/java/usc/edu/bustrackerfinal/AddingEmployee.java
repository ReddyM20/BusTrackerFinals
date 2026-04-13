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

public class AddingEmployee extends AppCompatActivity {

    private EditText editID, editFirst, editLast, editEmail, editAddress;
    private Spinner spinnerRole, spinnerUnit, spinnerStatus;
    private Button btnAdd;
    private LinearLayout layoutDashboard, layoutBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_employee);

        initViews();
        setupSpinners();
        setupNavigation();

        btnAdd.setOnClickListener(v -> {
            // Logic to save data would go here
            Toast.makeText(this, "Employee Added Successfully", Toast.LENGTH_SHORT).show();
            finish(); // Returns to EmployeeManagement list after adding
        });
    }

    private void initViews() {
        editID = findViewById(R.id.editEmployeeID);
        editFirst = findViewById(R.id.editFirstName);
        editLast = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnAdd = findViewById(R.id.btnAddEmployee);

        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutBack = findViewById(R.id.layout_setting);
    }

    private void setupSpinners() {
        // Status Spinner
        String[] statusOptions = {"Active", "Deactive"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        spinnerStatus.setAdapter(statusAdapter);

        // Role Spinner
        String[] roles = {"Driver", "Conductor", "Admin"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spinnerRole.setAdapter(roleAdapter);

        // Unit Spinner
        String[] units = {"Office", "Bus 101", "Jeepney A"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnit.setAdapter(unitAdapter);
    }

    private void setupNavigation() {
        layoutDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmployeeManagement.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        layoutBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }
}
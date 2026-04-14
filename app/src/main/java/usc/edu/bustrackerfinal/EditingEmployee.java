package usc.edu.bustrackerfinal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditingEmployee extends AppCompatActivity {

    private EditText editID, editFirst, editLast, editEmail, editAddress, editLicense, editContact, editPass;
    private Spinner spinnerRole, spinnerUnit, spinnerStatus;
    private Button btnSaveEdit;
    private ImageButton btnBack;

    private DatabaseReference dbRef;
    private String employeeId;
    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_employee);

        // Get the ID passed from the previous activity
        employeeId = getIntent().getStringExtra("EMPLOYEE_ID");

        initViews();
        setupSpinners();

        // Initialize Firebase Reference for this specific employee
        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Employees").child(employeeId);

        // Automatically fill the forms
        loadEmployeeData();

        btnSaveEdit.setOnClickListener(v -> updateEmployeeData());
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        editID = findViewById(R.id.editEmployeeID);
        editFirst = findViewById(R.id.editFirstName);
        editLast = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editLicense = findViewById(R.id.editLicense);
        editContact = findViewById(R.id.editContact);
        editAddress = findViewById(R.id.editAddress);
        editPass = findViewById(R.id.editDefaultPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSaveEdit = findViewById(R.id.btnEditEmployee);
        btnBack = findViewById(R.id.btnBackArrow);
    }

    private void loadEmployeeData() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Employee emp = snapshot.getValue(Employee.class);
                    if (emp != null) {
                        // Fill EditTexts
                        editID.setText(emp.getId());

                        // Splitting name back into First and Last
                        String[] nameParts = emp.getName().split(" ", 2);
                        editFirst.setText(nameParts[0]);
                        if (nameParts.length > 1) editLast.setText(nameParts[1]);

                        editEmail.setText(emp.getEmail());
                        editLicense.setText(emp.getLicense());
                        editContact.setText(emp.getContact());
                        editAddress.setText(emp.getAddress());
                        editPass.setText(emp.getPassword());

                        // Set Spinners
                        setSpinnerValue(spinnerRole, emp.getRole());
                        setSpinnerValue(spinnerUnit, emp.getUnit());
                        setSpinnerValue(spinnerStatus, emp.getStatus());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditingEmployee.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmployeeData() {
        // Collect updated data
        String updatedName = editFirst.getText().toString().trim() + " " + editLast.getText().toString().trim();

        Employee updatedEmployee = new Employee(
                employeeId,
                updatedName,
                spinnerRole.getSelectedItem().toString(),
                spinnerUnit.getSelectedItem().toString(),
                editEmail.getText().toString().trim(),
                editAddress.getText().toString().trim(),
                editLicense.getText().toString().trim(),
                editContact.getText().toString().trim(),
                editPass.getText().toString().trim(),
                spinnerStatus.getSelectedItem().toString()
        );

        // Save back to the same ID
        dbRef.setValue(updatedEmployee).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupSpinners() {
        String[] statusOptions = {"Active", "Deactive"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions));

        String[] roles = {"Driver", "Conductor", "Admin"};
        spinnerRole.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles));

        String[] units = {"Office", "Bus 101", "Jeepney A"};
        spinnerUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));
    }

    // Helper method to set spinner selection based on string value
    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(value);
        spinner.setSelection(position);
    }
}
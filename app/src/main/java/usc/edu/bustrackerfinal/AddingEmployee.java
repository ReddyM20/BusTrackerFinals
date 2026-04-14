package usc.edu.bustrackerfinal;

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

public class AddingEmployee extends AppCompatActivity {

    private EditText editID, editFirst, editLast, editEmail, editAddress, editLicense, editContact, editPass;
    private Spinner spinnerRole, spinnerUnit, spinnerStatus;
    private Button btnAdd;
    private LinearLayout layoutBack;

    // Your Southeast Asia URL
    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_employee);

        databaseReference = FirebaseDatabase.getInstance(DB_URL).getReference("Employees");

        initViews();
        setupSpinners();

        btnAdd.setOnClickListener(v -> saveEmployeeToFirebase());
        layoutBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        editID = findViewById(R.id.editEmployeeID);
        editFirst = findViewById(R.id.editFirstName);
        editLast = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);

        // New fields
        editLicense = findViewById(R.id.editLicense);
        editContact = findViewById(R.id.editContact);
        editPass = findViewById(R.id.editDefaultPassword);

        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        btnAdd = findViewById(R.id.btnAddEmployee);
        layoutBack = findViewById(R.id.layout_setting);
    }

    private void setupSpinners() {
        String[] statusOptions = {"Active", "Deactive"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions));

        String[] roles = {"Driver", "Conductor", "Admin"};
        spinnerRole.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles));

        String[] units = {"Office", "Bus 101", "Jeepney A"};
        spinnerUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));
    }

    private void saveEmployeeToFirebase() {
        String id = editID.getText().toString().trim();
        String firstName = editFirst.getText().toString().trim();
        String lastName = editLast.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String license = editLicense.getText().toString().trim();
        String contact = editContact.getText().toString().trim();
        String password = editPass.getText().toString().trim();

        String role = spinnerRole.getSelectedItem().toString();
        String unit = spinnerUnit.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        // Validation
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields (ID, Name, Password)", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullName = firstName + " " + lastName;

        // Create the updated object
        // NOTE: Make sure your Employee class has a constructor that accepts these!
        Employee employee = new Employee(id, fullName, role, unit, email, address, license, contact, password, status);

        // Save using ID as the key
        databaseReference.child(id).setValue(employee)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Employee Added Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
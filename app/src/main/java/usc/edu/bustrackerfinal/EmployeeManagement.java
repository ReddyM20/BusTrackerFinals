package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManagement extends AppCompatActivity {

    private RecyclerView rvEmployeeList;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList;
    private DatabaseReference dbRef;
    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Employees");

        rvEmployeeList = findViewById(R.id.rvEmployeeList);
        rvEmployeeList.setLayoutManager(new LinearLayoutManager(this));

        employeeList = new ArrayList<>();
        adapter = new EmployeeAdapter(employeeList);
        rvEmployeeList.setAdapter(adapter);

        // Fetch Data from Firebase
        fetchEmployees();

        // Setup Nav
        findViewById(R.id.layout_monitoring).setOnClickListener(v -> {
            startActivity(new Intent(this, AddingEmployee.class));
        });
        findViewById(R.id.layout_setting).setOnClickListener(v -> finish());
    }

    private void fetchEmployees() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeeList.clear(); // Clear old data
                for (DataSnapshot data : snapshot.getChildren()) {
                    Employee emp = data.getValue(Employee.class);
                    if (emp != null) {
                        employeeList.add(emp);
                    }
                }
                adapter.notifyDataSetChanged(); // Refresh the table
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
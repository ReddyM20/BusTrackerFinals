package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManagement extends AppCompatActivity {

    private LinearLayout layoutDashboard, layoutAddEmployee, layoutBack;
    private RecyclerView rvEmployeeList;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList; // This holds your original full list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        initViews();
        setupRecyclerView();
        setupSearchLogic(); // Initialize Search
        setupNavClickListeners();
    }

    private void initViews() {
        rvEmployeeList = findViewById(R.id.rvEmployeeList);
        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutAddEmployee = findViewById(R.id.layout_monitoring);
        layoutBack = findViewById(R.id.layout_setting);
        layoutAddEmployee.setSelected(true);
    }

    private void setupRecyclerView() {
        employeeList = new ArrayList<>();
        employeeList.add(new Employee("E001", "Juan Dela Cruz", "Driver"));
        employeeList.add(new Employee("E002", "Maria Santos", "Conductor"));
        employeeList.add(new Employee("E003", "Pedro Penduko", "Admin"));
        employeeList.add(new Employee("E004", "John Wick", "Driver"));

        adapter = new EmployeeAdapter(employeeList);
        rvEmployeeList.setLayoutManager(new LinearLayoutManager(this));
        rvEmployeeList.setAdapter(adapter);
    }

    private void setupSearchLogic() {
        SearchView searchView = findViewById(R.id.searchEmployee);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This triggers every time a letter is typed or deleted
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<Employee> filteredList = new ArrayList<>();

        for (Employee employee : employeeList) {
            // Check if the employee name contains the searched text (ignoring case)
            if (employee.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(employee);
            }
        }

        // Send the filtered list to the adapter to update the screen
        adapter.setFilteredList(filteredList);
    }

    private void setupNavClickListeners() {
        layoutDashboard.setOnClickListener(v -> rvEmployeeList.smoothScrollToPosition(0));

        layoutAddEmployee.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddingEmployee.class);
            startActivity(intent);
        });

        layoutBack.setOnClickListener(v -> finish());
    }
}
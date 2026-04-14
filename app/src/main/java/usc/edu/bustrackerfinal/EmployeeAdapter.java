package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;

    // Constructor
    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    // Method to update the list when searching
    public void setFilteredList(List<Employee> filteredList) {
        this.employeeList = filteredList;
        notifyDataSetChanged(); // This "filters out" the unnecessary names visually
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_employee.xml layout we created earlier
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        // Bind data to the views
        holder.txtId.setText(employee.getId());
        holder.txtName.setText(employee.getName());
        holder.txtRole.setText(employee.getRole());

        // Action button click (Edit/Settings)
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditingEmployee.class);
            // Pass the unique ID to the next activity
            intent.putExtra("EMPLOYEE_ID", employee.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    // ViewHolder class
    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName, txtRole;
        ImageButton btnEdit;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.row_id);
            txtName = itemView.findViewById(R.id.row_name);
            txtRole = itemView.findViewById(R.id.row_role);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }
}
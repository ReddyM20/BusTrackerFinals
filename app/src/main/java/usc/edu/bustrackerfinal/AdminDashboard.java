package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class AdminDashboard extends AppCompatActivity {

    TextView txtEmployeeCount, txtRouteCount;
    Button btnRouteManagement, btnEmployeeManagement;
    LinearLayout layoutDashboard, layoutMonitoring, layoutSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initViews();

        // Dashboard is the active tab when app opens
        layoutDashboard.setSelected(true);

        setupClickListeners();
    }

    private void initViews() {
        txtEmployeeCount = findViewById(R.id.txtemployee);
        txtRouteCount = findViewById(R.id.txtroute);
        btnRouteManagement = findViewById(R.id.btnroutemange);
        btnEmployeeManagement = findViewById(R.id.btnemployee);

        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutMonitoring = findViewById(R.id.layout_monitoring);
        layoutSetting = findViewById(R.id.layout_setting);
    }

    private void setupClickListeners() {
        // Bottom Navigation Bar
        layoutDashboard.setOnClickListener(v -> handleNavSelection(1));
        layoutMonitoring.setOnClickListener(v -> handleNavSelection(2));
        layoutSetting.setOnClickListener(v -> handleNavSelection(3));

        // Middle Route Button
        btnRouteManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, RouteManagement.class);
            startActivity(intent);
        });

        // Middle Employee Button (FIXED)
        btnEmployeeManagement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, EmployeeManagement.class);
            startActivity(intent);
        });
    }

    private void handleNavSelection(int index) {
        layoutDashboard.setSelected(index == 1);
        layoutMonitoring.setSelected(index == 2);
        layoutSetting.setSelected(index == 3);

        if (index == 3) {
            // This MUST point to EmployeeManagement (The List)
            Intent intent = new Intent(AdminDashboard.this, EmployeeManagement.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }
}
package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class MoreDetails extends AppCompatActivity {
    private TextView tvRouteCode, tvStatus, tvPlate, tvT1, tvT2, tvDept, tvDist, tvDriver, tvConductor, tvContact;
    private DatabaseReference dbRef;
    private String routeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        routeCode = getIntent().getStringExtra("ROUTE_CODE");
        if (routeCode == null) {
            finish();
            return;
        }

        initViews();
        
        // Use the specific Southeast Asia URL
        dbRef = FirebaseDatabase.getInstance("https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Routes").child(routeCode);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Route route = snapshot.getValue(Route.class);
                if (route != null) {
                    tvRouteCode.setText(route.getRouteCode());
                    tvStatus.setText("Status: " + route.getStatus());
                    tvPlate.setText("Plate Number: " + (route.getPlateNumber() != null && !route.getPlateNumber().isEmpty() ? route.getPlateNumber() : "None"));
                    tvT1.setText("Terminal 1 (Starting Point): " + route.getTerminalStart());
                    tvT2.setText("Terminal 2 (Ending Point): " + route.getTerminalEnd());
                    tvDept.setText("Assigned Departure Time: " + (route.getDepartureTime() != null && !route.getDepartureTime().isEmpty() ? route.getDepartureTime() : "Not Set"));
                    tvDist.setText("Distance: " + route.getDistance());
                    tvDriver.setText("Assigned Driver: " + (route.getAssignedDriver() != null && !route.getAssignedDriver().isEmpty() ? route.getAssignedDriver() : "Not Assigned"));
                    tvConductor.setText("Assigned Conductor: " + (route.getAssignedConductor() != null && !route.getAssignedConductor().isEmpty() ? route.getAssignedConductor() : "Not Assigned"));
                    tvContact.setText("Contact Info: " + (route.getContactInfo() != null && !route.getContactInfo().isEmpty() ? route.getContactInfo() : "None"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        findViewById(R.id.btnEditDetail).setOnClickListener(v -> {
            Intent intent = new Intent(MoreDetails.this, EditRoute.class);
            intent.putExtra("ROUTE_CODE", routeCode);
            startActivity(intent);
        });
    }

    private void initViews() {
        tvRouteCode = findViewById(R.id.tvDetailRouteCode);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvPlate = findViewById(R.id.tvDetailPlate);
        tvT1 = findViewById(R.id.tvDetailTerminal1);
        tvT2 = findViewById(R.id.tvDetailTerminal2);
        tvDept = findViewById(R.id.tvDetailDeparture);
        tvDist = findViewById(R.id.tvDetailDistance);
        tvDriver = findViewById(R.id.tvDetailDriver);
        tvConductor = findViewById(R.id.tvDetailConductor);
        tvContact = findViewById(R.id.tvDetailContact);
    }
}

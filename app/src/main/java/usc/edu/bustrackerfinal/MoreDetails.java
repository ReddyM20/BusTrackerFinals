package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoreDetails extends AppCompatActivity {

    private TextView tvRouteCode, tvStatus, tvPlate, tvT1, tvT2, tvDept, tvDist, tvDriver, tvConductor, tvContact;
    private DatabaseReference dbRef;
    private String plateNumber; // Or RouteCode depending on your Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        // 1. Get the ID from Intent FIRST
        plateNumber = getIntent().getStringExtra("PLATE_NUMBER");

        // 2. Check if it's null to avoid crashes
        if (plateNumber == null) {
            Toast.makeText(this, "Error: Trip data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();

        // 3. Now initialize dbRef using the retrieved plateNumber
        dbRef = FirebaseDatabase.getInstance("https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("AssignedTrips").child(plateNumber);

        loadData();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
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

    private void loadData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Route route = snapshot.getValue(Route.class);
                if (route != null) {
                    tvRouteCode.setText(route.getRouteCode());
                    tvStatus.setText("Status: " + route.getStatus());
                    tvPlate.setText("Plate Number: " + route.getPlateNumber());
                    tvT1.setText("Terminal 1 (Starting Point): " + route.getTerminalStart());
                    tvT2.setText("Terminal 2 (Ending Point): " + route.getTerminalEnd());
                    tvDept.setText("Assigned Departure Time: " + route.getDepartureTime());
                    tvDist.setText("Distance: " + route.getDistance());
                    tvDriver.setText("Assigned Driver: " + route.getAssignedDriver());
                    tvConductor.setText("Assigned Conductor: " + route.getAssignedConductor());
                    tvContact.setText("Contact Info: " + route.getContactInfo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
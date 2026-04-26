package usc.edu.bustrackerfinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TripViewActivity extends AppCompatActivity {

    private TextView tvStartPoint, tvEndPoint, tvDistance;
    private Button btnEndTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_view);

        tvStartPoint = findViewById(R.id.tvStartPoint);
        tvEndPoint = findViewById(R.id.tvEndPoint);
        tvDistance = findViewById(R.id.tvDistance);
        btnEndTrip = findViewById(R.id.btnEndTrip);

        btnEndTrip.setOnClickListener(v -> finish());
    }
}

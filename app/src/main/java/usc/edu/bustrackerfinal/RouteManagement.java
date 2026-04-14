package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RouteManagement extends AppCompatActivity {

    private RecyclerView rvRouteList;
    private RouteAdapter adapter;
    private List<Route> routeList;
    private DatabaseReference dbRef;
    private LinearLayout layoutDashboard, layoutAddRoute, layoutAssignedDriver, layoutBack;

    // Your specific Southeast Asia URL
    private final String DB_URL = "https://finalsprojectbus-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_management);

        initViews();
        setupRecyclerView();
        setupSearchLogic();
        fetchRoutesFromFirebase();
        setupNavClickListeners();
    }

    private void initViews() {
        rvRouteList = findViewById(R.id.rvRouteList);
        
        // Fix: Use the correct IDs from route_navbottons.xml
        layoutDashboard = findViewById(R.id.layout_dashboard);
        layoutAddRoute = findViewById(R.id.layout_addroute);
        layoutAssignedDriver = findViewById(R.id.layout_assigneddriver);
        layoutBack = findViewById(R.id.layout_back);

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Routes");
    }

    private void setupRecyclerView() {
        routeList = new ArrayList<>();
        adapter = new RouteAdapter(routeList);
        rvRouteList.setLayoutManager(new LinearLayoutManager(this));
        rvRouteList.setAdapter(adapter);
    }

    private void fetchRoutesFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                routeList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Route route = data.getValue(Route.class);
                    if (route != null) {
                        routeList.add(route);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void setupSearchLogic() {
        SearchView searchView = findViewById(R.id.searchRoute);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) { return false; }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterRoutes(newText);
                    return true;
                }
            });
        }
    }

    private void filterRoutes(String text) {
        List<Route> filteredList = new ArrayList<>();
        for (Route route : routeList) {
            String routeCode = route.getRouteCode() != null ? route.getRouteCode().toLowerCase() : "";
            String terminalStart = route.getTerminalStart() != null ? route.getTerminalStart().toLowerCase() : "";
            
            if (routeCode.contains(text.toLowerCase()) || terminalStart.contains(text.toLowerCase())) {
                filteredList.add(route);
            }
        }
        adapter.setFilteredList(filteredList);
    }

    private void setupNavClickListeners() {
        if (layoutAddRoute != null) {
            layoutAddRoute.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddingNewRoute.class);
                startActivity(intent);
            });
        }

        if (layoutAssignedDriver != null) {
            layoutAssignedDriver.setOnClickListener(v -> {
                Intent intent = new Intent(this, AssigningDriver.class);
                startActivity(intent);
            });
        }

        if (layoutBack != null) {
            layoutBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminDashboard.class);
                startActivity(intent);
            });
        }
    }
}

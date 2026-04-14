package usc.edu.bustrackerfinal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routeList;

    public RouteAdapter(List<Route> routeList) {
        this.routeList = routeList;
    }

    // Method to update the list during searching/filtering
    public void setFilteredList(List<Route> filteredList) {
        this.routeList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the card layout you created for the routes
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routeList.get(position);

        // Binding the Firebase data to your TextViews
        holder.tvCode.setText("Route Code: " + route.getRouteCode());
        holder.tvStart.setText("Starting Point: " + route.getTerminalStart());
        holder.tvEnd.setText("End Point: " + route.getTerminalEnd());
        holder.tvDist.setText("Distance: " + route.getDistance());
        holder.tvStatus.setText("Status: " + route.getStatus());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MoreDetails.class);

            // CHANGE THIS: Send the Plate Number since MoreDetails is looking for it
            intent.putExtra("PLATE_NUMBER", route.getPlateNumber());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return routeList != null ? routeList.size() : 0;
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvStart, tvEnd, tvDist, tvStatus;
        Button btnEdit;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            // These IDs must match exactly what you put in item_route.xml
            tvCode = itemView.findViewById(R.id.tvRouteCode);
            tvStart = itemView.findViewById(R.id.tvStartingPoint);
            tvEnd = itemView.findViewById(R.id.tvEndPoint);
            tvDist = itemView.findViewById(R.id.tvDistance);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEditRoute);
        }
    }
}
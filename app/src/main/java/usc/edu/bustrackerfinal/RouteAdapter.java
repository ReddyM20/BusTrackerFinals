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

    public RouteAdapter(List<Route> routeList) { this.routeList = routeList; }

    public void setFilteredList(List<Route> filteredList) {
        this.routeList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.tvCode.setText("Route Code: " + route.getRouteCode());
        holder.tvStart.setText("Starting Point: " + route.getTerminalStart());
        holder.tvEnd.setText("End Point: " + route.getTerminalEnd());

        holder.btnMoreDetails.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MoreDetails.class);
            intent.putExtra("ROUTE_CODE", route.getRouteCode());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return routeList.size(); }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvStart, tvEnd;
        Button btnMoreDetails;
        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvRouteCode);
            tvStart = itemView.findViewById(R.id.tvStartingPoint);
            tvEnd = itemView.findViewById(R.id.tvEndPoint);
            btnMoreDetails = itemView.findViewById(R.id.btnmoredetails);
        }
    }
}
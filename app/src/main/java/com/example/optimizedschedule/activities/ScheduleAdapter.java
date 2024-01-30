package com.example.optimizedschedule.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.optimizedschedule.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ScheduleItem> scheduleItems;
    private Context context;

    public ScheduleAdapter(List<ScheduleItem> scheduleItems, Context context) {
        this.scheduleItems = scheduleItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleItem item = scheduleItems.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemTimeSlot.setText(item.getTimeSlot());
        holder.itemTimeTaken.setText(item.getItemTimeTaken());

        // Optionally, adjust the layout or appearance based on item type
        switch (item.getType()) {
            case TASK:
                // Customize for task
                break;
            case BREAK:
                // Customize for break
                break;
        }
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemTimeSlot, itemTimeTaken;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemTimeSlot = itemView.findViewById(R.id.itemTimeSlot);
            itemTimeTaken = itemView.findViewById(R.id.ItemTimeTaken);
        }
    }
}

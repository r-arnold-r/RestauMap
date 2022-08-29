package com.example.RestauMap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.RestauMap.Model.detail.DetailsData;
import com.example.RestauMap.R;
import java.util.List;

public class DetailsDataAdapter extends RecyclerView.Adapter<DetailsDataAdapter.DataViewHolder> {

    private List<DetailsData> detailsDataList;
    private Context context;
    private ItemClickListener mItemClickListener;


    public DetailsDataAdapter(List<DetailsData> detailsDataList, Context context, ItemClickListener mItemClickListener) {
        this.detailsDataList = detailsDataList;
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public DetailsDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_menu, viewGroup, false);
        return new DetailsDataAdapter.DataViewHolder(itemView, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        int img_temp = Integer.parseInt(detailsDataList.get(position).getImg());
        holder.restaurantIcon.setImageResource(img_temp);
        holder.restaurantName.setText(detailsDataList.get(position).getName());
        holder.restaurantRating.setText(detailsDataList.get(position).getRating());
        holder.restaurantDescription.setText(detailsDataList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if(detailsDataList != null) {
            return detailsDataList.size();
        }
        return 0;
    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        ImageView restaurantIcon;
        TextView restaurantName, restaurantRating, restaurantDescription;
        ItemClickListener mItemClickListener;

        public DataViewHolder(View view, ItemClickListener itemClickListener){
            super(view);
            restaurantIcon = view.findViewById(R.id.restaurant_icon);
            restaurantName = view.findViewById(R.id.restaurant_name);
            restaurantRating = view.findViewById(R.id.restaurant_rating);
            restaurantDescription = view.findViewById(R.id.restaurant_description);
            mItemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(int position);
    }
}

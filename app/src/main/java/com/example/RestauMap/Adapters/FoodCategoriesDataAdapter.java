package com.example.RestauMap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RestauMap.Model.category.FoodCategoriesData;
import com.example.RestauMap.R;

import java.util.List;

public class FoodCategoriesDataAdapter extends RecyclerView.Adapter<FoodCategoriesDataAdapter.DataViewHolder>{
    private List<FoodCategoriesData> foodCategoriesDataList;
    private Context context;
    private ItemClickListener mItemClickListener;


    public FoodCategoriesDataAdapter(List<FoodCategoriesData> foodCategoriesDataList, Context context, FoodCategoriesDataAdapter.ItemClickListener mItemClickListener) {
        this.foodCategoriesDataList = foodCategoriesDataList;
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public FoodCategoriesDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_categories_menu, viewGroup, false);
        return new FoodCategoriesDataAdapter.DataViewHolder(itemView, mItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodCategoriesDataAdapter.DataViewHolder holder, int position) {
        holder.categoryImage.setBackgroundResource(foodCategoriesDataList.get(position).getImg());
        holder.categoryName.setText(foodCategoriesDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return foodCategoriesDataList.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        ImageView categoryImage;
        TextView categoryName;
        FoodCategoriesDataAdapter.ItemClickListener mItemClickListener;

        public DataViewHolder(View view, FoodCategoriesDataAdapter.ItemClickListener itemClickListener){
            super(view);
            categoryImage = view.findViewById(R.id.category_image);
            categoryName = view.findViewById(R.id.category_name);
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

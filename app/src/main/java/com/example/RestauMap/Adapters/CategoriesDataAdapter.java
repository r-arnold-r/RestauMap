package com.example.RestauMap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.RestauMap.Model.category.CategoriesData;
import com.example.RestauMap.R;
import java.util.List;

public class CategoriesDataAdapter extends RecyclerView.Adapter<CategoriesDataAdapter.DataViewHolder> {

    private List<CategoriesData> categoriesDataList;
    private Context context;
    private ItemClickListener mItemClickListener;

    public CategoriesDataAdapter(List<CategoriesData> categoriesDataList, Context context, ItemClickListener mItemClickListener) {
        this.categoriesDataList = categoriesDataList;
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public CategoriesDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_menu, viewGroup, false);
        return new CategoriesDataAdapter.DataViewHolder(itemView, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.categoryImage.setBackgroundResource(categoriesDataList.get(position).getImg());
        holder.categoryName.setText(categoriesDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categoriesDataList.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        ImageView categoryImage;
        TextView categoryName;
        ItemClickListener mItemClickListener;

        public DataViewHolder(View view, ItemClickListener itemClickListener){
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

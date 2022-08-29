package com.example.RestauMap.Fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.RestauMap.Adapters.FoodCategoriesDataAdapter;
import com.example.RestauMap.Model.category.FoodCategoriesData;
import com.example.RestauMap.R;
import java.util.ArrayList;
import java.util.List;

public class RestaurantFoodCategoriesFragment extends Fragment implements FoodCategoriesDataAdapter.ItemClickListener {

    //adapter + recycler
    private RecyclerView recyclerView = null;
    private FoodCategoriesDataAdapter foodCategoriesDataAdapter = null;
    private Toast toast = null;

    //layout
    private View view = null;
    private TextView foodType_tv = null;
    private ImageView back_button = null;

    //listst
    private List<FoodCategoriesData> foodCategoriesDataList = new ArrayList<>();

    //food type
    private String foodType;

    /**
     * Constructor
     */
    RestaurantFoodCategoriesFragment(List<FoodCategoriesData> foodCategoriesDataList, String foodType){
        this.foodCategoriesDataList = foodCategoriesDataList;
        this.foodType = foodType;
    }

    /**
     * On create
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //lock Portrait view
    }

    /**
     * On create view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant_food_categories, container, false);

        //layout initializer
        foodType_tv = view.findViewById(R.id.categories_restaurant_food_name);
        recyclerView = view.findViewById(R.id.recycler_view_categories);
        back_button = view.findViewById(R.id.back_button_restaurant_food_categories);

        //setting food type text
        foodType_tv.setText(foodType);

        //exit image
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
                        getActivity().getSupportFragmentManager().popBackStack("RestaurantFoodCategoriesFragment", 1);
                    } else {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .remove(RestaurantFoodCategoriesFragment.this).commit();
                    }
                }
                onStop();
                return false;
            }
        });

        //creating and setting up adapter with recyclerView
        foodCategoriesDataAdapter = new FoodCategoriesDataAdapter(foodCategoriesDataList, getActivity(), this); //setting the data and listener for adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(foodCategoriesDataAdapter);
        foodCategoriesDataAdapter.notifyDataSetChanged();

        return view;
    }

    /**
     * Called when item of adapter is clicked
     */
    @Override
    public void onItemClick(int position) {
        String name = foodCategoriesDataList.get(position).getName();

        Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
    }

    /**
     * Hide SupportActionBar when fragment is opened
     */
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        foodCategoriesDataList.clear();
    }
}
package com.example.RestauMap.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RestauMap.Activities.LoginActivity;
import com.example.RestauMap.Adapters.CategoriesDataAdapter;
import com.example.RestauMap.Activities.MapsActivity;
import com.example.RestauMap.Model.category.CategoriesData;
import com.example.RestauMap.Model.category.FoodCategoriesData;
import com.example.RestauMap.R;
import java.util.ArrayList;
import java.util.List;

public class RestaurantCategoriesFragment extends Fragment implements CategoriesDataAdapter.ItemClickListener{

    //layout
    private View view = null;
    private LinearLayout editSearchBox = null;
    private ImageView back_button, restaurant_location_image = null;
    private TextView restaurant_name_tv = null;

    //toast
    private Toast toast = null;

    //adapter + recyclerview
    private RecyclerView recyclerView = null;
    private CategoriesDataAdapter categoriesDataAdapter = null;

    //restaurant info
    private String restaurant_name;
    private String description;

    //Lists
    private List<CategoriesData> categoriesDataList = new ArrayList<>();
    private List<FoodCategoriesData> foodCategoriesDataList = new ArrayList<>();

    //maps activity
    public MapsActivity mapsActivity = null;

    //categories data list
    String[] pizzaList = {"Pizza","Peperoni", "Margharita", "Capricioasa"};
    String[] fishList = {"Fish","Calamari", "Shrimps", "Seafood Stew"};
    String[] crispyList = {"Crispy","Small", "Medium", "Big"};
    String[] saladList = {"Salad","Savannah Chopped Salad", "Garden Salad", "Caesar Salad"};
    String[][] foodTypeList = {pizzaList, fishList, crispyList, saladList};

    /**
     * constructor
     */
    public RestaurantCategoriesFragment(List<CategoriesData> categoriesDataList, String restaurant_name, String description){
        this.categoriesDataList = categoriesDataList;
        this.restaurant_name = restaurant_name;
        this.description = description;
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
        view = inflater.inflate(R.layout.fragment_restaurant_categories, container, false);

        //layout initializer
        editSearchBox = getActivity().findViewById(R.id.search_box_l_map);
        back_button = view.findViewById(R.id.back_button_restaurant_categories);
        restaurant_name_tv =  view.findViewById(R.id.categories_restaurant_name);
        recyclerView = view.findViewById(R.id.recycler_view_categories);
        restaurant_location_image = view.findViewById(R.id.location_restaurant_categories);

        //setting info
        restaurant_name_tv.setText(restaurant_name);

        //getting reference to MapsActivity
        mapsActivity = (MapsActivity) getActivity();

        //exit image
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {

                        for (int i = 0; i < getActivity().getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                            if ((getActivity().getSupportFragmentManager().getBackStackEntryAt(i).getName()).equals("RestaurantCategoriesFragment")) {
                                getActivity().getSupportFragmentManager().popBackStack("RestaurantCategoriesFragment", 1);
                            }
                            if ((getActivity().getSupportFragmentManager().getBackStackEntryAt(i).getName()).equals("categoriesDataList")) {
                                getActivity().getSupportFragmentManager().popBackStack("categoriesDataList", 1);
                                editSearchBox.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                onStop();
                return false;
            }
        });

        //creating and setting up adapter with recyclerView
        categoriesDataAdapter = new CategoriesDataAdapter(categoriesDataList, getActivity(), this); //setting the data and listener for adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoriesDataAdapter);
        categoriesDataAdapter.notifyDataSetChanged();

        //info button
        ImageView info_button = view.findViewById(R.id.info_restaurant_categories);
        info_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage(description);
                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    }).create();
                    builder.show();
                }
                return false;
            }
        });

        //restaurant location image
        restaurant_location_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mapsActivity.goToMarkerFromFragment(restaurant_name);
                }
                return false;
            }
        });

        return view;
    }

    /**
     * Called when item of adapter is clicked
     */
    @Override
    public void onItemClick(int position) {
        String name = categoriesDataList.get(position).getName();
        for(int i = 0; i < foodTypeList.length; ++i){
            if(foodTypeList[i][0].equals(name)){
                for(int j = 1 ; j < foodTypeList[i].length; ++j) {

                    //adjust text
                    String name_temp = foodTypeList[i][j].replaceAll("\\s+","_");
                    int img_temp = getResources().getIdentifier(name_temp.toLowerCase(), "drawable", getActivity().getPackageName());

                    //add data
                    FoodCategoriesData foodCategoriesData = new FoodCategoriesData(String.valueOf(j), foodTypeList[i][j], img_temp);
                    foodCategoriesDataList.add(foodCategoriesData);
                }
            }
        }

        //go to next fragment
        RestaurantFoodCategoriesFragment nextFrag= new RestaurantFoodCategoriesFragment(foodCategoriesDataList, name);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace( ((ViewGroup)getView().getParent()).getId(), nextFrag, "findThisFragment")
                .addToBackStack("RestaurantFoodCategoriesFragment")
                .commit();
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

    }


}
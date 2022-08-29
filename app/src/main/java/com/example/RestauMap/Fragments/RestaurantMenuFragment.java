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
import android.widget.LinearLayout;
import android.widget.SearchView;
import com.example.RestauMap.Model.category.CategoriesData;
import com.example.RestauMap.Model.detail.DetailsData;
import com.example.RestauMap.Adapters.DetailsDataAdapter;
import com.example.RestauMap.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantMenuFragment extends Fragment implements DetailsDataAdapter.ItemClickListener {

    //standard udid and appid for server
    private String udid = "12345";
    private String appid = "6";

    //layout
    private LinearLayout editSearchBox = null;
    private SearchView search_box = null;

    //adapter + recycler
    private RecyclerView recyclerView = null;
    private DetailsDataAdapter detailsDataAdapter = null;

    //lists
    private List<DetailsData> detailsDataList =  new ArrayList<>();
    private List<DetailsData> items = new ArrayList<>();
    private List<CategoriesData> categoriesDataList = new ArrayList<>();


    /**
     * constructor
     */
    public RestaurantMenuFragment(List<DetailsData> detailsDataList, List<CategoriesData> categoriesDataList) {
        this.detailsDataList = detailsDataList;
        this.categoriesDataList = categoriesDataList;
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
        View view = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);

        //layout initializer
        editSearchBox = getActivity().findViewById(R.id.search_box_l_map);
        search_box = view.findViewById(R.id.search_box_menu);

        //list copy
        Collections.copy(detailsDataList, items);
        items.addAll(detailsDataList);

        //exit image
        ImageView back_button = view.findViewById(R.id.back_button_restaurant_menu);
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        getActivity().getSupportFragmentManager().popBackStack("RestaurantMenuFragment", 1);
                    } else {
                        for(int i = 0; i < getActivity().getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                    onStop();
                    editSearchBox.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        //creating and setting up adapter with recyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        detailsDataAdapter = new DetailsDataAdapter(items, getActivity(), this); //setting the data and listener for adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(detailsDataAdapter);
        detailsDataAdapter.notifyDataSetChanged();


        //search_box
        search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return view;
    }

    /**
     * filter DataList for Searchview
     */
    public void filter( String text) {
        items.clear();
        if(text.isEmpty()){
            items.addAll(detailsDataList);
        } else{
            for(DetailsData item: detailsDataList){
                if(item.getName().toLowerCase().contains(text.toLowerCase())){
                    items.add(item);
                }
            }
        }
        detailsDataAdapter.notifyDataSetChanged();
    }

    /**
     * Called when item of adapter is clicked
     */
    @Override
    public void onItemClick(int position) {
        final String name = detailsDataList.get(position).getName();
        final String description = detailsDataList.get(position).getDescription();
        RestaurantCategoriesFragment nextFrag= new RestaurantCategoriesFragment(categoriesDataList, name, description);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace( ((ViewGroup)getView().getParent()).getId(), nextFrag, "findThisFragment")
                .addToBackStack("RestaurantCategoriesFragment")
                .commit();
    }

    /**
     * Hide SupportActionBar when fragment is opened
     */
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        search_box.setQuery("", false);
        search_box.clearFocus();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        search_box.setQuery("", false);
        search_box.clearFocus();
    }


}
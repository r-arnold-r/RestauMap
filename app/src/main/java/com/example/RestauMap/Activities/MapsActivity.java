package com.example.RestauMap.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.RestauMap.Fragments.ProfileFragment;
import com.example.RestauMap.Fragments.RestaurantCategoriesFragment;
import com.example.RestauMap.Fragments.RestaurantMenuFragment;
import com.example.RestauMap.Model.category.CategoriesData;
import com.example.RestauMap.Model.detail.DetailsData;
import com.example.RestauMap.Model.detail.RestaurantDetailPOST;
import com.example.RestauMap.Model.listing.ListingData;
import com.example.RestauMap.Model.listing.RestaurantPOST;
import com.example.RestauMap.Model.login.User;
import com.example.RestauMap.R;
import com.example.RestauMap.Retrofit.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, RoutingListener {

    //standard udid and appid for server
    private String udid = "12345";
    private String appid = "6";

    //google map and location
    private GoogleMap mMap = null;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LocationRequest locationRequest = null;

    //Tag of activity
    private static final String TAG = "MapsActivity";

    //buttons/layouts
    private Button goToBtn, goToRouteBtn = null;
    private int buttonClickedRoute = -1;   // -1 = not clicked, 1 = clicked
    private int buttonClicked = -1;
    private LinearLayout searchBoxL = null;
    private LinearLayout goToLayout = null;
    private AutoCompleteTextView editSearchBox = null;
    private Button searchBoxButton = null;

    //toolbar
    private Toolbar toolbar = null;

    //user markers/user circle
    private Marker userLocationMarker = null;
    private Circle userLocationAccuracyCircle = null;

    //markers
    private Marker tempMarker = null;
    private Marker markerClicked = null;

    //list of restaurant markers from server
    private int rdata_size;

    //drawer
    private DrawerLayout drawer = null;
    private NavigationView navigationView = null;

    //user details
    private User u = null;

    //details
    private String id, name, description, img, rating, latitude, longitude;
    private List<DetailsData> detailsDataList = new ArrayList<>();

    ///Restaurant categories
    private String[] idList = {"1", "2", "3", "4"};
    private String[] nameList = {"Pizza", "Fish", "Crispy", "Salad"};
    private String[] imgList = {"pizza", "fish", "crispy", "salad"};
    private List<CategoriesData> categoriesDataList = new ArrayList<>();

    //Lists
    private List<String> RESTAURANTS = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();

    //category variables
    private String id_category, name_category;
    private int img_category;

    //current and destination location objects
    protected LatLng start=null;
    protected LatLng end=null;

    //polyline object
    Polyline polyline = null;

    /**
     * Called when map is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //OnCreate/layout create
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //lock Portrait view

        //check if gps is enabled
        statusCheck();

        //LocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //locationrequest create/intervals/priorities
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //layout initializer
        editSearchBox = findViewById(R.id.search_box_map);
        searchBoxButton =(Button) findViewById(R.id.search_box_button_map);
        searchBoxL = (LinearLayout)findViewById(R.id.search_box_l_map);
        editSearchBox = (AutoCompleteTextView)findViewById(R.id.search_box_map);
        goToLayout = (LinearLayout) findViewById(R.id.go_to_l_map);
        goToRouteBtn =(Button) findViewById(R.id.go_to_follow_map);
        goToBtn = (Button) findViewById(R.id.go_to_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //adapters
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, RESTAURANTS);
        editSearchBox.setAdapter(adapter);

        //toolbar
        setSupportActionBar(toolbar);
        if (!toolbar.isShown()){
            ((AppCompatActivity)this).getSupportActionBar().show();
        }

        //drawer
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                for(int i = 0; i < navigationView.getMenu().size(); ++i)
                    navigationView.getMenu().getItem(i).setChecked(false);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Search box button
        searchBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editSearchBox.getText().toString())){
                    //proceed
                    for(int i = 0; i < markerList.size(); ++i){
                        if(editSearchBox.getText().toString().equals(markerList.get(i).getTitle())){
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(i).getPosition(), 17));
                            markerList.get(i).showInfoWindow();
                            goToLayout.setVisibility(View.VISIBLE);
                            markerClicked = markerList.get(i);
                            editSearchBox.getText().clear();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                        }
                    }
                }
            }
        });

        //GoTo Button
        goToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (buttonClicked == -1) {
                        goToBtn.setText("Stop route");
                        //zoom so we can see the two selected markers
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(userLocationMarker.getPosition());
                        builder.include(markerClicked.getPosition());
                        LatLngBounds bounds = builder.build();

                        int padding = 250; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                        mMap.animateCamera(cu);

                        goToRouteBtn.setText("Follow me");
                        goToRouteBtn.setVisibility(View.VISIBLE);
                    } else {
                        //set data
                        goToBtn.setText("Make route");
                        goToRouteBtn.setVisibility(View.GONE);
                        buttonClickedRoute = -1;
                    }
                    buttonClicked = buttonClicked * (-1);
                }else{
                    buildAlertMessageNoGps();
                }
            }
        });

        //goToRoute Button
        goToRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if(buttonClickedRoute == 1){
                        goToRouteBtn.setText("Follow me");
                    }
                    else{
                        goToRouteBtn.setText("Stop following");
                    }
                    buttonClickedRoute = buttonClickedRoute * (-1);
                }
                else{
                    buildAlertMessageNoGps();
                }
            }
        });

        //communicating with server ListingAPI,MarkerPlacer
        Call<RestaurantPOST> restaurantResponseCall = ApiClient.getRestaurantService().userRestaurants(udid, appid, "restaurants", "");
        restaurantResponseCall.enqueue(new Callback<RestaurantPOST>() {
            @Override
            public void onResponse(Call<RestaurantPOST> call, Response<RestaurantPOST> response) {
                if (response.body() != null) {
                    List<ListingData> data = response.body().getListingResponse().getData();
                    //markerplacer
                    MarkerPlacer(data);
                    rdata_size = data.size();
                    //detailresponse
                    getDetailResponse();
                }
            }

            @Override
            public void onFailure(Call<RestaurantPOST> call, Throwable t) {

            }
        });

        //categoriesDataList
        for(int i = 0; i < idList.length; ++i) {
            id_category = idList[i];
            name_category = nameList[i];
            int img_temp = getResources().getIdentifier(imgList[i], "drawable", this.getPackageName());
            img_category = img_temp;
            CategoriesData categoriesData = new CategoriesData(id_category, name_category, img_category);
            categoriesDataList.add(categoriesData);
        }

        //user
        u = (User) getIntent().getSerializableExtra("user_detail");
    }

    /**
     * Check if gps is activated
     */
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Communicating with server detailsDataList, called in OnCreate
     */
    public void getDetailResponse(){
        for(int i = 1; i <= rdata_size; ++i) {
            Call<RestaurantDetailPOST> restaurantResponseCall = ApiClient.getRestaurantServiceDetails().userRestaurantDetails(udid, appid, "restaurants", i);
            restaurantResponseCall.enqueue(new Callback<RestaurantDetailPOST>() {
                @Override
                public void onResponse(Call<RestaurantDetailPOST> call, Response<RestaurantDetailPOST> response) {
                    if (response.body() != null) {
                        //Filling our recycler View list
                        id = response.body().getDetailsResponse().getDetailsData().getId();
                        name = response.body().getDetailsResponse().getDetailsData().getName();
                        description = response.body().getDetailsResponse().getDetailsData().getDescription();
                        img = response.body().getDetailsResponse().getDetailsData().getImg();
                        rating = response.body().getDetailsResponse().getDetailsData().getRating();
                        latitude = response.body().getDetailsResponse().getDetailsData().getLatitude();
                        longitude = response.body().getDetailsResponse().getDetailsData().getLongitude();

                        String name_temp = name.replaceAll("\\s+","_");
                        int img_temp = getResources().getIdentifier(name_temp.toLowerCase(), "drawable", getPackageName());

                        DetailsData detailsData = new DetailsData(id, name, description, String.valueOf(img_temp) , rating, latitude, longitude);
                        detailsDataList.add(detailsData);

                    }
                }
                @Override
                public void onFailure(Call<RestaurantDetailPOST> call, Throwable t) {

                }
            });
        }
    }

    /**
     * Placing markers from database
     */
    public void MarkerPlacer(List<ListingData> rdata) {

        for (ListingData d : rdata) {

            RESTAURANTS.add(d.getName());

            Marker temp;
            double lat = Double.parseDouble(d.getLatitude());
            double lng = Double.parseDouble(d.getLongitude());

            LatLng POSITION = new LatLng(lat, lng);

            temp = mMap.addMarker(new MarkerOptions()
                    .position(POSITION)
                    .title(d.getName())
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_restaurants)));
            temp.setTag(d.getId());

            markerList.add(temp);

        }
    }

    /**
     * Called when the map is ready.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }

        //loading simple map style
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.map_style));

        //Disable Map Toolbar:
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

    }

    /**
     * Called when the user clicks a marker.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
            goToLayout.setVisibility(View.VISIBLE);
        }

        //tempMarker to see if another marker is clicked
        if(tempMarker != null){
            if(!marker.equals(tempMarker)){
                //set data
                goToBtn.setText("Make route");
                goToRouteBtn.setVisibility(View.GONE);
                buttonClickedRoute = -1;
                buttonClicked = -1;
            }
        }

        tempMarker = marker;

        setMarkerClicked(marker);

        return false;
    }

    /**
     * Called on windows info click
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        String description = detailsDataList.get(Integer.parseInt(String.valueOf(marker.getTag())) - 1).getDescription();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RestaurantCategoriesFragment(categoriesDataList, marker.getTitle(), description))
                .addToBackStack("categoriesDataList")
                .commit();
        searchBoxL.setVisibility(View.GONE);
        goToLayout.setVisibility(View.GONE);
        marker.hideInfoWindow();
    }

    /**
     * Called when location changes
     */
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            //Log.d(TAG, "onLocationResult: " + locationResult.getLocations());
            if (mMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    /**
     * Called for location updates
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Called when asked for permissions
     */
    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }
    }

    /**
     * Called on requesting permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
            } else {
                //We can show a dialog that permission is not granted...
            }
        }
    }

    /**
     * Called on app start
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            askLocationPermission();
        }
    }

    /**
     * Called on app stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    /**
     * Called for user location marker, circle, UI, Route
     */
    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_compass));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());

            if(buttonClickedRoute == 1) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }

            if(markerClicked != null) {
                if(buttonClicked == 1) {
                    drawRoute(markerClicked);
                }
                else{
                    if(polyline != null) {
                        polyline.remove();
                        polyline = null;
                    }
                }

                if(!markerClicked.isInfoWindowShown() && buttonClicked != 1) {
                    goToLayout.setVisibility(View.GONE);
                }
            }
        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 176, 224, 230));
            circleOptions.fillColor(Color.argb(255, 176, 224, 230));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }

    /**
     * draw Route
     */
    private void drawRoute(Marker marker){
        Findroutes(userLocationMarker.getPosition(), marker.getPosition());
    }

    /**
     * Called on selecting item from navigation list
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_map:
                for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i){
                 getSupportFragmentManager().popBackStack();
                 searchBoxL.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.nav_profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment(u.getEmail(),u.getName()))
                        .addToBackStack("ProfileFragment")
                        .commit();
                searchBoxL.setVisibility(View.GONE);
                goToLayout.setVisibility(View.GONE);
                goToRouteBtn.setVisibility(View.GONE);
                break;
            case R.id.nav_restaurants:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new RestaurantMenuFragment(detailsDataList, categoriesDataList))
                        .addToBackStack("RestaurantMenuFragment")
                        .commit();
                searchBoxL.setVisibility(View.GONE);
                goToLayout.setVisibility(View.GONE);
                goToRouteBtn.setVisibility(View.GONE);
                break;
            case R.id.nav_logout:
                openLogin();
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Called from Restaurant Categories Fragment for location update and
     */
    public void goToMarkerFromFragment(String markerName){

        Marker marker = null;

        for(Marker item : markerList){
            if(item.getTitle().equals(markerName)){
                marker = item;
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i){
            getSupportFragmentManager().popBackStack();
        }

        searchBoxL.setVisibility(View.VISIBLE);
        goToLayout.setVisibility(View.VISIBLE);
        markerClicked = marker;
        marker.showInfoWindow();
    }

    /**
     * Toolbar control
     */
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen((GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Called for markers with vector images
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * open Login
     */
    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * setters
     */
    public void setMarkerClicked(Marker markerClicked){
        this.markerClicked = markerClicked;
    }

    /**
     * FindRoutes
     */
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            //Toast.makeText(MapsActivity.this ,"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyDRWIFgO7UvhLKgT9CDiRQau6aih6SVVd0")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        //Toast.makeText(MapsActivity.this,"Finding Route...",Toast.LENGTH_SHORT).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;

        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                if(polyline == null) {
                    polyline = mMap.addPolyline(polyOptions);
                }
                else{
                    polyline.setPoints(route.get(shortestRouteIndex).getPoints());
                }
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
            }
            else {

            }

        }
    }

    //on roating canceled
    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

}


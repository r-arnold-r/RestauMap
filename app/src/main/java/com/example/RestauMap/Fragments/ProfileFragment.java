package com.example.RestauMap.Fragments;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.RestauMap.R;

public class ProfileFragment extends Fragment {

    //info user
    private String email;
    private String name;

    //layout
    private LinearLayout editSearchBox = null;
    private ImageView profile_picture = null;
    private View view = null;
    private TextView tv_name, tv_email = null;

    /**
     * Constructor
     */
    public ProfileFragment(String email, String name) {
        this.email = email;
        this.name = name;
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
     * On view created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //layout initializer
        editSearchBox = getActivity().findViewById(R.id.search_box_l_map);
        tv_name = view.findViewById(R.id.tv_name_profile);
        tv_email = view.findViewById(R.id.tv_email_profile);
        profile_picture = view.findViewById(R.id.profile_picture);

        //Set user details for profile
        tv_name.setText(name);
        tv_email.setText(email);

        //exit image
        ImageView back_button = view.findViewById(R.id.back_button_profile);
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        getActivity().getSupportFragmentManager().popBackStack("ProfileFragment", 1);
                    } else {
                        for(int i = 0; i < getActivity().getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                    onStop();
                }
                return false;
            }
        });

        //Load image from url
        /*
        LoadImage loadImage = new LoadImage(profile_picture);
        loadImage.execute("http://mobcom.testplat4m.net//images//neymar.jpg");
         */

        return view;
    }

    /**
     * Load img from URL
     */
    /*
    private class LoadImage extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public LoadImage(ImageView ivResult){
            this.imageView = ivResult;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;
            try{
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            profile_picture.setImageBitmap(bitmap);
        }
    }
    */

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
        editSearchBox.setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
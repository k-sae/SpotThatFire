package com.example.kareem.spotthatfire;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.kareem.spotthatfire.Connection.Request;
import com.example.kareem.spotthatfire.Connection.ServerConnection;
import com.example.kareem.spotthatfire.Connection.VolleyRequest;
import com.example.kareem.spotthatfire.Model.WeatherData;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LocationTrackerFragment {

    private TextView tempTextView;
    private TextView windTextView;
    private TextView humidityTextView;
    private TextView rainTextView;
    private Location mLastKnownLocation;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setUi(view);
        return view;
    }
    private void setUi(View view)
    {
         tempTextView = view.findViewById(R.id.temp_textView);
         windTextView = view.findViewById(R.id.wind_speed_textView);
         humidityTextView = view.findViewById(R.id.humidity_textView);
         rainTextView = view.findViewById(R.id.rain_textView);

    }
    private void updateData()
    {
        if (tempTextView == null) return;
        // check for lat
        if (mLastKnownLocation == null)
        {
            Toast.makeText(getActivity(), "Waiting for location", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String > params = new HashMap<>();
        params.put("lat", mLastKnownLocation.getLatitude() + "" );
        params.put("lon", mLastKnownLocation.getLongitude() + "" );
        params.put("APPID", Config.API_KEY);
        VolleyRequest volleyRequest = new VolleyRequest(Consts.BASE_URL
                + "?lat=" + mLastKnownLocation.getLatitude()
                + "&lon=" + mLastKnownLocation.getLongitude()
                + "&APPID=" + Config.API_KEY
                , params, getActivity()) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {
                Log.e("HomeFragment", "onResponse: " + response );
                WeatherData weatherData = new Gson().fromJson(response, WeatherData.class);
                tempTextView.setText(String.valueOf(weatherData.getMain().getTemp()));
                windTextView.setText(String.valueOf(weatherData.getWind().getSpeed()));
                humidityTextView.setText(String.valueOf(weatherData.getMain().getHumidity()));
                rainTextView.setText(String.valueOf(weatherData.getRain().get3h()));
//                ServerConnection.getInstance().sendRequest(new Request(Consts.UPLOAD_DATA, response));
            }

        };
        volleyRequest.start();
    }

    @Override
    public void onLocationChange(Location location) {
        if (mLastKnownLocation == null)
        {
//            ServerConnection.getInstance().sendRequest(new Request());
            mLastKnownLocation = location;
            updateData();
        }
        else mLastKnownLocation = location;
    }
}

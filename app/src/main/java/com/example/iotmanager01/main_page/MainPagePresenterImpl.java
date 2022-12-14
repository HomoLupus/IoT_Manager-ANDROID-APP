package com.example.iotmanager01.main_page;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.iotmanager01.api.TokenRepository;
import com.example.iotmanager01.api.RestClient;
import com.example.iotmanager01.api.model.InfoResponse;
import com.example.iotmanager01.api.model.Sensor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPagePresenterImpl implements MainPagePresenter, Callback<InfoResponse> {
    private static final String TAG = "TAGOWE";
    MainPageView mainPageView;
    TokenRepository token;
    RestClient restClient;
    Context context;
    List<Sensor> listOfSensors;


    MainPagePresenterImpl(MainPageView view, Context context) {
        mainPageView = view;
        this.context = context;
        token = TokenRepository.getInstance(context);

        restClient = new RestClient();

        callRestApi();


    }

    void callRestApi() {
        restClient.start();
        restClient.callGetUserInfo(token.getToken(), this);
    }


    @Override
    public void logout() {
        token.clear();
        mainPageView.openLoginView();
    }

    @Override
    public void openSettings() {

        mainPageView.openSettingsView(listOfSensors);
    }

    @Override
    public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
        if (response.isSuccessful()) {
            //Log.d(TAG, "S onResponse: " + response.body().sensors.get(0).print());
            Log.d(TAG, "S onResponse: " + response.body().sensors.get(0).getSensorId());
            Log.d(TAG, "S onResponse: " + token.getToken());


            listOfSensors = response.body().sensors;
            mainPageView.fillRecyclerView(listOfSensors);


        } else {
            Log.d(TAG, "onResponse: 10sec to recurrence");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    callRestApi();
                }
            }, 10000);

        }
    }

    @Override
    public void onFailure(Call<InfoResponse> call, Throwable t) {
        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
        Log.d(TAG, "onFailure: " + token.getToken());
    }


    public void openMainPange(int position) {
        mainPageView.openGraphView(listOfSensors.get(position).getSensorName(),listOfSensors.get(position).getSensorId());
    }
}

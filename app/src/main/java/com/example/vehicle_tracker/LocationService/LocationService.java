package com.example.vehicle_tracker.LocationService;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import com.example.vehicle_tracker.retrofit.PositionEntries;
import com.example.vehicle_tracker.retrofit.VehicleTrackerApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.os.Looper.getMainLooper;
public class LocationService {
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "http://192.168.0.106:8080";
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static LocationRequest locationRequest;
    public static void updatePositionEntries(int id, String lat, String lng, int vehicle_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VehicleTrackerApi vh = retrofit.create(VehicleTrackerApi.class);
        Call<PositionEntries> putPositionEntriesCall = vh.putPosition(id, lat, lng, vehicle_id, String.valueOf(Math.random()));
        putPositionEntriesCall.enqueue(new Callback<PositionEntries>() {
            @Override
            public void onResponse(Call<PositionEntries> call, Response<PositionEntries> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Error Sending put call: " + response.message());
                    return;
                }
                PositionEntries p = response.body();
                if (p != null) {
                    String content = "";
                    content = content + "ID: " + p.getId() + "\n";
                    content = content + "Lat: " + p.getLat() + "\n";
                    content = content + "Lng: " + p.getLng() + "\n";
                    content = content + "Position:  " + p.getPosition() + "\n\n";
                    Log.e(TAG, "Service sending data successfully: ");
                    Log.e("Data sent: ", content);
                }
            }
            @Override
            public void onFailure(Call<PositionEntries> call, Throwable t) {
            }
        });
    }
    public void requestLocationUpdates(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PermissionChecker.PERMISSION_GRANTED) {
            fusedLocationProviderClient = new FusedLocationProviderClient(context);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(4000);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    String latitude = locationResult.getLastLocation().getLatitude()+"";
                    String longitude = locationResult.getLastLocation().getLongitude()+"";
                    updatePositionEntries(31, latitude, longitude, 7);
                    Log.e(TAG,"lat: "+latitude+ " lng: "+longitude);
                }
            }, getMainLooper());
        }else callPermissions(context);
    }
    public void callPermissions(final Context context){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(context/*context*/, permissions, null/*rationale*/,
                null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        requestLocationUpdates(context);
                    }
                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);
                        callPermissions(context);
                    }
                });
    }
}
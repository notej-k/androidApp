package com.example.vehicle_tracker.LocationService;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.vehicle_tracker.vehicleTracker.MainActivity;

public class MyPeriodicWork extends Worker {
    private static final String TAG = "MyPeriodicWork";
    public MyPeriodicWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        LocationService ls = new LocationService();
        ls.callPermissions(MainActivity.getMainActivityContext());
        Log.e(TAG, "doWork: Work is done.");
        return Result.success();
    }
}

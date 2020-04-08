package com.example.vehicle_tracker.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VehicleTrackerApi {

    @FormUrlEncoded
    @PUT("api/positionEntries/{id}")
    Call<PositionEntries> putPosition(
            @Path("id")
                    int id,
            @Field("lat")
                    String lat,
            @Field("lng")
                    String lng,
            @Field("vehicle_id")
                    int vehicle_id,
            @Field("position")
                    String position);
}

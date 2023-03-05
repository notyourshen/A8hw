package edu.northeastern.team31project.ChatAndNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebAPI {
    @Headers(
            {       "Content-Type:application/json",
                    "Authorization:key=AAAA4h7-Q-w:APA91bGsJ2ew9_2iz77uOIfTKvOdYkOvN3RMVikF4FwkuZ9A_PkMACjvVlYgpPc4Xt4PCRaYXeyf2Cv_agIDmbcxu1Y2YL4GB2l0rZoKkfUiYDLaUepyWCy57wBcW1I_pUj_LBqEtY5Y"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendAndNotify(@Body Send body);
}

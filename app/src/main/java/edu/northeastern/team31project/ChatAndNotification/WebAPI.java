package edu.northeastern.team31project.ChatAndNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebAPI {
    @Headers(
            {       "Content-Type:application/json",
                    "Authorization:key=BACf7zsB0RLdF6EcnFsodCYKTJ_tjOAvkgoMfl6E4vw4ti228LUb2PhdSvJ0E57M2bNyrgI7uPkxPxDuXRB4DUg"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendAndNotify(@Body Send body);
}

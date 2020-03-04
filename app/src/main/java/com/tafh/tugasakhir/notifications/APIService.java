package com.tafh.tugasakhir.notifications;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({"Authorization: key=AAAA039sLhI:APA91bEIUTRrkRs1hhFG_HDSAaMYwwmVo45XM9sOUh07CW7z5AM0bFGO56kp4KJ_wVpUKVMRwljw1rvRTCaBPu2yxu4p2WWZYD2T2MIVAl_RxxEmSpON6N8DRUfmUTGlg2rPfQwG3d_z",
            "Content-Type:application/json"
            })

    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body Sender body);

}

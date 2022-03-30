package com.year3project.qrcodegenerator;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("php/upload_image_android.php")
    Call<ResponsePOJO> uploadImage(
            @Field("EN_IMAGE") String encodedImage,
            @Field("FILE_NAME") String fileName
    );
}

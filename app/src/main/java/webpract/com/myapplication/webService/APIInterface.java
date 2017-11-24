package webpract.com.myapplication.webService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import webpract.com.myapplication.models.fetchAllData.ClsFetchAllDataResponse;
import webpract.com.myapplication.models.insertData.ClsInsertBrandRequest;
import webpract.com.myapplication.models.insertData.ClsInsertBrandResponse;

public interface APIInterface {

//    @GET("/api/unknown")
//    Call doGetListResources();

    @POST("insert.php")
    Call<ClsInsertBrandResponse> insert(@Body ClsInsertBrandRequest clsInsertBrandRequest);

    @POST("list.php")
    Call<ClsFetchAllDataResponse> fetchAllData(@Body String timeStamp);

//    @GET("/api/users?")
//    Call doGetUserList(@Query("page") String page);
//
//    @FormUrlEncoded
//    @POST("/api/users?")
//    Call doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}
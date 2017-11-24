package webpract.com.myapplication.models.fetchAllData;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsFetchAllDataResponse {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("brand_list")
    @Expose
    private List<BrandList> brandList = null;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BrandList> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<BrandList> brandList) {
        this.brandList = brandList;
    }

}


package webpract.com.myapplication.models.insertData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsInsertBrandRequest {

    @SerializedName("brand")
    @Expose
    private List<Brand> brand = null;

    public List<Brand> getBrand() {
        return brand;
    }

    public void setBrand(List<Brand> brand) {
        this.brand = brand;
    }

}

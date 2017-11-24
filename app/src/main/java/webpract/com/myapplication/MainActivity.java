package webpract.com.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import webpract.com.myapplication.adapters.BrandAdapter;
import webpract.com.myapplication.models.fetchAllData.BrandList;
import webpract.com.myapplication.models.fetchAllData.ClsFetchAllDataResponse;
import webpract.com.myapplication.models.insertData.Brand;
import webpract.com.myapplication.models.insertData.ClsInsertBrandRequest;
import webpract.com.myapplication.models.insertData.ClsInsertBrandResponse;
import webpract.com.myapplication.webService.APIClient;
import webpract.com.myapplication.webService.APIInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static ProgressDialog progressDialog;
    private RecyclerView listBrands = null;
    private BrandAdapter brandAdapter;
    private ClsFetchAllDataResponse clsFetchAllDataResponse;
    private APIInterface apiInterface;
    private FloatingActionButton fabAdd , fabSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {

        listBrands = (RecyclerView) findViewById(R.id.listBrands);
        listBrands.setLayoutManager(new LinearLayoutManager(this));

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);

        fabSync = (FloatingActionButton) findViewById(R.id.fabSync);
        fabSync.setOnClickListener(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (isNetworkConnected()) {
            callFetchAllDataWs();
        }
        setAdapter();
    }

    private void callFetchAllDataWs() {

        showProgress(null , MainActivity.this , false);

        Call<ClsFetchAllDataResponse> call1 = apiInterface.fetchAllData(String.valueOf(System.currentTimeMillis()));
        call1.enqueue(new Callback<ClsFetchAllDataResponse>() {
            @Override
            public void onResponse(Call<ClsFetchAllDataResponse> call, Response<ClsFetchAllDataResponse> response) {
                clsFetchAllDataResponse = response.body();
                for (int i = 0; i < clsFetchAllDataResponse.getBrandList().size(); i++) {
                    AppDelegate.database.insertBrandDataTable(clsFetchAllDataResponse.getBrandList().get(i));
                }
                cancelProgress();
                setAdapter();
            }
            @Override
            public void onFailure(Call<ClsFetchAllDataResponse> call, Throwable t) {
                cancelProgress();
                Toast.makeText(MainActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        if (clsFetchAllDataResponse != null) {

            brandAdapter = new BrandAdapter(MainActivity.this, clsFetchAllDataResponse.getBrandList());
            listBrands.setAdapter(brandAdapter);

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void callInsertBrandWs() {

        showProgress("Syncing" , MainActivity.this , false);

        Call<ClsInsertBrandResponse> call1 = apiInterface.insert(getInsertDataRequest());
        call1.enqueue(new Callback<ClsInsertBrandResponse>() {
            @Override
            public void onResponse(Call<ClsInsertBrandResponse> call, Response<ClsInsertBrandResponse> response) {
                cancelProgress();
                ClsInsertBrandResponse clsInsertBrandResponse = response.body();
                if(clsInsertBrandResponse.getErrorCode() == 1) {
                    Toast.makeText(MainActivity.this, "Sync Completed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, clsInsertBrandResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClsInsertBrandResponse> call, Throwable t) {
                cancelProgress();
                Toast.makeText(MainActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ClsInsertBrandRequest getInsertDataRequest() {
        ClsInsertBrandRequest clsInsertBrandRequest = new ClsInsertBrandRequest();
        ArrayList<BrandList> brandList = AppDelegate.database.getAllBrands();
        ArrayList<Brand> brands = new ArrayList<>();
        for (int i = 0; i < brandList.size(); i++) {
            Brand brand = new Brand();
            brand.setName(brandList.get(i).getName());
            brand.setDescription(brandList.get(i).getDescription());
            brands.add(brand);
        }
        clsInsertBrandRequest.setBrand(brands);
        return clsInsertBrandRequest;
    }


    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Insert Data");
        alertDialog.setMessage("Enter Information");

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText edtBrandName = new EditText(MainActivity.this);
        edtBrandName.setHint("Name");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp),
                getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp),
                getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp),
                getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp));
        edtBrandName.setLayoutParams(lp);
        layout.addView(edtBrandName);

        final EditText edtBrandDesc = new EditText(MainActivity.this);
        edtBrandDesc.setHint("Description");
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp1.setMargins(getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp),
                getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp),
                getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp),
                getResources().getDimensionPixelOffset(R.dimen.margin_default_4dp));
        edtBrandDesc.setLayoutParams(lp1);
        layout.addView(edtBrandDesc);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("Insert",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       if(isValueNull(edtBrandName.getText().toString().trim())) {
                           Toast.makeText(MainActivity.this ,"Please enter Brand Name", Toast.LENGTH_SHORT).show();
                       } else if(isValueNull(edtBrandDesc.getText().toString().trim())) {
                           Toast.makeText(MainActivity.this ,"Please enter Brand Description", Toast.LENGTH_SHORT).show();
                       } else {
                            AppDelegate.database.insertBrandDataTable(edtBrandName.getText().toString().trim() , edtBrandDesc.getText().toString().trim());
                       }
                    }
                });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private boolean isValueNull(String string) {

        if(string == null || string.equals("") || string.equalsIgnoreCase("null") || string.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fabAdd:
                showDialog();
                break;

            case R.id.fabSync:
                callInsertBrandWs();
                break;

            default:
                break;
        }
    }

    private void showProgress(String message, Context context, boolean cancellable) {
        if (context == null)
            return;

        if (checkProgressOpen())
            return;
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message == null ? "Please wait..." : (message));
        progressDialog.setCancelable(cancellable);
        try {
            progressDialog.show();
        } catch (Exception e) {
            // catch exception for activity paused and dialog is going to be
            // show.
        }
    }

    private boolean checkProgressOpen() {
        if (progressDialog != null && progressDialog.isShowing())
            return true;
        else
            return false;
    }

    private void cancelProgress() {
        if (checkProgressOpen()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
            }
            progressDialog = null;
        }
    }
}

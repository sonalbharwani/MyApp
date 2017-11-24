package webpract.com.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import webpract.com.myapplication.R;
import webpract.com.myapplication.models.fetchAllData.BrandList;

/**
 * Created by wmtandroid5 on 23/11/17.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private Context context;
    private List<BrandList> brands;

    public BrandAdapter(Context context, List<BrandList> brands) {
        this.context = context;
        this.brands = brands;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_brand, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BrandList brandList = brands.get(position);
        if(!isValueNull(brandList.getName())) {
            holder.txtBrandName.setText(brandList.getName());
        } else {
            holder.txtBrandName.setText("");
        }

        if(!isValueNull(brandList.getDescription())) {
            holder.txtBrandDesc.setText(brandList.getDescription());
        } else {
            holder.txtBrandDesc.setText("");
        }

        if(!isValueNull(brandList.getCreatedAt())) {
            holder.txtTime.setText(brandList.getCreatedAt());
        } else {
            holder.txtTime.setText("");
        }
    }


    @Override
    public int getItemCount() {
        return brands.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtBrandName , txtBrandDesc , txtTime;

        public ViewHolder(View itemView) {
            super(itemView);
            txtBrandName = (TextView) itemView.findViewById(R.id.txtBrandName);
            txtBrandDesc = (TextView) itemView.findViewById(R.id.txtBrandDesc);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
        }
    }

    private boolean isValueNull(String string) {

        if(string == null || string.equals("") || string.equalsIgnoreCase("null") || string.isEmpty()) {
            return true;
        }
        return false;
    }
}

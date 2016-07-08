package com.itexico.utilities.lockmydevice.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.itexico.utilities.lockmydevice.R;
import com.itexico.utilities.lockmydevice.model.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iTexico Developer on 7/7/2016.
 */
public class DevicesListAdapter extends RecyclerViewAdapter implements Filterable {

    private List<DeviceInfo> mDeviceInfoList;
    private List<DeviceInfo> mFilteredDeviceInfoList;

    public DevicesListAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public void setDataset(final List<DeviceInfo> deviceInfoList) {
        mDeviceInfoList = deviceInfoList;
        mFilteredDeviceInfoList = deviceInfoList;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return LayoutInflater.from(context).inflate(R.layout.device_info_row, viewGroup, false);
    }

    @Override
    protected void bindView(int position, ViewHolder viewHolder) {
        if (mFilteredDeviceInfoList.isEmpty()) {
            return;
        }
        final TextView deviceInfoView = (TextView) viewHolder.getView(R.id.device_info);
        final DeviceInfo deviceInfo = mFilteredDeviceInfoList.get(position);
        deviceInfoView.setText(String.format(getContext().getResources().getString(R.string.device_info),deviceInfo.getDeviceIMEI(),deviceInfo.getDeviceModel()));
    }

    @Override
    public long getItemId(int position) {
        Object listItem = mFilteredDeviceInfoList.get(position);
        return listItem.hashCode();
    }

    @Override
    public int getItemCount() {
        if(null == mFilteredDeviceInfoList){
            return 0;
        }
        return mFilteredDeviceInfoList.size();
    }


    @Override
    public Filter getFilter() {
        return new DevicesInfoFilter();
    }

    private class DevicesInfoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<DeviceInfo> tempList = new ArrayList<DeviceInfo>();
                // search content in friend list
                for (DeviceInfo deviceInfo : mDeviceInfoList) {
                    if (deviceInfo.getDeviceModel().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(deviceInfo);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = mDeviceInfoList.size();
                filterResults.values = mDeviceInfoList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredDeviceInfoList = (ArrayList<DeviceInfo>) results.values;
            notifyDataSetChanged();
        }
    }
}

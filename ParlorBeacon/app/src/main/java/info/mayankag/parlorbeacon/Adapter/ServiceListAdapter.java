package info.mayankag.parlorbeacon.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import info.mayankag.parlorbeacon.Models.Service;
import info.mayankag.parlorbeacon.R;

import java.util.ArrayList;

public class ServiceListAdapter extends ArrayAdapter<Service> {

        private final Activity activity;
        private final ArrayList<Service> services;

        public ServiceListAdapter(Activity activity, ArrayList<Service> services) {
            super(activity, R.layout.service_shop_adapter, services);
            this.activity = activity;
            this.services = services;
        }

        @NonNull
        @Override
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.service_shop_adapter,null, true);

        TextView serviceName = (TextView) rowView.findViewById(R.id.serviceNameShop);
        TextView serviceDuration = (TextView) rowView.findViewById(R.id.serviceDurationShop);

        serviceName.setText(services.get(position).getName());
        serviceDuration.setText(services.get(position).getDuration());

        return rowView;
    }

}

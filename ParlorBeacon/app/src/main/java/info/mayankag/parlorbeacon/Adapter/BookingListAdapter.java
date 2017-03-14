package info.mayankag.parlorbeacon.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import info.mayankag.parlorbeacon.Models.Booking;
import info.mayankag.parlorbeacon.R;

import java.util.ArrayList;

public class BookingListAdapter extends ArrayAdapter<Booking> {

    private final Activity activity;
    private final ArrayList<Booking> bookings;
    private TextView customerName,serviceName,serviceTime;

    public BookingListAdapter(Activity activity, ArrayList<Booking> bookings) {
        super(activity, R.layout.booking_shop_adapter,bookings);
        this.activity = activity;
        this.bookings = bookings;
    }
    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder")
        final View rowView = inflater.inflate(R.layout.booking_shop_adapter,null, true);


        customerName = (TextView)rowView.findViewById(R.id.customerName);
        serviceName = (TextView)rowView.findViewById(R.id.serviceName);
        serviceTime = (TextView)rowView.findViewById(R.id.serviceTime);

        customerName.setText(bookings.get(position).getCustomerName());
        serviceName.setText(bookings.get(position).getServiceName());
        serviceTime.setText(bookings.get(position).getServiceTime());
        return rowView;
    }

}

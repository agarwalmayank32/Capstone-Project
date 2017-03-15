package info.mayankag.parlorbeacon.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import info.mayankag.parlorbeacon.Models.BookingCust;
import info.mayankag.parlorbeacon.R;

public class BookingAdapterCust extends ArrayAdapter<BookingCust> {

    private Activity mContext;
    private ArrayList<BookingCust> mBookings;

    public BookingAdapterCust(Activity context, ArrayList<BookingCust> bookings) {
        super(context, R.layout.booking_adapter, bookings);
        mContext = context;
        mBookings = bookings;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("ViewHolder")
        final View rowView = inflater.inflate(R.layout.booking_adapter, null, true);

        TextView shopEmail = (TextView) rowView.findViewById(R.id.shopEmailBooking);
        TextView serviceName = (TextView) rowView.findViewById(R.id.serviceNameBooking);
        TextView serviceTime = (TextView) rowView.findViewById(R.id.serviceTimeBooking);
        TextView serviceDate = (TextView) rowView.findViewById(R.id.serviceDateBooking);
        TextView custPhone = (TextView) rowView.findViewById(R.id.custPhoneBooking);
        TextView custEmail = (TextView) rowView.findViewById(R.id.custEmailBooking);
        TextView custName = (TextView) rowView.findViewById(R.id.custNameBooking);

        shopEmail.setText(mBookings.get(position).getShopEmail());
        serviceName.setText(mBookings.get(position).getServiceName());
        serviceTime.setText(mBookings.get(position).getServiceTime());
        serviceDate.setText(mBookings.get(position).getServiceDate());
        custPhone.setText(mBookings.get(position).getCustPhone());
        custEmail.setText(mBookings.get(position).getCustEmail());
        custName.setText(mBookings.get(position).getCustName());

        return rowView;

    }
}

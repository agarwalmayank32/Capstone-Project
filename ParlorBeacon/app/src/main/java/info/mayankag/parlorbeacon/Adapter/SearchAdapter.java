package info.mayankag.parlorbeacon.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import info.mayankag.parlorbeacon.Models.Search;
import info.mayankag.parlorbeacon.R;

public class SearchAdapter extends ArrayAdapter<Search>{

    private final Activity mContext;
    private final ArrayList<Search> mSearches;

    public SearchAdapter(Activity mcontext, ArrayList<Search> searches) {
        super(mcontext, R.layout.search_adapter,searches);
        mContext = mcontext;
        mSearches = searches;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.search_adapter,null, true);


        TextView name = (TextView)rowView.findViewById(R.id.searchShopName);
        RatingBar ratingBar = (RatingBar)rowView.findViewById(R.id.searchShopRating);
        TextView address = (TextView)rowView.findViewById(R.id.searchShopAddress);

        name.setText(mSearches.get(position).getName());
        ratingBar.setRating(Float.parseFloat(mSearches.get(position).getRating()));
        address.setText(mSearches.get(position).getAddress());

        return rowView;

    }
}

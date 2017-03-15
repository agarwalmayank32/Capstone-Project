package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.mayankag.parlorbeacon.Adapter.BookingListAdapter;
import info.mayankag.parlorbeacon.Models.Booking;

public class MyBookingsShop extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Booking> bookings;
    private ListView bookingListViewShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookingListViewShop = (ListView) findViewById(R.id.bookingListViewShop);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        bookingListSetup();
        bookings = new ArrayList<>();
    }

    private void bookingListSetup() {

        final TextView noBookingDoneText = (TextView) findViewById(R.id.noBookingDoneText);
        if (!UtilsShop.isNetworkAvailable(this)) {
            UtilsShop.NetworkToast(this);
        } else {
            String getBookingDetails_url = getResources().getString(R.string.getBookingDetails_url);

            JSONObject params = new JSONObject();
            try {
                params.put("email", UtilsShop.loadShopEmail(this));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getBookingDetails_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray detail = response.getJSONArray("booking");
                        String custName, custEmail, serviceName, serviceTime, serviceDate, serviceStatus;


                        if (detail.length() > 0) {
                            noBookingDoneText.setVisibility(View.GONE);
                            bookingListViewShop.setVisibility(View.VISIBLE);
                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);

                                custName = singledetail.getString("custname");
                                custEmail = singledetail.getString("custemail");
                                serviceName = singledetail.getString("service");
                                serviceTime = singledetail.getString("time");
                                serviceDate = singledetail.getString("date");
                                serviceStatus = singledetail.getString("status");

                                bookings.add(new Booking(custName, custEmail, serviceName, serviceTime, serviceDate, serviceStatus));
                            }
                            BookingListAdapter adapter = new BookingListAdapter(MyBookingsShop.this, bookings);
                            bookingListViewShop.setAdapter(adapter);
                        } else {
                            noBookingDoneText.setVisibility(View.VISIBLE);
                            noBookingDoneText.setText("No Booking done yet");
                            bookingListViewShop.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MyBookingsShop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MyBookingsShop.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        UtilsShop.onNaviationItemSelectedShop(this, item);
        return true;
    }
}

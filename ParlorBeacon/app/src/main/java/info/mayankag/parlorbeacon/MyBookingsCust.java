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
import android.view.Menu;
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

import info.mayankag.parlorbeacon.Adapter.BookingAdapterCust;
import info.mayankag.parlorbeacon.Models.BookingCust;

public class MyBookingsCust extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<BookingCust> mBookingCusts;
    ListView bookingsListView;
    TextView noBookingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_cust);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookingsListView = (ListView) findViewById(R.id.bookingsListView);
        noBookingText = (TextView) findViewById(R.id.noBookingText);
        mBookingCusts = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bookingFetch();
    }

    void bookingFetch() {
        JSONObject params = new JSONObject();
        try {
            params.put("email", UtilsCust.loadCustEmail(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String booking_fetch_url = getResources().getString(R.string.booking_fetch_url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, booking_fetch_url, params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                mBookingCusts.clear();
                try {
                    JSONArray detail = response.getJSONArray("allbookings");
                    String shopEmail, serviceName, serviceTime, serviceDate, custPhone, custEmail, custName;

                    if (detail.length() > 0) {
                        noBookingText.setVisibility(View.GONE);
                        bookingsListView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < detail.length(); i++) {
                            JSONObject singledetail = detail.getJSONObject(i);

                            shopEmail = singledetail.getString("shopemail");
                            serviceName = singledetail.getString("service");
                            serviceTime = singledetail.getString("time");
                            serviceDate = singledetail.getString("date");
                            custPhone = singledetail.getString("custphone");
                            custEmail = singledetail.getString("custemail");
                            custName = singledetail.getString("custname");

                            mBookingCusts.add(new BookingCust(shopEmail, serviceName, serviceTime, serviceDate, custPhone, custEmail, custName));
                        }
                        BookingAdapterCust bookingAdapterCust = new BookingAdapterCust(MyBookingsCust.this, mBookingCusts);
                        bookingsListView.setAdapter(bookingAdapterCust);
                    } else {
                        noBookingText.setVisibility(View.VISIBLE);
                        noBookingText.setText("No bookings done yet");
                        bookingsListView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(MyBookingsCust.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyBookingsCust.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.extra, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        UtilsCust.onNaviationItemSelected(this, item);
        return true;
    }
}

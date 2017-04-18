package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import info.mayankag.parlorbeacon.Adapter.BookingListAdapter;
import info.mayankag.parlorbeacon.Models.Booking;

public class MainScreenShop extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView todayBookingListView;
    private TextView todayNoBookingText;
    private RelativeLayout relativeLayoutTodayBooking;
    private SwipeRefreshLayout swipeRefreshLayoutTodayBooking;

    private ArrayList<Booking> todayBookings;
    private BookingListAdapter todayBookingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        swipeRefreshLayoutTodayBooking = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshTodayBooking);
        todayBookingListView = (ListView) findViewById(R.id.todaybookingListView);
        todayNoBookingText = (TextView) findViewById(R.id.today_no_booking_text);
        relativeLayoutTodayBooking = (RelativeLayout) findViewById(R.id.RLTodayScheduleListView);

        swipeRefreshLayoutTodayBooking.setOnRefreshListener(this);
        swipeRefreshLayoutTodayBooking.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    swipeRefreshLayoutTodayBooking.setRefreshing(true);
                                                    todayBookingListViewSetup();
                                                }
                                            }
        );

        todayBookings = new ArrayList<>();
        todayBookingListViewSetup();
    }

    private void todayBookingListViewSetup() {
        swipeRefreshLayoutTodayBooking.setRefreshing(true);

        if (!UtilsShop.isNetworkAvailable(this)) {
            UtilsShop.NetworkToast(this);
            swipeRefreshLayoutTodayBooking.setRefreshing(false);
        } else {
            String getTodayBooking_url = getResources().getString(R.string.getTodayBooking_url);
            todayBookings.clear();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendarInstance = Calendar.getInstance();
            String todayDate = dateFormat.format(calendarInstance.getTime());

            JSONObject params = new JSONObject();
            try {
                params.put("email", UtilsShop.loadShopEmail(this));
                params.put("todaydate", todayDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getTodayBooking_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray detail = response.getJSONArray("todaybooking");
                        String custName, custEmail, serviceName, serviceTime, serviceDate, serviceStatus;

                        if (detail.length() > 0) {
                            relativeLayoutTodayBooking.setVisibility(View.VISIBLE);
                            todayNoBookingText.setVisibility(View.GONE);

                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);

                                custName = singledetail.getString("custname");
                                custEmail = singledetail.getString("custemail");
                                serviceName = singledetail.getString("service");
                                serviceTime = singledetail.getString("time");
                                serviceDate = singledetail.getString("date");
                                serviceStatus = singledetail.getString("status");

                                todayBookings.add(new Booking(custName, custEmail, serviceName, serviceTime, serviceDate, serviceStatus));
                            }
                            swipeRefreshLayoutTodayBooking.setRefreshing(false);
                            todayBookingListAdapter = new BookingListAdapter(MainScreenShop.this, todayBookings);
                            todayBookingListView.setAdapter(todayBookingListAdapter);
                        } else {
                            swipeRefreshLayoutTodayBooking.setRefreshing(false);
                            relativeLayoutTodayBooking.setVisibility(View.GONE);
                            todayNoBookingText.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainScreenShop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        swipeRefreshLayoutTodayBooking.setRefreshing(false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainScreenShop.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayoutTodayBooking.setRefreshing(false);
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
        int id = item.getItemId();

        if (id == R.id.shop_myBooking) {
            startActivity(new Intent(this, MyBookingsShop.class));
        } else if (id == R.id.shop_myemployee) {
            startActivity(new Intent(this, MyEmployeesShop.class));
        } else if (id == R.id.shop_services) {
            startActivity(new Intent(this, ServicesShop.class));
        } else if (id == R.id.shop_details) {
            startActivity(new Intent(this, ShopDetails.class));
        } else if (id == R.id.about) {
            startActivity(new Intent(this, About.class));
        } else if (id == R.id.contact) {
            startActivity(new Intent(this, ContactUs.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        todayBookingListViewSetup();
    }

}

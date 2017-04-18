package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.mayankag.parlorbeacon.Adapter.ServiceListAdapter;
import info.mayankag.parlorbeacon.Models.Service;

public class ServicesShop extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView serviceListView;
    private TextView noServiceText;
    private SwipeRefreshLayout swipeRefreshLayoutService;

    private ArrayList<Service> services;
    private ServiceListAdapter serviceListAdapter;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickServiceAddDialog(ServicesShop.this);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        swipeRefreshLayoutService = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshService);
        serviceListView = (ListView) findViewById(R.id.serviceDetailsListView);
        noServiceText = (TextView) findViewById(R.id.no_service_text);

        swipeRefreshLayoutService.setOnRefreshListener(this);
        swipeRefreshLayoutService.post(new Runnable() {
                                           @Override
                                           public void run() {
                                               swipeRefreshLayoutService.setRefreshing(true);
                                               serviceListViewSetup();
                                           }
                                       }
        );

        services = new ArrayList<>();
        serviceListViewSetup();

    }

    private void serviceListViewSetup() {
        swipeRefreshLayoutService.setRefreshing(true);

        if (!UtilsShop.isNetworkAvailable(this)) {
            UtilsShop.NetworkToast(this);
            swipeRefreshLayoutService.setRefreshing(false);
        } else {

            JSONObject params = new JSONObject();
            try {
                params.put("shopemail", UtilsShop.loadShopEmail(this));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String getServiceDetails_url = getResources().getString(R.string.getServiceDetails_url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getServiceDetails_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    services.clear();
                    try {
                        JSONArray detail = response.getJSONArray("info");
                        String serviceName, serviceDuration, serviceType;

                        if (detail.length() > 0) {
                            serviceListView.setVisibility(View.VISIBLE);
                            noServiceText.setVisibility(View.GONE);

                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);

                                serviceName = singledetail.getString("service");
                                serviceDuration = singledetail.getString("duration") + " min";
                                serviceType = singledetail.getString("servicetype");
                                services.add(new Service(serviceName, serviceDuration, serviceType));
                            }
                            swipeRefreshLayoutService.setRefreshing(false);
                            serviceListAdapter = new ServiceListAdapter(ServicesShop.this, services);
                            serviceListView.setAdapter(serviceListAdapter);

                        } else {
                            swipeRefreshLayoutService.setRefreshing(false);
                            serviceListView.setVisibility(View.GONE);
                            noServiceText.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        swipeRefreshLayoutService.setRefreshing(false);
                        Toast.makeText(ServicesShop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ServicesShop.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayoutService.setRefreshing(false);
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


    private void onClickServiceAddDialog(final Context context) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_service_shop);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final EditText serviceName = (EditText) dialog.findViewById(R.id.serviceNameAdd);
        final String[] durationArray = getResources().getStringArray(R.array.serviceDuration);

        Spinner spin = (Spinner) dialog.findViewById(R.id.serviceDurationAdd);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    duration = Integer.parseInt(durationArray[position].replace(" min", ""));
                } else {
                    Toast.makeText(ServicesShop.this, R.string.service_duration_select_text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button addButton = (Button) dialog.findViewById(R.id.serviceAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UtilsShop.isNetworkAvailable(context)) {
                    UtilsShop.NetworkToast(context);
                } else {
                    final String name = serviceName.getText().toString();
                    final String email = UtilsShop.loadShopEmail(ServicesShop.this);

                    if (name.equals("")) {
                        UtilsShop.ShortToast(context, getString(R.string.fill_all_details_text));
                    } else {
                        String addService_url = getResources().getString(R.string.addService_url);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, addService_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (response.equalsIgnoreCase("Success")) {
                                        serviceListViewSetup();
                                        dialog.dismiss();
                                        UtilsShop.ShortToast(context, getString(R.string.service_add_success_text));
                                    } else if (response.equalsIgnoreCase("Failed")) {
                                        UtilsShop.ShortToast(context, getString(R.string.service_add_fail_text));
                                    }
                                } catch (Exception e) {
                                    UtilsShop.ShortToast(context, e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                UtilsShop.ShortToast(context, error.getMessage());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parameters = new HashMap<>();
                                parameters.put("email", email);
                                parameters.put("duration", String.valueOf(duration));
                                parameters.put("service", name);
                                return parameters;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onRefresh() {
        serviceListViewSetup();
    }
}

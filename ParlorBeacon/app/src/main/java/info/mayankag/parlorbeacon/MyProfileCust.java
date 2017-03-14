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

import java.util.HashMap;
import java.util.Map;

public class MyProfileCust extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView name,email,phone,gender,address,city,zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_cust);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (TextView)findViewById(R.id.nameCust);
        email = (TextView)findViewById(R.id.emailCust);
        phone = (TextView)findViewById(R.id.phoneCust);
        gender = (TextView)findViewById(R.id.genderCust);
        address = (TextView)findViewById(R.id.addressCust);
        city = (TextView)findViewById(R.id.cityCust);
        zip = (TextView)findViewById(R.id.zipCust);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        profileFetch();
    }

    void profileFetch()
    {
        JSONObject params = new JSONObject();
        try {
            params.put("custemail", UtilsCust.loadCustEmail(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String profile_fetch_url = getResources().getString(R.string.profile_fetch_url) ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, profile_fetch_url,params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray detail=response.getJSONArray("custdetails");

                    if(detail.length()>0) {
                        for (int i = 0; i < detail.length(); i++) {
                            JSONObject singledetail = detail.getJSONObject(i);

                            name.setText(singledetail.getString("name"));
                            email.setText(singledetail.getString("email"));
                            phone.setText(singledetail.getString("phone"));
                            gender.setText(singledetail.getString("gender"));
                            address.setText(singledetail.getString("address"));
                            city.setText(singledetail.getString("city"));
                            zip.setText(singledetail.getString("zip"));
                        }
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(MyProfileCust.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyProfileCust.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        UtilsCust.onNaviationItemSelected(this,item);
        return true;
    }
}

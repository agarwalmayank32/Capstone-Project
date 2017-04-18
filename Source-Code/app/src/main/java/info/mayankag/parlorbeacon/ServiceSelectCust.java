package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

@SuppressWarnings("ALL")
public class ServiceSelectCust extends AppCompatActivity {

    private Spinner selectServiceNameDropdown;
    private Spinner selectServiceTimeDropdown;
    private String selectServiceName;
    private String selectServiceTime;
    private String email;
    private ArrayList<String> serviceName;
    private ArrayList<String> serviceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_select_cust);

        selectServiceNameDropdown = (Spinner) findViewById(R.id.serviceNameSelect);
        selectServiceTimeDropdown = (Spinner) findViewById(R.id.serviceTimeSelect);
        selectServiceTimeDropdown.setEnabled(false);
        serviceName = new ArrayList<>();
        serviceTime = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("ShopEmail");
            fetchServices();
        }

        selectServiceNameDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectServiceName = serviceName.get(i);
                    fetchTimeAvailable();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectServiceTimeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectServiceTime = serviceTime.get(i);
                    Intent intent = new Intent(ServiceSelectCust.this, BookingActivityCust.class);
                    intent.putExtra("ServiceName", selectServiceName);
                    intent.putExtra("ServiceTime", selectServiceTime);
                    intent.putExtra("ShopEmail", email);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetchServices() {
        if (!UtilsCust.isNetworkAvailable(this)) {
            UtilsCust.NetworkToast(this);
        } else {
            JSONObject params = new JSONObject();
            try {
                params.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String service_select_url = getResources().getString(R.string.service_select_url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, service_select_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    serviceName.clear();
                    serviceName.add("Select Service");
                    try {
                        JSONArray detail = response.getJSONArray("servicedata");

                        if (detail.length() > 0) {
                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);
                                serviceName.add(singledetail.getString("service"));
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter(ServiceSelectCust.this, android.R.layout.simple_spinner_item, serviceName);
                            //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            selectServiceNameDropdown.setAdapter(dataAdapter);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ServiceSelectCust.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ServiceSelectCust.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void fetchTimeAvailable() {
        selectServiceTimeDropdown.setEnabled(false);
        if (!UtilsCust.isNetworkAvailable(this)) {
            UtilsCust.NetworkToast(this);
        } else {
            JSONObject params = new JSONObject();
            try {
                params.put("shopemail", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String get_available_time_url = getResources().getString(R.string.get_available_time_url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, get_available_time_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    serviceTime.clear();
                    serviceTime.add("Select Time");
                    try {
                        JSONArray detail = response.getJSONArray("bookingData");
                        String time;

                        if (detail.length() > 0) {
                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);
                                time = singledetail.getString("time");
                                serviceTime.add(time);
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter(ServiceSelectCust.this, android.R.layout.simple_spinner_item, serviceTime);
                            //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            selectServiceTimeDropdown.setAdapter(dataAdapter);
                            selectServiceTimeDropdown.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ServiceSelectCust.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ServiceSelectCust.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
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
}


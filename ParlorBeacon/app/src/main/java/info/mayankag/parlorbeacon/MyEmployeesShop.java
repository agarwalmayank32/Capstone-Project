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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import info.mayankag.parlorbeacon.Adapter.EmployeeListAdapter;
import info.mayankag.parlorbeacon.Models.Employee;

public class MyEmployeesShop extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView employeeListView;
    private TextView noEmployeeText;
    private SwipeRefreshLayout swipeRefreshLayoutEmployee;

    private ArrayList<Employee> employees;
    private EmployeeListAdapter employeeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_employees_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEmployeeAddDialog(MyEmployeesShop.this);
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

        swipeRefreshLayoutEmployee = (SwipeRefreshLayout) findViewById(R.id.swiperefreshEmployee);
        employeeListView = (ListView) findViewById(R.id.employeeDetailListView);
        noEmployeeText = (TextView) findViewById(R.id.no_employee_text);

        swipeRefreshLayoutEmployee.setOnRefreshListener(this);
        swipeRefreshLayoutEmployee.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                swipeRefreshLayoutEmployee.setRefreshing(true);
                                                employeeListViewSetup();
                                            }
                                        }
        );

        employees = new ArrayList<>();
        employeeListViewSetup();
    }

    private void employeeListViewSetup() {
        swipeRefreshLayoutEmployee.setRefreshing(true);

        if (!UtilsShop.isNetworkAvailable(this)) {
            UtilsShop.NetworkToast(this);
            swipeRefreshLayoutEmployee.setRefreshing(false);
        } else {

            JSONObject params = new JSONObject();
            try {
                params.put("shopemail", UtilsShop.loadShopEmail(this));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String getEmployeeDetails_url = getResources().getString(R.string.getEmployeeDetails_url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getEmployeeDetails_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    employees.clear();
                    try {
                        JSONArray detail = response.getJSONArray("empdetail");
                        String empName, empEmail, empPhoneNo, empDesig;

                        if (detail.length() > 0) {
                            employeeListView.setVisibility(View.VISIBLE);
                            noEmployeeText.setVisibility(View.GONE);

                            for (int i = 0; i < detail.length(); i++) {
                                JSONObject singledetail = detail.getJSONObject(i);

                                empName = singledetail.getString("name");
                                empEmail = singledetail.getString("email");
                                empPhoneNo = singledetail.getString("Phone");
                                empDesig = singledetail.getString("designation");

                                employees.add(new Employee(empName, empEmail, empPhoneNo, empDesig));
                            }
                            swipeRefreshLayoutEmployee.setRefreshing(false);
                            employeeListAdapter = new EmployeeListAdapter(MyEmployeesShop.this, employees);
                            employeeListView.setAdapter(employeeListAdapter);

                        } else {
                            swipeRefreshLayoutEmployee.setRefreshing(false);
                            employeeListView.setVisibility(View.GONE);
                            noEmployeeText.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        swipeRefreshLayoutEmployee.setRefreshing(false);
                        Toast.makeText(MyEmployeesShop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MyEmployeesShop.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayoutEmployee.setRefreshing(false);
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

    private void onClickEmployeeAddDialog(final Context context) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_employee_shop);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final EditText empName = (EditText) dialog.findViewById(R.id.empNameAdd);
        final EditText empEmail = (EditText) dialog.findViewById(R.id.empEmailAdd);
        final EditText empPhone = (EditText) dialog.findViewById(R.id.empPhoneAdd);
        final EditText empDesig = (EditText) dialog.findViewById(R.id.empDesigAdd);

        Button addButton = (Button) dialog.findViewById(R.id.empAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UtilsShop.isNetworkAvailable(context)) {
                    UtilsShop.NetworkToast(context);
                } else {
                    final String name = empName.getText().toString();
                    final String email = empEmail.getText().toString();
                    final String phone = empPhone.getText().toString();
                    final String desig = empDesig.getText().toString();

                    if (name.equals("") && email.equals("") && phone.equals("") && desig.equals("")) {
                        UtilsShop.ShortToast(context, getString(R.string.fill_all_details_text));
                    } else if (!email.contains("@") && !email.contains(".com")) {
                        UtilsShop.ShortToast(context, getString(R.string.valid_email_text));
                    } else {
                        String addEmployeeDetails_url = getResources().getString(R.string.addEmployeeDetails_url);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, addEmployeeDetails_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (response.equalsIgnoreCase("success")) {
                                        employeeListViewSetup();
                                        dialog.dismiss();
                                        Toast.makeText(context, R.string.employee_add_success_text, Toast.LENGTH_SHORT).show();
                                    } else if (response.equalsIgnoreCase("failed")) {
                                        Toast.makeText(context, R.string.employee_add_fail_text, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parameters = new HashMap<>();
                                parameters.put("shopemail", UtilsShop.loadShopEmail(context));
                                parameters.put("name", name);
                                parameters.put("email", email);
                                parameters.put("designation", desig);
                                parameters.put("phone", phone);
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
        employeeListViewSetup();
    }
}

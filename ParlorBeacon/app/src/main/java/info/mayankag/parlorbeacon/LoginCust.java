package info.mayankag.parlorbeacon;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginCust extends DialogFragment {
    private EditText emailText;
    private EditText passText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_cust, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Login");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        emailText = (EditText) v.findViewById(R.id.login_cust_email);
        passText = (EditText) v.findViewById(R.id.login_cust_password);
        Button login = (Button) v.findViewById(R.id.login_cust_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        return v;
    }

    private void login() {
        if (!UtilsCust.isNetworkAvailable(getActivity())) {
            UtilsCust.NetworkToast(getActivity());
        } else {
            String login_url = getResources().getString(R.string.login_cust_url);
            if (validateForm(emailText.getText().toString(), passText.getText().toString())) {
                final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Signing in...", false, false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            if (response.equalsIgnoreCase(String.valueOf(0))) {
                                UtilsCust.saveCustEmail(emailText.getText().toString(), getActivity());
                                saveNameCust();
                                startActivity(new Intent(getActivity(), MainScreenCust.class));
                                getActivity().finish();
                            } else if (response.equalsIgnoreCase(String.valueOf(1))) {
                                UtilsCust.ShortToast(getActivity(), getString(R.string.request_accepted_text));
                            } else if (response.equalsIgnoreCase(String.valueOf(2))) {
                                UtilsCust.ShortToast(getActivity(), getString(R.string.not_registered_text));
                            }
                        } catch (Exception e) {
                            UtilsCust.ShortToast(getActivity(), e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        UtilsCust.ShortToast(getActivity(), error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("email", emailText.getText().toString());
                        parameters.put("password", passText.getText().toString());
                        return parameters;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        }
    }

    private boolean validateForm(String email, String password) {
        if (!(email.contains("@") && email.contains(".com"))) {
            UtilsCust.ShortToast(getActivity(), getString(R.string.invalid_email_text_cust));
            return false;
        } else if (password.length() < 8) {
            UtilsCust.ShortToast(getActivity(), getString(R.string.invalid_password_text_cust));
            return false;
        } else {
            return true;
        }
    }

    private void saveNameCust() {

        if (!UtilsCust.isNetworkAvailable(getActivity())) {
            UtilsCust.NetworkToast(getActivity());
        } else {
            String login_name_url = getResources().getString(R.string.login_fetch_name_cust_url);

            final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Fetching Your Name...", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, login_name_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loading.dismiss();
                    try {
                        if (!response.equals("Failed")) {
                            String FILENAME = "customername.txt";
                            try {
                                FileOutputStream fos = getActivity().getApplication().openFileOutput(FILENAME, Context.MODE_PRIVATE);
                                fos.write(response.getBytes());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        UtilsCust.ShortToast(getActivity(), e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    UtilsCust.ShortToast(getActivity(), error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("email", UtilsCust.loadCustEmail(getActivity()));
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.extra, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Customer.relativeLayout.setVisibility(View.VISIBLE);
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
package info.mayankag.parlorbeacon;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpCust extends DialogFragment
{
    private EditText custemailText;
    private EditText custpassText;
    private EditText custNameText;
    private EditText custPhoneText;
    private EditText custAddressText;
    private RadioGroup genderRadioGroup;
    private CheckBox agree;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.signup_cust, container, false);
        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        toolbar.setTitle("Signup");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        custNameText = (EditText)v.findViewById(R.id.signup_cust_name);
        custemailText = (EditText)v.findViewById(R.id.signup_cust_email);
        custpassText = (EditText)v.findViewById(R.id.signup_cust_pass);
        custPhoneText = (EditText)v.findViewById(R.id.signup_cust_phone);
        custAddressText = (EditText)v.findViewById(R.id.signup_cust_address);

        genderRadioGroup = (RadioGroup)v.findViewById(R.id.genderRadioGroup);

        agree = (CheckBox)v.findViewById(R.id.acceptTC);
        Button signup = (Button) v.findViewById(R.id.signup_cust_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(v);
            }
        });

        return v;
    }

    private void signup(View v)
    {
        if (!UtilsCust.isNetworkAvailable(getActivity())) {
            UtilsCust.NetworkToast(getActivity());
        } else {
            final String custemail = custemailText.getText().toString();
            final String custpass = custpassText.getText().toString();

            final String custname = custNameText.getText().toString();
            final String custphone = custPhoneText.getText().toString();
            final String custaddr = custAddressText.getText().toString();

            RadioButton genderRadioButton = (RadioButton) v.findViewById(genderRadioGroup.getCheckedRadioButtonId());
            final String custgender = genderRadioButton.getText().toString();

            if (custname.equals("") || custemail.equals("") || custpass.equals("") || custphone.equals("") || custaddr.equals("") || custgender.equals("")) {
                UtilsCust.ShortToast(getActivity(), "Please enter all the Details");
            } else if (!agree.isChecked()) {
                UtilsCust.ShortToast(getActivity(), "Please agree with Terms and Conditions");
            } else {
                if (!custemail.contains("@") && !custemail.contains(".com")) {
                    UtilsCust.ShortToast(getActivity(), "Please enter a valid email address");
                } else {
                    if (!(custpass.length() > 8)) {
                        UtilsCust.ShortToast(getActivity(), "Please Enter Password greater than 8 character");
                    } else {
                        String signup_url = getResources().getString(R.string.signup_cust_url);
                        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Signing Up", "Please Wait", true, true);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {

                                    if (response.equalsIgnoreCase(String.valueOf(0))) {
                                        UtilsCust.saveCustEmail(custemail,getActivity());
                                        UtilsCust.saveCustName(custname,getActivity());
                                        startActivity(new Intent(getActivity(), MainScreenCust.class));
                                        getActivity().finish();
                                    } else if (response.equalsIgnoreCase(String.valueOf(1))) {
                                        UtilsCust.ShortToast(getActivity(), "Sorry You have Already Registered Yet");
                                    } else if (response.equalsIgnoreCase(String.valueOf(2))) {
                                        UtilsCust.ShortToast(getActivity(), "Something missing");
                                    } else
                                    {
                                        UtilsCust.ShortToast(getActivity(), response);
                                    }
                                } catch (Exception e) {
                                    UtilsCust.ShortToast(getActivity(), "Error");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                UtilsCust.ShortToast(getActivity(), error.getMessage());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parameters = new HashMap<>();
                                parameters.put("name", custname);
                                parameters.put("email", custemail);
                                parameters.put("password", custpass);
                                parameters.put("shopaddress", custaddr);
                                parameters.put("phoneno", custphone);
                                parameters.put("gender", custgender);
                                return parameters;
                            }
                        };

                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(stringRequest);
                    }
                }
            }
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
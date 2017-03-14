package info.mayankag.parlorbeacon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

public class Customer extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @SuppressLint("StaticFieldLeak")
    static RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cust);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(!UtilsCust.loadCustEmail(this).equals(""))
        {
            startActivity(new Intent(this,MainScreenCust.class));
            finish();
        }

        relativeLayout = (RelativeLayout)findViewById(R.id.content_main);

    }

    public void onClickCustSignUp(View view) {
        relativeLayout.setVisibility(View.GONE);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        SignUpCust signUp = new SignUpCust();
        transaction.replace(android.R.id.content, signUp).addToBackStack(null).commit();
    }

    public void onClickCustLogin(View view) {
        relativeLayout.setVisibility(View.GONE);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        LoginCust login = new LoginCust();
        transaction.replace(android.R.id.content, login).addToBackStack(null).commit();
    }

}

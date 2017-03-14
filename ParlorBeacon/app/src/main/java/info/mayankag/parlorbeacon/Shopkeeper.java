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

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class Shopkeeper extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @SuppressLint("StaticFieldLeak")
    static RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!UtilsShop.loadShopEmail(this).equals(""))
        {
            startActivity(new Intent(this,MainScreenShop.class));
            finish();
        }

        relativeLayout = (RelativeLayout) findViewById(R.id.content_main_activity_shop);

        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Shopkeeper.relativeLayout.setVisibility(View.VISIBLE);
    }

    public void onClickShopSignUp(View view)
    {
        relativeLayout.setVisibility(View.GONE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        SignUpShop signUp = new SignUpShop();
        transaction.replace(android.R.id.content, signUp).addToBackStack(null).commit();
    }



    public void onClickShopLogin(View view)
    {
        relativeLayout.setVisibility(View.GONE);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        LoginShop login = new LoginShop();
        transaction.replace(android.R.id.content, login).addToBackStack(null).commit();
    }
}

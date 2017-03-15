package info.mayankag.parlorbeacon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);


        String preference = UtilsShop.loadPreference(this);
        if (!preference.equals("")) {
            if (preference.equals("Shopkeeper")) {
                startActivity(new Intent(this, Shopkeeper.class));
            } else {
                startActivity(new Intent(this, Customer.class));
            }
            finish();
        }
    }

    public void select(View view) {

        if (view.getId() == R.id.shopkeeperSelect) {
            UtilsShop.savePreference("Shopkeeper", this);
            startActivity(new Intent(this, Shopkeeper.class));
        } else {
            UtilsShop.savePreference("Customer", this);
            startActivity(new Intent(this,Customer.class));
        }
        finish();
    }
}

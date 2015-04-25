package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;


public class DetailActivity extends Activity {
    private ResultItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        item = MainActivity.items.get(intent.getIntExtra("itemID", 0));
        String superSized;
        if (!"".equals(item.basicInfo.get("pictureURLSuperSize"))) superSized = item.basicInfo.get("pictureURLSuperSize");
        else superSized = item.basicInfo.get("galleryURL");
        try {
            new ImgTask((ImageView) findViewById(R.id.superSize)).execute(new URL(superSized));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.dtlTitle)).setText(item.basicInfo.get("title"));
        String p = "Price: $" + item.basicInfo.get("convertedCurrentPrice") + " (";
        String shipCost = item.basicInfo.get("shippingServiceCost");
        if ("".equals(shipCost) || 0 == Double.parseDouble(shipCost)) p += "FREE Shipping)";
        else p += "+ $" + shipCost + " Shipping)";
        ((TextView) findViewById(R.id.pricePlusShip)).setText(p);
        ((TextView) findViewById(R.id.locFrom)).setText(item.basicInfo.get("location"));

        ((TextView) findViewById(R.id.cateName)).setText(item.basicInfo.get("categoryName"));
        ((TextView) findViewById(R.id.cond)).setText(item.basicInfo.get("conditionDisplayName"));
        ((TextView) findViewById(R.id.buyFmt)).setText(item.basicInfo.get("listingType"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

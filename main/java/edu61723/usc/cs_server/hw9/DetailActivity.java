package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;


public class DetailActivity extends Activity {
    final private static LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
    final private static LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(0, 0, 0.0f);
    private ResultItem item;
    private LinearLayout basicInfo;
    private LinearLayout sellerInfo;
    private LinearLayout shipInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        item = MainActivity.items.get(intent.getIntExtra("itemID", 0));
        new LoadInfoTask().execute(item);
    }

    private class LoadInfoTask extends AsyncTask<ResultItem, Void, Void> {

        @Override
        protected Void doInBackground(ResultItem... params) {
            ResultItem item = params[0];
            basicInfo = ((LinearLayout) findViewById(R.id.basicInfo));
            sellerInfo = ((LinearLayout) findViewById(R.id.sellerInfo));
            shipInfo = ((LinearLayout) findViewById(R.id.shipInfo));

            String superSized;
            if (!"".equals(item.basicInfo.get("pictureURLSuperSize")))
                superSized = item.basicInfo.get("pictureURLSuperSize");
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
            findViewById(R.id.buyItNow).setOnClickListener(new BrowseItemOnClickListener(item.basicInfo.get("viewItemURL"), DetailActivity.this));

            ((TextView) findViewById(R.id.cateName)).setText(item.basicInfo.get("categoryName"));
            ((TextView) findViewById(R.id.cond)).setText(item.basicInfo.get("conditionDisplayName"));
            ((TextView) findViewById(R.id.buyFmt)).setText(item.basicInfo.get("listingType"));

            ((TextView) findViewById(R.id.userName)).setText(item.sellerInfo.get("sellerUserName"));
            ((TextView) findViewById(R.id.fdbkScore)).setText(item.sellerInfo.get("feedbackScore"));
            ((TextView) findViewById(R.id.posFdbk)).setText(item.sellerInfo.get("positiveFeedbackPercent") + "%");
            ((TextView) findViewById(R.id.fdbkRate)).setText(item.sellerInfo.get("feedbackRatingStar"));
            ((TextView) findViewById(R.id.storeName)).setText(item.sellerInfo.get("sellerStoreName"));
            if ("true".equals(item.sellerInfo.get("topRatedSeller")))
                ((ImageView) findViewById(R.id.topRated)).setImageResource(R.mipmap.checkmark1_32);
            else {
                ((ImageView) findViewById(R.id.topRated)).setImageResource(R.mipmap.cancel_32);
                findViewById(R.id.topRatedIcon).setVisibility(View.INVISIBLE);
            }

            ((TextView) findViewById(R.id.shipType)).setText(item.shippingInfo.get("shippingType"));
            ((TextView) findViewById(R.id.hdlTime)).setText(item.shippingInfo.get("handlingTime"));
            ((TextView) findViewById(R.id.shipLoc)).setText(item.shippingInfo.get("shipToLocations"));
            if ("true".equals(item.shippingInfo.get("expeditedShipping")))
                ((ImageView) findViewById(R.id.expdShip)).setImageResource(R.mipmap.checkmark1_32);
            else ((ImageView) findViewById(R.id.expdShip)).setImageResource(R.mipmap.cancel_32);
            if ("true".equals(item.shippingInfo.get("oneDayShippingAvailable")))
                ((ImageView) findViewById(R.id.oneDShip)).setImageResource(R.mipmap.checkmark1_32);
            else ((ImageView) findViewById(R.id.oneDShip)).setImageResource(R.mipmap.cancel_32);
            if ("true".equals(item.shippingInfo.get("returnsAccepted")))
                ((ImageView) findViewById(R.id.retAcpt)).setImageResource(R.mipmap.checkmark1_32);
            else ((ImageView) findViewById(R.id.retAcpt)).setImageResource(R.mipmap.cancel_32);

            findViewById(R.id.basicInfoBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basicInfo.setLayoutParams(lp1);
                    sellerInfo.setLayoutParams(lp0);
                    shipInfo.setLayoutParams(lp0);
                }
            });
            findViewById(R.id.sellerInfoBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basicInfo.setLayoutParams(lp0);
                    sellerInfo.setLayoutParams(lp1);
                    shipInfo.setLayoutParams(lp0);
                }
            });
            findViewById(R.id.shipInfoBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basicInfo.setLayoutParams(lp0);
                    sellerInfo.setLayoutParams(lp0);
                    shipInfo.setLayoutParams(lp1);
                }
            });
            return null;
        }
    }
}

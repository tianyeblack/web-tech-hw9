package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.net.MalformedURLException;
import java.net.URL;


public class DetailActivity extends Activity {
    final private static LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
    final private static LinearLayout.LayoutParams lp0 = new LinearLayout.LayoutParams(0, 0, 0.0f);
    private ResultItem item;
    private LinearLayout basicInfo;
    private LinearLayout sellerInfo;
    private LinearLayout shipInfo;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();

        Intent intent = getIntent();
        item = MainActivity.items.get(intent.getIntExtra("itemID", 0));
        new LoadInfoTask().execute(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class ShareOnClickListener implements View.OnClickListener {
        private final String price;
        private ResultItem item;

        public ShareOnClickListener(ResultItem item, String price) {
            this.item = item;
            this.price = price;
        }
        @Override
        public void onClick(View v) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentTitle(item.basicInfo.get("title"))
                        .setImageUrl(Uri.parse(item.basicInfo.get("galleryURL")))
                        .setContentUrl(Uri.parse(item.basicInfo.get("viewItemURL")))
                        .setContentDescription(price + ", Location: " + item.basicInfo.get("location"))
                        .build();

                shareDialog.show(content, ShareDialog.Mode.FEED);
            }
        }

    }

    private class LoadInfoTask extends AsyncTask<ResultItem, Void, Void> {
        final Drawable normalBtn = getDrawable(R.drawable.normalbtn);
        final Drawable highlightBtn = getDrawable(R.drawable.highlightbtn);

        @Override
        protected Void doInBackground(ResultItem... params) {
            final ResultItem item = params[0];
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
            findViewById(R.id.fbShareIcon).setOnClickListener(new ShareOnClickListener(item, p));

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

            final Button bInfoBtn = (Button) findViewById(R.id.basicInfoBtn);
            bInfoBtn.setBackground(highlightBtn);
            final Button seInfoBtn = (Button) findViewById(R.id.sellerInfoBtn);
            final Button shInfoBtn = (Button) findViewById(R.id.shipInfoBtn);
            bInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bInfoBtn.setBackground(highlightBtn);
                    seInfoBtn.setBackground(normalBtn);
                    shInfoBtn.setBackground(normalBtn);
                    basicInfo.setLayoutParams(lp1);
                    sellerInfo.setLayoutParams(lp0);
                    shipInfo.setLayoutParams(lp0);
                }
            });
            seInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bInfoBtn.setBackground(normalBtn);
                    seInfoBtn.setBackground(highlightBtn);
                    shInfoBtn.setBackground(normalBtn);
                    basicInfo.setLayoutParams(lp0);
                    sellerInfo.setLayoutParams(lp1);
                    shipInfo.setLayoutParams(lp0);
                }
            });
            shInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bInfoBtn.setBackground(normalBtn);
                    seInfoBtn.setBackground(normalBtn);
                    shInfoBtn.setBackground(highlightBtn);
                    basicInfo.setLayoutParams(lp0);
                    sellerInfo.setLayoutParams(lp0);
                    shipInfo.setLayoutParams(lp1);
                }
            });

            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    if (result.getPostId() == null || "".equals(result.getPostId())) Toast.makeText(DetailActivity.this, "Facebook Post cancelled", Toast.LENGTH_LONG).show();
                    else Toast.makeText(DetailActivity.this, result.getPostId(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(DetailActivity.this, "Facebook Post cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(DetailActivity.this, "Facebook Post Error", Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }
    }
}

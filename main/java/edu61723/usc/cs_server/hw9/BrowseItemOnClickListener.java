package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class BrowseItemOnClickListener implements View.OnClickListener {
    String itemURL;
    Activity activity;

    public BrowseItemOnClickListener(String itemURL, Activity activity) {
        this.itemURL = itemURL;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse(itemURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }
}

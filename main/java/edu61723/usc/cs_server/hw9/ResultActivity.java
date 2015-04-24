package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ResultActivity extends Activity {
    ListView resultList;

    private class ItemAdapter extends BaseAdapter {
        ArrayList<ResultItem> items;
        Activity context;

        public ItemAdapter (Activity _context, ArrayList<ResultItem> _items) {
            context = _context;
            items = _items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }

            ResultItem item = items.get(position);
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            TextView itemTitle = (TextView) convertView.findViewById(R.id.itmTitle);
            TextView price = (TextView) convertView.findViewById(R.id.price);

            itemTitle.setText(item.basicInfo.get("title"));
            String p = "Price: $" + item.basicInfo.get("convertedCurrentPrice") + " (";
            String shipCost = item.basicInfo.get("shippingServiceCost");
            if ("".equals(shipCost) || 0 == Double.parseDouble(shipCost)) p += "FREE Shipping)";
            else p += "+ $" + shipCost + " Shipping";
            price.setText(p);

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        ArrayList<ResultItem> items = MainActivity.items;
        ((TextView) findViewById(R.id.resultTitle)).setText("Results for \'" + intent.getStringExtra("keywords") + "\'");
        resultList = (ListView) findViewById(R.id.resultList);
        resultList.setAdapter(new ItemAdapter(this, items));
    }
}

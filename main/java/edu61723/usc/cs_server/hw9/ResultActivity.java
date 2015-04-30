package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ResultActivity extends Activity {
    ListView resultList;
    TextView resultTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        ArrayList<ResultItem> items = MainActivity.items;

        resultTitle = (TextView) findViewById(R.id.resultTitle);
        resultTitle.setText("Results for \'" + intent.getStringExtra("keywords") + "\'");

        resultList = (ListView) findViewById(R.id.resultList);
        resultList.setAdapter(new ItemAdapter(this, items));
    }

    private class TitleOnClickListener implements View.OnClickListener {
        int position;

        public TitleOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
            intent.putExtra("itemID", position);
            startActivity(intent);
        }
    }

    private class ItemAdapter extends BaseAdapter {
        private final int height;
        private ArrayList<ResultItem> items;
        private Activity context;

        public ItemAdapter(Activity _context, ArrayList<ResultItem> _items) {
            context = _context;
            items = _items;
            height = 256;
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
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }

            ResultItem item = items.get(position);
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            img.setOnClickListener(new BrowseItemOnClickListener(item.basicInfo.get("viewItemURL"), ResultActivity.this));
            TextView itemTitle = (TextView) convertView.findViewById(R.id.itmTitle);
            itemTitle.setOnClickListener(new TitleOnClickListener(position));
            TextView price = (TextView) convertView.findViewById(R.id.price);

            itemTitle.setText(item.basicInfo.get("title"));
            String p = "Price: $" + item.basicInfo.get("convertedCurrentPrice") + " (";
            String shipCost = item.basicInfo.get("shippingServiceCost");
            if ("".equals(shipCost) || 0 == Double.parseDouble(shipCost)) p += "FREE Shipping)";
            else p += "+ $" + shipCost + " Shipping)";
            price.setText(p);

            img.setMaxHeight(height);
            convertView.setMinimumHeight(height);
            try {
                new ImgTask(img).execute(new URL(item.basicInfo.get("galleryURL")));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}

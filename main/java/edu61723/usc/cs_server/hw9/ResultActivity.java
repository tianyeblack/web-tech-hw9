package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class ResultActivity extends Activity {
    ListView resultList;
    TextView resultTitle;

    private class ImgTask extends AsyncTask<URL, Void, Bitmap> {
        ImageView imgView;

        public ImgTask(ImageView _imgView) {
            imgView = _imgView;
        }

        @Override
        protected Bitmap doInBackground(URL... params) {
            try {
                return BitmapFactory.decodeStream(params[0].openStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imgView.setImageBitmap(result);
        }
    }
    private class ItemAdapter extends BaseAdapter {
        private ArrayList<ResultItem> items;
        private Activity context;
        private final int height;

        public ItemAdapter (Activity _context, ArrayList<ResultItem> _items) {
            context = _context;
            items = _items;
            height = 200;
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
            TextView itemTitle = (TextView) convertView.findViewById(R.id.itmTitle);
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
}

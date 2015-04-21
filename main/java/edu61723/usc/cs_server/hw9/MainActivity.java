package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {

    private class RequestSearchTask extends AsyncTask<URL, Integer, Long> {
        @Override
        protected Long doInBackground(URL... params) {
            return new Long(0);
        }

        @Override
        protected void onPostExecute(Long result) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("EbaySearch");
        setContentView(R.layout.activity_main);
        Spinner sortBy = (Spinner) findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortBy_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setAdapter(adapter);

        Button clrBtn = (Button) findViewById(R.id.clrBtn);
        clrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.keywords)).setText("");
                ((EditText) findViewById(R.id.priceFrom)).setText("");
                ((EditText) findViewById(R.id.priceTo)).setText("");
                ((Spinner) findViewById(R.id.sortBy)).setSelection(0);
            }
        });

        Button srchBtn = (Button) findViewById(R.id.srchBtn);
        srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = ((EditText) findViewById(R.id.keywords)).getText().toString();
                Double priceFrom;
                priceFrom = new Double(((EditText) findViewById(R.id.priceFrom)).getText().toString());
                Double priceTo;
                priceTo = new Double(((EditText) findViewById(R.id.priceTo)).getText().toString());
                Integer sortBy;
                switch (((Spinner) findViewById(R.id.sortBy)).getSelectedItemPosition()) {
                    case 0:
                        sortBy = new Integer(0);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;

                    default:
                        break;
                }
                URL qURL = null;
                try {
                    qURL = new URL("HTTP", "hw8-yetian-env.elasticbeanstalk.com", "index.php?");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (qURL != null) {
                    new RequestSearchTask().execute(qURL);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

package edu61723.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {

    private View.OnFocusChangeListener ofcl = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (hasFocus) imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            else imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    };

    private class RequestSearchTask extends AsyncTask<URL, Void, Long> {
        JSONObject response;
        @Override
        protected Long doInBackground(URL... params) {
            for (URL param : params) {
                try {
                    URLConnection conn = param.openConnection();
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String result = "";
                    for (String ln; (ln = bufReader.readLine()) != null; ) result += ln;
                    try {
                        response = new JSONObject(result);
                    } catch (JSONException je) {
                        je.printStackTrace();
                        return 1L;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return 1L;
                }
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (result == 0) {
                if (response != null) {

                }
            }
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
                ((TextView) findViewById(R.id.errMsg)).setText("");
            }
        });

        Button srchBtn = (Button) findViewById(R.id.srchBtn);
        srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errMsg = "";
                String keywords = ((EditText) findViewById(R.id.keywords)).getText().toString();
                if (keywords.isEmpty()) {
                    errMsg += "Please enter a keyword.\n";
                }
                String priceFrom = ((EditText) findViewById(R.id.priceFrom)).getText().toString();
                String priceTo = ((EditText) findViewById(R.id.priceTo)).getText().toString();
                if (!priceFrom.isEmpty()) {
                    boolean pfNum = false;
                    double pf = 0;
                    try {
                        pf = Double.parseDouble(priceFrom);
                        pfNum = true;
                        if (pf < 0) {
                            errMsg += "Minimum price cannot be below 0.\n";
                        }
                    } catch (NumberFormatException nfe) {
                        errMsg += "Price (From) should be a valid number.\n";
                    }
                    if (!priceTo.isEmpty()) {
                        double pt;
                        try {
                            pt = Double.parseDouble(priceTo);
                            if (pt < 0) {
                                errMsg += "Maximum price cannot be below 0.\n";
                            }
                            if (pfNum && pf > pt) {
                                errMsg += "Minimum price cannot be more than maximum price.\n";
                            }
                        } catch (NumberFormatException nfe) {
                            errMsg += "Price (To) should be a valid number.\n";
                        }
                    }
                } else {
                    if (!priceTo.isEmpty()) {
                        try {
                            double pt = Double.parseDouble(priceTo);
                            if (pt < 0) {
                                errMsg += "Maximum price cannot be below 0.\n";
                            }
                        } catch (NumberFormatException nfe) {
                            errMsg += "Price (To) should be a valid number.\n";
                        }
                    }
                }
                if (!errMsg.isEmpty()) {
                    ((TextView) findViewById(R.id.errMsg)).setText(errMsg);
                    return;
                }
                String sortBy;
                switch (((Spinner) findViewById(R.id.sortBy)).getSelectedItemPosition()) {
                    case 0:
                        sortBy = "BestMatch";
                        break;
                    case 1:
                        sortBy = "CurrentPriceHighest";
                        break;
                    case 2:
                        sortBy = "PricePlusShippingHighest";
                        break;
                    case 3:
                        sortBy = "PricePlusShippingLowest";
                        break;

                    default:
                        sortBy = "BestMatch";
                        break;
                }
                String query = "/?keywords=" + keywords + "&lowrange=" + priceFrom + "&highrange=" + priceTo + "&sortby=" + sortBy + "&resultpp=5&pgNum=1";
                URL qURL = null;
                try {
                    qURL = new URL("http", "hw8-yetian-env.elasticbeanstalk.com", query);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (qURL != null) {
                    new RequestSearchTask().execute(qURL);
                }
            }
        });

        findViewById(R.id.keywords).setOnFocusChangeListener(ofcl);
        findViewById(R.id.priceFrom).setOnFocusChangeListener(ofcl);
        findViewById(R.id.priceTo).setOnFocusChangeListener(ofcl);
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

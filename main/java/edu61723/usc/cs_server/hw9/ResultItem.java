package edu61723.usc.cs_server.hw9;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class ResultItem {
    public long id = -1;
    public HashMap<String, String> basicInfo = new HashMap<>();
    public HashMap<String, String> sellerInfo = new HashMap<>();
    public HashMap<String, String> shippingInfo = new HashMap<>();

    public ResultItem(long _id, JSONObject _basicInfo, JSONObject _sellerInfo, JSONObject _shippingInfo) {
        id = _id;
        String key;
        Iterator<String> iter;
        if (_basicInfo != null) {
            iter = _basicInfo.keys();
            while (iter.hasNext()) {
                key = iter.next();
                if ("".equals(_basicInfo.optString(key)) && !"shippingServiceCost".equals(key))
                    basicInfo.put(key, "N/A");
                else basicInfo.put(key, _basicInfo.optString(key, "N/A"));
            }
        }
        if (_sellerInfo != null) {
            iter = _sellerInfo.keys();
            while (iter.hasNext()) {
                key = iter.next();
                if ("".equals(_sellerInfo.optString(key))) sellerInfo.put(key, "N/A");
                else sellerInfo.put(key, _sellerInfo.optString(key, "N/A"));
            }
        }
        if (_shippingInfo != null) {
            iter = _shippingInfo.keys();
            while (iter.hasNext()) {
                key = iter.next();
                if ("".equals(_shippingInfo.optString(key))) shippingInfo.put(key, "N/A");
                else shippingInfo.put(key, _shippingInfo.optString(key, "N/A"));
            }
        }
    }
}

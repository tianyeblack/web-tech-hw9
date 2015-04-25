package edu61723.usc.cs_server.hw9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Peter on 4/24/2015 0024.
 */
public class ImgTask extends AsyncTask<URL, Void, Bitmap> {
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

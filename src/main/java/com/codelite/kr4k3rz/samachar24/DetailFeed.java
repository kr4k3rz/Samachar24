package com.codelite.kr4k3rz.samachar24;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar24.model.ItemItem;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/*
* shows the details of the  every feeds clicked*/
public class DetailFeed extends AppCompatActivity {
    private static final String TAG = DetailFeed.class.getSimpleName();
    private ItemItem entry;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);
        entry = (ItemItem) getIntent().getSerializableExtra("ENTRY");

        TextView title = (TextView) findViewById(R.id.title_detail);
        TextView date = (TextView) findViewById(R.id.date_detail);
        TextView content = (TextView) findViewById(R.id.content_detail);
        ImageView imageView = (ImageView) findViewById(R.id.detailImageView);
        TextView author = (TextView) findViewById(R.id.authorTv);

        Log.d("DETAIL FEEDS", "Entry : " + entry.getTitle());
        content.setTextSize(getSharedPreferences("setting", MODE_PRIVATE).getFloat("textsize", 16));
        title.setText(entry.getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM ''yy, HH:mm ", Locale.ENGLISH);
        date.setText(simpleDateFormat.format(Date.parse(entry.getPubDate())));
        Log.i(TAG, "" + entry.getEncoded());

       /*use to remove the TAG */
      /*  String check = "<p>The post <a rel=\"nofollow\" href=\"" + entry.getLink() + "\">" + entry.getTitle() + "</a> appeared first on <a rel=\"nofollow\" href=\"" + entry.getLink() + "\">" + entry.getTitle() + "</a>.</p>";
        Log.i(TAG, "" + check);

        String contentHtml = entry.getEncoded().replace(check, "");*/
        content.setText(Html.fromHtml(entry.getEncoded(), Parse.EMPTY_IMAGE_GETTER, null));

        author.setText(entry.getCreator());

        displayImage(imageView);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void displayImage(ImageView imageView) {
        String actualUrl = null;
        String url = Parse.parseImg(entry.getEncoded());
        try {
            actualUrl = Parse.convertImgUrl(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Glide.with(getApplicationContext())
                .load(actualUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DetailFeed Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

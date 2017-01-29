package com.codelite.kr4k3rz.samachar24;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar24.model.ItemItem;
import com.codelite.kr4k3rz.samachar24.model.ResponseFeed;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final ArrayList<String> rss = new NewspaperNP().getLinksList();
    private final List<ItemItem> itemItemsList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Handler mHandler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler(Looper.getMainLooper());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_layout);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        if (!Paper.book().exist("CHECK")) {
            loadFeeds();
            Paper.book().write("CHECK", true);
        } else {
            List<ItemItem> itemItems = Paper.book().read("FEEDS");
            recyclerView.setAdapter(new CVAdapter(getApplicationContext(), itemItems));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckInternet.isNetworkAvailable(getApplicationContext()))
                    loadFeeds();
                else {
                    SnackMsg.showMsgLong(recyclerView, "connect internet ");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loadFeeds() {
        for (int i = 0; i < rss.size(); i++) {
            Request request = new Request.Builder().url(rss.get(i)).build();
            final int finalI = i;
            final int finalI1 = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    okHttpClient.dispatcher().cancelAll();
                    Log.d("TAG", "" + e.getMessage());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            List<ItemItem> tempList;
                            tempList = Parse.deleteEnglishFeeds(itemItemsList);
                            tempList = Parse.deleteDuplicate(tempList);
                            tempList = Parse.sortByTime(tempList);
                            tempList = Parse.deleteEnglishFeeds(tempList);
                            recyclerView.setAdapter(new CVAdapter(getApplicationContext(), tempList));
                            if (finalI == rss.size() - 1)
                                swipeRefreshLayout.setRefreshing(false);
                            Paper.book().write("FEEDS", tempList);
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();
                    Log.d("TAG", "" + jsonStr);
                    Gson gson = new Gson();
                    ResponseFeed responseFeed = gson.fromJson(jsonStr, ResponseFeed.class);

                    try {
                        final List<ItemItem> itemItems = responseFeed.getQuery().getResults().getRss().getChannel().getItem();
                        assert itemItems != null;
                        itemItemsList.addAll(itemItems);
                        Log.d("TAG", "size : " + responseFeed.getQuery().getCount());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                List<ItemItem> tempList;
                                tempList = Parse.deleteEnglishFeeds(itemItemsList);
                                tempList = Parse.deleteDuplicate(tempList);
                                tempList = Parse.sortByTime(tempList);
                                tempList = Parse.deleteEnglishFeeds(tempList);
                                recyclerView.setAdapter(new CVAdapter(getApplicationContext(), tempList));
                                if (finalI1 == rss.size() - 1)
                                    swipeRefreshLayout.setRefreshing(false);
                                Paper.book().write("FEEDS", tempList);

                            }
                        });


                    } catch (Exception e) {
                        Log.d("TAG", "Rss error");
                    }


                }
            });


        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
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


    class CVAdapter extends RecyclerView.Adapter<CVAdapter.ViewHolder> {
        private final Context context;
        private final List<ItemItem> itemItemList;

        CVAdapter(Context context, List<ItemItem> itemItemsList) {
            this.context = context;
            this.itemItemList = itemItemsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final ItemItem itemItem = itemItemList.get(position);

            String actualUrl = null;
            String url = Parse.parseImg(itemItem.getEncoded());
            try {
                actualUrl = convertImgUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            holder.title.setText(itemItem.getTitle());
            try {
                holder.source.setText(Parse.capitalize(String.format("%s", Parse.getSource(itemItem.getLink()))));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

            holder.date.setText(String.format("%s", Parse.convertLongDateToAgoString(Date.parse(itemItem.getPubDate()), System.currentTimeMillis()))); //to set date time in '3 minutes ago' like
            holder.date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            Glide.with(context)
                    .load(actualUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailFeed.class);
                    intent.putExtra("ENTRY", itemItem);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemItemList.size();
        }

        private String convertImgUrl(String url) throws MalformedURLException {
            if (url != null && url.startsWith("http://")) {
                if (url.toLowerCase().contains(".png".toLowerCase())) {
                    URL url1 = new URL(url);
                    String tempUrl = url1.getHost() + ".rsz.io" + url1.getPath() + "?format=jpg";
                    url = "http://images.weserv.nl/?url=" + tempUrl + "&w=300&h=300&q=10";
                    Log.i("PNG TAG", "" + url);
                } else {
                    url = "http://images.weserv.nl/?url=" + url.replace("http://", "") + "&w=300&h=300&q=10";
                    Log.i("TAG", " String to be shows : " + url);
                }
            } else Log.i("TAG", " String is null");
            return url;
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView title;
            final TextView date;
            final CardView cardView;
            final ImageView imageView;
            final TextView source;

            ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                date = (TextView) itemView.findViewById(R.id.date);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                source = (TextView) itemView.findViewById(R.id.source);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }
    }

}

package com.muraluniversitario;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Script;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.muraluniversitario.model.Event;
import com.muraluniversitario.service.EventWS;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventDetailsActivity extends BaseActivity {

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub) findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.content_event_detail);
        View inflated = stub.inflate();

        Intent intent = getIntent();
        String id = intent.getStringExtra("event_id");

        Log.w(this.getClass().getName(), id);

        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        final Gson gson = builder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/mural-universitario/rest/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final EventWS service = retrofit.create(EventWS.class);
        Call<Event> getEventsService = service.getEvent(id);

        getEventsService.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event event = response.body();
                loadEvent(event);
                Log.w(this.getClass().getName(), event.getDescription());
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadEvent(final Event event) {
        final ImageView image = (ImageView) findViewById(R.id.event_image);
        TextView name = (TextView) findViewById(R.id.event_name);
        TextView description = (TextView) findViewById(R.id.event_description);
        TextView place = (TextView) findViewById(R.id.event_place);
        TextView begin = (TextView) findViewById(R.id.event_begin);
        TextView end = (TextView) findViewById(R.id.event_end);
        TextView price = (TextView) findViewById(R.id.event_price);
        ImageView facebook = (ImageView) findViewById(R.id.event_facebook);
        ImageView website = (ImageView) findViewById(R.id.event_website);

        if (event.getFacebook().length() == 0) {
            facebook.setVisibility(View.GONE);
        }

        if (event.getWebsite().length() == 0) {
            website.setVisibility(View.GONE);
        }

        Picasso.with(this)
                .load("http://10.0.2.2:8080/mural-universitario/"+event.getImage())
                .into(image);

        final Intent fullscreenImage = new Intent(this, FullscreenImageActivity.class);
        fullscreenImage.putExtra("image", "http://10.0.2.2:8080/mural-universitario/"+event.getImage());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(fullscreenImage);
            }
        });

        Log.w(this.getClass().getName(),"http://10.0.2.2:8089/mural-universitario/"+event.getImage());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        name.setText(event.getName());
        description.setText(event.getDescription());
        place.setText(event.getPlace());
        begin.setText(dateFormat.format(event.getBegin()));
        end.setText(dateFormat.format(event.getEnd()));
        price.setText(event.getPrice());

        final Intent intentPlace = new Intent(this, MapsActivity.class);
        intentPlace.putExtra("place", event.getPlace());
        intentPlace.putExtra("event", event.getName());

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentPlace);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = loadFacebook(getPackageManager(), event.getFacebook());
                startActivity(i);
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getWebsite()));
                startActivity(browserIntent);
            }
        });
    }

    public Intent loadFacebook(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {}

        return new Intent(Intent.ACTION_VIEW, uri);
    }

}

package com.muraluniversitario;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.muraluniversitario.database.dao.CategoryDAO;
import com.muraluniversitario.database.dao.InstitutionDAO;
import com.muraluniversitario.model.Category;
import com.muraluniversitario.model.Event;
import com.muraluniversitario.model.Institution;
import com.muraluniversitario.service.EventWS;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;

public class MainActivity extends BaseActivity {

    private CategoryDAO categoryDAO;
    private InstitutionDAO institutionDAO;
    private List<Event> events;

    public static void hideSoftKeyboard (Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub) findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.content_main);
        View inflated = stub.inflate();

        final Button btnSearch = (Button) findViewById(R.id.btn_search_events);
        final EditText txtSearch = (EditText) findViewById(R.id.txt_search_events);

        categoryDAO = new CategoryDAO(this);
        institutionDAO = new InstitutionDAO(this);

        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        final Gson gson = builder.create();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearch.clearFocus();
                hideSoftKeyboard(MainActivity.this, v);

                List<String> categories = categoryDAO.getSelecteds();
                List<String> institutions = institutionDAO.getSelecteds();

                Log.w(this.getClass().getName(), categories.size()+" "+institutions.size());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.0.127:8089/mural-universitario/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final EventWS service = retrofit.create(EventWS.class);
                Call<List<Event>> getEventsService = service.getEvents(categories, institutions);

                getEventsService.enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        events = response.body();
                        for (Event e : events) {
                            Log.i(this.getClass().getName(), "Teste:"+e.getId());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

    }

}
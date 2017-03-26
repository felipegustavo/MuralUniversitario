package com.muraluniversitario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;

public class MainActivity extends BaseActivity {

    private CategoryDAO categoryDAO = new CategoryDAO(this);
    private InstitutionDAO institutionDAO = new InstitutionDAO(this);
    private List<Event> events = new ArrayList<Event>();

    private ListView listView = null;

    public static void hideSoftKeyboard (Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reload) {
            loadData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub) findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.content_main);
        View inflated = stub.inflate();

        loadData();
    }

    public void loadData() {
        GsonBuilder builder = new GsonBuilder();

        final Gson gson = builder.create();

        List<String> categories = categoryDAO.getSelecteds();
        List<String> institutions = institutionDAO.getSelecteds();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/mural-universitario/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final EventWS service = retrofit.create(EventWS.class);
        Call<List<Event>> getEventsService = service.getEvents(categories, institutions);

        getEventsService.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events.clear();

                if (response.body() != null) {
                    events.addAll(response.body());
                    displayListView();
                }

                for (Event e : events) {
                    Log.w(this.getClass().getName(), e.getName());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void displayListView() {
        listView = (ListView) findViewById(R.id.listview_events);
        ArrayAdapter<Event> eventHistory = new ArrayAdapter<Event>(this, R.layout.event_info_simple,
                R.id.text_institution, events);

        listView.setAdapter(eventHistory);
        final Intent intent = new Intent(this, EventDetailsActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);
                intent.putExtra("event_id", event.getId());
                startActivity(intent);
            }
        });
    }

}
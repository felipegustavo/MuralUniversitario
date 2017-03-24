package com.muraluniversitario;

import android.content.Context;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muraluniversitario.database.dao.CategoryDAO;
import com.muraluniversitario.model.Category;

import java.util.ArrayList;
import java.util.List;


public class ShowCategoriesActivity extends BaseActivity {

    private CustomAdapter dataAdapter;
    private CategoryDAO categoryDAO;
    private List<Category> categoryList;

    private boolean categoriesState = true;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub) findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.content_show_categories);
        View inflated = stub.inflate();

        final Button btnAll = (Button) findViewById(R.id.btn_categories_all);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Category c : categoryList) {
                    c.setSelected(categoriesState);
                }

                if (categoriesState) {
                    btnAll.setText(R.string.btn_deselect_all);
                } else {
                    btnAll.setText(R.string.btn_select_all);
                }

                categoryDAO.updateAllStates(categoriesState);
                categoriesState = !categoriesState;
                dataAdapter.notifyDataSetChanged();
            }
        });

        categoryDAO = new CategoryDAO(this);

        displayListView();
    }

    private void displayListView() {
        categoryList = categoryDAO.getAll();

        dataAdapter = new CustomAdapter(this, R.layout.category_info, categoryList);
        ListView listView = (ListView) findViewById(R.id.listview_categories);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        category.getName()+" : "+category.getDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private class CustomAdapter extends ArrayAdapter<Category> {

        private List<Category> categoryList;

        public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Category> categoryList) {
            super(context, resource, categoryList);
            this.categoryList = categoryList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.category_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.text_category);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkbox_category);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Category category = (Category) cb.getTag();

                        String text;
                        if (cb.isChecked()) {
                            text = "Você selecionou a categoria " + category.getName();
                        } else {
                            text = "Você desselecionou a categoria " + category.getName();
                        }

                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        category.setSelected(cb.isChecked());

                        categoryDAO.updateState(category.getId(), category.isSelected());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Category category = categoryList.get(position);
            holder.code.setText(category.getName());
            holder.code.setCompoundDrawablesWithIntrinsicBounds(getResources().
                    getIdentifier("ic_"+category.getId(), "drawable", getPackageName()), 0, 0, 0);

            holder.name.setChecked(category.isSelected());
            holder.name.setTag(category);

            return convertView;
        }

    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

}


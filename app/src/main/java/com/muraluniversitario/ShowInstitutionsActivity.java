package com.muraluniversitario;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
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

import com.muraluniversitario.database.dao.InstitutionDAO;
import com.muraluniversitario.model.Institution;

import java.util.List;

public class ShowInstitutionsActivity extends BaseActivity {

    private CustomAdapter dataAdapter;
    private InstitutionDAO institutionDAO;
    private List<Institution> institutionList;
    private boolean institutionsState = true;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub) findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.content_show_institutions);
        View inflated = stub.inflate();

        final Button btnAll = (Button) findViewById(R.id.btn_institutions_all);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Institution i : institutionList) {
                    i.setSelected(institutionsState);
                }

                if (institutionsState) {
                    btnAll.setText(R.string.btn_deselect_all);
                } else {
                    btnAll.setText(R.string.btn_select_all);
                }

                institutionDAO.updateAllStates(institutionsState);
                institutionsState = !institutionsState;
                dataAdapter.notifyDataSetChanged();
            }
        });

        institutionDAO = new InstitutionDAO(this);

        displayListView();
    }

    private void displayListView() {
        institutionList = institutionDAO.getAll();

        dataAdapter = new CustomAdapter(this, R.layout.institution_info, institutionList);
        ListView listView = (ListView) findViewById(R.id.listview_institutions);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Institution institution = (Institution) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        institution.getName()+" : "+institution.getDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private class CustomAdapter extends ArrayAdapter<Institution> {

        private List<Institution> institutionList;

        public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Institution> institutionList) {
            super(context, resource, institutionList);
            this.institutionList = institutionList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.institution_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.text_institution);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkbox_institution);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Institution institution = (Institution) cb.getTag();

                        String text;
                        if (cb.isChecked()) {
                            text = "Você selecionou a instituição " + institution.getName();
                        } else {
                            text = "Você desselecionou a instituição " + institution.getName();
                        }

                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        institution.setSelected(cb.isChecked());

                        institutionDAO.updateState(institution.getId(), institution.isSelected());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Institution institution = institutionList.get(position);
            holder.code.setText(institution.getName());
            holder.code.setCompoundDrawablesWithIntrinsicBounds(getResources().
                            getIdentifier("ic_"+institution.getId(), "drawable", getPackageName()),0, 0, 0);

            holder.name.setChecked(institution.isSelected());
            holder.name.setTag(institution);

            return convertView;
        }

    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

}
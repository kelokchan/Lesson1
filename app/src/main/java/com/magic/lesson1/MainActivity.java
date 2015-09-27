package com.magic.lesson1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements TextWatcher, AdapterView.OnItemClickListener {

    EmployeeDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list);

        db = new EmployeeDatabase(this);


        EditText searchText = (EditText) findViewById(R.id.el_searchText);
        searchText.addTextChangedListener(this);

        ListView listView = (ListView) findViewById(R.id.el_list);
        listView.setOnItemClickListener(this);

        //display all employees by searching null
        search("");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //dont care
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //dont care
    }

    @Override
    public void afterTextChanged(Editable s) {
        search(s.toString());
    }

    public void search(String searchText) {
        EmployeeAdapter adapter = new EmployeeAdapter(this, db.searchByName(searchText));
        ListView listView = (ListView) findViewById(R.id.el_list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Get item from the adapter and cast it to an Employee instance
        EmployeeDatabase.Employee employee = (EmployeeDatabase.Employee) parent.getItemAtPosition(position);

        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra("id", employee.id);
        startActivity(intent);
    }
}

package com.magic.lesson1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements TextWatcher, AdapterView.OnItemClickListener {

    public static Employee selectedEmployee = null;

    EmployeeDatabase db;
    boolean isSelectMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.employee_list);

        //Respond to select mode
        isSelectMode = getIntent().getBooleanExtra("isSelectMode", false);
        if (isSelectMode) {
            findViewById(R.id.el_newButton).setVisibility(View.GONE);
        }


        db = new EmployeeDatabase(this);


        EditText searchText = (EditText) findViewById(R.id.el_searchText);
        searchText.addTextChangedListener(this);

        ListView listView = (ListView) findViewById(R.id.el_list);
        listView.setOnItemClickListener(this);
        //display all employees by searching null
        search("");

    }

    @Override
    protected void onResume() {
        super.onResume();
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

        findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar_horizontal).setVisibility(View.VISIBLE);
        MagicApiRequest<Employee[]> request = new MagicApiRequest<Employee[]>(Employee[].class) {
            @Override
            public void onSucess(Employee[] o, String json) {
                EmployeeAdapter adapter = new EmployeeAdapter(MainActivity.this, o);
                ListView listView = (ListView) findViewById(R.id.el_list);
                listView.setAdapter(adapter);
                findViewById(R.id.progressBar3).setVisibility(View.GONE);
                findViewById(R.id.progressBar_horizontal).setVisibility(View.GONE);
            }

            @Override
            public void onFailed(Throwable e) {
                Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
            }
        };

        request.execute("search.php", "q", searchText);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Get item from the adapter and cast it to an Employee instance
        Employee employee = (Employee) parent.getItemAtPosition(position);

        if (isSelectMode) {
            //Announce to caller that selected
            selectedEmployee = employee;

            setResult(RESULT_OK);
            finish();
            return;
        }

        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra("ID", employee.ID);
        startActivity(intent);
    }

    public void onAddEmployee(View button) {
        Snackbar.make(button, "FAB Clicked", Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, CreateEmployeeActivity.class);
        startActivity(intent);
    }

}

package com.magic.lesson1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class EmployeeDetailsActivity extends AppCompatActivity {

    static final String TAG = "EmployeeDetailsActivity";
    EmployeeDatabase.Employee employee = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_details);

        String employeeId = getIntent().getStringExtra("id");

        //This is not creating a new database, it is not to gain access to current db
        EmployeeDatabase db = new EmployeeDatabase(this);

        //From database, get employee table data
        employee = db.getEmployee(employeeId);

        TextView nameView = (TextView) findViewById(R.id.ed_name);
        TextView positionView = (TextView) findViewById(R.id.ed_position);
        TextView officePhoneView = (TextView) findViewById(R.id.ed_officeText);
        TextView hpView = (TextView) findViewById(R.id.ed_hpText);
        TextView emailVIew = (TextView) findViewById(R.id.ed_emailText);
        TextView supervisorView = (TextView) findViewById(R.id.ed_supervisorText);
        TextView idView = (TextView) findViewById(R.id.ed_employeeID);

        nameView.setText(employee.name);
        positionView.setText(employee.position + ", " + employee.department);
        officePhoneView.setText("Call Office: "  + employee.officePhone);
        hpView.setText("Call HP: "  + employee.cellPhone);
        supervisorView.setText("Supervisor: " + employee.supervisorName );
        emailVIew.setText("Email: " + employee.email);
        idView.setText("ID: " + employee.id);
    }

    public void callOffice(View Button){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + employee.officePhone));
        startActivity(intent);
    }

    public void callHP(View Button){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + employee.cellPhone));
        startActivity(intent);
    }

    public void email(View Button){
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + employee.email));
        intent.setType("text/plain");
        startActivity(intent);
    }

    public void viewSupervisor(View Button){

    }

}

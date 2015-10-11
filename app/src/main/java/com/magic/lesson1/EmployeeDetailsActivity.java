package com.magic.lesson1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class EmployeeDetailsActivity extends AppCompatActivity {

    static final String TAG = "EmployeeDetailsActivity";
    Employee employee = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_details);

        String employeeId = getIntent().getStringExtra("ID");

        MagicApiRequest<Employee> request = new MagicApiRequest<Employee>(Employee.class) {
            @Override
            public void onSucess(Employee o, String json) {
                showDetails(o);
            }

            @Override
            public void onFailed(Throwable e) {
                Toast.makeText(EmployeeDetailsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        request.execute("get.php", "ID", employeeId);
        findViewById(R.id.content).setVisibility(View.GONE);
    }

    public void showDetails(Employee employee) {
        View contentView = findViewById(R.id.content);
        contentView.setVisibility(View.VISIBLE);
        contentView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));

        findViewById(R.id.progressBar2).setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        this.employee = employee;
        TextView nameView = (TextView) findViewById(R.id.ed_name);
        TextView positionView = (TextView) findViewById(R.id.ed_position);
        TextView officePhoneView = (TextView) findViewById(R.id.ed_officeText);
        TextView hpView = (TextView) findViewById(R.id.ed_hpText);
        TextView emailVIew = (TextView) findViewById(R.id.ed_emailText);
        TextView supervisorView = (TextView) findViewById(R.id.ed_supervisorText);
        TextView idView = (TextView) findViewById(R.id.ed_employeeID);
        View supervisorContent = findViewById(R.id.el_supervisorContent);

        nameView.setText(employee.name);
        positionView.setText(employee.position + ", " + employee.department);
        officePhoneView.setText("Call Office: " + employee.officePhone);
        hpView.setText("Call HP: " + employee.cellPhone);
        supervisorView.setText("Supervisor: " + employee.supervisorName);
        emailVIew.setText("Email: " + employee.email);
        idView.setText("Employee ID: " + employee.ID);
        if (employee.supervisorID == null) {
            //GONE = totally gone, does not take any space unlike INVISIBLE
            supervisorContent.setVisibility(View.GONE);
        } else {
            supervisorView.setText("Supervisor: " + employee.supervisorName);
        }

    }

    public void callOffice(View Button) {
        Toast.makeText(this, "Hi", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + employee.officePhone));
        startActivity(intent);
    }

    public void callHP(View Button) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + employee.cellPhone));
        startActivity(intent);
    }

    public void email(View Button) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + employee.email));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{employee.email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Email intent successful");
        intent.putExtra(Intent.EXTRA_TEXT, "Testing");
        startActivity(intent);
    }

    public void viewSupervisor(View Button) {
        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra("ID", employee.supervisorID);
        startActivity(intent);
    }

}

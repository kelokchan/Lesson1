package com.magic.lesson1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEmployeeActivity extends AppCompatActivity implements View.OnTouchListener {
    static final String TAG = "CreateEmployeeActivity";

    boolean hasSupervisor = true;
    Employee supervisor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);

        findViewById(R.id.ec_supervisorName).setOnTouchListener(this);
    }

    public void onToggleSupervisor(View view) {
        Log.d(TAG, "Supervisor checkbox toggled");

        CheckBox checkBox = (CheckBox) view;
        hasSupervisor = checkBox.isChecked();

        if (hasSupervisor) {
            findViewById(R.id.ec_supervisorLayout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.ec_supervisorLayout).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_UP)
            return false;

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isSelectMode", true);
        startActivityForResult(intent, 1001, null);

        //prevent keyboard popup
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            supervisor = MainActivity.selectedEmployee;
            ((TextView) findViewById(R.id.ec_supervisorName)).setText(supervisor.name);
        }
    }

    public void submit(View button) {
        String name = ((TextView) findViewById(R.id.ec_name)).getEditableText().toString();
        String id = ((TextView) findViewById(R.id.ec_id)).getEditableText().toString();
        String position = ((TextView) findViewById(R.id.ec_position)).getEditableText().toString();
        String department = ((TextView) findViewById(R.id.ec_department)).getEditableText().toString();
        String officePhone = ((TextView) findViewById(R.id.ec_officePhone)).getEditableText().toString();
        String personalPhone = ((TextView) findViewById(R.id.ec_personalPhone)).getEditableText().toString();
        String email = ((TextView) findViewById(R.id.ec_email)).getEditableText().toString();
        String monthlySalary = ((TextView) findViewById(R.id.ec_salary)).getEditableText().toString();
        String annualLeaves = ((TextView) findViewById(R.id.ec_annualLeaves)).getEditableText().toString();
        String supervisorID = supervisor == null ? "" : supervisor.ID;

        MagicApiRequest<Employee> request = new MagicApiRequest<Employee>(Employee.class){
            @Override
            public void onSucess(Employee o, String json) {
                Toast.makeText(CreateEmployeeActivity.this, "New employee added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Throwable e) {
                Toast.makeText(CreateEmployeeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        };

        request.execute(
                "create.php",
                "ID",               id,
                "name",             name,
                "position",         position,
                "cellPhone",        personalPhone,
                "officePhone",      officePhone,
                "department",       department,
                "email",            email,
                "salary",           monthlySalary,
                "profileUrl",       "",
                "annualLeavesLeft", annualLeaves,
                "supervisorID",     supervisorID
        );
        finish();
    }
}

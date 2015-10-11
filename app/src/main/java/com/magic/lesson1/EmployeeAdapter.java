package com.magic.lesson1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Kelok on 6/9/2015.
 */
public class EmployeeAdapter extends BaseAdapter{

    final Activity activity;
    final Employee[] employees;

    public EmployeeAdapter(Activity activity, Employee[] employees){
        this.activity = activity;
        this.employees = employees;
    }


    @Override
    public int getCount() {
        return employees.length;
    }

    @Override
    public Object getItem(int position) {
        return employees[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.employee_list_item, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.el_name);
        TextView positionView = (TextView) view.findViewById(R.id.el_position);
        Employee employee = employees[position];
        nameView.setText(employee.name);
        positionView.setText(employee.position + ", " + employee.department);
        return view;
    }
}

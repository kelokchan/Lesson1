package com.magic.lesson1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kelok on 6/9/2015.
 */
public class EmployeeDatabase extends SQLiteOpenHelper {

    public EmployeeDatabase(Context context) {
        super(context, "EmployeeDatabase", null, 1);
    }

    public Employee[] searchByName(String name) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT ID, name FROM employee WHERE name LIKE ?",
                new String[]{"%" + name + "%"});

        Employee[] employees = new Employee[cursor.getCount()];
        for (int c = 0; c < employees.length; c++) {
            cursor.moveToPosition(c);
            String id = cursor.getString(cursor.getColumnIndex("ID"));
            employees[c] = getEmployee(id);
        }

        cursor.close();
        db.close();
        return employees;
    }

    public Employee getEmployee(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT emp.name, emp.position, emp.officePhone, emp.cellPhone, emp.department, emp.email, " +
                        "emp.salary, emp.profileUrl, emp.annualLeavesLeft, emp.supervisorID, mgr.name supervisorName " +
                        "FROM employee emp LEFT OUTER JOIN employee mgr ON emp.supervisorID = mgr.ID WHERE emp.ID = ?",
                new String[]{id}
        );
        cursor.moveToPosition(0);

        Employee employee = new Employee();
        employee.name = cursor.getString(cursor.getColumnIndex("name"));
        employee.position = cursor.getString(cursor.getColumnIndex("position"));
        employee.officePhone = cursor.getString(cursor.getColumnIndex("officePhone"));
        employee.cellPhone = cursor.getString(cursor.getColumnIndex("cellPhone"));
        employee.department = cursor.getString(cursor.getColumnIndex("department"));
        employee.email = cursor.getString(cursor.getColumnIndex("email"));
        employee.salary = cursor.getInt(cursor.getColumnIndex("salary"));
        employee.profileURL = cursor.getString(cursor.getColumnIndex("profileURL"));
        employee.annualLeavesLeft = cursor.getInt(cursor.getColumnIndex("annualLeavesLeft"));
        employee.supervisorID = cursor.getString(cursor.getColumnIndex("supervisorID"));
        employee.supervisorName = cursor.getString(cursor.getColumnIndex("supervisorName"));
        employee.ID = id;

        cursor.close();
        db.close();
        return employee;
    }

    public void addEmployee(String id, String name, String position, String officePhone, String cellPhone,
                            String department, String email, int salary, String profileURL, int annualLeavesLeft, String supervisorID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", id);
        values.put("name", name);
        values.put("position", position);
        values.put("officePhone", officePhone);
        values.put("cellPhone", cellPhone);
        values.put("department", department);
        values.put("email", email);
        values.put("salary", salary);
        values.put("profileURL", profileURL);
        values.put("annualLeavesLeft", annualLeavesLeft);
        if (supervisorID != null)
            values.put("supervisorID", supervisorID);
        db.insert("employee", "name", values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS employee(" +
                "ID TEXT(12) PRIMARY KEY," +
                "name TEXT," +
                "position TEXT," +
                "officePhone TEXT," +
                "cellPhone TEXT," +
                "department TEXT," +
                "email TEXT," +
                "salary INT," +
                "profileURL TEXT," +
                "supervisorID TEXT(12)," +
                "annualLeavesLeft INT)";

        db.execSQL(sql);

        addEmployee("888888888801", "RonaldCEO", "Tech Support", "+03 425 424", "+6019 523 2444", "Tech Support", "ronaldo@tech.com", 2500, "www.tech.com/profile/ronaldo.jpg", 2, null);
        addEmployee("888888888802", "Chan", "Tech Support", "+03 425 424", "+6019 523 2444", "Tech Support", "ronaldo@tech.com", 2500, "www.tech.com/profile/ronaldo.jpg", 2, "888888888801");
        addEmployee("888888888803", "Alex", "Tech Support", "+03 425 424", "+6019 523 2444", "Tech Support", "ronaldo@tech.com", 2500, "www.tech.com/profile/ronaldo.jpg", 2, "888888888801");
        addEmployee("888888888804", "Lawrence", "Tech Support", "+03 425 424", "+6019 523 2444", "Tech Support", "ronaldo@tech.com", 2500, "www.tech.com/profile/ronaldo.jpg", 2, "888888888801");
        addEmployee("888888888805", "Darky", "Tech Support", "+03 425 424", "+6019 523 2444", "Tech Support", "ronaldo@tech.com", 2500, "www.tech.com/profile/ronaldo.jpg", 2, "888888888801");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS employee");
        onCreate(db);
    }
}

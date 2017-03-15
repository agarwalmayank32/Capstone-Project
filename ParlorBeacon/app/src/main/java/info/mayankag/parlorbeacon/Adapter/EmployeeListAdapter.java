package info.mayankag.parlorbeacon.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import info.mayankag.parlorbeacon.Models.Employee;
import info.mayankag.parlorbeacon.R;

public class EmployeeListAdapter extends ArrayAdapter<Employee> {

    private final Activity activity;
    private final ArrayList<Employee> employees;

    public EmployeeListAdapter(Activity activity, ArrayList<Employee> employees) {
        super(activity, R.layout.employee_details_shop_adapter, employees);
        this.activity = activity;
        this.employees = employees;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder")
        final View rowView = inflater.inflate(R.layout.employee_details_shop_adapter, null, true);

        TextView empName = (TextView) rowView.findViewById(R.id.empNameShop);
        TextView empEmail = (TextView) rowView.findViewById(R.id.empEmailShop);
        TextView empPhone = (TextView) rowView.findViewById(R.id.empPhoneShop);
        TextView empDesig = (TextView) rowView.findViewById(R.id.empDesigShop);

        empName.setText(employees.get(position).getName());
        empEmail.setText(employees.get(position).getEmail());
        empPhone.setText(employees.get(position).getPhoneNo());
        empDesig.setText(employees.get(position).getDesignation());

        return rowView;
    }

}

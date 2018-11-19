package com.adriangrabowski.android.oudegreecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adrian on 17/02/2018.
 */


public class ModuleAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<OUModule> dataSource;


    public ModuleAdapter(Context c, ArrayList<OUModule> items) {
        context = c;
        dataSource = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public ArrayList<OUModule> getDataSource() {
        return dataSource;
    }


    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        final View rowView = layoutInflater.inflate(R.layout.list_item_module, viewGroup, false);

        TextView moduleCodeTextView = (TextView) rowView.findViewById(R.id.tv_module_code);
        TextView numberOfCreditsTextView = (TextView) rowView.findViewById(R.id.tv_number_of_credits);
        TextView gradeOfPassTextView = (TextView) rowView.findViewById(R.id.tv_grade_of_pass);
        TextView level = (TextView) rowView.findViewById(R.id.tv_module_level);

        ImageButton removeModuleButton = (ImageButton) rowView.findViewById(R.id.ib_delete_module);
        removeModuleButton.setBackgroundColor(context.getResources().getColor(R.color.OUBlue));

        final OUModule module = (OUModule) getItem(i);

        removeModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity mainActivity = (MainActivity) context;
                mainActivity.removeModule(module);




            }
        });


        moduleCodeTextView.setText(module.getModuleCode() + "");
        numberOfCreditsTextView.setText(module.getNumberOfCredits() + " credits");
        gradeOfPassTextView.setText(module.getGradeOfPassStringRepresentation());
        level.setText("Level " + module.getLevel());


        return rowView;
    }
}

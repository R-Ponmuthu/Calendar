package com.it.calendar.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.it.calendar.R;

import java.util.HashMap;
import java.util.List;

public class PoruthamExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    private HashMap<String, String> listDataChild;
    private String[] poruthamArray;

    PoruthamExpandableAdapter(Context context, String[] poruthamArray, List<String> listDataHeader, HashMap<String, String> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.poruthamArray = poruthamArray;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.porutham_child_item, null);
        }

        String childText = (String) getChild(groupPosition, childPosition);

        TextView poruthamDetail = convertView.findViewById(R.id.poruthamDetail);
        poruthamDetail.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.porutham_item, null);
        }

        TextView poruthamName = convertView.findViewById(R.id.poruthamName);
        TextView porutham = convertView.findViewById(R.id.porutham);
        AppCompatImageView arrow = convertView.findViewById(R.id.arrow);

        if (isExpanded)
            arrow.setImageResource(R.drawable.ic_keyboard_arrow_up);
        else
            arrow.setImageResource(R.drawable.ic_keyboard_arrow_down);

        if (groupPosition < poruthamArray.length) {
            if (poruthamArray[groupPosition].equals("1")) {
                porutham.setText("பொருந்தும்");
                porutham.setTextColor(Color.parseColor("#2E7D32"));
                porutham.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);
            } else {
                porutham.setText("பொருந்தாது");
                porutham.setTextColor(Color.parseColor("#c62828"));
                porutham.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close, 0, 0, 0);
            }
        } else {
            porutham.setText("பொருந்தாது");
            porutham.setTextColor(Color.parseColor("#c62828"));
            porutham.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close, 0, 0, 0);
        }

        poruthamName.setText((groupPosition + 1) + ". " + listDataHeader.get(groupPosition));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

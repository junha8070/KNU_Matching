package com.example.knu_matching.Nav;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.knu_matching.GetSet.Select;
import com.example.knu_matching.R;

import java.util.ArrayList;

public class Select_Person_Adapter extends BaseAdapter {

    LayoutInflater layoutInflater = null;
    private ArrayList<Select> listArr = null;
    private int listcnt = 0;

    public Select_Person_Adapter(ArrayList<Select> mlistArr){
        listArr = mlistArr;
        listcnt = mlistArr.size();
    }

    @Override
    public int getCount() {
        return listcnt;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (layoutInflater == null)
            {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.item_select_person, parent, false);
        }
        TextView tv_nickname = convertView.findViewById(R.id.tv_nickname);
        ImageView iv_level = convertView.findViewById(R.id.iv_level);
        CheckBox cb_select_person = convertView.findViewById(R.id.cb_select_person);
        tv_nickname.setText(listArr.get(position).getStr_nickname());
        switch (listArr.get(position).getStr_level()){
            case "0":
                iv_level.setImageResource(R.drawable.ic_level_6);
                break;
            case "1":
                iv_level.setImageResource(R.drawable.ic_level_5);
                break;
            case "2":
                iv_level.setImageResource(R.drawable.ic_level_4);
                break;
            case "3":
                iv_level.setImageResource(R.drawable.ic_level_3);
                break;
            case "4":
                iv_level.setImageResource(R.drawable.ic_level_2);
                break;
            case "5":
                iv_level.setImageResource(R.drawable.ic_level_1);
                break;
            case "6":
            default:
                iv_level.setImageResource(R.drawable.ic_level_0);
                break;
        }

        return convertView;
    }
}

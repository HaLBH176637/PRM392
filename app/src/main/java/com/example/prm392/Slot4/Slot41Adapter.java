package com.example.prm392.Slot4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm392.R;

import java.util.List;

public class Slot41Adapter extends BaseAdapter {
    private Context context;
    private List<Student1> list;

    public Slot41Adapter(Context context, List<Student1> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public Object getItem(int position){
        return list.get(position);
    }
    @Override
    public  long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Slot41ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.slot41_item_view,parent,false);
            holder = new Slot41ViewHolder();
            holder.img_hinh = convertView.findViewById(R.id.slot41_item_hinh);
            holder.tvTen = convertView.findViewById(R.id.slot41_ten);
            holder.tvTuoi = convertView.findViewById(R.id.slot41_tuoi);
            convertView.setTag(holder);
        }
        else {
            holder = (Slot41ViewHolder) convertView.getTag();
        }
        Student1 student1 = list.get(position);
//        holder.img_hinh.setImageResource(student1.getHinh());
//        holder.tvTen.setText(student1.getTen());
//        holder.tvTuoi.setText(student1.getTuoi());
        return convertView;
    }
    static class Slot41ViewHolder{
        ImageView img_hinh;
        TextView tvTen,tvTuoi;
    }
}

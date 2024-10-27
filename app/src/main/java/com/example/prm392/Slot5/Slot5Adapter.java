package com.example.prm392.Slot5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm392.R;

import java.util.List;


public class Slot5Adapter extends BaseAdapter {
    private List<Slot5Product> mlist;
    private Context context;

    public Slot5Adapter(List<Slot5Product> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Slot5ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context)
                    .inflate(R.layout.slot5_itemview,parent,false);
            //reference
            holder=new Slot5ViewHolder();
            holder.img=convertView.findViewById(R.id.slot51_item_img);
            holder.tvId=convertView.findViewById(R.id.slot5_item_id);
            holder.tvName=convertView.findViewById(R.id.slot5_item_name);
            holder.tvPrice=convertView.findViewById(R.id.slot5_item_price);
            //create a template
            convertView.setTag(holder);
        }
        else {
            holder=(Slot5ViewHolder) convertView.getTag();
        }
        //set data
        Slot5Product product=mlist.get(position);
        if(product!=null){
            holder.img.setImageResource(R.drawable.android);
            holder.tvId.setText(product.getId());
            holder.tvName.setText(product.getName());
            holder.tvPrice.setText(product.getPrice());
        }
        return convertView;
    }
    static class Slot5ViewHolder{
        ImageView img;
        TextView tvId,tvName,tvPrice;
    }
}

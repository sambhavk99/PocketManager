package com.example.pokktmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewholder> {

    Context context;
    private List<MoneyAmount> list;

    public RecyclerAdapter(List<MoneyAmount> list,Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item,viewGroup,false);
        MyViewholder myViewholder = new MyViewholder(relativeLayout);
        return myViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder viewHolder, int i) {

        final MoneyAmount moneyAmount = list.get(i);
        TextView amount = (TextView)viewHolder.relativeLayout.findViewById(R.id.Amount);
        TextView type = (TextView) viewHolder.relativeLayout.findViewById(R.id.Transaction_type);
        TextView date = (TextView) viewHolder.relativeLayout.findViewById(R.id.Date);
        TextView tag = (TextView) viewHolder.relativeLayout.findViewById(R.id.tag_view);

        Button del_btn = (Button) viewHolder.relativeLayout.findViewById(R.id.del_btn);

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new delTransaction().execute(moneyAmount);
            }
        });

        amount.setText("Rs. "+String.valueOf(moneyAmount.getAmount()));
        type.setText(moneyAmount.getType());
        date.setText(moneyAmount.getDate());
        tag.setText(moneyAmount.getTag());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;

        public MyViewholder(RelativeLayout itemView) {
            super(itemView);
            relativeLayout = itemView;
        }
    }

    private class delTransaction extends AsyncTask<MoneyAmount,Void,Void>{

        @Override
        protected Void doInBackground(MoneyAmount... moneyAmounts) {
            MainActivity.pmDatabase.transactionsDao().delTransaction(moneyAmounts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Deleted Transaction", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,UserHomeActivity.class);
            intent.putExtra(LoginActivity.USER_EMAIL,new SharedPreferenceConfig(context).getEmail());
            context.startActivity(intent);
            ((Activity)context).finish();
        }

    }


}

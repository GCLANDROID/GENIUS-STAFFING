package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.FeedBackRatingActivity;
import io.cordova.myapp00d753.module.Question1Model;

public class Question1Adapter extends RecyclerView.Adapter<Question1Adapter.MyViewHolder> {
    ArrayList<Question1Model> itemList = new ArrayList();
    Context context;
    String rate1;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_raw, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final Question1Model qeuModel = itemList.get(i);
        myViewHolder.tvQuestion.setText(itemList.get(i).getQuestion());

        if (itemList.get(i).isSelected()) {
              myViewHolder.tvRate.setVisibility(View.GONE);

        } else {

            myViewHolder.tvRate.setVisibility(View.GONE);
        }


        if(i %2 == 1)
        {
            myViewHolder.llItem.setBackgroundColor(Color.parseColor("#ECECEC"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }else {
            myViewHolder.llItem.setBackgroundColor(Color.parseColor("#ffffff"));
        }







        // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);
        myViewHolder.llExcited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qeuModel.setSelected(!qeuModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (qeuModel.isSelected()) {

                    Log.d("ratestar","1");
                    myViewHolder.imgExcited1.setVisibility(View.VISIBLE);
                    myViewHolder.imgExcited.setVisibility(View.GONE);

                    myViewHolder.imgAverage1.setVisibility(View.GONE);
                    myViewHolder.imgAverage.setVisibility(View.VISIBLE);

                    myViewHolder.imgVeryGood1.setVisibility(View.GONE);
                    myViewHolder.imgVeryGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgGood1.setVisibility(View.GONE);
                    myViewHolder.imgGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgPoor1.setVisibility(View.GONE);
                    myViewHolder.imgPoor.setVisibility(View.VISIBLE);

                    itemList.get(i).setSelected(true);
                    itemList.get(i).setAnswervalue("5"+"#"+itemList.get(i).getOp1());
                    myViewHolder.tvRate.setVisibility(View.VISIBLE);
                    myViewHolder.tvRate.setText("Rate:5");

                    notifyDataSetChanged();
                    ((FeedBackRatingActivity) context).updateStatus1(i, true);



                } else {

                    ((FeedBackRatingActivity) context).updateStatus1(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                    myViewHolder.tvRate.setVisibility(View.GONE);

                }

            }
        });

        myViewHolder.llVeryGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qeuModel.setSelected(!qeuModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (qeuModel.isSelected()) {

                    Log.d("ratestar","2");
                    myViewHolder.imgExcited1.setVisibility(View.GONE);
                    myViewHolder.imgExcited.setVisibility(View.VISIBLE);

                    myViewHolder.imgAverage1.setVisibility(View.GONE);
                    myViewHolder.imgAverage.setVisibility(View.VISIBLE);

                    myViewHolder.imgVeryGood1.setVisibility(View.VISIBLE);
                    myViewHolder.imgVeryGood.setVisibility(View.GONE);

                    myViewHolder.imgGood1.setVisibility(View.GONE);
                    myViewHolder.imgGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgPoor1.setVisibility(View.GONE);
                    myViewHolder.imgPoor.setVisibility(View.VISIBLE);

                    myViewHolder.tvRate.setVisibility(View.VISIBLE);
                    myViewHolder.tvRate.setText("Rate:4");

                    itemList.get(i).setAnswervalue("4"+"#"+itemList.get(i).getOp2());
                    itemList.get(i).setSelected(true);

                    ((FeedBackRatingActivity) context).updateStatus1(i, true);
                    notifyDataSetChanged();



                } else {
                    myViewHolder.tvRate.setVisibility(View.GONE);
                    ((FeedBackRatingActivity) context).updateStatus1(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }
            }
        });

        myViewHolder.llGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qeuModel.setSelected(!qeuModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (qeuModel.isSelected()) {

                    Log.d("ratestar","2");
                    myViewHolder.imgExcited1.setVisibility(View.GONE);
                    myViewHolder.imgExcited.setVisibility(View.VISIBLE);

                    myViewHolder.imgAverage1.setVisibility(View.GONE);
                    myViewHolder.imgAverage.setVisibility(View.VISIBLE);

                    myViewHolder.imgVeryGood1.setVisibility(View.GONE);
                    myViewHolder.imgVeryGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgGood1.setVisibility(View.VISIBLE);
                    myViewHolder.imgGood.setVisibility(View.GONE);

                    myViewHolder.imgPoor1.setVisibility(View.GONE);
                    myViewHolder.imgPoor.setVisibility(View.VISIBLE);
                    myViewHolder.tvRate.setVisibility(View.VISIBLE);
                    myViewHolder.tvRate.setText("Rate:3");
                    itemList.get(i).setAnswervalue("3"+"#"+itemList.get(i).getOp3());

                    itemList.get(i).setSelected(true);

                    ((FeedBackRatingActivity) context).updateStatus1(i, true);
                    notifyDataSetChanged();



                } else {
                    myViewHolder.tvRate.setVisibility(View.GONE);
                    ((FeedBackRatingActivity) context).updateStatus1(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }
            }
        });


        myViewHolder.llAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qeuModel.setSelected(!qeuModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (qeuModel.isSelected()) {

                    Log.d("ratestar","2");
                    myViewHolder.imgExcited1.setVisibility(View.GONE);
                    myViewHolder.imgExcited.setVisibility(View.VISIBLE);

                    myViewHolder.imgAverage1.setVisibility(View.VISIBLE);
                    myViewHolder.imgAverage.setVisibility(View.GONE);

                    myViewHolder.imgVeryGood1.setVisibility(View.GONE);
                    myViewHolder.imgVeryGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgGood1.setVisibility(View.GONE);
                    myViewHolder.imgGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgPoor1.setVisibility(View.GONE);
                    myViewHolder.imgPoor.setVisibility(View.VISIBLE);
                    myViewHolder.tvRate.setVisibility(View.VISIBLE);
                    myViewHolder.tvRate.setText("Rate:2");

                    itemList.get(i).setAnswervalue("2"+"#"+itemList.get(i).getOp4());
                    itemList.get(i).setSelected(true);

                    ((FeedBackRatingActivity) context).updateStatus1(i, true);
                    notifyDataSetChanged();



                } else {

                    ((FeedBackRatingActivity) context).updateStatus1(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                    myViewHolder.tvRate.setVisibility(View.GONE);
                }
            }
        });


        myViewHolder.llPoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qeuModel.setSelected(!qeuModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (qeuModel.isSelected()) {

                    Log.d("ratestar","2");
                    myViewHolder.imgExcited1.setVisibility(View.GONE);
                    myViewHolder.imgExcited.setVisibility(View.VISIBLE);

                    myViewHolder.imgAverage1.setVisibility(View.GONE);
                    myViewHolder.imgAverage.setVisibility(View.VISIBLE);

                    myViewHolder.imgVeryGood1.setVisibility(View.GONE);
                    myViewHolder.imgVeryGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgGood1.setVisibility(View.GONE);
                    myViewHolder.imgGood.setVisibility(View.VISIBLE);

                    myViewHolder.imgPoor1.setVisibility(View.VISIBLE);
                    myViewHolder.imgPoor.setVisibility(View.GONE);
                    myViewHolder.tvRate.setVisibility(View.VISIBLE);
                    myViewHolder.tvRate.setText("Rate:1");


                    itemList.get(i).setAnswervalue("1"+"#"+itemList.get(i).getOp5());
                    itemList.get(i).setSelected(true);

                    ((FeedBackRatingActivity) context).updateStatus1(i, true);
                    notifyDataSetChanged();



                } else {

                    ((FeedBackRatingActivity) context).updateStatus1(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                    myViewHolder.tvRate.setVisibility(View.GONE);
                }
            }
        });


        myViewHolder.tvOp1.setText(itemList.get(i).getOp1());
        myViewHolder.tvOp2.setText(itemList.get(i).getOp2());
        myViewHolder.tvOp3.setText(itemList.get(i).getOp3());
        myViewHolder.tvOp4.setText(itemList.get(i).getOp4());
        myViewHolder.tvOp5.setText(itemList.get(i).getOp5());





    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgExcited, imgExcited1, imgVeryGood, imgVeryGood1, imgGood, imgGood1, imgAverage, imgAverage1, imgPoor, imgPoor1;
        TextView tvQuestion,tvRate;
        LinearLayout llExcited,llVeryGood,llGood,llAverage,llPoor;
        TextView tvOp5,tvOp4,tvOp3,tvOp2,tvOp1;
        LinearLayout llItem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgExcited = (ImageView) itemView.findViewById(R.id.imgExcited);
            imgExcited1 = (ImageView) itemView.findViewById(R.id.imgExcited1);

            imgVeryGood = (ImageView) itemView.findViewById(R.id.imgVeryGood);
            imgVeryGood1 = (ImageView) itemView.findViewById(R.id.imgVeryGood1);

            imgGood = (ImageView) itemView.findViewById(R.id.imgGood);
            imgGood1 = (ImageView) itemView.findViewById(R.id.imgGood1);

            imgAverage = (ImageView) itemView.findViewById(R.id.imgAverage);
            imgAverage1 = (ImageView) itemView.findViewById(R.id.imgAverage1);

            imgPoor = (ImageView) itemView.findViewById(R.id.imgPoor);
            imgPoor1 = (ImageView) itemView.findViewById(R.id.imgPoor1);

            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
            tvRate = (TextView) itemView.findViewById(R.id.tvRate);

            llExcited=(LinearLayout)itemView.findViewById(R.id.llExcited);
            llVeryGood=(LinearLayout)itemView.findViewById(R.id.llVerYGood);
            llGood=(LinearLayout)itemView.findViewById(R.id.llGood);
            llAverage=(LinearLayout)itemView.findViewById(R.id.llAverage);
            llPoor=(LinearLayout)itemView.findViewById(R.id.llPoor);
            tvOp5=(TextView)itemView.findViewById(R.id.tvOp5);
            tvOp4=(TextView)itemView.findViewById(R.id.tvOp4);
            tvOp3=(TextView)itemView.findViewById(R.id.tvOp3);
            tvOp2=(TextView)itemView.findViewById(R.id.tvOp2);
            tvOp1=(TextView)itemView.findViewById(R.id.tvOp1);
            llItem=(LinearLayout)itemView.findViewById(R.id.llItem);

        }
    }

    public Question1Adapter(ArrayList<Question1Model> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}

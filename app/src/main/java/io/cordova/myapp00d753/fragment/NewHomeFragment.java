package io.cordova.myapp00d753.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmployeeDashBoardActivity;
import io.cordova.myapp00d753.activity.LoginActivity;
import io.cordova.myapp00d753.activity.NewUserDashboardActivity;
import io.cordova.myapp00d753.adapter.DDInfoAdapter;
import io.cordova.myapp00d753.adapter.NeedToActAdapter;
import io.cordova.myapp00d753.adapter.NewPFDocumentAdapter;
import io.cordova.myapp00d753.adapter.NotiAdapter;
import io.cordova.myapp00d753.adapter.NotificationModel;
import io.cordova.myapp00d753.adapter.PFDocumentAdapter;
import io.cordova.myapp00d753.module.DDInfoModel;
import io.cordova.myapp00d753.module.NeedToActModel;
import io.cordova.myapp00d753.module.PFDocumentModule;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class NewHomeFragment extends Fragment implements OnChartValueSelectedListener {
    private static final String TAG = "NewHomeFragment";
    View v;
    RecyclerView rvDDInfo,rvPFEPFO;
    DDInfoAdapter ddInfoAdapter;
    ArrayList<DDInfoModel> ddInfoList;
    private PieChart pieChart;
    private Typeface tf;
    Pref pref;
    ArrayList<PFDocumentModule>docList=new ArrayList<>();
    TextView labelPFDocumentLink;
    LinearLayout llPFDocumentLinkSection;
    //ArrayList<String> parties = new ArrayList<>();
    ArrayList<PieChartModel> dataListApi = new ArrayList<>();
    ArrayList<NeedToActModel> needToActModelList = new ArrayList<>();
    LinearLayout llMainDDI;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_home, container, false);
        initView();
        return v;
    }

    private void initView() {
        pref = new Pref(getContext());
        labelPFDocumentLink = v.findViewById(R.id.labelPFDocumentLink);
        llPFDocumentLinkSection = v.findViewById(R.id.llPFDocumentLinkSection);
        llMainDDI = v.findViewById(R.id.llMainDDI);
        rvDDInfo = v.findViewById(R.id.rvDDInfo);
        rvDDInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPFEPFO = v.findViewById(R.id.rvPFEPFO);
        rvPFEPFO.setLayoutManager(new LinearLayoutManager(getContext()));
        ddInfoList = new ArrayList<>();

        pieChart = v.findViewById(R.id.chart1);
        dataListApi.add(new PieChartModel(34.5f, "Complete", "#00C853"));
        dataListApi.add(new PieChartModel(17.2f, "Official Info", "#42A5F5"));
        dataListApi.add(new PieChartModel(13.8f, "Personal Info", "#FF7043"));
        dataListApi.add(new PieChartModel(13.8f, "Contact Info", "#FFA726"));
        dataListApi.add(new PieChartModel(13.8f, "Statutory Info", "#1E88E5"));
        setupPieChart();
        loadPieData();
        JSONObject object=new JSONObject();
        try {
            object.put("MasterID",pref.getMasterId());
            object.put("SecurityCode",pref.getSecurityCode());
            getNotification(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject objNeedToAct=new JSONObject();
        try {
            objNeedToAct.put("MasterID", pref.getMasterId());
            getNeedToActData(objNeedToAct,"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupPieChart() {

        // Remove description label
        pieChart.getDescription().setEnabled(false);

        // Enable percent values
        pieChart.setUsePercentValues(true);

        // Enable hole (Donut style)
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(65f);

        // Center text
        pieChart.setCenterText("Profile\nCompletion");
        pieChart.setCenterTextSize(11f);

        // Animation
        pieChart.animateY(1000);

        // Hide labels inside slices
        pieChart.setDrawEntryLabels(false);

        // Legend setup (right side)
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
    }

    private void loadPieData() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(34.5f, "Complete  34.5%"));
        entries.add(new PieEntry(17.2f, "Official Info 17.2%"));
        entries.add(new PieEntry(13.8f, "Personal Info 13.8%"));
        entries.add(new PieEntry(13.8f, "Contact Info 13.8%"));
        entries.add(new PieEntry(13.8f, "Statutory Info 13.8%"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);

        // Enable value lines (polyline)
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // Line settings
        dataSet.setValueLinePart1OffsetPercentage(80f); // distance from slice
        dataSet.setValueLinePart1Length(0.3f); // first segment
        dataSet.setValueLinePart2Length(0.4f); // second segment
        dataSet.setValueLineWidth(2f);
        dataSet.setValueLineColor(Color.GRAY);

        // Colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#00C853")); // Green
        colors.add(Color.parseColor("#42A5F5")); // Blue
        colors.add(Color.parseColor("#FF7043")); // Orange
        colors.add(Color.parseColor("#FFA726")); // Light Orange
        colors.add(Color.parseColor("#1E88E5")); // Dark Blue

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.setDrawEntryLabels(false); // hide inside labels
        pieChart.setHoleRadius(50f);
        pieChart.invalidate(); // refresh
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");

    }

    /*private void setData(int count, float range) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) (Math.random() * range) + range / 5, parties.get(i % parties.size())));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }*/

    class PieChartModel{
        private float value;
        private String label;
        private String colorCode;

        public PieChartModel(float value, String label,String colorCode) {
            this.value = value;
            this.label = label;
            this.colorCode = colorCode;
        }

        public float getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        private String getColorCode(){
            return colorCode;
        }

    }


    public void getNotification(JSONObject jsonObject) {
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading.....");
        pd.show();
        AndroidNetworking.post(AppData.GetPFNotificationAPI)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            Log.e(TAG, "GET_PF_NOTIFICATION: "+response.toString(4));
                            JSONObject job1 = response;
                            String Response_Code = job1.optString("Response_Code");
                            if (Response_Code.equals("101")) {
                                JSONObject Response_Data = job1.optJSONObject("Response_Data");
                                JSONArray Content=Response_Data.optJSONArray("Content");
                                JSONObject contentobj=Content.optJSONObject(0);
                                String sContent=contentobj.optString("Content");
                                //tvNotifcation.setText("* "+sContent);
                                /*contentList = new ArrayList<>();
                                if (Content.length() > 0){
                                    for (int i = 0; i < Content.length(); i++) {
                                        JSONObject conOBJ=Content.optJSONObject(i);
                                        //contentList.add(conOBJ.optString("Content"));
                                        contentList.add(new NotificationModel(conOBJ.optString("Content"),conOBJ.optString("C_Url")));
                                    }
                                    tvNotifcation.setText(contentList.toString().replace("[","").replace("]","").replaceAll(",","\n\n"));
                                }*/


                                JSONArray Document=Response_Data.optJSONArray("Document");
                                if (Document.length()>0){
                                    llPFDocumentLinkSection.setVisibility(View.VISIBLE);
                                    labelPFDocumentLink.setVisibility(View.VISIBLE);
                                    for (int i=0;i<Document.length();i++){
                                        JSONObject docOBJ=Document.optJSONObject(i);
                                        String Doc_Info=docOBJ.optString("Doc_Info");
                                        String Doc_Url=docOBJ.optString("Doc_Url");
                                        PFDocumentModule pfmodule=new PFDocumentModule();
                                        pfmodule.setDoc_Info(Doc_Info);
                                        pfmodule.setDoc_Url(Doc_Url);
                                        docList.add(pfmodule);
                                    }

                                    NewPFDocumentAdapter docAdapter=new NewPFDocumentAdapter(docList, getActivity());
                                    rvPFEPFO.setAdapter(docAdapter);

                                }else {
                                    llPFDocumentLinkSection.setVisibility(View.GONE);
                                    labelPFDocumentLink.setVisibility(View.GONE);
                                }

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Log.e(TAG, "GET_MENU_error: "+anError.getErrorBody());
                        if (anError.getErrorCode()==401){
                            Toast.makeText(getActivity(), "Session expired, please login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void getNeedToActData(JSONObject objNeedToAct,String flag) {
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading.....");
        pd.show();
        AndroidNetworking.post(AppData.NEED_TO_ACT)
                .addJSONObjectBody(objNeedToAct)
                .addHeaders("SecurityKey", "gStbCQYjYBDCQ4fkGoQSUj7LYe8uVdZ1")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "NEED_TO_ACT: "+response.toString(4));
                            pd.dismiss();
                            JSONObject job = response;
                            boolean isSuccess = job.optBoolean("Success");
                            needToActModelList.clear();
                            if (isSuccess){
                                JSONObject Data = job.getJSONObject("Data");
                                JSONArray Table = Data.optJSONArray("Table");
                                int mandatoryPopupCount = 0;
                                needToActModelList.add(new NeedToActModel(0,"","","","","",0,0,""));
                                if (Table.length() > 0){
                                    if (Table.length() > 0){
                                        for (int i = 0; i < Table.length(); i++) {
                                            JSONObject object = Table.getJSONObject(i);
                                            int LetterID = object.optInt("LetterID");
                                            String MasterID = object.optString("MasterID");
                                            String Domain = object.optString("Domain");
                                            String DocName = object.optString("DocName");
                                            String Category = object.optString("Category");
                                            String ExpDate = object.optString("ExpDate");
                                            int AcceptanceType = object.optInt("AcceptanceType");
                                            int IsMandatoryPopup = object.optInt("IsMandatoryPopup");
                                            String ActUrl = object.optString("ActUrl");
                                            if(IsMandatoryPopup == 1){
                                                mandatoryPopupCount ++;
                                                openActionRequiredPopUp(DocName,ActUrl);
                                            }

                                            needToActModelList.add(new NeedToActModel(LetterID,MasterID,Domain,DocName,Category,ExpDate,
                                                    AcceptanceType,IsMandatoryPopup,ActUrl));


                                        }
                                        Log.e(TAG, "needToActModelList SIZE: "+needToActModelList.size());
                                        ddInfoAdapter = new DDInfoAdapter(getContext(),needToActModelList);
                                        rvDDInfo.setAdapter(ddInfoAdapter);
                                        llMainDDI.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    llMainDDI.setVisibility(View.GONE);
                                }

                            } else {
                                //llNeedToAct.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        //llNeedToAct.setVisibility(View.GONE);
                        Log.e(TAG, "NEED_TO_ACT_error: "+anError.getErrorBody());
                    }
                });
    }

    public void openActionRequiredPopUp(String docName, String actUrl){
        Dialog dialog = new Dialog(getActivity(),R.style.CustomDialogNew2);
        dialog.setContentView(R.layout.action_requried_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        TextView tvDocName = dialog.findViewById(R.id.tvDocName);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        tvDocName.setText(docName);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(actUrl));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
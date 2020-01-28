package com.example.whealingservice.bicyclePage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.HttpsConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.whealingservice.MainActivity;
import com.example.whealingservice.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BicycleSelectedList extends Fragment {

    private Context mContext = null;
    private String APIKey = "EQafxZ%2FegzPmtTi8boFIXIHrtIKKO%2FSPmmrEKfg3xFJhbcDxlZL8DTw1WVSzLyvhe4mzasJlNzCZRqtfG9MQ7w%3D%3D";
    private String RoadKey = "83EA03AA-5927-3AC2-9D91-427F84CDE0B6";
    //http://api.vworld.kr/req/wfs?key=83EA03AA-5927-3AC2-9D91-427F84CDE0B6&domain=http://dev.vworld.kr/dev/v4dv_wmsguide2_s001.do&~
    // 산책로, 공원, 가로수길
    private String bigState = null;
    private String smallState = null;
    private String institutionNm = null;    //실제 관리기관
    private String institutionNm2 = null;
    private String institutionNm3 = null;
    private String institutionNm4 = null;


    private TextView stateName;

    private ArrayList<BicycleListInfo> bicycleListInfosResult;
    private ArrayList<BicycleKeepListInfo> bicycleKeepListInfosResult;
    private ArrayList<BIcycleStreetTreeListInfo> bIcycleStreetTreeListInfos;
    private ArrayList<BicycleParkListInfo> bicycleParkListInfos;

    private BicycleViewAdapter adapter;
    private BicycleKeepViewAdapter keepAdapter;
    private BicycleStreetTreeViewAdapter streetTreeViewAdapter;
    private BicycleParkViewAdapter parkViewAdapter;

    private RadioButton lend_radio;
    private RadioButton keep_radio;
    private RadioButton repair_radio;
    private RadioButton walk_radio;
    private RadioGroup bike_radio;

    private TextView noData;
    private ProgressBar progressBar;

    public static BicycleSelectedList newInstance(String bigState, String smallState) {
        BicycleSelectedList fragment = new BicycleSelectedList();
        Bundle args = new Bundle();
        args.putString("bigState", bigState);
        args.putString("smallState", smallState);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        if (getArguments() != null) {
            bigState = getArguments().getString("bigState");
            smallState = getArguments().getString("smallState");
            if ("서울".equals(bigState)) {
                institutionNm = "서울특별시 " + smallState;
            } else if ("부산".equals(bigState)) {
                institutionNm = "부산광역시 " + smallState;
            } else if ("대구".equals(bigState)) {
                institutionNm = "대구광역시 " + smallState;
            } else if ("인천".equals(bigState)) {
                institutionNm = "인천광역시 " + smallState;
            } else if ("광주".equals(bigState)) {
                institutionNm = "광주광역시 " + smallState;
            } else if ("대전".equals(bigState)) {
                institutionNm = "대전광역시 " + smallState;
            } else if ("울산".equals(bigState)) {
                institutionNm = "울산광역시 " + smallState;
            } else if ("세종".equals(bigState)) {
                institutionNm = "세종특별자치시";
                //TODO 세종 동 구분X 추가적으로 구현하거나 따로 api 사용해야함 !
            } else if ("경기".equals(bigState)) {
                institutionNm = "경기도 " + smallState;
            } else if ("강원".equals(bigState)) {
                institutionNm = "강원도 " + smallState;
            } else if ("충남".equals(bigState)) {
                institutionNm = "충청남도 " + smallState;
            } else if ("충북".equals(bigState)) {
                institutionNm = "충청북도 " + smallState;
            } else if ("전북".equals(bigState)) {
                institutionNm = "전라북도 " + smallState;
            } else if ("전남".equals(bigState)) {
                institutionNm = "전라남도 " + smallState;
            } else if ("경남".equals(bigState)) {
                institutionNm = "경상남도 " + smallState;
            } else if ("경북".equals(bigState)) {
                institutionNm = "경상북도 " + smallState;
            } else if ("제주".equals(bigState)) {
                institutionNm = "제주특별자치도 " + smallState;
                //TODO 제주 동 구분X 추가적으로 구현하거나 따로 api 사용해야함
            }
            System.out.println("INSTTNM = " + institutionNm);
            institutionNm2 = institutionNm;
            institutionNm3 = institutionNm;
            institutionNm4 = institutionNm;
        }



        View view = inflater.inflate(R.layout.bike_selected_list, container, false);

        noData = (TextView) view.findViewById(R.id.empty_view);

        bike_radio = (RadioGroup) view.findViewById(R.id.bike_radio);
        lend_radio = (RadioButton) view.findViewById(R.id.lend_radio);
        keep_radio = (RadioButton) view.findViewById(R.id.keep_radio);
        repair_radio = (RadioButton) view.findViewById(R.id.repair_radio);
        walk_radio = (RadioButton) view.findViewById(R.id.walk_radio);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(String.format("%s %s", bigState, smallState));
//        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bike_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);

        bicycleListInfosResult = new ArrayList<>();
        bicycleKeepListInfosResult = new ArrayList<>();
        bIcycleStreetTreeListInfos = new ArrayList<>();
        bicycleParkListInfos = new ArrayList<>();

        adapter = new BicycleViewAdapter(bicycleListInfosResult);
        keepAdapter = new BicycleKeepViewAdapter(bicycleKeepListInfosResult);
        streetTreeViewAdapter = new BicycleStreetTreeViewAdapter(bIcycleStreetTreeListInfos);
        parkViewAdapter = new BicycleParkViewAdapter(bicycleParkListInfos);

        if (lend_radio.isChecked()) {
            recyclerView.setAdapter(adapter);
            findBicycleInstitution();
        }
        else if (keep_radio.isChecked()) {
            recyclerView.setAdapter(keepAdapter);
            findBicycleKeepInstitution();
        }
        else if (walk_radio.isChecked()) {
//            recyclerView.setAdapter(walkAdapter);
            recyclerView.setAdapter(streetTreeViewAdapter);
            findStreetTree();
        }
        else if (repair_radio.isChecked()) {
            recyclerView.setAdapter(parkViewAdapter);
        }

        bike_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.lend_radio:
                        progressBar.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        if (bicycleListInfosResult.size() != 0)
                            bicycleListInfosResult.clear();
                        recyclerView.setAdapter(adapter);
                        institutionNm = institutionNm2;
                        findBicycleInstitution();
                        break;
                    case R.id.keep_radio:
                        progressBar.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        if (bicycleKeepListInfosResult.size() != 0)
                            bicycleKeepListInfosResult.clear();
                        institutionNm = institutionNm2;
                        recyclerView.setAdapter(keepAdapter);
                        findBicycleKeepInstitution();
                        break;
                    case R.id.walk_radio:
                        progressBar.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        if (bIcycleStreetTreeListInfos.size() != 0)
                            bIcycleStreetTreeListInfos.clear();
                        recyclerView.setAdapter(streetTreeViewAdapter);
                        findStreetTree();
//                        container.setBackgroundColor(Color.rgb(0, 255, 0));
                        break;
                    case R.id.repair_radio:
                        progressBar.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        if (bicycleParkListInfos.size() != 0)
                            bicycleParkListInfos.clear();
                        recyclerView.setAdapter(parkViewAdapter);
                        findPark();
                }
            }
        });

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (lend_radio.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (bicycleListInfosResult.size() != 0)
                        bicycleListInfosResult.clear();
                    recyclerView.setAdapter(adapter);
                    institutionNm = institutionNm2;
                    findBicycleInstitution();
                }
                else if (keep_radio.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (bicycleKeepListInfosResult.size() != 0)
                        bicycleKeepListInfosResult.clear();
                    institutionNm = institutionNm2;
                    recyclerView.setAdapter(keepAdapter);
                    findBicycleKeepInstitution();
                }
                else if (walk_radio.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (bIcycleStreetTreeListInfos.size() != 0)
                        bIcycleStreetTreeListInfos.clear();
                    recyclerView.setAdapter(streetTreeViewAdapter);
                    findStreetTree();
                }
                else if (repair_radio.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    if (bicycleParkListInfos.size() != 0)
                        bicycleParkListInfos.clear();
                    recyclerView.setAdapter(parkViewAdapter);
                    findPark();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //handle click event
                FragmentTransaction transaction = null;
                if (getFragmentManager() != null) {
                    transaction = MainActivity.fragmentManager.beginTransaction();
                    if (lend_radio.isChecked())
                        transaction.replace(R.id.bike_selected_list_layout, BicycleViewDetail.newInstance(bicycleListInfosResult.get(position))).addToBackStack(null).commit();
                    else if (keep_radio.isChecked())
                        transaction.replace(R.id.bike_selected_list_layout, BicycleKeepViewDetail.newInstance(bicycleKeepListInfosResult.get(position))).addToBackStack(null).commit();
                    else if (walk_radio.isChecked())
                        transaction.replace(R.id.bike_selected_list_layout, BicycleStreetTreeViewDetail.newInstance(bIcycleStreetTreeListInfos.get(position))).addToBackStack(null).commit();
                    else if (repair_radio.isChecked())
                        transaction.replace(R.id.bike_selected_list_layout, BicycleParkViewDetail.newInstance(bicycleParkListInfos.get(position))).addToBackStack(null).commit();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    public void findBicycleInstitution() {
        (new Thread() {
            public void run() {

                StringBuilder lend_sb = new StringBuilder();

                lend_sb.append("http://api.data.go.kr/openapi/bcycl-lend-place-std?pageNo=1&numOfRows=1000&type=json");
                lend_sb.append("&serviceKey=").append(APIKey);
                try {
                    if (institutionNm.equals("부산광역시 강서구"))
                        lend_sb.append("&insttNm=").append(URLEncoder.encode("부산광역시", "UTF-8"));
                    else if (institutionNm.contains("대전광역시"))
                        lend_sb.append("&insttNm=").append(URLEncoder.encode("대전광역시시설관리공단", "UTF-8"));
                    else
                        lend_sb.append("&insttNm=").append(URLEncoder.encode(institutionNm, "UTF-8"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    URL url = new URL(lend_sb.toString());
                    System.out.println("URL = " + url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    System.out.println("Response code: " + conn.getResponseCode());

                    BufferedReader rd;
                    // 응답코드
                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    else
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    int idx= 0;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                        System.out.println("LINE = " + line);

                        JSONObject jsonObject = new JSONObject(line);
                        String response = jsonObject.getString("response");
                        JSONObject responseObject = new JSONObject(response);
                        String header = responseObject.getString("header");
                        JSONObject headerObject = new JSONObject(header);
                        String resultCode = headerObject.getString("resultCode");
                        if (resultCode.equals("00")) {
                            String body = responseObject.getString("body");
                            JSONObject bodyObject = new JSONObject(body);
                            String items = bodyObject.getString("items");
                            JSONArray jsonArray = new JSONArray(items);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject subObject = jsonArray.getJSONObject(i);

                                String bcyclLendNm = subObject.getString("bcyclLendNm");
                                String bcyclLendSe = subObject.getString("bcyclLendSe");    // 무인, 유인
                                String rdnmadr = subObject.getString("rdnmadr");
                                //TODO 구주소 없는 경우 신주소로 대체
                                if (rdnmadr.equals("null") || rdnmadr == null)
                                    rdnmadr = subObject.getString("lnmadr");
                                String latitude = subObject.getString("latitude");
                                String hardness = subObject.getString("hardness");  //longitude
                                String operOpenHm = subObject.getString("operOpenHm");  //오픈시간
                                String operCloseHm = subObject.getString("operCloseHm");
                                String rstde = subObject.getString("rstde");    // 휴무일
                                String chrgeSe = subObject.getString("chrgeSe");    //요금구분
                                String bcyclUseCharge = subObject.getString("bcyclUseCharge");  //요금
                                String bcyclHoldCharge = subObject.getString("bcyclHoldCharge");    //자전거 보유 대수
                                String holderCo = subObject.getString("holderCo");  //거치대수
                                String repairStandYn = subObject.getString("repairStandYn");    //수리대설치여부
                                String phoneNumber = subObject.getString("phoneNumber");
                                String instnm = subObject.getString("insttNm");
                                BicycleListInfo info = new BicycleListInfo(bcyclLendNm,
                                        bcyclLendSe,
                                        rdnmadr,
                                        latitude,
                                        hardness,
                                        operOpenHm,
                                        operCloseHm,
                                        rstde,
                                        chrgeSe,
                                        bcyclUseCharge,
                                        bcyclHoldCharge,
                                        holderCo,
                                        repairStandYn,
                                        phoneNumber,
                                        instnm);
                                if (institutionNm.equals("세종특별자치시")) {
                                    if (rdnmadr.contains(smallState))
                                        bicycleListInfosResult.add(info);
                                } else if (institutionNm.contains("대전광역시")) {
                                    if (rdnmadr.contains(smallState))
                                        bicycleListInfosResult.add(info);
                                } else if (institutionNm.contains("제주특별자치도")) {
                                    if (rdnmadr.contains(smallState))
                                        bicycleListInfosResult.add(info);
                                } else
                                    bicycleListInfosResult.add(info);
                            }
                        }
                    }
                    if (institutionNm.equals("울산광역시 중구")) {
                        institutionNm = "울산광역시중구도시관리공단";
                        findBicycleInstitution();
                    }
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                    rd.close();
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    System.out.println("URL ERROR!!");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("HTTPCONNECTION ERROR!!");
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void findBicycleKeepInstitution () {
        (new Thread() {
            public void run() {

                StringBuilder lend_sb = new StringBuilder();

                lend_sb.append("http://api.data.go.kr/openapi/bcycl-dpstry-std?pageNo=1&numOfRows=1000&type=json");
                lend_sb.append("&serviceKey=").append(APIKey);
                try {
//                    if (institutionNm.contains("제주특별자치도")) {
//                        lend_sb.append("&insttNm=").append(URLEncoder.encode("제주특별자치도 서귀포시", "UTF-8"));
//                    } else
                    lend_sb.append("&insttNm=").append(URLEncoder.encode(institutionNm4, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    URL url = new URL(lend_sb.toString());
                    System.out.println("URL = " + url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    System.out.println("Response code: " + conn.getResponseCode());

                    BufferedReader rd;
                    // 응답코드
                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    else
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    int idx= 0;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                        System.out.println("LINE = " + line);

                        JSONObject jsonObject = new JSONObject(line);
                        String response = jsonObject.getString("response");
                        JSONObject responseObject = new JSONObject(response);
                        String header = responseObject.getString("header");
                        JSONObject headerObject = new JSONObject(header);
                        String resultCode = headerObject.getString("resultCode");
                        if (resultCode.equals("00")) {
                            String body = responseObject.getString("body");
                            JSONObject bodyObject = new JSONObject(body);
                            String items = bodyObject.getString("items");
                            JSONArray jsonArray = new JSONArray(items);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject subObject = jsonArray.getJSONObject(i);
                                String dpstryNm = subObject.getString("dpstryNm");
                                String rdnmadr = subObject.getString("rdnmadr");
                                //TODO 구주소 없는 경우 신주소로 대체
                                if (rdnmadr.equals("null") || rdnmadr == null)
                                    rdnmadr = subObject.getString("lnmadr");
                                String cstdyCo = subObject.getString("cstdyCo");
                                String repairStandYn = subObject.getString("repairStandYn");
                                String airInjectorYn = subObject.getString("airInjectorYn");
                                String airInjectorType = subObject.getString("airInjectorType");
                                String phoneNumber = subObject.getString("phoneNumber");
                                String latitude = subObject.getString("latitude");
                                String hardness = subObject.getString("hardness");     //longitude

                                BicycleKeepListInfo info = new BicycleKeepListInfo(dpstryNm,
                                        rdnmadr,
                                        cstdyCo,
                                        repairStandYn,
                                        airInjectorYn,
                                        airInjectorType,
                                        phoneNumber,
                                        latitude,
                                        hardness);
                                if (institutionNm4.equals("인천교통공사")) {
                                    if (rdnmadr.contains(smallState)) {
                                        bicycleKeepListInfosResult.add(info);
                                    }
                                }
                                else if (institutionNm4.equals("서울특별시")) {
                                    if (rdnmadr.contains(smallState))
                                        bicycleKeepListInfosResult.add(info);
                                }
                                else if (institutionNm4.equals("대구광역시")) {
                                    if (rdnmadr.contains(smallState))
                                        bicycleKeepListInfosResult.add(info);
                                }
                                else if (institutionNm4.equals("울산광역시")) {
                                    if (rdnmadr.contains(smallState))
                                        bicycleKeepListInfosResult.add(info);
                                }
                                else if (institutionNm4.equals("울산시설공단")) {
                                    if (rdnmadr.contains(smallState))
                                        bicycleKeepListInfosResult.add(info);
                                }
                                else
                                    bicycleKeepListInfosResult.add(info);
                            }
                        }
                    }
                    if (institutionNm4.equals("서울특별시 송파구")) {
                        institutionNm4 = "송파구시설관리공단";
                        findBicycleKeepInstitution();
                    }
                    else if (institutionNm4.equals("송파구시설관리공단") || institutionNm4.contains("서울특별시") && !institutionNm4.equals("서울특별시")) {
                        institutionNm4 = "서울특별시";
                        findBicycleKeepInstitution();
                    }
                    if (institutionNm4.contains("인천광역시")) {
                        institutionNm4 = "인천교통공사";
                        findBicycleKeepInstitution();
                    }
                    if (institutionNm4.equals("경상북도 구미시")) {
                        institutionNm4 = "구미시설공단";
                        findBicycleKeepInstitution();
                    }
                    if (institutionNm4.contains("대구광역시") && !institutionNm4.equals("대구광역시")) {
                        institutionNm4 = "대구광역시";
                        findBicycleKeepInstitution();
                    }
                    if (institutionNm4.contains("울산광역시") && !institutionNm4.equals("울산광역시")) {
                        institutionNm4 = "울산광역시";
                        findBicycleKeepInstitution();
                    }
                    if (institutionNm4.equals("울산광역시")) {
                        institutionNm4 = "울산시설공단";
                        findBicycleKeepInstitution();
                    }
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                    rd.close();
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    System.out.println("URL ERROR!!");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("HTTPCONNECTION ERROR!!");
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void findStreetTree () {
        (new Thread() {
            public void run() {
                StringBuilder lend_sb = new StringBuilder();

                lend_sb.append("http://api.data.go.kr/openapi/sttree-stret-info-std?&pageNo=1&numOfRows=1000&type=json");
                lend_sb.append("&serviceKey=").append(APIKey);
                try {
                    lend_sb.append("&insttNm=").append(URLEncoder.encode(institutionNm2, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    URL url = new URL(lend_sb.toString());
                    System.out.println("URL = " + url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    System.out.println("Response code: " + conn.getResponseCode());

                    BufferedReader rd;
                    // 응답코드
                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    else
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    int idx= 0;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                        System.out.println("LINE = " + line);

                        JSONObject jsonObject = new JSONObject(line);
                        String response = jsonObject.getString("response");
                        JSONObject responseObject = new JSONObject(response);
                        String header = responseObject.getString("header");
                        JSONObject headerObject = new JSONObject(header);
                        String resultCode = headerObject.getString("resultCode");
                        if (resultCode.equals("00")) {
                            String body = responseObject.getString("body");
                            JSONObject bodyObject = new JSONObject(body);
                            String items = bodyObject.getString("items");
                            JSONArray jsonArray = new JSONArray(items);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject subObject = jsonArray.getJSONObject(i);

                                String sttreeStretNm = subObject.getString("sttreeStretNm");
                                String roadSctn = subObject.getString("roadSctn");
                                String sttreeKnd = subObject.getString("sttreeKnd");
                                String startLnmadr = subObject.getString("startLnmadr");     //longitude
                                String startLatitude = subObject.getString("startLatitude");
                                String endLnmadr = subObject.getString("endLnmadr");
                                String endLatitude = subObject.getString("endLatitude");
                                String phoneNumber = subObject.getString("phoneNumber");

                                BIcycleStreetTreeListInfo info = new BIcycleStreetTreeListInfo(sttreeStretNm,
                                        roadSctn,
                                        sttreeKnd,
                                        startLnmadr,
                                        startLatitude,
                                        endLnmadr,
                                        endLatitude,
                                        phoneNumber);
                                bIcycleStreetTreeListInfos.add(info);

                            }
                        }
                    }
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                    rd.close();
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    System.out.println("URL ERROR!!");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("HTTPCONNECTION ERROR!!");
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void findPark () {
        (new Thread() {
            public void run() {
                StringBuilder lend_sb = new StringBuilder();

                lend_sb.append("http://api.data.go.kr/openapi/cty-park-info-std?&pageNo=1&numOfRows=1000&type=json");
                lend_sb.append("&serviceKey=").append(APIKey);
                try {
                    lend_sb.append("&insttNm=").append(URLEncoder.encode(institutionNm3, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    URL url = new URL(lend_sb.toString());
                    System.out.println("URL = " + url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "application/json");
                    System.out.println("Response code: " + conn.getResponseCode());

                    BufferedReader rd;
                    // 응답코드
                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    else
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    int idx= 0;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                        System.out.println("LINE = " + line);

                        JSONObject jsonObject = new JSONObject(line);
                        String response = jsonObject.getString("response");
                        JSONObject responseObject = new JSONObject(response);
                        String header = responseObject.getString("header");
                        JSONObject headerObject = new JSONObject(header);
                        String resultCode = headerObject.getString("resultCode");
                        if (resultCode.equals("00")) {
                            String body = responseObject.getString("body");
                            JSONObject bodyObject = new JSONObject(body);
                            String items = bodyObject.getString("items");
                            JSONArray jsonArray = new JSONArray(items);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject subObject = jsonArray.getJSONObject(i);

                                String parkNm = subObject.getString("parkNm");
                                String rdnmadr = subObject.getString("rdnmadr");
                                if (rdnmadr.equals("null") || rdnmadr == null)
                                    rdnmadr = subObject.getString("lnmadr");
                                String parkSe = subObject.getString("parkSe");
                                String latitude = subObject.getString("latitude");
                                String hardness = subObject.getString("hardness");
                                String phoneNumber = subObject.getString("phoneNumber");

                                BicycleParkListInfo info = new BicycleParkListInfo(parkNm,
                                        rdnmadr,
                                        parkSe,
                                        latitude,
                                        hardness,
                                        phoneNumber);
                                //기장군도시관리공단
                                //대구광역시
                                //부산시설공단
                                //세종특별자치시
                                //울산광역시
                                //울산시설공단
                                //인천광역시
                                //인천광역시미추홀구시설관리공단
                                //인천시설공단
                                //전라남도
                                //전라북도
                                //충청남도
                                if (!parkSe.equals("소공원") && !parkSe.equals("어린이공원")) {
                                    if (institutionNm3.equals("대구광역시")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("부산광역시")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("울산광역시")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("울산시설공단")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("인천광역시")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("인천시설공단")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("전라남도")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("전라북도")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    } else if (institutionNm3.equals("충청남도")) {
                                        if (rdnmadr.contains(smallState))
                                            bicycleParkListInfos.add(info);
                                    }
                                    else
                                        bicycleParkListInfos.add(info);
                                }
                            }
                        }
                    }
                    if (institutionNm3.equals("부산광역시 기장군")) {
                        institutionNm3 = "기장군도시관리공단";
                        findPark();
                    } else if (institutionNm3.contains("대구광역시") && !institutionNm3.equals("대구광역시")) {
                        institutionNm3 = "대구광역시";
                        findPark();
                    } else if (institutionNm3.contains("부산광역시") && !institutionNm3.equals("부산광역시")) {
                        institutionNm3 = "부산광역시";
                        findPark();
                    } else if (institutionNm3.contains("울산광역시") && !institutionNm3.equals("울산광역시")) {
                        institutionNm3 = "울산광역시";
                        findPark();
                    } else if (institutionNm3.equals("울산광역시")) {
                        institutionNm3 = "울산시설공단";
                        findPark();
                    } else if (institutionNm3.equals("인천광역시 미추홀구")) {
                        institutionNm3 = "인천광역시미추홀구시설관리공단";
                        findPark();
                    } else if (institutionNm3.contains("인천광역시") && !institutionNm3.equals("인천광역시")) {
                        institutionNm3 = "인천광역시";
                        findPark();
                    } else if (institutionNm3.equals("인천광역시")) {
                        institutionNm3 = "인천시설공단";
                        findPark();
                    } else if (institutionNm3.contains("전라남도") && !institutionNm3.equals("전라남도")) {
                        institutionNm3 = "전라남도";
                        findPark();
                    } else if (institutionNm3.contains("전라북도") && !institutionNm3.equals("전라북도")) {
                        institutionNm3 = "전라북도";
                        findPark();
                    } else if (institutionNm3.contains("충청남도") && !institutionNm3.equals("충청남도")) {
                        institutionNm3 = "충청남도";
                        findPark();
                    }
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                    rd.close();
                    conn.disconnect();

                } catch (MalformedURLException e) {
                    System.out.println("URL ERROR!!");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("HTTPCONNECTION ERROR!!");
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if (lend_radio.isChecked()) {
                if (bicycleListInfosResult.size() == 0)
                    noData.setVisibility(View.VISIBLE);
                else
                    noData.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
            else if (keep_radio.isChecked()) {
                if (bicycleKeepListInfosResult.size() == 0)
                    noData.setVisibility(View.VISIBLE);
                else
                    noData.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                keepAdapter.notifyDataSetChanged();
            }
            else if (walk_radio.isChecked()) {
                if (bIcycleStreetTreeListInfos.size() == 0)
                    noData.setVisibility(View.VISIBLE);
                else
                    noData.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                streetTreeViewAdapter.notifyDataSetChanged();
            }
            else if (repair_radio.isChecked()) {
                if (bicycleParkListInfos.size() == 0)
                    noData.setVisibility(View.VISIBLE);
                else
                    noData.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                parkViewAdapter.notifyDataSetChanged();
            }
            System.out.println("result num = " + bicycleListInfosResult.size());
        }

    };
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
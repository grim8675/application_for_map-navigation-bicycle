package com.example.whealingservice.bicyclePage;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whealingservice.MainActivity;
import com.example.whealingservice.R;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class BicycleViewDetail extends Fragment {

    private TextView bcyclLendNm;
    private TextView bcyclLendSe;    // 무인, 유인
    private TextView rdnmadr;
    private TextView operHm;  //오픈시간
    private TextView rstde;    // 휴무일
    private TextView chrgeSe;    //요금구분
    private TextView bcyclUseCharge;  //요금
    private TextView bcyclHoldCharge;    //자전거 보유 대수
    private TextView holderCo;  //거치대수
    private TextView repairStandYn;    //수리대설치여부
    private TextView phoneNumber;
    private BicycleListInfo bicycleListInfos = null;
    private static String mApiKey = "8df32ba9-a5c5-4eb1-aaed-c3f2807da77b";
    private Context mContext;
    private Button HowButton;

    public static BicycleViewDetail newInstance(BicycleListInfo bicycleListInfo) {
        BicycleViewDetail fragment = new BicycleViewDetail();
        Bundle args = new Bundle();
        args.putSerializable("Info", bicycleListInfo);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bike_selected_detail, container, false);

        mContext = getActivity();

        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                finish();
            }
        });

        HowButton = (Button) view.findViewById(R.id.how_to_use);
        bcyclLendNm = (TextView) view.findViewById(R.id.selected_LendNm);
        rdnmadr = (TextView) view.findViewById(R.id.selected_Rdnmadr);
        bcyclHoldCharge = (TextView) view.findViewById(R.id.selected_HoldCharge);    //자전거 보유 대수
        holderCo = (TextView) view.findViewById(R.id.selected_holderCo);  //거치대수
        chrgeSe = (TextView) view.findViewById(R.id.selected_ChrgeSe);    //요금구분
        bcyclUseCharge = (TextView) view.findViewById(R.id.selected_UseCharge);  //요금
        bcyclLendSe = (TextView) view.findViewById(R.id.selected_bcyclLendSe);    // 무인, 유인
        rstde = (TextView) view.findViewById(R.id.selected_rstde);    // 휴무일
        operHm = (TextView) view.findViewById(R.id.selected_operHm);  //오픈시간
        repairStandYn = (TextView) view.findViewById(R.id.selected_repairStandYn);    //수리대설치여부
        phoneNumber = (TextView) view.findViewById(R.id.selected_phoneNumber);
        HowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        if (getArguments() != null) {

            bicycleListInfos = (BicycleListInfo) getArguments().getSerializable("Info");

            LinearLayout miniMap = (LinearLayout) view.findViewById(R.id.miniMap);
            TMapView tmapview = new TMapView(mContext);
            tmapview.setSKTMapApiKey(mApiKey);

            tmapview.setCenterPoint(Double.parseDouble(bicycleListInfos.getHardness()), Double.parseDouble(bicycleListInfos.getLatitude()));
            tmapview.setCompassMode(false);
            /* 현위치 아이콘표시 */
            tmapview.setIconVisibility(false);
            /* 줌레벨 */
            tmapview.setZoomLevel(15);
            tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
            tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
            /*  화면중심을 단말의 현재위치로 이동 */
            tmapview.setTrackingMode(false);
            tmapview.setSightVisible(false);

            miniMap.addView(tmapview);

            ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle(bicycleListInfos.getBcyclLendNm());
//            actionBar.setDisplayHomeAsUpEnabled(true);

//            bcyclLendNm.setText(bicycleListInfos.getBcyclLendNm());
            bcyclLendSe.setText(bicycleListInfos.getBcyclLendSe());    // 무인, 유인
            rdnmadr.setText(bicycleListInfos.getRdnmadr());
            operHm.setText(String.format("%s ~ %s", bicycleListInfos.getOperOpenHm(), bicycleListInfos.getOperCloseHm()));
            rstde.setText(String.format("\t\t\t휴무 : %s", bicycleListInfos.getRstde()));    // 휴무일
            chrgeSe.setText(bicycleListInfos.getChrgeSe());    //요금구분
            bcyclUseCharge.setText(String.format("\t\t\t%s",bicycleListInfos.getBcyclUseCharge()));  //요금
            bcyclHoldCharge.setText(String.format("자전거보유대수 : %s",bicycleListInfos.getBcyclHoldCharge()));    //자전거 보유 대수
            holderCo.setText(String.format("거치대수 : %s", bicycleListInfos.getHolderCo()));  //거치대수
            repairStandYn.setText(String.format("수리대 : %s", bicycleListInfos.getRepairStandYn()));    //수리대설치여부
            phoneNumber.setText(bicycleListInfos.getPhoneNumber());

        }

        return view;
    }
    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String name_detail = bicycleListInfos.getBcyclLendNm();
        String inst_nm = bicycleListInfos.getinsttNm();
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.chunbuk_guisan_pop);
        View layout = inflater.inflate(R.layout.chungbuk_guisan, linearLayout, true);
        switch(inst_nm)
        {
            case "서울특별시 성북구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.sungbuk_pop);
                layout = inflater.inflate(R.layout.sungbook, linearLayout, true);
                break;
            case "서울특별시 노원구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.nowon_pop);
                layout = inflater.inflate(R.layout.nowon_sumbat, linearLayout, true);
                break;
            case "서울특별시 강남구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.sooseo_pop);
                layout = inflater.inflate(R.layout.sooseo, linearLayout, true);
                break;
            case "서울특별시 강동구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gangdong_pop);
                layout = inflater.inflate(R.layout.gangdong, linearLayout, true);
                break;
            case "서울특별시 관악구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gwanak_pop);
                layout = inflater.inflate(R.layout.gwanak, linearLayout, true);
                break;
            case "서울특별시 광진구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gwanjin_pop);
                layout = inflater.inflate(R.layout.gwangjin, linearLayout, true);
                break;
            case "서울특별시 금천구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.geumcheon_pop);
                layout = inflater.inflate(R.layout.geumcheon, linearLayout, true);
                break;
            case "서울특별시 동작구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.dongjak_pop);
                layout = inflater.inflate(R.layout.dongjak, linearLayout, true);
                break;
            case "서울특별시 서대문구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.seodaemoon_pop);
                layout = inflater.inflate(R.layout.seodaemoon, linearLayout, true);
                break;
            case "서울특별시 서초구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.seocho_pop);
                layout = inflater.inflate(R.layout.seocho, linearLayout, true);
                break;
            case "서울특별시 성동구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.sungdong_pop);
                layout = inflater.inflate(R.layout.sungdong, linearLayout, true);
                break;
            case "부산광역시 금정구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_geumjung_pop);
                layout = inflater.inflate(R.layout.busan_geumjung, linearLayout, true);
                break;
            case "부산광역시 동래구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_donrae_pop);
                layout = inflater.inflate(R.layout.busan_donlae, linearLayout, true);
                break;
            case "부산광역시 사하구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_saha_pop);
                layout = inflater.inflate(R.layout.busan_sahagu, linearLayout, true);
                break;
            case "부산광역시 수영구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_soo_pop);
                layout = inflater.inflate(R.layout.busan_sooyoung, linearLayout, true);
                break;
            case "부산광역시 연제구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_yeon_pop);
                layout = inflater.inflate(R.layout.busan_yeonjae, linearLayout, true);
                break;
            case "부산광역시 해운대구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_haewoon_pop);
                layout = inflater.inflate(R.layout.busan_haewoon, linearLayout, true);
                break;
            case "대구광역시 서구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.daegu_seogu_pop);
                layout = inflater.inflate(R.layout.daegu_seogu, linearLayout, true);
                break;
            case "광주광역시 남구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gwangju_nam_pop);
                layout = inflater.inflate(R.layout.gwangju_namgu, linearLayout, true);
                break;
            case "광주광역시 북구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gwangju_buk_pop);
                layout = inflater.inflate(R.layout.gwangju_bookgu, linearLayout, true);
                break;
            case "광주광역시 서구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gwangju_seo_pop);
                layout = inflater.inflate(R.layout.gwanju_seogu, linearLayout, true);
                break;
            case "대전광역시 대덕구":
            case "대전광역시 동구":
            case "대전광역시 서구":
            case "대전광역시 중구":
            case "대전광역시시설관리공단":
                linearLayout = (LinearLayout) getView().findViewById(R.id.tashoo_pop);
                layout = inflater.inflate(R.layout.daejeon_tashoo, linearLayout, true);
                break;
            case "울산광역시 중구":
                linearLayout = (LinearLayout) getView().findViewById(R.id.ulsan_pop);
                layout = inflater.inflate(R.layout.ulsan_joongu, linearLayout, true);
                break;
            case "세종특별자치시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.sejong_pop);
                layout = inflater.inflate(R.layout.sejong, linearLayout, true);
                break;
            case "경기도 고양시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_go_pop);
                layout = inflater.inflate(R.layout.gyeonggi_goyang, linearLayout, true);
                break;
            case "경기도 과천시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_gwa_pop);
                layout = inflater.inflate(R.layout.gyeongi_gwancheon, linearLayout, true);
                break;
            case "경기도 군포시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_gun_pop);
                layout = inflater.inflate(R.layout.gyeongi_gunpo, linearLayout, true);
                break;
            case "경기도 김포시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_gim_pop);
                layout = inflater.inflate(R.layout.gyeongi_gimpo, linearLayout, true);
                break;
            case "경기도 부천시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_bucheon_pop);
                layout = inflater.inflate(R.layout.gyenggi_bucheon, linearLayout, true);
                break;
            case "경기도 수원시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyoengi_suwon_pop);
                layout = inflater.inflate(R.layout.gyeongi_suwon, linearLayout, true);
                break;
            case "경기도 시흥시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_sihun_pop);
                layout = inflater.inflate(R.layout.gyenggi_sihung, linearLayout, true);
                break;
            case "경기도 안산시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_ans_pop);
                layout = inflater.inflate(R.layout.gyeongi_ansan, linearLayout, true);
                break;
            case "경기도 연천군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_yun_pop);
                layout = inflater.inflate(R.layout.gyengi_yeoncheon, linearLayout, true);
                break;
            case "경기도 오산시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gyeongi_osan_pop);
                layout = inflater.inflate(R.layout.gyeongi_osan, linearLayout, true);
                break;
            case "제주특별자치도 제주시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.jeju_pop);
                layout = inflater.inflate(R.layout.jeju, linearLayout, true);
                break;
            case "강원도 강릉시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gangwon_gangreung_pop);
                layout = inflater.inflate(R.layout.gangwon_gangreung, linearLayout, true);
                break;
            case "강원도 고성군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gangwon_gosung_pop);
                layout = inflater.inflate(R.layout.gangwon_gosung, linearLayout, true);
                break;
            case "강원도 삼척시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gangwon_samchuck_pop);
                layout = inflater.inflate(R.layout.gangwon_samchuck, linearLayout, true);
                break;
            case "강원도 화천군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.gangwon_hawchenon_pop);
                layout = inflater.inflate(R.layout.gangwon_hwacheon, linearLayout, true);
                break;
            case "충청남도 공주시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.chungnam_gong_pop);
                layout = inflater.inflate(R.layout.chungnam_gongju, linearLayout, true);
                break;
            case "충청남도 당진시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.chungnam_dang_pop);
                layout = inflater.inflate(R.layout.chungnam_dangjin, linearLayout, true);
                break;
            case "충청남도 서천군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.chungnam_seocheon_pop);
                layout = inflater.inflate(R.layout.chungnam_seocheon, linearLayout, true);
                break;
            case "충청남도 아산시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.chungnam_asan_pop);
                layout = inflater.inflate(R.layout.chungnam_asan, linearLayout, true);
                break;
            case "충청북도 괴산군":
            case "전라남도 화순군":
            case "강원도 춘천시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.chunbuk_guisan_pop);
                layout = inflater.inflate(R.layout.chungbuk_guisan, linearLayout, true);
                break;
            case "충청북도 청주시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.chunbuk_chungjoo_pop);
                layout = inflater.inflate(R.layout.chungbuk_chungjoo, linearLayout, true);
                break;
            case "충청북도 충주시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.chunbuk_choongjoo_pop);
                layout = inflater.inflate(R.layout.chungbuk_choongjoo, linearLayout, true);
                break;
            case "전라남도 강진군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junnam_ganjin_pop);
                layout = inflater.inflate(R.layout.junnam_ganjin, linearLayout, true);
                break;
            case "전라남도 담양군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junnam_damyang_pop);
                layout = inflater.inflate(R.layout.junnam_damyang, linearLayout, true);
                break;
            case "전라남도 순천시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junnam_sooncheon_pop);
                layout = inflater.inflate(R.layout.junnam_sooncheon, linearLayout, true);
                break;
            case "전라남도 신안군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junnam_sinan_pop);
                layout = inflater.inflate(R.layout.junnam_sinan, linearLayout, true);
                break;
            case "전라남도 여수시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junnam_yeosoo_pop);
                layout = inflater.inflate(R.layout.junnam_yeosoo, linearLayout, true);
                break;
            case "전라남도 영광군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junnam_younggwang_pop);
                layout = inflater.inflate(R.layout.junnam_younggwang, linearLayout, true);
                break;
            case "전라북도 군산시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junbuk_goonsan_pop);
                layout = inflater.inflate(R.layout.junbuk_goonsan, linearLayout, true);
                break;
            case "전라북도 남원시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junbuk_namwon_pop);
                layout = inflater.inflate(R.layout.junbuk_namwon, linearLayout, true);
                break;
            case "전라북도 전주시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junbuk_junjoo_pop);
                layout = inflater.inflate(R.layout.junbuk_junjoo, linearLayout, true);
                break;
            case "전라북도 정읍시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.junbuk_jungup_pop);
                layout = inflater.inflate(R.layout.junbuk_jungup, linearLayout, true);
                break;
            case "경상남도 거창군":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungnam_guchang_pop);
                layout = inflater.inflate(R.layout.kyungnam_guchang, linearLayout, true);
                break;
            case "경상남도 김해시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungnam_gimhae_pop);
                layout = inflater.inflate(R.layout.kyungnam_gimhae, linearLayout, true);
                break;
            case "경상남도 양산시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungnam_yangsan_pop);
                layout = inflater.inflate(R.layout.kyungnam_yangsan, linearLayout, true);
                break;
            case "경상남도 창원시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungnam_changwon_pop);
                layout = inflater.inflate(R.layout.kyungnam_changwon, linearLayout, true);
                break;
            case "경상북도 구미시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungbuk_goomi_pop);
                layout = inflater.inflate(R.layout.kyungbuk_goomi, linearLayout, true);
                break;
            case "경상북도 영주시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungbuk_youngjoo_pop);
                layout = inflater.inflate(R.layout.kyungbuk_youngjoo, linearLayout, true);
                break;
            case "경상북도 포항시":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungbuk_pohang_pop);
                layout = inflater.inflate(R.layout.kyunbuk_pohang, linearLayout, true);
                break;



        }
        switch(name_detail)
        {

            case "대저생태공원(맥도생태공원 관리사무소 옆)":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_dae_pop);
                layout = inflater.inflate(R.layout.busan_gangseo, linearLayout, true);
                break;
            case "맥도생태공원(구포대교 밑 대저생태공원 관리사무소 앞)":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_mac_pop);
                layout = inflater.inflate(R.layout.busan_gangseo1, linearLayout, true);
                break;
            case "화명생태공원(화명운동장 내)":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_hwa_pop);
                layout = inflater.inflate(R.layout.busan_ganseo2, linearLayout, true);
                break;
            case "삼락생태공원(삼락 4번 주차장 옆 인라인스케이트장 내)":
                linearLayout = (LinearLayout) getView().findViewById(R.id.busan_sim_pop);
                layout = inflater.inflate(R.layout.busan_gangseo3, linearLayout, true);
                break;
            case "신안동 신안둔치 족구장":
            case "상대동 공영자전거무료대여소":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungnam_jinjoo_free_pop);
                layout = inflater.inflate(R.layout.kyungnam_jinjoo, linearLayout, true);
                break;
            case "칠암동 경남문화예술회관 건너둔치":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungnam_jinjoo_not_free_pop);
                layout = inflater.inflate(R.layout.kyunnam_jinjoo_m, linearLayout, true);
                break;
            case "분천산타마을 자전거셰어링":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungbuk_bonghwa_santa_pop);
                layout = inflater.inflate(R.layout.kyungbuk_bonghwa_santa, linearLayout, true);
                break;
            case "봉화열목어마을":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungbuk_bonghwa_yulmok_pop);
                layout = inflater.inflate(R.layout.kyungbuk_bonghwa, linearLayout, true);
                break;
            case "상주자전거박물관":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungbuk_sangjoo_museum_pop);
                layout = inflater.inflate(R.layout.kyungbuk_sangjoo, linearLayout, true);
                break;
            case "상주역 자전거 공영주차장":
                linearLayout = (LinearLayout) getView().findViewById(R.id.kyungbuk_sangjoo_station_pop);
                layout = inflater.inflate(R.layout.kyungbuk_sangjoo_station, linearLayout, true);
                break;

        }

        builder.setView(layout);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext,"확인을 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });

        builder.show();
    }
}

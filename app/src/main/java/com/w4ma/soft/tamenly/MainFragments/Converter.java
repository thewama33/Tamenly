package com.w4ma.soft.tamenly.MainFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.w4ma.soft.tamenly.CategoryActivities.Models.CurrencyList;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.LeakCanary.LeakCanaryinit;
import com.w4ma.soft.tamenly.Utils.NetworkApiPressenter.ApiClient;
import com.w4ma.soft.tamenly.Utils.NetworkApiPressenter.RetrofitInterface;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Converter extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @BindView(R.id.spinnerFrom) MaterialSpinner spinnerFrom;
    @BindView(R.id.spinnerTo) MaterialSpinner spinnerTo;
    @BindView(R.id.txtResult) TextView result;
    @BindView(R.id.Amount) EditText txtAmount;

    public Converter() {
    }

    @BindView(R.id.btnConvert) Button btnConvert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_converter,container,false);
        ButterKnife.bind(this,view);
        SpinnerCountries();





        return view;
    }

    public void ConvertCurrency(){

        String convertTO = spinnerTo.getText().toString();
        String convertFrom = spinnerFrom.getText().toString();
        String ammount = txtAmount.getText().toString();



        Map<String , String> ApiInfo = new HashMap<>();

        ApiInfo.put("api_key","wi9BMULCmEKAuiDJU4DP9EGVbecsCD");
        ApiInfo.put("from",convertFrom);
        ApiInfo.put("to",convertTO);
        ApiInfo.put("amount",ammount);


       if (TextUtils.isEmpty(ammount)){
           Toasty.warning(getActivity(),"Input Empty",Toast.LENGTH_SHORT).show();
       }else {

           RetrofitInterface api = ApiClient.getRetrofit().create(RetrofitInterface.class);

           api.Convert(ApiInfo).enqueue(new Callback<CurrencyList>() {
               @Override
               public void onResponse(Call<CurrencyList> call, Response<CurrencyList> response) {

                   CurrencyList list = response.body();
                   if (response.isSuccessful()){

                       result.setText(list.getAmount().toString());

                   }else if (!response.isSuccessful()){

                       Toasty.error(getActivity(),"Error : " + list.getError().toString() + "\n"
                               + " Error Message : " + list.getErrorMessage(),Toast.LENGTH_LONG).show();

                   }



               }
               @Override
               public void onFailure(Call<CurrencyList> call, Throwable t) {

                   Toasty.error(getActivity(),"Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
               }
           });


       }
    }

    public void SpinnerCountries(){

        spinnerTo.setMaxHeight(150);
        spinnerFrom.setMaxHeight(150);

        spinnerFrom.setHeight(80);
        spinnerTo.setHeight(80);

       spinnerTo.setItems(
               "AED",
               "AFN",
              "ALL",
               "AMD",
               "ANG",
               "AOA",
               "ARS",
              "AUD",
               "AWG",
               "AZN",
               "BAM",
              "BBD",
               "BDT",
               "BGN",
               "BHD",
               "BIF",
               "BMD",
               "BND",
               "BOB",
               "BRL",
               "BSD",
               "BTN",
               "BWP",
               "BYN",
               "BZD",
               "CAD",
               "CDF",
               "CHF",
               "CLP",
               "CNY",
               "COP",
               "CRC",
               "CUC",
               "CVE",
               "CZK",
               "DJF",
               "DKK",
               "DOP",
               "DZD",
               "EGP",
               "ERN",
               "ETB",
               "EUR",
               "FJD",
               "GBP",
               "GEL",
               "GHS",
               "GIP",
               "GMD",
               "GNF",
               "GTQ",
               "GYD",
               "HKD",
               "HNL",
               "HRK",
               "HTG",
               "HUF",
               "IDR",
               "ILS",
               "INR",
               "IQD",
               "IRR",
               "ISK",
               "JMD",
               "JOD",
               "JPY",
               "KES",
               "KGS",
               "KHR",
               "KMF",
               "KPW",
               "KRW",
               "KWD",
               "KYD",
               "KZT",
               "LAK",
               "LBP",
               "LKR",
               "LRD",
               "LSL",
               "LYD",
               "MAD",
               "MDL",
               "MGA",
               "MKD",
               "MMK",
               "MNT",
               "MOP",
               "MRO",
               "MUR",
               "MVR",
               "MWK",
               "MXN",
               "MYR",
               "MZN",
               "NAD",
               "NGN",
               "NIO",
               "NOK",
               "NPR",
               "NZD",
               "OMR",
               "PAB",
               "PEN",
               "PGK",
               "PHP",
               "PKR",
               "PLN",
               "PYG",
               "QAR",
               "RON",
               "RSD",
               "RUB",
               "RWF",
               "SAR",
               "SBD",
               "SCR",
               "SDG",
               "SEK",
               "SGD",
               "SHP",
               "SLL",
               "SOS",
               "SRD",
               "SSP",
               "STD",
                "SYP",
               "SZL",
               "THB",
               "TJS",
               "TMT",
               "TND",
               "TOP",
               "TRY",
               "TTD",
               "TWD",
               "TZS",
               "UAH",
               "UGX",
               "USD",
               "UYU",
               "UZS",
               "VEF",
               "VND",
               "VUV",
               "WST",
               "XAF",
               "XCD",
               "XOF",
               "XPF",
               "YER",
               "ZAR",
               "ZMW");

        spinnerFrom.setItems( "AED",
                "AFN",
                "ALL",
                "AMD",
                "ANG",
                "AOA",
                "ARS",
                "AUD",
                "AWG",
                "AZN",
                "BAM",
                "BBD",
                "BDT",
                "BGN",
                "BHD",
                "BIF",
                "BMD",
                "BND",
                "BOB",
                "BRL",
                "BSD",
                "BTN",
                "BWP",
                "BYN",
                "BZD",
                "CAD",
                "CDF",
                "CHF",
                "CLP",
                "CNY",
                "COP",
                "CRC",
                "CUC",
                "CVE",
                "CZK",
                "DJF",
                "DKK",
                "DOP",
                "DZD",
                "EGP",
                "ERN",
                "ETB",
                "EUR",
                "FJD",
                "GBP",
                "GEL",
                "GHS",
                "GIP",
                "GMD",
                "GNF",
                "GTQ",
                "GYD",
                "HKD",
                "HNL",
                "HRK",
                "HTG",
                "HUF",
                "IDR",
                "ILS",
                "INR",
                "IQD",
                "IRR",
                "ISK",
                "JMD",
                "JOD",
                "JPY",
                "KES",
                "KGS",
                "KHR",
                "KMF",
                "KPW",
                "KRW",
                "KWD",
                "KYD",
                "KZT",
                "LAK",
                "LBP",
                "LKR",
                "LRD",
                "LSL",
                "LYD",
                "MAD",
                "MDL",
                "MGA",
                "MKD",
                "MMK",
                "MNT",
                "MOP",
                "MRO",
                "MUR",
                "MVR",
                "MWK",
                "MXN",
                "MYR",
                "MZN",
                "NAD",
                "NGN",
                "NIO",
                "NOK",
                "NPR",
                "NZD",
                "OMR",
                "PAB",
                "PEN",
                "PGK",
                "PHP",
                "PKR",
                "PLN",
                "PYG",
                "QAR",
                "RON",
                "RSD",
                "RUB",
                "RWF",
                "SAR",
                "SBD",
                "SCR",
                "SDG",
                "SEK",
                "SGD",
                "SHP",
                "SLL",
                "SOS",
                "SRD",
                "SSP",
                "STD",
                "SYP",
                "SZL",
                "THB",
                "TJS",
                "TMT",
                "TND",
                "TOP",
                "TRY",
                "TTD",
                "TWD",
                "TZS",
                "UAH",
                "UGX",
                "USD",
                "UYU",
                "UZS",
                "VEF",
                "VND",
                "VUV",
                "WST",
                "XAF",
                "XCD",
                "XOF",
                "XPF",
                "YER",
                "ZAR",
                "ZMW");



    }

    @OnClick(R.id.btnConvert)
    public void ConvertNow(){
        ConvertCurrency();
    }

    public static android.support.v4.app.Fragment newInstance(String s) {
        Converter f = new Converter();
        Bundle bundle = new Bundle();
        bundle.putString("msg",s);
        f.setArguments(bundle);

        return f;
    }
}


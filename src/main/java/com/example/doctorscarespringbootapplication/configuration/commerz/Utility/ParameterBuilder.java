package com.example.doctorscarespringbootapplication.configuration.commerz.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ParameterBuilder {
    public static String getParamsString(Map<String, String> params, boolean urlEncode) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (urlEncode)
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            else
                result.append(entry.getKey());

            result.append("=");
            if (urlEncode)
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            else
                result.append(entry.getValue());
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public static Map<String, String> constructRequestParameters() {
        // CREATING LIST OF POST DATA
        String baseUrl = "https://doctors-care-application.herokuapp.com/";//Request.Url.Scheme + "://" + Request.Url.Authority + Request.ApplicationPath.TrimEnd('/') + "/";
        Map<String, String> postData = new HashMap<String, String>();
        postData.put("total_amount", "150.00");
        postData.put("tran_id", "TESTASPNET1234");
        postData.put("success_url", baseUrl + "patient/appoint-doctor/success");
        postData.put("fail_url", "https://sandbox.sslcommerz.com/developer/fail.php");
        postData.put("cancel_url", "https://sandbox.sslcommerz.com/developer/cancel.php");
        postData.put("version", "3.00");
        postData.put("cus_name", "ABC XY");
        postData.put("cus_email", "abc.xyz@mail.co");
        postData.put("cus_add1", "Address Line On");
        postData.put("cus_add2", "Address Line Tw");
        postData.put("cus_city", "City Nam");
        postData.put("cus_state", "State Nam");
        postData.put("cus_postcode", "Post Cod");
        postData.put("cus_country", "Countr");
        postData.put("cus_phone", "0111111111");
        postData.put("cus_fax", "0171111111");
        postData.put("ship_name", "ABC XY");
        postData.put("ship_add1", "Address Line On");
        postData.put("ship_add2", "Address Line Tw");
        postData.put("ship_city", "City Nam");
        postData.put("ship_state", "State Nam");
        postData.put("ship_postcode", "Post Cod");
        postData.put("ship_country", "Countr");
        postData.put("value_a", "ref00");
        postData.put("value_b", "ref00");
        postData.put("value_c", "ref00");
        postData.put("value_d", "ref00");
        postData.put("ipn_url", "https://doctors-care-application.herokuapp.com/patient/appoint-doctor/pay-success-validator");
        return postData;
    }

    public static Map<String, String> constructRequestParam(String baseUrl, String payment, String transactionID, String patientName) {
        // CREATING LIST OF POST DATA
        //baseUrl = Request.Url.Scheme + "://" + Request.Url.Authority + Request.ApplicationPath.TrimEnd('/') + "/";
        Map<String, String> postData = new HashMap<String, String>();
        postData.put("total_amount", payment);
        postData.put("tran_id", transactionID);
        postData.put("success_url", baseUrl + "patient/appoint-doctor/success");
        postData.put("fail_url", "https://sandbox.sslcommerz.com/developer/fail.php");
        postData.put("cancel_url", "https://sandbox.sslcommerz.com/developer/cancel.php");
        postData.put("version", "3.00");
        postData.put("cus_name", patientName);
        postData.put("cus_email", "xyz@mail.com");
        postData.put("cus_add1", "Dhaka");
        postData.put("cus_city", "Khilkhet");
        postData.put("cus_state", "Dhaka");
        postData.put("cus_postcode", "1212");
        postData.put("cus_country", "Bangladesh");
        postData.put("cus_phone", "0111111111");
        postData.put("cus_fax", "0171111111");
        postData.put("ship_name", "Doctors Care");
        postData.put("ship_add1", "Address Line On");
        postData.put("ship_add2", "Address Line Tw");
        postData.put("ship_city", "City Nam");
        postData.put("ship_state", "State Nam");
        postData.put("ship_postcode", "Post Cod");
        postData.put("ship_country", "Country");
        return postData;
    }

}

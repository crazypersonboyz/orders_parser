package com.unlimint.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.unlimint.model.OrderBean;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author neera
 */
public class ParserUtil {
     public static void validateOrderId(OrderBean inOrder, String orderId, List excpList) {
        try {
            if (StringUtils.hasLength(orderId)) {
                excpList.add(ParserUtilConstants.ORDER_ID_NOT_AVAILABLE);
                return;
            }
            inOrder.setOrderId(Integer.parseInt(orderId));
        } catch (Exception e) {
            inOrder.setOrderId(0);
            excpList.add(ParserUtilConstants.INVALID_ORDER);

        }
    }

    public static void validateAmount(OrderBean inOrder, String amount, List excpList) {
        try {
            if (StringUtils.hasLength(amount)) {
                excpList.add(ParserUtilConstants.AMOUNT_NOT_AVAILABLE);
                return;
            }
            inOrder.setAmount(Double.parseDouble(amount));
        } catch (Exception e) {
            inOrder.setAmount(0);
            excpList.add(ParserUtilConstants.INVALID_AMOUNT);

        }
    }

    public static void validateCurrency(OrderBean inOrder, String currency, List excpList) {
        try {
            if (StringUtils.hasLength(currency)) {
                excpList.add(ParserUtilConstants.CURRENCY_NOT_AVAILABLE);
                return;
            }
            if (currency.equals(ParserUtilConstants.USD) || currency.equals(ParserUtilConstants.EUR)) {
                inOrder.setCurrenncy(currency);
            } else {
                excpList.add(ParserUtilConstants.INVALID_CURRENCY);
            }
        } catch (Exception e) {
            excpList.add(ParserUtilConstants.INVALID_CURRENCY);
        }
}
    
    public static void validateComment(OrderBean inOrder, String comment, List excpList) {
        try {
            if (StringUtils.hasLength(comment)) {
                excpList.add(ParserUtilConstants.COMMENT_NOT_AVAILABLE);
                return;
            }else{
                inOrder.setComment(comment);
            }
            
        } catch (Exception e) {
            excpList.add(ParserUtilConstants.INVALID_COMMENT);
        }
}
    
    
     public static Map<String, Object> getJsonObject(JSONObject jsonObj) {
        Map<String, Object> mp = new HashMap<>();
        jsonObj.keySet().forEach(keyStr -> {
            Object keyvalue = jsonObj.get((String) keyStr);
            //System.out.println("key: " + keyStr + " value: " + keyvalue);
            mp.put((String) keyStr, keyvalue);
       
        });
        return mp;
    }
     
     public static void validateCsvLine(String line[], List excpList) {
       
            if (line.length<1) {
                excpList.add(ParserUtilConstants.CSV_ATTRIBUTE_NOT_FOUND_MSG_1);
            }else if (line.length<2) {
                excpList.add(ParserUtilConstants.CSV_ATTRIBUTE_NOT_FOUND_MSG_2);
            }else if (line.length<3) {
                excpList.add(ParserUtilConstants.CSV_ATTRIBUTE_NOT_FOUND_MSG_3);
            }if (line.length<4) {
                excpList.add(ParserUtilConstants.COMMENT_NOT_AVAILABLE);
            }
        
}
}

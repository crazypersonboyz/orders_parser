package com.unlimint.process;

import com.unlimint.model.OrderBean;
import com.unlimint.util.ParserUtil;
import com.unlimint.util.ParserUtilConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author neera
 */
public class ProcesJsonTask implements Callable<OrderBean> {

    private final String line;
    private final String fileName;
    private final int id;
    private final AtomicInteger lineAi;

    public ProcesJsonTask(String line, String fileName, int id, AtomicInteger lineAi) {
        this.line = line;
        this.fileName=fileName;
        this.id = id;
        this.lineAi = lineAi;
    }

    @Override
    public OrderBean call() throws Exception {
        Map<String, Object> jsonMp=null;
        List<String> excpList = new ArrayList<>();
        OrderBean model = new OrderBean();
        model.setFilename(fileName);
        try {
             jsonMp = ParserUtil.getJsonObject(new JSONObject(line));
        } catch (Exception e) {
        }
        if (null != jsonMp) {
            jsonMp.entrySet().forEach(e -> {
                //System.out.println(e.getKey() + " : " + e.getValue());
                switch (e.getKey()) {
                    case ParserUtilConstants.ORDER_ID:
                        ParserUtil.validateOrderId(model, e.getValue().toString(), excpList);
                        break;
                    case ParserUtilConstants.AMOUNT:
                        ParserUtil.validateAmount(model, e.getValue().toString(), excpList);
                        break;
                    case ParserUtilConstants.CURRENCY:
                        ParserUtil.validateCurrency(model, e.getValue().toString(), excpList);
                        break;
                    case ParserUtilConstants.COMMENT:
                        ParserUtil.validateComment(model, e.getValue().toString(), excpList);
                        break;
                    default:
                        excpList.add(ParserUtilConstants.INVALID_JSON_ATRRIBUTE+" "+e.getKey());
                        break;
                }
            });
        } else {
            excpList.add(ParserUtilConstants.INVALID_JSON_DATA+"  "+line);
        }
        model.setLine(lineAi.getAndIncrement());
        model.setId(id);
        if (!excpList.isEmpty()) {
            model.setResult(String.join(",", excpList));
        } else {
            model.setResult(ParserUtilConstants.OK);
        }

        return model;
    }

}

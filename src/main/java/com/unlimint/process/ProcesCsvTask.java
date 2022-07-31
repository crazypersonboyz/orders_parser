package com.unlimint.process;

import com.unlimint.model.OrderBean;
import com.unlimint.util.ParserUtil;
import com.unlimint.util.ParserUtilConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author neera
 */
public class ProcesCsvTask implements Callable<OrderBean>{

    private final String[] line;
    private final String fileName;
    private final int id;
    private final AtomicInteger lineAi;

    public ProcesCsvTask(String[] line, String fileName, int id, AtomicInteger lineAi) {
        this.line = line;
        this.fileName=fileName;
        this.id = id;
        this.lineAi = lineAi;
    }

    @Override
    public OrderBean call() throws Exception {
        List<String> excpList = new ArrayList<>();
        OrderBean model = new OrderBean();
        model.setFilename(fileName);
        ParserUtil.validateCsvLine(line, excpList);
        if (excpList.isEmpty()) {
            ParserUtil.validateAmount(model, line[1], excpList);
            ParserUtil.validateCurrency(model, line[2], excpList);
            ParserUtil.validateOrderId(model, line[0], excpList);
            ParserUtil.validateComment(model, line[3], excpList);
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

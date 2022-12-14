package com.unlimint.process;

import com.unlimint.model.OrderBean;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newFixedThreadPool;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author neera
 */
public class ProcessorJson implements Callable <List<OrderBean>>{

    private final String filePath;
    private final AtomicInteger lineAi;
    private final CyclicBarrier cb;

    public ProcessorJson(String filePath, AtomicInteger lineAi, CyclicBarrier cb) {
        this.filePath = filePath;
        this.lineAi = lineAi;
        this.cb = cb;
    }

    @Override
    public List<OrderBean> call() {
        List<OrderBean> modelList = new ArrayList<>();
        lineAi.set(1);
        ExecutorService executor = newFixedThreadPool(10);

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<ProcesJsonTask> processorJsonList;
            processorJsonList = IntStream.range(0, lines.size()).mapToObj((int i) -> {
                return new ProcesJsonTask(lines.get(i), Paths.get(filePath).getFileName().toString(),i + 1, lineAi);
            }).collect(Collectors.toList());
            final List<Future<OrderBean>> invokeAll1 = executor.invokeAll(processorJsonList);

            executor.shutdownNow();
            System.out.println("i am ehere");
            for (Future<OrderBean> f : invokeAll1) {
                modelList.add(f.get());
            }

            cb.await();
        } catch (InterruptedException | IOException | BrokenBarrierException | ExecutionException ex) {
            // Logger.getLogger(ReadCSVFile.class.getName()).log(Level.SEVERE, null, ex);
        }
return modelList;
    }

}

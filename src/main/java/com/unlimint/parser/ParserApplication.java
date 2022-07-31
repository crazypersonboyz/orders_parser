package com.unlimint.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unlimint.model.OrderBean;
import com.unlimint.process.ProcessorCsv;
import com.unlimint.process.ProcessorJson;
import com.unlimint.util.ParserUtilConstants;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserApplication implements CommandLineRunner {

    public static void main(final String[] args) {

        SpringApplication.run(ParserApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        
        if (args.length ==2) {
            List<OrderBean> modelList = new ArrayList<>();
            String jsonFilePath = args[1];
            String csvFilePath = args[0];
            try {
                 Files.readAllLines(Paths.get(jsonFilePath));
            } catch (Exception e) {
                System.out.println(ParserUtilConstants.JSON_FILE_PATH_ERROR);
                return;
            }
            try {
                 Files.readAllLines(Paths.get(csvFilePath));
            } catch (Exception e) {
                System.out.println(ParserUtilConstants.CSV_FILE_PATH_ERROR);
                return;
            }
           
            AtomicInteger lineAi = new AtomicInteger();
            lineAi.set(1);

            CyclicBarrier barrier = new CyclicBarrier(3);
            ExecutorService service = Executors.newFixedThreadPool(2);
            Future<List<OrderBean>> future1 = service.submit(new ProcessorCsv(csvFilePath, lineAi, barrier));
            Future<List<OrderBean>> future2 = service.submit(new ProcessorJson(jsonFilePath, lineAi, barrier));
            try {
                service.shutdown();
                //System.out.println("Main: Waiting for the threads to call await");
                // 4th thread to call await
                barrier.await();
                for (OrderBean order : future1.get()) {
                    modelList.add(order);
                }
                for (OrderBean inOrder : future2.get()) {
                    modelList.add(inOrder);
                }

                modelList.stream().forEach((OrderBean e) -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        String json = mapper.writeValueAsString(e);
                        System.out.println(json);
                    } catch (JsonProcessingException ex) {

                    }
                });
          //System.out.println("Main: Finished");

            } catch (BrokenBarrierException | InterruptedException e) {
                //  e.printStackTrace();
            }
        }else{
            System.out.println(ParserUtilConstants.INPUT_ARG_ERROR);
        }

    }

}

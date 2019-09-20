/*
package com.finance.index.calculate.util;

import com.product.aggregator.domain.Product;
import com.product.aggregator.exception.AggregatorBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ParseExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ParseExcelUtils.class);
    private static final String SYSTEM_USER = "ADMIN";

    public static List<Product> parseFile(String fileName) throws AggregatorBusinessException {
        String line = "";
        String cvsSplitBy = ",";
        List<Product> list = new ArrayList<>();

        //fileNames - The names of the publisher advertising reporting data
        String fileNames[] = {"templates/"+fileName};

        for(String file : fileNames) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new ClassPathResource(file).getInputStream()));) {
                int count = 0;
                while ((line = br.readLine()) != null) {
                    // ignore headers
                    if (count == 0) {
                        count++;
                        continue;
                    }

                    // use comma as separator
                    String[] data = line.split(cvsSplitBy);
                    list.add(new Product(data[0], data[1], data[2], data[3], data[4], data[5], new Date().toString(), SYSTEM_USER ));
                }

            } catch (FileNotFoundException ex) {
                throw new AggregatorBusinessException("File not found [ERR151], Msg : " + ex.getMessage()
                        ,ex);
            } catch (IOException ex) {
                throw new AggregatorBusinessException("Error while doing I/O operations [ERR152], Msg : " + ex.getMessage()
                        ,ex);
            }
        }
        return list;
    }
}
*/

package com.nuanyou.cms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Felix on 2017/5/16.
 */
public class GenerateSql {

    private static final Logger log = LoggerFactory.getLogger(GenerateSql.class.getSimpleName());

    private static void genSQL(String file, String sql) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(Thread.class.getResourceAsStream(file), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split("\t");
            log.error("insert into bd_contract_template_parameter (remark,type) values('{}','{}');", split);
        }
        br.close();
    }

    public static void main1(String[] args) throws Exception {
        genSQL("/Noname1.txt", "");
    }
}

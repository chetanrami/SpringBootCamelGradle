package org.chetan.camel.rest.example.spring.boot;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;

public class ScratchPad {
    public static void main(String [] args)throws Exception{
        String abc = new String(Files.readAllBytes(Paths.get("src/test/resources/OriginalResponse")));
        String start = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        String end = "</env:Body></env:Envelope>";
        System.out.println(start + StringUtils.substringBetween(abc, start, end) + end);
    }
}

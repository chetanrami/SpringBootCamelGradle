package org.chetan.camel.rest.example.spring.boot.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class StringConverter {

    public String convert2Base64(String string){
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    public String convertBase642String(String string){
        byte[] decodedBytes = Base64.getDecoder().decode(string);
        return new String(decodedBytes);
    }

}

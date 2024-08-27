package com.sunil.TrendNest.Utils;

public class JwtUtil {
    public static String extractJwtToken (String header){
        return header.substring(7);
    }
}

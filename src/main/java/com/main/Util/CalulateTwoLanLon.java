package com.main.Util;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.NumberFormat;

@Component
public class CalulateTwoLanLon {
    //地球半径,单位千米
   // private static final double EARTH_RADIUS = 6378.137;
    private static final double EARTH_RADIUS = 6378137;
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    public static double getDistance(double lat1,double lng1,double lat2,double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
       // s = Math.round(s * 10000) / 10000;
        s = Math.round(s * 10000);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.UP);
        Double.parseDouble(nf.format(s/10000));

        return Double.parseDouble(nf.format(s/10000));

    }
    @Test
    public static double getDistance2(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    public static double distanceByLongNLat(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R = 6378137;//地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }
    public static double distanceByLongNLat2(double lat1, double long1, double lat2, double long2) {
        double a, b, R;
        R = 6378137;//地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }
}

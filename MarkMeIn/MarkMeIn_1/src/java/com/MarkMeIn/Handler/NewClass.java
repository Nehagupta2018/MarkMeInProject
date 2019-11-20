/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MarkMeIn.Handler;

import java.text.DecimalFormat;

/**
 *
 * @author test
 */
public class NewClass {

    public static void main(String[] args) {
        double p = 0.0;
        p = ((double) 4 / (26 * 4)) * 100;
         DecimalFormat decFormat = new DecimalFormat("##.00");
        System.out.println(decFormat.format(p));
    }
}

package com.utils;

import java.io.IOException;

public class OpenBrowse extends Thread {
    private static void openBrowse(String[] urls) {
        for (String url : urls) {
            if (java.awt.Desktop.isDesktopSupported()) {
                try {
                    //创建一个URI实例,注意不是URL
                    java.net.URI uri = java.net.URI.create(url);
                    //获取当前系统桌面扩展
                    java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                    //判断系统桌面是否支持要执行的功能
                    if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        //获取系统默认浏览器打开链接
                        dp.browse(uri);
                    }
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000*60*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        while(true) {
            String[] urls = {"http://cloudbility.cmbchina.io/fintech-szh/hosts/cloud/142"};
            openBrowse(urls);
        }
    }

    public static void main(String[] args) {
        Thread thread = new OpenBrowse();
        thread.start();
    }
}

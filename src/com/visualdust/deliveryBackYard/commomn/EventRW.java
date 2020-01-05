package com.visualdust.deliveryBackYard.commomn;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventRW {
    public static File logoutfile = new File(LocalDate.now().toString() + "_" + Resource.SOFTWARE_NAME + ".md");
    public static OutputStream logstream;

    public static void Write(Exception e) {
        try {
            logstream = new FileOutputStream(logoutfile, true);
            logstream.write(("").getBytes());
            logstream.write(("> " + LocalDateTime.now().toString() + " Exception : " + e.toString() + "\r\n\r\n").getBytes());
            logstream.write(("").getBytes());
        } catch (Exception e1) {
            System.out.println(e1.toString());
            e1.printStackTrace();
        }
        System.out.println(e.toString());
        e.printStackTrace();
    }

    public static void Write(String event) {
        try {
            logstream = new FileOutputStream(logoutfile, true);
            logstream.write((LocalDateTime.now().toString() + " Event : " + event + "  \r\n").getBytes());
        } catch (Exception e1) {
            System.out.println(e1.toString());
            e1.printStackTrace();
        }
        System.out.println(event);
    }

    public static void WriteAsRichText(boolean succeed, String whoOccur, String extraMessage) {
        String str = /*"<color=" + (succeed ? "green" : "red") + ">" + */(succeed ? "[√]" : "[×]")/* + "</color> " */+
                whoOccur + /*"<color=grey>" +*/ extraMessage /*+ "</color>"*/;
        Write(str);
    }

    public static void WriteStrOnly(String string) {
        try {
            logstream = new FileOutputStream(logoutfile, true);
            logstream.write((string + "  \r\n").getBytes());
        } catch (Exception e1) {
            System.out.println(e1.toString());
            e1.printStackTrace();
        }
        System.out.println(string);
    }
}

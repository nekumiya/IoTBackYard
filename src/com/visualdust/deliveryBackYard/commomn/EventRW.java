package com.visualdust.deliveryBackYard.commomn;


import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventRW {
    public static File logoutfile = new File("Log_" + LocalDate.now().toString() + "_" + Resource.SOFTWARE_NAME + Resource.VERSION + ".log");
    public static OutputStream logstream;
    public static LocalDateTime dateTime = LocalDateTime.now();

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
        System.out.print(e.toString() + "\n>>>");
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
        System.out.print(event + "\n>>>");
    }

    public static void WriteAsRichText(boolean succeed, String whoOccur, String extraMessage) {
        String str = /*"<color=" + (succeed ? "green" : "red") + ">" + */(succeed ? "[√]" : "[×]")/* + "</color> " */ +
                whoOccur + /*"<color=grey>" +*/ extraMessage /*+ "</color>"*/;
        Write(str);
    }

    public static void WriteStrOnly(String string) {
        try {
            logstream = new FileOutputStream(logoutfile, true);
            logstream.write((string).getBytes());
        } catch (Exception e1) {
            System.out.println(e1.toString());
            e1.printStackTrace();
        }
        System.out.print(string + "\n>>>");
    }

    public static void updateTime() {
        try {
            File runtimeFile = new File(Resource.VERSION + "_runtime");
            if (!runtimeFile.exists()) {
                PrintStream printStream = new PrintStream(runtimeFile);
                printStream.print("0");
            }
            InputStream runtimeInStream = new FileInputStream(runtimeFile);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runtimeInStream, Charset.forName("UTF-8")));
            String str = bufferedReader.readLine();
            int runtime = Integer.valueOf(str);
            FileOutputStream runtimeOutStream = new FileOutputStream(runtimeFile, false);
            runtimeOutStream.write(String.valueOf(++runtime).getBytes());
            logoutfile = new File("Log_" + LocalDate.now().toString() + "_" + Resource.SOFTWARE_NAME + Resource.VERSION + ".log");
            EventRW.Write("----------" + LocalDateTime.now() + ">Version=" + Resource.VERSION +
                    ">ServerRuntimeClockBump: " + runtime +
                    "hours after server launch----------");
        } catch (Exception e) {
            EventRW.Write(e);
        }
    }
}

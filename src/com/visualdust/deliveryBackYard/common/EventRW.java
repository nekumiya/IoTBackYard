package com.visualdust.deliveryBackYard.common;


import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, Integer> runtimes = new HashMap<>();

    public static void GainRunTime(String sideName) throws FileNotFoundException {
        File runtimeFile = new File(sideName + "" + Resource.VERSION + "_runtime");
        try {
            if (!runtimeFile.exists()) {
                PrintStream printStream = new PrintStream(runtimeFile);
                printStream.print("0");
            }
            InputStream runtimeInStream = new FileInputStream(runtimeFile);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runtimeInStream, Charset.forName("UTF-8")));
            String str = bufferedReader.readLine();
            runtimes.putIfAbsent(sideName, Integer.valueOf(str));
            FileOutputStream runtimeOutStream = new FileOutputStream(runtimeFile, false);
            runtimes.replace(sideName, runtimes.get(sideName) + 1);
            runtimeOutStream.write(String.valueOf(runtimes.get(sideName)).getBytes());
            logoutfile = new File("Log_" + LocalDate.now().toString() + "_" + Resource.SOFTWARE_NAME + Resource.VERSION + ".log");

        } catch (Exception e) {
            EventRW.Write(e);
            PrintStream printStream = new PrintStream(runtimeFile);
            printStream.print("0");
        }
    }

    public static String getRuntimeLog(String sideName) {
        runtimes.putIfAbsent(sideName, 0);
        return "[Runtime]>" + LocalDateTime.now() + ">Version=" + Resource.VERSION +
                ">ServerRuntimeClockBump: " + runtimes.get(sideName) +
                " hour(s) after server launched";
    }
}

package banking;

import java.util.ArrayList;
import java.util.Scanner;


// we do need _only_one_ log and Scanner(System.in) for our application
// so let's utilize the 'singleton' design pattern

class LoggedConsoleIO {
    private static LoggedConsoleIO loggedConsoleIOSingleton = null;
    private static ArrayList<String> actualLog = null;
    private static Scanner sc = null;

    private LoggedConsoleIO() {
        actualLog = new ArrayList<>();
        sc = new Scanner(System.in);
    }

    static LoggedConsoleIO getInstance() {
        if (loggedConsoleIOSingleton != null) {
            assert actualLog != null : "LoggedConsoleIO instance is not null and private variable 'actualLog' is null.";
            assert sc != null : "LoggedConsoleIO Instance is not null and private variable 'sc' is null.";

            return loggedConsoleIOSingleton;
        }

        return loggedConsoleIOSingleton = new LoggedConsoleIO();
    } // static Log getInstance()


    // method: String read(Boolean doLog)
    // input parameter:
    //  -   Boolean doLog - if 'true' logs strings read from System.in
    //      (and then returned) to 'ArrayList<String> actualLog' too
    //      because 'doLog' is a method's parameter,
    //      one can turn on/off logging of every individual method's calls
    // returns:
    //  -   String read from 'System.in' (and possibly logged / saved in 'actualLog')
    //
    String read(Boolean doLog) {
        String s = sc.nextLine().strip();
        if (doLog) actualLog.add(s);
        return s;
    } // String readLineWithLog()


    // method: void print(String s, Boolean doLog)
    // input parameters:
    //  -   s - string to print and (optionally) log
    //  -   doLog - if 'true' logs strings to be printed to 'ArrayList<String> actualLog' too
    //      because 'doLog' is a method's parameter, one can turn on/off logging of every individual method's calls
    // returns:
    //  -   void
    //
    void print(String s, Boolean doLog) {
        System.out.println(s);
        if (doLog) actualLog.add(s);
    } //  void printLineWithLog()


    // returns size of  ArrayList<String> 'actualLog'
    int getSize() {
        return actualLog.size();
    }

    public String toString() {
        if (actualLog == null || actualLog.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder("LoggedConsoleIO{\n");

        for (String s : actualLog) {
            sb.append(s);
            sb.append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    } // toString()

} // class Log

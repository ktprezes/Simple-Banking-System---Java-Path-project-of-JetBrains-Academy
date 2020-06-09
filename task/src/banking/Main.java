package banking;

/*
 * JBA - JetBrains Academy
 * project: 'simple banking system'
 * www: https://hyperskill.org/projects/93?goal=7
 *
 * 2020-06-06 - starting project / stage #1 'card anatomy'
 *
 * stage #1 "card anatomy"
 *      www: https://hyperskill.org/projects/93/stages/515/implement
 *      goal of the stage: create account / login to account / check balance
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main implements AppConst {

    // variables regarding processing console operations - user inputs / outputs
    static Scanner sc = new Scanner(System.in);
    static String action = "";
    static String subAction = ""; // submenu action
    static ArrayList<String> log = new ArrayList<>();

    static Map<String, Account> accounts = new HashMap<>();
    static String currAccNo = "";

    static APP_STATES state = APP_STATES.MAIN_LOOP;

    // it can be changed via CLI argument '-fileName <fileName>'
    static String dbFileName = DB_FILE_DEF_NAME;

    public static void main(String[] args) {


        // process command line arguments
        // the condition is 'i < args.length -1' because we do assume
        // the last argument is file name itself, not the '-fileName' option
        for (int i = 0; i < args.length - 1; i++) {
            if ("-filename".equals(args[i].toLowerCase())) {
                // we take the 'fileName' argument literally
                // without any 'to lower / upper case' conversion
                dbFileName = args[++i];
            }
        }
// TODO: try to open or create the DB file
/*
        if (doInitialImport) {
            processInitialImport();
        }

        // print command line arguments read
        if (DEBUG_LVL > 0) {
            printAndLog("Command Line Arguments:", DO_LOG);
            if (doInitialImport) printAndLog("Init. import file: " + initialImportFileName, DO_LOG);
            if (doFinalExport) printAndLog("Final export file: " + finalExportFileName, DO_LOG);
         }
*/


        // BEGIN OF MAIN APPLICATION LOOP
        do {

            printAndLog(MAIN_MENU_STR, DO_LOG);
            action = readAndLog(DO_LOG).toUpperCase();

            switch (action) { // begin of 'main menu' switch

                case "1": // create account
                    processAccCreate();
                    break; // end of "1" - create account

                case "2": // log into account
                    currAccNo = processAccLogin();
                    if (currAccNo != null) {
                        // when successfully logged into account
                        // process account operations
                        state = APP_STATES.LOGGED_IN;

                        do { // begin of 'logged into account' loop

                            printAndLog(ACC_SUBMENU_STR, DO_LOG);
                            subAction = readAndLog(DO_LOG);

                            switch (subAction) { // being of 'logged into account' switch
                                case "0": // exit
                                    state = APP_STATES.ON_EXIT;
                                    break;

                                case "1": // balance
                                    processBalance(currAccNo);
                                    break;

                                case "2": // logout - intentionally no break
                                default: // 'unknown command' - the same as 'logout'
                                    currAccNo = "";
                                    printAndLog("\nYou have successfully logged out!", DO_LOG);
                                    state = APP_STATES.MAIN_LOOP;

                            } // end of 'logged into account' submenu switch

                        } while (state == APP_STATES.LOGGED_IN);
                        // end of 'logged into account' loop
                    } // if(currAccNo != null)
                    break; // end of "2" - login into account

                case "IMPORT":
//                    processImport();
                    break; // end of "IMPORT"

                case "EXPORT":
//                    processExport();
                    break; // end of "EXPORT"

                case "LIST": // list all accounts stored / remembered
                    processAccList();
                    break; // end of "LIST"

                case "LOG":
//                    processLog();
                    break; // end of "LOG"

                case "RESET STATS":
//                    processResetStats();
                    break; // end of "RESET STATS"

                case "0": // exit
                    printAndLog("Bye!", DO_LOG);
                    state = APP_STATES.ON_EXIT;
                    break; // leave the program :)

                default:
                    if (DEBUG_LVL > 0) {
                        printAndLog("I'm so sorry - I don't understand your command: "
                                + MSG_DELIM + action + MSG_DELIM, DO_LOG);
                    }
                    // end of "DEFAULT"

            } // end of 'main menu' switch

        } while (state != APP_STATES.ON_EXIT);
        // end of main application loop

    } // psv main()


    // creates new account and puts it into 'accounts' map
    // prints it's 'full number' and 'pin' value
    //
    private static void processAccCreate() {

        Account newAcc = new Account();
        accounts.put(newAcc.getFullNoAsString(), newAcc);

        printAndLog("\nYour card has been created\nYour card number:", DO_LOG);
        printAndLog(newAcc.getFullNoAsString(), DO_LOG);
        printAndLog("Your card PIN:", DO_LOG);
        printAndLog(newAcc.getPinAsString(), DO_LOG);
    } // private static void processAccCreate()


    // asks the user for the account / card number and pin
    // and check if the account with that number and pin do exist
    // returns:
    //   on 'successful login': String representing 16-digit 'long number' of account you are 'logged into'
    //   on 'login failed': 'null'
    //
    private static String processAccLogin() {
        printAndLog("\nEnter your card number:", DO_LOG);
        String no = readAndLog(DO_LOG);
        printAndLog("Enter your PIN:", DO_LOG);
        String pin = readAndLog(DO_LOG);

        if (no == null || pin == null || "null".equals(no) || "null".equals(pin)) {
            printAndLog("\nWrong card number or PIN!", DO_LOG);
            return null;
        }

        if (!accounts.containsKey(no)) {
            printAndLog("\nWrong card number or PIN!", DO_LOG);
            return null;
        }

        if (!accounts.get(no).getPinAsString().equals(pin)) {
            printAndLog("\nWrong card number or PIN!", DO_LOG);
            return null;
        }

        printAndLog("\nYou have successfully logged in!", DO_LOG);
        return no;
    } // private static void processAccLogin()


    private static void processBalance(String acc) {
        if (acc == null || "null".equals(acc) || "".equals(acc))
            return;
        if (!accounts.containsKey(acc))
            return;

        printAndLog("\nBalance: " + accounts.get(acc).getBalance(), DO_LOG);
    } // private static void processBalance()

    // list all created / remembered accounts
    //
    private static void processAccList() {
        for (Account acc : accounts.values()) {
            printAndLog(acc.toString(), DO_LOG);
        }
    } // private static void processAccList() {


    // uses global symbols:
    // Scanner sc - scanner from System.in
    // ArrayList<String> log - array to log input and outputs
    // parameter:
    // Boolean doLog - if 'true' logs returned strings (read from System.in) to 'ArrayList<String> log' too
    // because 'doLog' is a method's parameter, one can turn on/off logging of every individual method's calls
    //
    private static String readAndLog(Boolean doLog) {
        String s = sc.nextLine().strip();
        if (doLog) log.add(s);
        return s;
    } // private static String readLineWithLog()


    // uses global symbols:
    // ArrayList<String> log - array to log input and outputs
    // parameters:
    //  - s - string to print and (optionally) log
    //  - doLog - if 'true' logs returned strings (read from System.in) to 'ArrayList<String> log' too
    // because 'doLog' is a method's parameter, one can turn on/off logging of every individual method's calls
    //
    private static void printAndLog(String s, Boolean doLog) {
        System.out.println(s);
        if (doLog) log.add(s);
    } // private static void printLineWithLog()

} // class BankSystem

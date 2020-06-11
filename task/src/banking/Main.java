package banking;

/*
 * JBA - JetBrains Academy
 * project: 'simple banking system'
 * www: https://hyperskill.org/projects/93?goal=7
 *
 * 2020-06-06 - starting project / stage #1 'card anatomy'
 * 2020-06-07 - begin of stage #2 'Luhn formula'
 * 2020-06-09 - begin of stage #3 'I'm so lite'
 *
 * stage #1 "card anatomy"
 *      www: https://hyperskill.org/projects/93/stages/515/implement
 *      goal of the stage: create account / login to account / check balance
 * stage #2 "Luhn algorithm"
 *      www: https://hyperskill.org/projects/93/stages/516/implement
 *      goal of the stage: check digit generation, card number validation
 *          according to the 'Luhn formula'
 * stage #3 "I'm so lite"
 *      www: https://hyperskill.org/projects/93/stages/517/implement
 *      goal of the stage: ensuring persistence of collected data
 *          using SQLite DB
 */


import banking.constants.AppConst;
import banking.constants.DBConst;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class Main implements AppConst, DBConst {

    // variables regarding processing console operations - user inputs / outputs
    // the Scanner(System.in) is hidden inside the 'LoggedConsoleIO' class
    //
    static LoggedConsoleIO loggedIO = LoggedConsoleIO.getInstance();
    static String action = "";
    static String subAction = ""; // submenu action

    // while implementing stage#3 of this application
    // it was necessary to use _the_same_ log in different classes
    // ('Main' and 'SimpleBankingDataBase' eg.)
    // therefore it was necessary to make the 'Log' as separate class
    // utilizing the 'singleton' design pattern
    // thus hat 'log' local variable became obsolete
    // static ArrayList<String> log = new ArrayList<>();

    static Map<String, Account> accounts = new HashMap<>();
    static String currAccNo = "";

    static APP_STATES state = APP_STATES.MAIN_LOOP;

    // it can be changed via CLI argument '-fileName <fileName>'
    static String dbFileName = DB_IN_MEMORY_NAME;
//  static String dbFileName = DB_FILE_DEF_NAME;

    static AccDataBase accDB = null;

    /*******************************************************************************
     *
     *  begin of public static void main()
     *
     */
    public static void main(String[] args) {

        // process command line arguments
        // the condition is 'i < args.length -1' because we do assume
        // the last argument is the database file name itself,
        // so the '-fileName' option has to be second-to-last
        for (int i = 0; i < args.length - 1; i++) {
            if ("-filename".equals(args[i].toLowerCase())) {
                // we take the 'fileName' argument literally
                // without any 'to lower / upper case' conversion
                dbFileName = args[++i];
                break;
            }
        }

        if (DEBUG_LVL > 0) {
            loggedIO.print("DB file name: " + dbFileName, DO_LOG);
        }
// TODO: try to open or create the DB file

        accDB = AccDataBase.openOrCreate(dbFileName);


        // BEGIN OF MAIN APPLICATION LOOP
        do {

            loggedIO.print(MAIN_MENU_STR, DO_LOG);
            action = loggedIO.read(DO_LOG).toUpperCase();

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

                            loggedIO.print(ACC_SUBMENU_STR, DO_LOG);
                            subAction = loggedIO.read(DO_LOG);

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
                                    loggedIO.print("\nYou have successfully logged out!", DO_LOG);
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
                    processLog();
                    break; // end of "LOG"

                case "RESET STATS":
//                    processResetStats();
                    break; // end of "RESET STATS"

                case "0": // exit
                    loggedIO.print("Bye!", DO_LOG);
                    state = APP_STATES.ON_EXIT;
                    break; // leave the program :)

                default:
                    if (DEBUG_LVL > 0) {
                        loggedIO.print("I'm so sorry - I don't understand your command: "
                                + MSG_DELIM + action + MSG_DELIM, DO_LOG);
                    }
                    // end of "DEFAULT"

            } // end of 'main menu' switch

        } while (state != APP_STATES.ON_EXIT);
        // end of main application loop

        accDB.close();

    } // psv main()


    /*******************************************************************************
     *
     *  end of public static void main()
     *  begin of other method's section
     *
     */


    // creates new account and puts it into 'accounts' map
    // prints it's 'full number' and 'pin' value
    //
    private static void processAccCreate() {

        Account newAcc = new Account();
        accounts.put(newAcc.getFullNoAsString(), newAcc);

        loggedIO.print("\nYour card has been created\nYour card number:", DO_LOG);
        loggedIO.print(newAcc.getFullNoAsString(), DO_LOG);
        loggedIO.print("Your card PIN:", DO_LOG);
        loggedIO.print(newAcc.getPinAsString(), DO_LOG);
    } // private static void processAccCreate()


    // asks the user for the account / card number and pin
    // and check if the account with that number and pin do exist
    // returns:
    //   on 'successful login': String representing 16-digit 'long number' of account you are 'logged into'
    //   on 'login failed': 'null'
    //
    private static String processAccLogin() {
        loggedIO.print("\nEnter your card number:", DO_LOG);
        String no = loggedIO.read(DO_LOG);
        loggedIO.print("Enter your PIN:", DO_LOG);
        String pin = loggedIO.read(DO_LOG);

        if (no == null || pin == null || "null".equals(no) || "null".equals(pin)) {
            loggedIO.print("\nWrong card number or PIN!", DO_LOG);
            return null;
        }

        if (!accounts.containsKey(no)) {
            loggedIO.print("\nWrong card number or PIN!", DO_LOG);
            return null;
        }

        if (!accounts.get(no).getPinAsString().equals(pin)) {
            loggedIO.print("\nWrong card number or PIN!", DO_LOG);
            return null;
        }

        loggedIO.print("\nYou have successfully logged in!", DO_LOG);
        return no;
    } // private static void processAccLogin()


    private static void processBalance(String acc) {
        if (acc == null || "null".equals(acc) || "".equals(acc))
            return;
        if (!accounts.containsKey(acc))
            return;

        loggedIO.print("\nBalance: " + accounts.get(acc).getBalance(), DO_LOG);
    } // private static void processBalance()


    // lists data of all stored accounts / cards
    //
    private static void processAccList() {
        for (Account acc : accounts.values()) {
            loggedIO.print(acc.toString(), DO_LOG);
        }
    } // private static void processAccList() {


    // saves IO Log to file
    //
    private static void processLog() {
        if (DEBUG_LVL > 1) loggedIO.print("Save log to file.", DO_LOG);
        if (loggedIO.getSize() > 0) {
            loggedIO.print("File name:", DO_LOG);

            try (PrintWriter printWriter = new PrintWriter(new FileWriter(loggedIO.read(DO_LOG), FILE_APPEND), FILE_AUTO_FLUSH)) {
                printWriter.println(loggedIO.toString());
                loggedIO.print("The log has been saved.", DO_LOG);
            } catch (FileNotFoundException e) {
                loggedIO.print("File not found.", DO_LOG);
            } catch (Exception e) {
                loggedIO.print("Some Other Exception Occurred.", DO_LOG);
            }

        } else {
            loggedIO.print("Log is empty.", DO_LOG);
        }
    } // private static void processLog()

} // class BankSystem

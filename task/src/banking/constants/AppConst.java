package banking.constants;

public interface AppConst { // constants regarding the application as a whole

    // the higher DEBUG_LVL is
    // the more verbose this application is
    // DEBUG_LVL == 0 means 'no debug info'
    //
    int DEBUG_LVL = 1;

    // when 'true' every console input and output is recorded into:
    // static ArrayList<String> log = new ArrayList<>();
    //
    boolean DO_LOG = true;

    boolean FILE_APPEND = false;
    boolean FILE_AUTO_FLUSH = true;

    String MSG_DELIM = "\"";

    enum APP_STATES {
        MAIN_LOOP, LOGGED_IN, ON_EXIT
    }

    String APP_NAME = "SimpleBanking";

    String MAIN_MENU_STR =
            "\n1. Create an account"
                    + "\n2. Log into account"
                    + "\n0. Exit";

    String ACC_SUBMENU_STR =
            "\n1. Balance"
                    + "\n2. Log out"
                    + "\n0. Exit";

} // public interface AppConst

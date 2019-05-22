package com.mrgarin.mininmonitor.aplication;

public class AppConfig {

    /************************************* Bases URL's ******************************************/

    public static final String baseurl_btcCom = "https://pool.api.btc.com/v1/";
    public static final String baseurl_ethermineorg = "https://api.ethermine.org";
    public static final String baseurl_binance = "https://api.binance.com";

    /******************************** end of Basses URL's ***************************************/

    /******************************** Debug options *********************************************/
    public static final boolean write_debug_save_on_sd = false;
    public static final boolean debug = true;
    /******************************** end of debug options***************************************/

    /******************************** Constants  ************************************************/
    public static final String ALERT_NOTIFICATION_CHANEL = "Alert notifications";
    /******************************** end of Constants ******************************************/

    /******************************** Global Values *********************************************/
    public static int userAutoUpdateTime = 10;
    private static final int timeMinutes = 60000;
    public static int autoUpdateTime = userAutoUpdateTime * timeMinutes;
    public static double buildVersion = 0.494;
    public static boolean reNewAutoUpdate = false;
    /******************************** end og Global Values **************************************/

    /******************************** Values Procedures *****************************************/
    public static void setUserAutoUpdateTime(int time){
        userAutoUpdateTime = time;
        autoUpdateTime = userAutoUpdateTime * timeMinutes;
        reNewAutoUpdate = true;
    }
    /******************************* end of Values Procedures ***********************************/
}

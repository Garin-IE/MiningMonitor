package com.mrgarin.mininmonitor.aplication;

public class AppConfig {

    /************************************* Bases URL's ******************************************/

    public static final String baseurl_btcCom = "https://pool.api.btc.com/v1/";
    public static final String baseurl_ethermineorg = "https://api.ethermine.org";
    public static final String baseurl_binance = "https://api.binance.com";

    /******************************** end of Basses URL's ***************************************/

    /******************************** Debug options *********************************************/
    public static final boolean write_debug_save_on_sd = false;
    public static final boolean debug = false;
    public static final boolean isDebug_mining_monitor_service = false;
    /******************************** end of debug options***************************************/

    /******************************** Constants  ************************************************/
    public static final String ALERT_NOTIFICATION_CHANEL = "Alert notifications";
    public static final String SERVICE_NOTIFICATION_CHANEL = "Service notifications";
    /******************************** end of Constants ******************************************/

    /******************************** Global Values *********************************************/
    public static int userAutoUpdateTime = 1;
    private static final int timeMinutes = 60000;
    public static int autoUpdateTime = userAutoUpdateTime * timeMinutes;
    public static double buildVersion = 0.495;
    public static boolean reNewAutoUpdate = false;
    public static boolean autoUpdatePools = true;
    /******************************** end og Global Values **************************************/

    /******************************** Values Procedures *****************************************/
    public static void setUserAutoUpdateTime(int time){
        userAutoUpdateTime = time;
        autoUpdateTime = userAutoUpdateTime * timeMinutes;
        reNewAutoUpdate = true;
    }
    /******************************* end of Values Procedures ***********************************/
}

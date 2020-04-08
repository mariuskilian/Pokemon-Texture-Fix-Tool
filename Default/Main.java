package Default;

public class Main {

    public static String prefix = "";
    public static String ending = "png";
    public static String[] locationFixedOptions = {"Prefix", "Postfix"};
    public static int locationFixed = 0;
    public static boolean createEyeMerged = true;
    public static boolean affectSubfolders = false;
    public static String[] formatFileNameOptions = {"No", "Try", "Force"};
    public static int formatFileName = 0; //0 = No, 1 = Try, 2 = Force
    public static boolean deleteOldFiles = false;
    
    public static void main(final String[] args) {
        GUI.popUpWindow();
    }
}
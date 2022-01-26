package Main;

/**
 * This class holds methods that accommodate the user's language preferences
 */
public class Language {

    /**
     * This variable holds the current language setting
     */
    private static boolean localLanguageIsFrench;

    /**
     * This method returns the current language setting
     * @return language setting
     */
    public static boolean getLocalLanguageIsFrench() {
        return localLanguageIsFrench;
    }

    /**
     * This method sets the current language setting
     * @param localLanguageIsFrench language setting
     */
    public static void setLocalLanguageIsFrench(boolean localLanguageIsFrench) {
        Language.localLanguageIsFrench = localLanguageIsFrench;
    }
}

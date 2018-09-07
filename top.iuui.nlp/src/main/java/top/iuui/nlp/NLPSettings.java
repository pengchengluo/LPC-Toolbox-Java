package top.iuui.nlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NLPSettings {

    private static Logger log = Logger.getLogger(NLPSettings.class.getName());

    private static String fudanNLPModePath;
    private static String stanfordNLPModePath;

    static {
        Properties properties = new Properties();
        try (InputStream inStream = NLPSettings.class.getClassLoader().getResourceAsStream("nlp.properties")){
            properties.load(inStream);
            fudanNLPModePath = properties.getProperty("fudannlp_model_path");
            if(fudanNLPModePath.endsWith("/")) {
                fudanNLPModePath = fudanNLPModePath.substring(0, fudanNLPModePath.length() - 1);
            }
            stanfordNLPModePath = properties.getProperty("stanfordnlp_mode_path");
            if(stanfordNLPModePath.endsWith("/")){
                stanfordNLPModePath = stanfordNLPModePath.substring(0, stanfordNLPModePath.length()-1);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE,"", e);
        }

    }

    public static String getFudanNLPModelPath(){
        return fudanNLPModePath;
    }

    public static String getStanfordNLPModePath(){
        return stanfordNLPModePath;
    }
}

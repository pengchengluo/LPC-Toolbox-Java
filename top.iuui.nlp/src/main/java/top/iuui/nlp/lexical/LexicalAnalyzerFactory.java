package top.iuui.nlp.lexical;

import top.iuui.nlp.lexical.impl.FudanNLPLexicalAnalyzer;
import top.iuui.nlp.lexical.impl.HanLPLexicalAnalyzer;
import top.iuui.nlp.lexical.impl.StanfordNLPLexicalAnalyzer;

public class LexicalAnalyzerFactory {

    public static LexicalAnalyzer create(String name){
        switch (name){
            case "HanLP":
                return HanLPLexicalAnalyzer.getInstance();
            case "FudanNLP":
                return FudanNLPLexicalAnalyzer.getInstance();
            case "StanfordNLP":
                return StanfordNLPLexicalAnalyzer.getInstance();
            default:
                return HanLPLexicalAnalyzer.getInstance();
        }
    }

}

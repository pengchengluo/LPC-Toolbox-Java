package top.iuui.nlp.lexical.impl;

import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.util.exception.LoadModelException;
import top.iuui.nlp.NLPSettings;
import top.iuui.nlp.lexical.LexicalAnalyzer;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.joining;

public class FudanNLPLexicalAnalyzer implements LexicalAnalyzer {

    private static Logger log = Logger.getLogger(FudanNLPLexicalAnalyzer.class.getName());
    private CNFactory factory;

    private FudanNLPLexicalAnalyzer(){
        if (NLPSettings.getFudanNLPModelPath() != null) {
            try {
                factory = CNFactory.getInstance(NLPSettings.getFudanNLPModelPath(),
                        CNFactory.Models.SEG);
            } catch (LoadModelException e) {
                log.log(Level.SEVERE, "", e);
            }
        }
    }

    @Override
    public String segment(String text) {
        if (factory == null) {
            return "";
        }
        return Arrays.stream(factory.seg(text))
                .collect(joining(" "));
    }

    private static FudanNLPLexicalAnalyzer fudanNLPLexicalAnalyzer;
    public static FudanNLPLexicalAnalyzer getInstance(){
        if(fudanNLPLexicalAnalyzer == null){
            fudanNLPLexicalAnalyzer = new FudanNLPLexicalAnalyzer();
        }
        return fudanNLPLexicalAnalyzer;
    }
}

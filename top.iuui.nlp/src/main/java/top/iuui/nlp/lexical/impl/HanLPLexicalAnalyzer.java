package top.iuui.nlp.lexical.impl;

import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import top.iuui.nlp.lexical.LexicalAnalyzer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.joining;


public class HanLPLexicalAnalyzer implements LexicalAnalyzer {

    private static Logger log = Logger.getLogger(HanLPLexicalAnalyzer.class.getName());

    private CRFLexicalAnalyzer analyzer;

    private HanLPLexicalAnalyzer(){
        try {
            analyzer = new CRFLexicalAnalyzer();
        } catch (IOException e) {
            log.log(Level.SEVERE,"", e);
        }
    }

    @Override
    public String segment(String text) {
        if (analyzer == null) {
            return "";
        }
        return analyzer.seg(text).stream()
                .map(e -> e.word)
                .collect(joining(" "));
    }

    private static HanLPLexicalAnalyzer hanLPLexicalAnalyzer;

    public static HanLPLexicalAnalyzer getInstance(){
        if (hanLPLexicalAnalyzer == null){
            hanLPLexicalAnalyzer = new HanLPLexicalAnalyzer();
        }
        return hanLPLexicalAnalyzer;
    }
}

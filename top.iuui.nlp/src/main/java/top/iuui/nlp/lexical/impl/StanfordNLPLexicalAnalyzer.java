package top.iuui.nlp.lexical.impl;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import top.iuui.nlp.NLPSettings;
import top.iuui.nlp.lexical.LexicalAnalyzer;
import java.util.Properties;

import static java.util.stream.Collectors.joining;

public class StanfordNLPLexicalAnalyzer implements LexicalAnalyzer {

    private StanfordCoreNLP pipeline;

    public StanfordNLPLexicalAnalyzer(){
        String modelPath = NLPSettings.getStanfordNLPModePath();
        if(modelPath != null) {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit");
            props.setProperty("tokenize.language", "zh");
            props.setProperty("segment.model", modelPath + "/edu/stanford/nlp/models/segmenter/chinese/ctb.gz");
            props.setProperty("segment.sighanCorporaDict", modelPath + "/edu/stanford/nlp/models/segmenter/chinese");
            props.setProperty("segment.serDictionary", modelPath + "/edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz");
            props.setProperty("segment.sighanPostProcessing", "true");
            props.setProperty("ssplit.boundaryTokenRegex", "[.。]|[!?！？]+");
            pipeline = new StanfordCoreNLP(props);
        }
    }


    public String segment(String text) {
        if (pipeline == null) {
            return "";
        }
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        return document.tokens().stream()
                .map(e -> e.word())
                .collect(joining(" "));
    }

    private static StanfordNLPLexicalAnalyzer stanfordNLPLexicalAnalyzer;
    public static StanfordNLPLexicalAnalyzer getInstance(){
        if(stanfordNLPLexicalAnalyzer == null){
            stanfordNLPLexicalAnalyzer = new StanfordNLPLexicalAnalyzer();
        }
        return stanfordNLPLexicalAnalyzer;
    }
}

package top.iuui.nlp.gui;

import top.iuui.nlp.lexical.LexicalAnalyzerFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class NLPFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(JFrame.class.getName());

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 500;

    private JComboBox<String> nlpLibraries;
    private JComboBox<String> nlpFunctions;
    private JButton runButton;
    private JButton openInputFile;
    private JTextArea textAreaInput;
    private JTextArea textAreaOutput;

    public NLPFrame(){
        initFrame();
        initEvent();
    }

    private void initFrame(){
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("自然语言处理工具");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setLocation((screenSize.width - getWidth())/2, (screenSize.height - getHeight())/2);

        initMenu();
        initBody();
    }

    private void initMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("文件");
        JMenuItem openMenuItem = new JMenuItem("打开");
        JMenuItem exitMenuItem = new JMenuItem("退出");
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem manualMenuItem = new JMenuItem("指南");
        JMenuItem aboutMenuItem = new JMenuItem("关于");
        helpMenu.add(manualMenuItem);
        helpMenu.add(aboutMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
    }

    private void initBody(){
        initNorth();
        initCenter();
        initSouth();
    }

    private void initNorth(){
        JPanel panelNorth = new JPanel();
        add(panelNorth, BorderLayout.NORTH);
        Border etched = BorderFactory.createEtchedBorder();
        Border titled = BorderFactory.createTitledBorder(etched, "选项", TitledBorder.LEFT, TitledBorder.TOP);
        panelNorth.setBorder(titled);

        panelNorth.add(new JLabel("类库:",SwingConstants.RIGHT));
        nlpLibraries = new JComboBox<>();
        Stream.of("FudanNLP", "HanLP", "StanfordNLP")
                .forEach(e -> nlpLibraries.addItem(e));
        panelNorth.add(nlpLibraries);

        panelNorth.add(new JLabel("功能:",SwingConstants.RIGHT));
        nlpFunctions = new JComboBox<>();
        Stream.of("分词", "词性标注", "命名实体识别", "词法分析", "情感分析")
                .forEach(e -> nlpFunctions.addItem(e));
        panelNorth.add(nlpFunctions);

        runButton = new JButton("运行");
        panelNorth.add(runButton);
    }

    private void initCenter(){
        JPanel panelCenter = new JPanel();
        add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new GridLayout(2,1));

        JPanel panelCenterInput = new JPanel();
        panelCenter.add(panelCenterInput);
        panelCenterInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "输入", TitledBorder.LEFT, TitledBorder.TOP));
        panelCenterInput.setLayout(new BorderLayout());

//        JPanel inputOptionPanel = new JPanel();
//        ButtonGroup group = new ButtonGroup();
//        Stream.of("手动输入","多个文件").forEach(e ->{
//            JRadioButton inputOption = new JRadioButton(e, false);
//            group.add(inputOption);
//            inputOptionPanel.add(inputOption);
//        });
//        panelCenterInput.add(inputOptionPanel, BorderLayout.NORTH);
        openInputFile = new JButton("打开文件");
        panelCenterInput.add(openInputFile, BorderLayout.NORTH);

        textAreaInput = new JTextArea();
        textAreaInput.setLineWrap(true);
        JScrollPane scrollPanelInput = new JScrollPane(textAreaInput);
        panelCenterInput.add(scrollPanelInput, BorderLayout.CENTER);


        JPanel panelCenterOutput = new JPanel();
        panelCenter.add(panelCenterOutput);
        panelCenterOutput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "输入", TitledBorder.LEFT, TitledBorder.TOP));
        panelCenterOutput.setLayout(new GridLayout(1,1));
        textAreaOutput = new JTextArea();
        textAreaOutput.setLineWrap(true);
        JScrollPane scrollPanelOutput = new JScrollPane(textAreaOutput);
        panelCenterOutput.add(scrollPanelOutput);
    }

    private void initSouth(){
        JPanel panelSouth = new JPanel();
//        panelSouth.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "状态", TitledBorder.LEFT, TitledBorder.BELOW_TOP));
        JLabel label = new JLabel("状态：准备");
        panelSouth.add(label);
        add(panelSouth, BorderLayout.SOUTH);
    }

    public void initEvent(){
        runButton.addActionListener(e ->{
            String library = nlpLibraries.getItemAt(nlpLibraries.getSelectedIndex());
            String function = nlpFunctions.getItemAt(nlpFunctions.getSelectedIndex());
            String result = "";
            switch (function) {
                case "分词":
                    result = LexicalAnalyzerFactory.create(library).segment(textAreaInput.getText());
                    break;
            }
            textAreaOutput.setText(result);
        });

        openInputFile.addActionListener( e ->{
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(new FileNameExtensionFilter("文本文件（.txt）", "txt"));
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                try {
                    textAreaInput.setText(new String(Files.readAllBytes(chooser.getSelectedFile().toPath())));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getFile());
        LogManager.getLogManager().readConfiguration();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.log(Level.WARNING, "", e);
        }

        EventQueue.invokeLater(() ->{
            NLPFrame frame = new NLPFrame();
            frame.setVisible(true);
        });
    }
}

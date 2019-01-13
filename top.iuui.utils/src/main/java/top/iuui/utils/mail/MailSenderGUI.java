package top.iuui.utils.mail;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MailSenderGUI extends JFrame{

    private static final Logger log = Logger.getLogger(MailSender.class.getName());

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 500;

    private MailSender mailSender;
    private JButton submitEmail;
    private JTextArea textAreaInputMail;
    private JTextArea textAreaMailQueue;
    private JTextArea textAreaSendedMail;

    public MailSenderGUI(){
        initFrame();

        String sender = "luopc@lib.pku.edu.cn";
        String title = "test";
        String body  = "test mail body.";
        mailSender = new MailSender(sender, title, body);
        new Thread(mailSender::sendMail, "Sender").start();
    }

    private void initFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("邮件批量发送工具");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setLocation((screenSize.width - getWidth())/2, (screenSize.height - getHeight())/2);

        initCenter();
        initEvent();
    }

    private void initCenter(){
        JPanel panelCenter = new JPanel();
        add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new GridLayout(1,3));

        JPanel panelInputMail = new JPanel();
        panelCenter.add(panelInputMail);
        panelInputMail.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "提交邮箱地址", TitledBorder.LEFT, TitledBorder.TOP));
        panelInputMail.setLayout(new BorderLayout());

        JPanel panelMailQueue = new JPanel();
        panelCenter.add(panelMailQueue);
        panelMailQueue.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "邮箱地址队列", TitledBorder.LEFT, TitledBorder.TOP));
        panelMailQueue.setLayout(new BorderLayout());

        JPanel panelSendedMail = new JPanel();
        panelCenter.add(panelSendedMail);
        panelSendedMail.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "已发送邮箱地址", TitledBorder.LEFT, TitledBorder.TOP));
        panelSendedMail.setLayout(new BorderLayout());


        textAreaInputMail = new JTextArea();
        textAreaInputMail.setLineWrap(true);
        panelInputMail.add(new JScrollPane(textAreaInputMail), BorderLayout.CENTER);
        submitEmail = new JButton("提交");
        panelInputMail.add(submitEmail, BorderLayout.SOUTH);

        textAreaMailQueue = new JTextArea();
        textAreaMailQueue.setLineWrap(true);
        textAreaMailQueue.setEditable(false);
        panelMailQueue.add(new JScrollPane(textAreaMailQueue), BorderLayout.CENTER);

        textAreaSendedMail = new JTextArea();
        textAreaSendedMail.setLineWrap(true);
        textAreaSendedMail.setEditable(false);
        panelSendedMail.add(new JScrollPane(textAreaSendedMail), BorderLayout.CENTER);
    }

    private void initEvent(){
        this.submitEmail.addActionListener(e ->{
            String data = this.textAreaInputMail.getText().strip();
            if(data.length() == 0)return ;
            java.util.List<String> mails = Arrays.stream(data.split("\n"))
                    .filter(m -> true)
                    .collect(Collectors.toList());
            new Thread(() -> mailSender.addMailAddress(mails)).start();
            this.textAreaInputMail.setText("");
        });

        new javax.swing.Timer(1000, e ->{
            EventQueue.invokeLater(() -> {
                this.textAreaMailQueue.setText(String.join("\n", this.mailSender.getMailQueue()));
            });
        }).start();
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
            log.log(Level.WARNING, "", e);
        }

        EventQueue.invokeLater(() ->{
            MailSenderGUI frame = new MailSenderGUI();
            frame.setVisible(true);
        });
    }
}

package top.iuui.utils.mail;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MailSender{

    private Queue<String> mailQueue;
    private String sender;
    private String title;
    private String body;
    private final int maxQueueSize;

    public MailSender(String sender, String title, String body){
        this.sender = sender;
        this.title = title;
        this.body = body;
        this.mailQueue = new LinkedList<>();
        this.maxQueueSize = 1000;
    }

    public void addMailAddress(List<String> mails){
        mails.forEach(e -> this.addMailAddress(e));
    }

    public void addMailAddress(String mail){
        synchronized (this.mailQueue) {
            while(this.mailQueue.size() >= this.maxQueueSize){
                try {
                    System.out.println(Thread.currentThread().getName()
                            +": queue is full and thread will wait.");
                    this.mailQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.mailQueue.offer(mail);
            System.out.println(Thread.currentThread().getName()
                    +": add mail address "+mail);
            this.mailQueue.notifyAll();
        }
    }

    private String getMailAddress(){
        synchronized(this.mailQueue) {
            while (this.mailQueue.isEmpty()) {
                try {
                    System.out.println(Thread.currentThread().getName()
                            +": queue is empty and thread will wait.");
                    this.mailQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String mail = this.mailQueue.poll();
            System.out.println(Thread.currentThread().getName()
                    +": return mail address "+mail);
            this.mailQueue.notifyAll();
            return mail;
        }
    }

    public void sendMail(){
        while(true) {
            String mailAddress = this.getMailAddress();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Send email:" + mailAddress);
        }
    }

    public List<String> getMailQueue(){
        List<String> queue = new ArrayList<>();
        this.mailQueue.forEach(e -> queue.add(e));
        return queue;
    }

    public static void main(String[] args){
        String sender = "luopc@lib.pku.edu.cn";
        String title = "test";
        String body  = "test mail body.";
        MailSender mailSender = new MailSender(sender, title, body);
        new Thread(mailSender::sendMail, "Sender").start();
        new Thread(() -> mailSender.addMailAddress("zhangsan@126.com")).start();
        new Thread(() -> mailSender.addMailAddress(Arrays.asList("abc@126.com", "zz@pku.edu.cn"))).start();
    }
}

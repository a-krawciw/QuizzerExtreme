/**
 * Created by Windows on 2016-09-20.
 */
import javax.swing.*;
import javax.swing.event.*;;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.*;
import java.awt.*;
import java.text.*;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

        static String nome = "";
        static JTextField field;
        static boolean done = true;
        static int win = 0;
        static int times = 0;
        static ImageIcon back = new ImageIcon("frenchflag.jpg");

        public static void main(String[] args) throws Exception{
            String fileName = "";
            int length = 0;
            String compare = "";
            String answer = "";
            String correct = "";
            BufferedReader in;
            BufferedWriter redo = new BufferedWriter(new FileWriter("redo.txt"));
            int i = 0;
            AudioInputStream audioIn;

            welcomeScreen();
            randomize();
            in = new BufferedReader(new FileReader("random.txt"));

            in.mark(Short.MAX_VALUE);
            while (in.readLine() != null) {
                length++;
            }//while
            in.reset();
            AudioInputStream boot = AudioSystem.getAudioInputStream(new File("Jeopardy.wav"));
            Clip back = AudioSystem.getClip();
            back.open(boot);
            back.start();
            back.loop(100000);
            for(int y = 0; y < (length / 2); y++){

                compare = in.readLine();
                correct = in.readLine();
                i = 3;
                do{


                    answer = entry("'" + compare +"'", butons());
                    i--;
                    if(answer.toLowerCase().equals(correct.toLowerCase()
                    )){
                        audioIn = AudioSystem.getAudioInputStream(new File("ding.wav"));
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioIn);
                        clip.start();
                        message("Correct");
                        times++;
                        if(i == 2){
                            win++;
                        }
                        break;
                    } else {
                        audioIn = AudioSystem.getAudioInputStream(new File("buzz.wav"));
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioIn);
                        clip.start();
                        message("Incorrect try again. You have " + i + " tries left");
                        if(i == 0){
                            message("The correct answer was: " + correct);
                            redo.write(compare + "\n" + correct + "\n");
                            times++;
                        }//if
                    }
                }while(i > 0);

            }//for
            redo.close();
            redo();
            back.stop();
            exitScreen();

        }

        public static String setFrameTitle(){
            return field.getText();
        }
        static boolean end = false;
        public static String entry(String input, JPanel obj){
            JFrame frame = new JFrame();
            JLabel label = new JLabel(input);
            JButton ok = new JButton("OK");
            DecimalFormat df = new DecimalFormat("#.#");
            JLabel percentage = new JLabel();
            field = new JTextField();
            String output = "";
            end = false;

            frame.setIconImage(back.getImage());
            percentage.setOpaque(true);

            label.setFont(new Font("Calibri",Font.PLAIN, 30));
            field.setFont(new Font("Calibri",Font.PLAIN, 20));

            label.setForeground(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
            label.setBounds(300, 300, 1000, 100);

            if(times < 1){
                percentage.setText("");
            } else {
                percentage.setText("" + df.format(win * 100.0 / times) + "%");
            }//else

            frame.setLayout(new FlowLayout());
            frame.setSize(400, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            if((win * 100.0 / times) > 50) {
                percentage.setBackground(Color.GREEN);
            } else {
                percentage.setBackground(Color.RED);
            }



            frame.add(label);
            frame.pack();
            int w = label.getWidth();
            if(w < 400){
                w = 400;
            }

            field.setPreferredSize(new Dimension(w - 50, 40));
            frame.add(percentage);
            frame.add(field);
            frame.add(ok);
            frame.add(obj);


            frame.pack();
            frame.setBounds(0, 0, w + 100, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);





            ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    end = true;
                }
            });

            field.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        end = true;
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
                public void keyTyped(KeyEvent e) {
                }
            });


            while(end == false){
                output = setFrameTitle();
            }
            frame.dispose();
            return output;
        }


        public static void randomize() throws Exception{
            String fileName = "";
            BufferedReader in;
            ArrayList<String> file = new ArrayList<>();
            int length = 0;
            int times = 0;
            String input = "";
            BufferedWriter out = new BufferedWriter(new FileWriter("random.txt"));
            do{
                fileName = entry("which file will we use today don't include .txt", new JPanel());
                input = entry("how many words today?", new JPanel());
                try{
                    in = new BufferedReader(new FileReader(fileName + ".txt"));
                    times = Integer.parseInt(input);
                }catch(Exception e){
                    message( "That file does not exist");
                    continue;
                }
                break;
            }while(true);

            in.mark(Short.MAX_VALUE);
            while (in.readLine() != null) {
                length++;
            }//while
            in.reset();


            for(int i = 0; i <
                    length; i+=2){
                file.add(in.readLine() + "\n"+ in.readLine() + "\n");
                System.out.println("\n" + file.get(i / 2));
            }

            if(times > length){
                times = length;
            }

            Collections.shuffle(file);

            for(int i = 0; i < times; i++){

                out.write(file.get(i));
            }//for
            out.close();
        }

        public static void redo() throws Exception{
            BufferedReader in;
            int length = 0;
            String compare = "";
            String correct = "";
            String answer = "";
            int i = 0;

            in = new BufferedReader(new FileReader("redo.txt"));


            in.mark(Short.MAX_VALUE);

            while (in.readLine() != null) {
                length++;
            }//while
            in.reset();

            for(int y = 0; y < (length / 2); y++){

                compare = in.readLine();
                correct = in.readLine();
                i = 3;
                do{


                    answer = entry("Translate '" + compare +"'", butons());
                    i--;
                    if(answer.equals(correct)){
                        message("Correct");
                        break;
                    } else {
                        message("Incorrect try again. You have " + i + " tries left");
                        if(i == 0){
                            message("The correct answer was: " + correct);
                        }//if
                    }
                }while(i > 0);

            }//for


        }
        static int clicks = 0;
        public static void welcomeScreen() throws Exception{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("marse.wav"));
            Clip clip = AudioSystem.getClip();

            clip.open(audioIn);
            clip.start();
            message("In this game you will try to translate as many words as you know.");
            message("Words will appear at the top of the window");
            message("You can choose however many words that you want to do.");
            message("Have fun.");
            clip.stop();

        }

        public static void exitScreen() throws Exception{
            AudioInputStream audioIn;
            String output = "";



            if((win * 100.0 / times ) >= 80){
                audioIn = AudioSystem.getAudioInputStream(new File("pomp.wav"));
                output = "You had a score of " + (win * 100.0 / times) + "% That is very good.";
            } else if (((win * 100.0 / times) < 80) && ((win * 100.0 / times ) > 50)){
                audioIn = AudioSystem.getAudioInputStream(new File("ma.wav"));
                output = "You had a score of " + (win * 100.0 / times) + "% That is ok. Do better next time";
            } else {
                audioIn = AudioSystem.getAudioInputStream(new File("chop.wav"));
                output = "You had a score of " + (win * 100.0 / times) + "% That is not good at all. Pay attention in French Class";
            }//else
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            message(output);

        }

        public static void message(String input){
            JFrame enter = new JFrame();
            JLabel label = new JLabel(input);
            JButton ok = new JButton("OK");
            end = false;

            label.setFont(new Font("Calibri",Font.PLAIN, 30));

            label.setForeground(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
            label.setBounds(300, 300, 1000, 100);


            enter.setIconImage(back.getImage());
            enter.setLayout(new FlowLayout());
            enter.setSize(400, 400);
            enter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



            enter.add(label);
            enter.pack();
            int w = label.getWidth();
            if(w < 400){
                w = 400;
            }

            enter.add(ok);

            enter.pack();
            enter.setBounds(0, 0, w + 100, 100);
            enter.setLocationRelativeTo(null);
            enter.setVisible(true);

            ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    end = true;
                }
            });


            ok.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        end = true;
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
                public void keyTyped(KeyEvent e) {
                }
            });


            while(end == false){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            enter.dispose();
            return;
        }

        public static JPanel butons(){
            JPanel obj = new JPanel();
            JButton button = new JButton("Click for é");
            JButton grave = new JButton("Click for è");
            JButton agrave = new JButton("Click for à");
            JButton circon = new JButton("Click for ê");
            JButton ugrave = new JButton("Click for ù");
            JButton ced = new JButton("Click for ç");
            JButton icirc = new JButton("Click for î");

            obj.setLayout(new FlowLayout());
            obj.setPreferredSize(new Dimension(400, 400));
            obj.add(button);
            obj.add(grave);
            obj.add(circon);
            obj.add(agrave);
            obj.add(ugrave);
            obj.add(ced);
            obj.add(icirc);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    String s = setFrameTitle();
                    s += "é";
                    field.setText(s);
                }
            });
            grave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    String s = setFrameTitle();
                    s += "è";
                    field.setText(s);
                }//actionPerformed
            });

            agrave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    String s = setFrameTitle();
                    s += "à";
                    field.setText(s);
                }
            });
            ugrave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    String s = setFrameTitle();
                    s += "ù";
                    field.setText(s);
                }
            });

            circon.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    String s = setFrameTitle();
                    s += "ê";
                    field.setText(s);
                }
            });

            ced.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    String s = setFrameTitle();
                    s += "ç";
                    field.setText(s);
                }
            });

            icirc.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    String s = setFrameTitle();
                    s += "î";
                    field.setText(s);
                }
            });

            return obj;
        }



}

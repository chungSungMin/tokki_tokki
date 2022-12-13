package client.frame;
import controller.Controller;
import server.userdb.User;
import server.userdb.UserDAO;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.awt.Color.RED;

public class SearchFriend extends JFrame implements ActionListener,  ListSelectionListener {
    JButton btn1 = new JButton("검색");
    JButton btn2 = new JButton("추가");
    TextField tf = new TextField(20);
    //친구 list 가져올때 사용
    JList list;
    String[] found;
    String name;
    String find_email;
    JFrame jf = new JFrame();

    JScrollPane aa;

    public Controller controller = Controller.getInstance();

    //친구 수를 나타내기 위해 사용
    int count = 0;
    public String[] sstr;
    SearchFriend()
    {
        super("이메일로 친구 찾기");
        JPanel jp =new JPanel();
        setSize(400,400);

        setLayout(new BorderLayout());

        Label li = new Label("search :");
        btn1.addActionListener( this);

        //search 랑 textfiled 저장해놓음.
        jp.setLayout(new FlowLayout());
        jp.add(li);
        jp.add(tf);
        jp.add(btn1);
        //jp.add(btn2);
        add(jp,BorderLayout.NORTH);

        setVisible(true);
    }

    public void friendList(int k)
    {
        if(k!= 0) {
            list = new JList(found);
            JPanel jp2 = new JPanel();
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(this);
            aa = new JScrollPane(list);
            aa.setSize(300,300);
            jp2.add(aa);
            add(jp2, BorderLayout.CENTER);
        }else {
            JLabel JL = new JLabel("Can not found!");
            JPanel jp2 = new JPanel();
            jp2.add(JL);
            add(jp2, BorderLayout.CENTER);
        }
        setVisible(true);
    }


    public void find(String text)
    {
        //add(jl);
        String[] str = controller.searchUser();
        System.out.println(1);
        //str은 database로 부터 받아온 나를 제외한 사람들의 list이다.

        String[] result = new String[30];
        int i =0;
        int k = 0;
        while(str[i] != null)
        {
            if(str[i].contains(text))
            {
                result[k] = str[i];
                //System.out.println(result[k]);
                k++;
            }
            i++;
        }


        found = result;
        count = k;
        friendList(k);

        jf.setLayout(new FlowLayout());
        jf.setSize(300,200);

        JPanel jp = new JPanel();
        JLabel jl = new JLabel("친구를 추가하겠습니까?");
        jp.add(jl);

        btn2.addActionListener(this);
        jp.add(btn2);

        jf.add(jp);
    }
    public void accept(){

        jf.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()== btn1) {
            System.out.println("검색기능");
            String str = tf.getText();
            System.out.println("찾고자 하는 사람의 이메일: " + str);
            find(str);
        }

        if(e.getSource() == btn2){ //추가
            System.out.println("추가 버튼 눌림!!!!!!!!!!!!!!!!!!!!!!!!!!");
            name = controller.username;
            controller.insertFriend(name, find_email);
            System.out.println("my Name :" + name + "friend Name : " + find_email);
            jf.setVisible(false);
        }
    }

    boolean flag = true;
    @Override
    public void valueChanged(ListSelectionEvent e) {

        if(flag) {
            find_email = (String) list.getSelectedValue();

            accept();
            /*IndexPanel indexPanel = new IndexPanel();
            MainPanel.frame.change(indexPanel);*/
        }
        flag = !flag;
    }
}
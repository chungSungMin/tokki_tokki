package client.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import controller.Controller;
import server.datacommunication.Message;
import util.ColorSet;
import util.UseImageFile;
import util.UserProfileButton;

@SuppressWarnings("serial")
public class FriendListPanel extends JPanel {

  private ArrayList<String> friends; // 친구들 이름 저장

  private ArrayList<ImageIcon> friendIcons = new ArrayList<ImageIcon>(); // 친구들 프로필 이미지 저장

  public static ArrayList<JButton> friendButtons = new ArrayList<JButton>(); // 친구들 정보 버튼 저장

  private JLabel jLabel2;

  private final int FRIEND_PROFILE_IMG_MAX = 8;

  private final int FRIEND_PROFILE_IMG_MIN = 1;

  public FriendListPanel() {

    setBackground(ColorSet.talkBackgroundColor);
    Controller controller = Controller.getInstance();
    friends = controller.friendList();
    System.out.println("친구리스트 불러오기 성공");
    int friendNum = friends.size();
    System.out.println("친구수 : " + friendNum);
    setLayout(new GridLayout(friendNum,10));
    for (int index = 0; index < friendNum; index++) {
      Random rand = new Random();
      int randomNum =
          rand.nextInt((FRIEND_PROFILE_IMG_MAX - FRIEND_PROFILE_IMG_MIN) + FRIEND_PROFILE_IMG_MIN)
              + 1;
      Image img = UseImageFile.getImage("resources/friendProfile/profile" + randomNum + ".png");
      ImageIcon imageIcon = new ImageIcon(img);
      UserProfileButton userprofileButton = new UserProfileButton(imageIcon);
      userprofileButton.setText(friends.get(index));
      add(userprofileButton);
      friendIcons.add(imageIcon);
      friendButtons.add(userprofileButton);

      String today_talk = controller.today_talk(friends.get(index));
      jLabel2 = new JLabel(today_talk);
      jLabel2.setBounds(200, 115,1000, 90);
      jLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 13));
      add(jLabel2);
    }
    for (int i = 0; i < friendNum; i++) {
      friendButtons.get(i).putClientProperty("page", i);
      friendButtons.get(i).addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

          int idx = (Integer) ((JButton) e.getSource()).getClientProperty("page");
          if (friendButtons.get(idx).getText().contains("대화 중..")) {
            // 작동x
          } else {
            friendButtons.get(idx).setText(friendButtons.get(idx).getText() + "       대화 중..");
            String messageType = "text";
            Message message = new Message(controller.username, controller.username + "님이 입장하였습니다.",
                LocalTime.now(), messageType, friends.get(idx));
            ChatWindowPanel c = new ChatWindowPanel(friendIcons.get(idx), friends.get(idx));
            new ChatWindowFrame(c, friends.get(idx));
            IndexPanel.chatPanelName.add(c);
            
            Controller controller = Controller.getInstance();
            controller.clientSocket.send(message);
          }
        }
      });
    }
  }
}

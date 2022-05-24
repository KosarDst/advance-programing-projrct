
import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

import java.util.Scanner;

public class Data {

    int gid = 10000;

    public static final ArrayList<User> accounts = new ArrayList<>();

    public static ArrayList<Game> games;

    private static void read() throws FileNotFoundException {
        File f = new File("users");
        Scanner sc = new Scanner(f);

        while (sc.hasNext()) {
            try {
                String[] s1 = sc.nextLine().split(",");

                User u = new User();
                u.setName(s1[0]);
                u.setUsername(s1[1]);
                u.setPassword(s1[2]);
                u.setScore(Long.parseLong(s1[3]));
                u.setCoin(Integer.parseInt(s1[4]));
                accounts.add(u);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public Data(int port) throws IOException {

        games = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket(port);

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("waiting");
                    try {
                        Socket socket = serverSocket.accept();

                        new Thread(new Runnable() {
                            public void run() {
                                try {

                                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                                    String message = (String) objectInputStream.readObject();
                                    System.out.println(message);

                                    if (message.startsWith("login")) {
                                        boolean checkU = false;
                                        boolean checkP = false;
                                        String name = message.substring(5, message.indexOf(','));
                                        String p = message.substring(message.indexOf(',') + 1);

                                        for (User u : accounts) {
                                            if (u.getUsername().equals(name)) {
                                                checkU = true;
                                                if (u.getPassword().equals(p)) {
                                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                                    objectOutputStream.writeObject("OK");
                                                    checkP = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!checkU) {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("NOU");
                                        }
                                        if (!checkP) {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("NOP");
                                        }

                                    } else if (message.startsWith("guest")) {

                                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                        objectOutputStream.writeObject("guest" + accounts.size());

                                        User u = new User();
                                        u.setName("guest" + accounts.size());
                                        u.setUsername("guest" + accounts.size());
                                        u.setPassword("");
                                        u.setCoin(100);
                                        u.setScore(0);
                                        u.guest = true;
                                        accounts.add(u);

                                    } else if (message.startsWith("reg")) {
                                        String[] reg = message.substring(3).split(",");

                                        boolean exist = false;
                                        for (User u : accounts) {
                                            if (u.getUsername().equals(reg[1])) {
                                                exist = true;
                                            }
                                        }
                                        if (!exist) {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("OK");

                                            User u = new User();
                                            u.setName(reg[0]);
                                            u.setUsername(reg[1]);
                                            u.setPassword(reg[2]);
                                            u.setCoin(100);
                                            u.setScore(0);
                                            accounts.add(u);
                                            u.write();

                                        } else {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("EXIST");
                                        }

                                    } else if (message.startsWith("chk")) {
                                        String uname = message.substring(3);

                                        boolean exist = false;
                                        for (User u : accounts) {
                                            if (u.getUsername().equals(uname)) {
                                                exist = true;
                                            }
                                        }
                                        if (exist) {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("EXIST");
                                        } else {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("no");
                                        }

                                    } else if (message.startsWith("chk2")) {
                                        String pass = message.substring(3);

                                        boolean chekP = false;
                                        for (User u : accounts) {
                                            if (u.getPassword().equals(pass)) {
                                                chekP = true;
                                            }
                                        }
                                        if (chekP) {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("EXIST");
                                        } else {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("no");
                                        }
                                    } else if (message.startsWith("get")) {
                                        String info = message.substring(3);

                                        for (User u : accounts) {
                                            if (u.getUsername().equals(info)) {
                                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                oos.writeObject("u" + u.getCoin() + "," + u.getScore());
                                                break;
                                            }
                                        }

                                    } else if (message.startsWith("newname")) {
                                        String[] newnamemessage = message.substring(7).split(",");
                                        boolean correct = false;

                                        for (User u : accounts) {
                                            if (u.getUsername().equals(newnamemessage[1])) {

                                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                                objectOutputStream.writeObject("NO" + "," + u.getUsername());

                                                correct = true;
                                                break;
                                            }
                                        }
                                        if (!correct) {
                                            for (User u : accounts) {
                                                if (u.getUsername().equals(newnamemessage[0])) {

                                                    u.setUsername(newnamemessage[1]);
                                                    Game.update();
                                                    break;
                                                }
                                            }
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("OK");
                                        }
                                    } else if (message.startsWith("newpass")) {
                                        String[] newpassmessage = message.substring(7).split(",");
                                        boolean correct = false;

                                        for (User u : accounts) {
                                            if (u.getUsername().equals(newpassmessage[0])) {
                                                if (u.getPassword().equals(newpassmessage[1])) {
                                                    correct = false;
                                                } else {
                                                    u.setPassword(newpassmessage[1]);
                                                    Game.update();
                                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                                    objectOutputStream.writeObject("OK");

                                                    correct = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (!correct) {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("NO");
                                        }

                                    } else if (message.startsWith("hint")) {
                                        String hintmessage = message.substring(4);
                                        boolean correct = false;

                                        for (User u : accounts) {
                                            if (u.getUsername().equals(hintmessage)) {

                                                if (u.getCoin() >= 50) {
                                                    u.setCoin(u.getCoin() - 50);
                                                    Game.update();
                                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                    oos.writeObject("OK");
                                                } else {
                                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                    oos.writeObject("NO");
                                                }
                                                correct = true;
                                                break;
                                            }
                                        }
                                        if (!correct) {
                                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                            oos.writeObject("NO");
                                        }

                                    } else if (message.startsWith("gamedata")) {
                                        String reg = message.substring(8);

                                        boolean gameon = false;
                                        for (Game g : games) {
                                            if ((g.gid + "").equals(reg)) {
                                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                                objectOutputStream.writeObject(g.gameData());
                                                gameon = true;

                                                break;
                                            }
                                        }
                                        if (!gameon) {
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                            objectOutputStream.writeObject("end");
                                        }

                                    } else if (message.startsWith("G")) {
                                        String op = message.substring(1);

                                        for (User u : accounts) {
                                            if (u.getUsername().equals(op)) {
                                                int flag = 0;
                                                for (int i = 0; i < games.size(); i++) {
                                                    Game g = games.get(i);
                                                    if (!g.op1.getUsername().equals(op) && g.op2 == null && flag == 0) {
                                                        flag = 1;
                                                        System.out.println("team up !!");

                                                        int tmp = g.gid;
                                                        g.setOp2(u);

                                                        g.start();
                                                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                        oos.writeObject("OK" + tmp);
                                                    }
                                                }
                                                if (flag == 0) {
                                                    Game tmp = new Game(20);
                                                    tmp.setOp1(u);
                                                    tmp.setGid(++gid);
                                                    games.add(tmp);
                                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                    oos.writeObject("WAIT" + gid);
                                                }
                                                break;
                                            }
                                        }
                                    } else if (message.startsWith("check")) {
                                        String op = message.substring(5);

                                        for (User u : accounts) {
                                            if ((u.getUsername() + "").equals(op)) {
                                                String gamnes = "";

                                                for (int i = 0; i < games.size(); i++) {
                                                    Game g = games.get(i);
                                                    if (g.l1 >= 12 && g.l2 >= 12) {
                                                        games.remove(g);
                                                    } else if (g.op1.getUsername().equals(u.getUsername()) ||
                                                            (g.op2 != null && g.op2.getUsername().equals(u.getUsername()))) {
                                                        if (g.op2 == null) {
                                                            gamnes += "," + -1 * g.gid;
                                                        } else {
                                                            gamnes += "," + g.gid;
                                                        }
                                                    }
                                                }
                                                if (gamnes.startsWith(",")) {
                                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                    oos.writeObject(gamnes.substring(1));
                                                } else {
                                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                    oos.writeObject(gamnes);
                                                }
                                                break;
                                            }
                                        }
                                    } else if (message.startsWith("level")) {
                                        int hint = Integer.parseInt(message.substring(5, 6));
                                        String reg = message.substring(6, message.indexOf(","));
                                        String uname = message.substring(1 + message.indexOf(","));

                                        boolean f = false;
                                        for (Game g : games) {
                                            if ((g.getGid() + "").equals(reg)) {

                                                g.time(g.getOp1().getUsername().equals(uname));

                                                if (g.getOp1().getUsername().equals(uname)) {
                                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                    oos.writeObject(g.level(g.l1, g.getOp1().getUsername().equals(uname), hint));
                                                } else {
                                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                    oos.writeObject(g.level(g.l2, g.getOp1().getUsername().equals(uname), hint));
                                                }
                                                f = true;
                                                break;
                                            }
                                        }
                                        if (!f) {
                                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                            oos.writeObject("end");
                                        }
                                    } else if (message.startsWith("resp")) {
                                        int hint = Integer.parseInt(message.substring(4, 5));
                                        int i = Integer.parseInt(message.substring(5, 6)) + 1;
                                        String reg = message.substring(6, message.indexOf(","));
                                        String uname = message.substring(1 + message.indexOf(","));

                                        for (Game g : games) {
                                            if ((g.getGid() + "").equals(reg)) {

                                                if (g.getOp1().getUsername().equals(uname)) {
                                                    if (g.l1 == 0 || g.l1 == 8) {
                                                        g.cat = Integer.parseInt(g.cats[i - 1]);
                                                        g.l1++;

                                                        g.chosen = false;
                                                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                        oos.writeObject("OK");
                                                    } else {

                                                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                        oos.writeObject(g.check(g.l1, i + "", g.getOp1().getUsername().equals(uname), hint));
                                                        if (hint != 1 && g.l1 % 4 != 0) {
                                                            g.l1++;
                                                            g.chosen = false;
                                                        }
                                                    }
                                                } else {
                                                    if (g.l2 == 4) {
                                                        g.cat = Integer.parseInt(g.cats[i - 1]);
                                                        g.l2++;

                                                        g.chosen = false;
                                                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                        oos.writeObject("OK");
                                                    } else {

                                                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                        oos.writeObject(g.check(g.l2, i + "", g.getOp1().getUsername().equals(uname), hint));

                                                        if (hint != 1 && g.l2 % 4 != 0) {
                                                            g.l2++;
                                                            g.chosen = false;
                                                        }
                                                    }
                                                }

                                                if (((g.l2 == 0 || g.l2 == 8) && g.l1 > g.l2)) {
                                                    g.l2++;
                                                }
                                                if (((g.l1 == 4) && g.l1 < g.l2)) {
                                                    g.l1++;
                                                }
                                                break;
                                            }
                                        }
                                    } else if (message.startsWith("history")) {
                                        String reg = message.substring(7);

                                        for (User u : accounts) {
                                            if ((u.getUsername() + "").equals(reg)) {
                                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                                oos.writeObject("Score : " + u.getScore() + "\n" + "Rate : " + u.getRate(u.getScore()) + "'s place" + "\n" + User.listGames(reg));
                                                break;
                                            }
                                        }
                                    }
                                } catch (ClassNotFoundException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public static void main(String[] args) throws IOException {
        read();
        Data dataServer = new Data(7923);
    }

}


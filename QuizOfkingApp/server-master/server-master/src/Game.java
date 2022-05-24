
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Game {
    User op1 = null;
    User op2 = null;

    int gid;

    int l1 = -1;
    int l2 = -1;

    int[] score1 = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    int[] score2 = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    boolean chosen = false;
    String[] cats = null;
    String[] q = null;
    int cat = -1;

    int t1 = 0;
    int t2 = 0;
    int sec = 0;

    public Game(int sec) {
        this.sec = sec;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getGid() {
        return gid;
    }

    public void setOp1(User op1) {
        this.op1 = op1;
    }

    public void setOp2(User op2) {
        this.op2 = op2;
    }

    public User getOp1() {
        return op1;
    }

    public String level(int l, boolean first, int hint) throws IOException {
        if (l1 == l2 && l1 >= 12) {
            this.end();

            return "end";
        } else if (l1 >= 12 && first) {
            return "end";
        } else if (l2 >= 12 && !first) {
            return "end";
        }
        if (l >= 12) {
            return "end";
        }

        if (l == 0 || l == 4 || l == 8) {
            if (!chosen) {
                cats = cat().split(",");
                q = qs().split(",");
                chosen = true;
            }
            String s = cats[0] + "," + cats[1] + "," + cats[2];
            if (((!first && l == 4) || (first && l != 4)) && l1 == l2) {
                if (first) {
                    t1 = 0;
                } else {
                    t2 = 0;
                }
                return l + s;
            } else {
                if (first) {
                    t1 = 0;
                } else {
                    t2 = 0;
                }
                return l + "wait";
            }
        } else {
            File fw = new File(cat + "");
            Scanner s = new Scanner(fw);

            for (int i = 0; i < 6 * (Integer.parseInt(q[l % 4 - 1]) - 1); i++) {
                s.nextLine();
            }
            String[] ss = {s.nextLine(), s.nextLine(), s.nextLine(), s.nextLine(), s.nextLine()};
            String sss = "";

            int ans = Integer.parseInt(s.nextLine());
            if (hint == 2) {
                if (ans == 1 || ans == 2) {
                    sss = ss[0] + "|" + ss[1] + "|" + ss[2] + "|" + ss[3] + " (WRONG ANSWER)|" + ss[4] + " (WRONG ANSWER)|";
                } else {
                    sss = ss[0] + "|" + ss[1] + " (WRONG ANSWER)|" + ss[2] + " (WRONG ANSWER)|" + ss[3] + "|" + ss[4] + "|";
                }
            } else {
                sss = ss[0] + "|" + ss[1] + "|" + ss[2] + "|" + ss[3] + "|" + ss[4] + "|";
            }
            if (first)
                return l + sss + "\ntime : " + (sec - (System.currentTimeMillis() / 1000 - t1));
            else {
                return l + sss + "\ntime : " + (sec - (System.currentTimeMillis() / 1000 - t2));
            }
        }
    }

    public void time(boolean first) {

        if (t1 == 0 && l1 % 4 != 0) {
            t1 = (int) ((System.currentTimeMillis() / 1000));
        }
        if (t2 == 0 && l2 % 4 != 0) {
            t2 = (int) ((System.currentTimeMillis() / 1000));
        }

        if (first) {
            if (!(l1 == 0 || l1 == 4 || l1 == 8)) {
                if ((System.currentTimeMillis() / 1000 - t1) >= sec) {
                    t1 = (int) ((System.currentTimeMillis() / 1000));
                    score1[l1] = -2;
                    l1++;
                }
            }
        } else {
            if (!(l2 == 0 || l2 == 4 || l2 == 8)) {
                if ((System.currentTimeMillis() / 1000 - t2) >= sec) {
                    t2 = (int) ((System.currentTimeMillis() / 1000));
                    score2[l2] = -2;
                    l2++;
                }
            }
        }
    }

    public String gameData() {
        String result = "";
        result += "     " + op1.getUsername() + "               :                " + op2.getUsername() + "\n";
        for (int i = 0; i < 12; i++) {
            result += score1[i] + ":";

            result += score2[i] + "\n";
        }
        return result;
    }

    public String check(int l, String a, boolean first, int hint) {
        try {

            File fw = new File(cat + "");
            Scanner s = new Scanner(fw);

            if (l % 4 == 0) {
                l--;
            }
            for (int i = 0; i < 6 * (Integer.parseInt(q[l % 4 - 1]) - 1) + 5; i++) {
                System.out.println(s.nextLine());
            }

            String ss = "";
            String test = s.nextLine();
            if (a.equals(test)) {
                ss = "correct";
                if (first) {
                    score1[l] = 1;
                } else {
                    score2[l] = 1;
                }
            }
            if (!a.equals(test)) {
                ss = "wrong";
                if (first) {
                    score1[l] = 0;
                } else {
                    score2[l] = 0;
                }
            }
            if (first) {
                t1 = (int) ((System.currentTimeMillis() / 1000));
            } else {
                t2 = (int) ((System.currentTimeMillis() / 1000));
            }
            return ss;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String cat() {
        HashSet<Integer> c = new HashSet<Integer>();
        while (c.size() != 3) {
            Random r = new Random(System.currentTimeMillis());
            c.add(r.nextInt(6) + 1);
        }

        String s = "";
        for (Integer i : c) {
            s += "," + i;
        }
        return s.substring(1);
    }

    private String qs() {
        HashSet<Integer> c = new HashSet<Integer>();
        while (c.size() != 3) {
            Random r = new Random(System.currentTimeMillis());
            c.add(r.nextInt(7) + 1);
        }

        String s = "";
        for (Integer i : c) {
            s += "," + i;
        }
        return s.substring(1);
    }

    public void start() {
        new Thread(new Runnable() {
            public void run() {
                System.out.println("game started");
                l1 = 0;
                l2 = 0;
            }
        }).start();
    }

    public void end() throws IOException {
        int s1 = 0;
        int s2 = 0;
        for (int i = 0; i < 12; i++) {
            if (score1[i] > 0) {
                s1 += score1[i];
            }
            if (score2[i] > 0) {
                s2 += score2[i];
            }
        }

        int ii = 0;
        if (s1 > s2) {
            Data.accounts.get(Data.accounts.indexOf(op1)).setScore(op1.getScore() + 20);
            Data.accounts.get(Data.accounts.indexOf(op1)).setCoin(op1.getCoin() + 10);
            ii = 1;
        }
        if (s1 < s2) {
            Data.accounts.get(Data.accounts.indexOf(op2)).setScore(op2.getScore() + 20);
            Data.accounts.get(Data.accounts.indexOf(op2)).setCoin(op2.getCoin() + 10);
            ii = 2;
        }
        if (s1 == s2) {
            Data.accounts.get(Data.accounts.indexOf(op2)).setScore(op2.getScore() + 10);
            Data.accounts.get(Data.accounts.indexOf(op1)).setScore(op1.getScore() + 10);
            ii = 0;
        }

        update();
        FileWriter fw = new FileWriter("record", true);
        fw.write("\n" + this.op1.getUsername() + "," + this.op2.getUsername() + "," + ii);
        fw.close();
    }

    public static void update() throws IOException {
        FileWriter fw = new FileWriter("users", false);

        boolean b = false;
        for (User u : Data.accounts) {
            if (b) {
                fw.write("\n" + u.getName() + "," + u.getUsername() + "," + u.getPassword() + ","
                        + u.getScore() + "," + u.getCoin());
            } else {
                fw.write(u.getName() + "," + u.getUsername() + "," + u.getPassword() + ","
                        + u.getScore() + "," + u.getCoin());
                b = true;
            }
        }
        fw.close();
    }
}



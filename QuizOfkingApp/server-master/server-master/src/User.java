

import java.io.*;
import java.util.Scanner;

public class User {
    private String name;
    private String username;
    private String password;
    private int coin;
    private long score;
    public boolean guest = false;

    public void write() throws IOException {
        FileWriter fw = new FileWriter("users", true);
        fw.write("\n" + this.name + "," + this.getUsername() + "," + this.getPassword() + "," + this.getScore() + "," + this.getCoin());//appends the string to the file
        fw.close();
    }

    public static String listGames(String uname) throws FileNotFoundException {
        File f = new File("record");
        String result = "";
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String x = s.nextLine();
            try {
                String[] str = x.split(",");
                if (str[0].equals(uname)) {
                    if (str[2].equals("1")) {
                        result += "    You played with " + str[1] + " and " + "you won\n";
                    } else if (str[2].equals("2")) {
                        result += "    You played with " + str[1] + " and " + "you lost\n";
                    } else {
                        result += "    You played with " + str[1] + " and " + "it is a tie!\n";
                    }
                } else if (str[1].equals(uname)) {
                    if (str[2].equals("2")) {
                        result += "    You played with " + str[0] + " and " + "you won\n";
                    } else if (str[2].equals("1")) {
                        result += "    You played with " + str[0] + " and " + "you lost\n";
                    } else {
                        result += "    You played with " + str[0] + " and " + "it is a tie!\n";
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return result;
    }

    public int getRate(long score) throws FileNotFoundException {
        File f = new File("users");
        int rate = 1;
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            try {
                String[] str = s.nextLine().split(",");
                int tmp = Integer.parseInt(str[3]);
                if (tmp > score) {
                    rate++;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return rate;
    }

    public int getCoin() {
        return coin;
    }

    public long getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}



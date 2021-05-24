import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 用于实现CFG的类
 */
public class CFG {
    /**
     * 生成式
     */
    private final ArrayList<P> p = new ArrayList<>();
    /**
     * 非终结符
     */
    private ArrayList<String> N;
    /**
     * 终结符
     */
    private ArrayList<String> T;
    /**
     * 起始符
     */
    private String S;

    /**
     * 构造函数
     *
     * @param fileName 存放CFG的文件
     */
    public CFG(String fileName) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));

            if (!in.readLine().equals("N:")) {
                System.err.println("文件出错!(N)");
                System.exit(-1);
            }
            String N;
            N = in.readLine();
            String[] Ns = N.split(",");
            this.N = new ArrayList<>(Arrays.asList(Ns));

            if (!in.readLine().equals("T:")) {
                System.err.println("文件出错!(T)");
                System.exit(-1);
            }
            String T;
            T = in.readLine();
            String[] Ts = T.split(",");
            this.T = new ArrayList<>(Arrays.asList(Ts));

            if (!in.readLine().equals("S:")) {
                System.err.println("文件出错!(S)");
                System.exit(-1);
            }
            String S;
            S = in.readLine();
            this.S = S;

            if (!in.readLine().equals("P:")) {
                System.err.println("文件出错!(P)");
                System.exit(-1);
            }
            boolean flag = false;
            String str;
            String L = null;
            String R;
            while ((str = in.readLine()) != null) {
                if (flag) {
                    R = str;
                    String[] Rs = R.split(",");
                    this.p.add(new P(L, new ArrayList<>(Arrays.asList(Rs))));
                    flag = false;
                } else {
                    L = str;
                    flag = true;
                }
            }

            in.close();
        } catch (Exception e) {
            System.err.println("文件读取出错！");
            System.exit(-1);
        }
    }

    /**
     * 将转换结果写入文件
     *
     * @param fileName 文件名
     */
    public void writeToFile(String fileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

            out.write("N:" + System.lineSeparator());
            for (int i = 0; i < N.size(); i++) {
                out.write(N.get(i));
                if (i < N.size() - 1) {
                    out.write(",");
                }
            }
            out.write(System.lineSeparator());

            out.write("T:" + System.lineSeparator());
            for (int i = 0; i < T.size(); i++) {
                out.write(T.get(i));
                if (i < T.size() - 1) {
                    out.write(",");
                }
            }
            out.write(System.lineSeparator());

            out.write("S:" + System.lineSeparator());
            out.write(this.S + System.lineSeparator());

            out.write("P:" + System.lineSeparator());
            for (var item : this.p) {
                out.write(item.L + System.lineSeparator());
                for (int i = 0; i < item.R.size(); i++) {
                    out.write(item.R.get(i));
                    if (i < item.R.size() - 1) {
                        out.write(",");
                    }
                }
                out.write(System.lineSeparator());
            }

            out.close();
        } catch (IOException e) {
            System.err.println("文件写入出错！");
        }
    }

    /**
     * 算法1：找出有用非终结符
     */
    public void algorithm1() {
        HashSet<String> N0 = new HashSet<>();
        HashSet<String> N1 = new HashSet<>(this.T);
        while (N0.addAll(N1)) {
            out:
            for (var p : this.p) {
                for (var R : p.R) {
                    boolean flag = true;
                    for (int i = 0; i < R.length(); i++) {
                        if (!N0.contains(String.valueOf(R.charAt(i)))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        N1.add(p.L);
                        continue out;
                    }
                }
            }
        }
        this.N.retainAll(N1);
        ArrayList<String> delL = new ArrayList<>();
        for (P value : this.p) {
            ArrayList<String> tmpRs = value.R;
            ArrayList<String> del = new ArrayList<>();
            for (String tmpR : tmpRs) {
                boolean flag = false;
                for (int k = 0; k < tmpR.length(); k++) {
                    if (!N1.contains(String.valueOf(tmpR.charAt(k)))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    del.add(tmpR);
                }
            }
            tmpRs.removeAll(del);
            if (tmpRs.size() == 0)
                delL.add(value.L);
        }
        for (var L : delL) {
            for (int i = 0; i < this.p.size(); i++) {
                if (this.p.get(i).L.equals(L)) {
                    this.p.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * 算法2：找出有用符号（从S可达的符号）
     */
    public void algorithm2() {
        HashSet<String> N0 = new HashSet<>();
        N0.add(this.S);
        HashSet<String> N1 = new HashSet<>();
        do {
            N1.addAll(N0);
            for (var L : N0) {
                for (var p : this.p) {
                    if (p.L.equals(L)) {
                        for (var R : p.R) {
                            for (var ch : R.toCharArray()) {
                                N1.add(String.valueOf(ch));
                            }
                        }
                        break;
                    }
                }
            }
        } while (N0.addAll(N1));
        this.N.retainAll(N0);
        this.T.retainAll(N0);
        HashSet<String> delL = new HashSet<>();
        for (P value : this.p) {
            if (!this.N.contains(value.L))
                delL.add(value.L);
            ArrayList<String> tmpRs = value.R;
            ArrayList<String> del = new ArrayList<>();
            for (String tmpR : tmpRs) {
                boolean flag = false;
                for (int k = 0; k < tmpR.length(); k++) {
                    if (!N0.contains(String.valueOf(tmpR.charAt(k)))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    del.add(tmpR);
                }
            }
            tmpRs.removeAll(del);
            if (tmpRs.size() == 0)
                delL.add(value.L);
        }
        for (var L : delL) {
            for (int i = 0; i < this.p.size(); i++) {
                if (this.p.get(i).L.equals(L)) {
                    this.p.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * 算法3：生成无ε文法
     */
    public void algorithm3() {
        HashSet<String> N0 = new HashSet<>();
        HashSet<String> N1 = new HashSet<>();
        do {
            out:
            for (var p : this.p) {
                for (var R : p.R) {
                    if (R.equals("ε")) {
                        N1.add(p.L);
                        continue out;
                    } else {
                        boolean flag = true;
                        for (var ch : R.toCharArray()) {
                            if (!N0.contains(String.valueOf(ch))) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            N1.add(p.L);
                            continue out;
                        }
                    }
                }
            }
        } while (N0.addAll(N1));

        ArrayList<String> N2 = new ArrayList<>(N0);

        ArrayList<P> newP = new ArrayList<>();
        for (P value : this.p) {
            HashSet<String> waitToAdd = new HashSet<>();
            for (int j = 0; j < value.R.size(); j++) {
                ArrayList<Integer> index = getIndex(N2, value.R.get(j));
                if (!index.isEmpty()) {
                    waitToAdd.addAll(generateP(index, value.R.get(j)));
                } else if (!value.R.get(j).equals("ε")) {
                    waitToAdd.add(value.R.get(j));
                }
            }
            if (!waitToAdd.isEmpty()) {
                newP.add(new P(value.L, new ArrayList<>(waitToAdd)));
            }
        }
        for (var n : N2) {
            if (n.equals("S")) {
                String[] z = {"S", "ε"};
                newP.add(new P("Z", new ArrayList<>(Arrays.asList(z))));
                this.N.add("Z");
                this.S = "Z";
                break;
            }
        }
        this.p.clear();
        this.p.addAll(newP);
    }

    /**
     * 算法4：消去单产生式
     */
    public void algorithm4() {
        HashSet<P> newP = new HashSet<>();
        for (var item : this.N) {
            HashSet<String> N0 = new HashSet<>();
            N0.add(item);
            HashSet<String> N1 = new HashSet<>();
            do {
                N1.addAll(N0);
                for (var L : N0) {
                    for (var p : this.p) {
                        if (p.L.equals(L)) {
                            for (var R : p.R) {
                                if (this.N.contains(R))
                                    N1.add(R);
                            }
                            break;
                        }
                    }
                }
            } while (N0.addAll(N1));
            HashSet<String> tmpR = new HashSet<>();
            for (var L : N0) {
                for (var p : this.p) {
                    if (p.L.equals(L)) {
                        for (var R : p.R) {
                            if (!this.N.contains(R))
                                tmpR.add(R);
                        }
                        break;
                    }
                }
            }
            newP.add(new P(item, new ArrayList<>(tmpR)));
        }
        this.p.clear();
        this.p.addAll(newP);
    }

    /**
     * CFG的简化：以3，4，1，2顺序运行算法
     */
    public void simplify() {
        this.algorithm3();
        this.algorithm4();
        this.algorithm1();
        this.algorithm2();
    }

    /**
     * 用于算法3产生去空的生成式
     *
     * @param index 生成式中可置空的符号所在位置
     * @param R     生成式的右侧
     * @return 置空后的生成式
     */
    private ArrayList<String> generateP(ArrayList<Integer> index, String R) {
        HashSet<String> result = new HashSet<>();
        int n = index.size();
        int up = (int) Math.pow(2, n) - 1;
        for (int i = 0; i <= up; i++) {
            StringBuilder sign = new StringBuilder(Integer.toBinaryString(i));
            int count = n - sign.length();
            for (int j = 0; j < count; j++) {
                sign.insert(0, "0");
            }
            StringBuilder str = new StringBuilder();
            int k = 0;
            int l;
            for (int j = 0; j < R.length(); j++) {
                l = index.get(k);
                if (l == j) {
                    if (sign.charAt(k) == '1') {
                        str.append(R.charAt(j));
                    }
                    if (k < index.size()) {
                        k++;
                    }
                } else {
                    str.append(R.charAt(j));
                }
            }
            if (!str.isEmpty() && !str.toString().equals("ε"))
                result.add(str.toString());
        }
        return new ArrayList<>(result);
    }

    /**
     * 找到生成式右侧可置空符号的位置
     *
     * @param N0 可置空符号的数量
     * @param R  生成式的右侧
     * @return 计算后的索引集合
     */
    private ArrayList<Integer> getIndex(ArrayList<String> N0, String R) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < R.length(); i++) {
            for (var item : N0) {
                if (item.equals(String.valueOf(R.charAt(i)))) {
                    result.add(i);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 存放生成式
     */
    static class P {
        /**
         * 生成式左侧
         */
        String L;
        /**
         * 生成式右侧，同左侧的生成式需合并
         */
        ArrayList<String> R;

        /**
         * 构造函数
         *
         * @param L 生成式左侧
         * @param R 生成式右侧
         */
        P(String L, ArrayList<String> R) {
            this.L = L;
            this.R = R;
        }
    }
}

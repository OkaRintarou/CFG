import java.io.IOException;

/**
 * 主类
 */
public class Main {
    /**
     * 程序入口
     *
     * @param args 第一个参数为输入文件名，第二个参数为输出文件名
     */
    public static void main(String[] args) {
        try {
            CFG cfg = new CFG(args[0]);
            cfg.simplify();
            cfg.writeToFile(args[1]);
            System.out.println("转换完成，按回车退出...");
            try {
                int read = System.in.read();
                if (read == '\n')
                    System.exit(0);
            } catch (IOException e) {
                System.exit(-1);
            }
        } catch (Exception e) {
            System.err.println("参数错误!");
            System.exit(-1);
        }
    }
}

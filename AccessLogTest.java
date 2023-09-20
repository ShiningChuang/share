import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * access.log: 10365152
 */
public class AccessLogTest {
    private String ipAddress;
    private String dateTime;
    private String request;
    private int status;
    private int bytes;
    private String referer;
    private String agent;

    public AccessLogTest(String ipAddress, String dateTime, String request,
                         int status, int bytes, String referer, String agent) {
        this.ipAddress = ipAddress;
        this.dateTime = dateTime;
        this.request = request;
        this.status = status;
        this.bytes = bytes;
        this.referer = referer;
        this.agent = agent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getRequest() {
        return request;
    }

    public int getStatus() {
        return status;
    }

    public int getBytes() {
        return bytes;
    }

    public String getReferer() {
        return referer;
    }

    public String getAgent() {
        return agent;
    }

    public String toString() {
        return getIpAddress() + " " + getDateTime() + " " + getRequest() + " " + getStatus() + " " + getBytes() +
                " " + getReferer() + " " + getAgent();
    }

    public static void showMem() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        System.out.println("Max memory: " + maxMemory / 1024 / 1024 / 1024 + " GB");
        System.out.println("Allocated memory: " + allocatedMemory / 1024 / 1024 / 1024 + " GB");
        System.out.println("Free memory: " + freeMemory / 1024 / 1024 / 1024 + " GB");

        long availableMemory = maxMemory - allocatedMemory + freeMemory;
        System.out.println("Available memory: " + availableMemory / 1024 / 1024 / 1024 + " GB");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        showMem();

//        List<AccessLogTest> logs = new ArrayList();
        AccessLogTest[] logs = new AccessLogTest[10400000];
        String fileName = "src/main/java/pascal/taie/analysis/replace/rdmaTest/test/access.log";
        String line;
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Pattern pattern = Pattern.compile("(\\S+) (\\S+) (\\S+) \\[(.*)\\] \"(.*)\" (\\d{3}) (\\d+) \"([^\"]*)\" \"([^\"]*)\" \"(.*)\"");
        int cnt = 0;
        while ((line = bufferedReader.readLine()) != null) {
            cnt++;
//            if (cnt == 10) break;
//            System.out.println(line);
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String ipAddress = matcher.group(1);
                String timestamp = matcher.group(4);
                String request = matcher.group(5);
                int statusCode = Integer.parseInt(matcher.group(6));
                int responseSize = Integer.parseInt(matcher.group(7));
                String referer = matcher.group(8);
                String userAgent =  matcher.group(9);
                AccessLogTest log = new AccessLogTest(ipAddress,timestamp,request,statusCode,responseSize,referer,userAgent);
                logs[cnt-1] = log;
            }
            else {
                System.out.println("not match");
            }
            if (cnt % 100000 == 0) {
                System.out.println("line" + cnt);
                showMem();
            }

        }
        bufferedReader.close();
        System.out.println("Success!");
        Random random = new Random();
        System.out.println("logArray.length: " + logs.length);
        for (int i = 0; i < 99; i++) {
            int randomNum = random.nextInt(logs.length);
            System.out.println(logs[randomNum]);
            Thread.sleep(1000);
        }
        Thread.sleep(10000);

//        List<Object> objects = new ArrayList<>();
////
//        try {
//            while (true) {
//                objects.add(new Object());
//            }
//        } catch (OutOfMemoryError e) {
//            System.out.println("Out of memory!");
//        }
    }
}
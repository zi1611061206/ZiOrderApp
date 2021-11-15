package zitech.ziorder.Objects;

public class JdbcObject {
    private String sServerName;
    private String sUserId;
    private String sPwd;
    private String sDatabase;
    private String sClass;
    private String sPort;

    public JdbcObject(String sServerName, String sUserId, String sPwd, String sDatabase, String sPort) {
        this.sServerName = sServerName;
        this.sUserId = sUserId;
        this.sPwd = sPwd;
        this.sDatabase = sDatabase;
        this.sClass = "net.sourceforge.jtds.jdbc.Driver";
        this.sPort = sPort;
    }

    public String getsPort() {
        return sPort;
    }

    public void setsPort(String sPort) {
        this.sPort = sPort;
    }

    public String getsServerName() {
        return sServerName;
    }

    public void setsServerName(String sServerName) {
        this.sServerName = sServerName;
    }

    public String getsUserId() {
        return sUserId;
    }

    public void setsUserId(String sUserId) {
        this.sUserId = sUserId;
    }

    public String getsPwd() {
        return sPwd;
    }

    public void setsPwd(String sPwd) {
        this.sPwd = sPwd;
    }

    public String getsDatabase() {
        return sDatabase;
    }

    public void setsDatabase(String sDatabase) {
        this.sDatabase = sDatabase;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }
}

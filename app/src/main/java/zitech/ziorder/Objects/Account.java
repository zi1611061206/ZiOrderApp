package zitech.ziorder.Objects;

public class Account {
    String username;
    int positionId;
    int staffId;
    String password;
    String fullName;
    String address;
    String phone;
    String idCard;
    byte[] avartar;

    public Account(String username, int positionId, int staffId, String password, String fullName, String address, String phone, String idCard, byte[] avartar) {
        this.username = username;
        this.positionId = positionId;
        this.staffId = staffId;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.idCard = idCard;
        this.avartar = avartar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public byte[] getAvartar() {
        return avartar;
    }

    public void setAvartar(byte[] avartar) {
        this.avartar = avartar;
    }
}

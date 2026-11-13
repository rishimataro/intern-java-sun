public class ChuSoHuu {
    private final String cmnd;
    private final String hoTen;
    private final String email;

    public ChuSoHuu(String cmnd, String hoTen, String email) {
        if (cmnd == null || !cmnd.matches("\\d{12}")) {
            throw new IllegalArgumentException("CMND/CCCD phải gồm đúng 12 chữ số.");
        }
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }
        this.cmnd = cmnd;
        this.hoTen = hoTen.trim();
        this.email = email.trim();
    }

    public String getCmnd() {
        return cmnd;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getEmail() {
        return email;
    }

    public boolean cungThongTin(ChuSoHuu other) {
        return cmnd.equals(other.cmnd)
                && hoTen.equalsIgnoreCase(other.hoTen)
                && email.equalsIgnoreCase(other.email);
    }
}

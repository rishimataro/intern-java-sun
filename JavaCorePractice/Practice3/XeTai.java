public class XeTai extends PhuongTien {
    private final double trongTaiTan;

    public XeTai(String bienSo, String hangSanXuat, int namSanXuat, String mauXe,
                 ChuSoHuu chuSoHuu, double trongTaiTan) {
        super(bienSo, hangSanXuat, namSanXuat, mauXe, chuSoHuu);
        if (!Double.isFinite(trongTaiTan) || trongTaiTan <= 0) {
            throw new IllegalArgumentException("Trọng tải phải là số hữu hạn lớn hơn 0.");
        }
        this.trongTaiTan = trongTaiTan;
    }

    @Override
    public String getLoaiPhuongTien() {
        return "Xe tải";
    }

    @Override
    protected String getThongTinRieng() {
        return String.format("Trọng tải: %.2f tấn", trongTaiTan);
    }
}

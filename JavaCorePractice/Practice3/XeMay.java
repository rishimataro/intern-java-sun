public class XeMay extends PhuongTien {
    private final double dungTichXiLanh;

    public XeMay(String bienSo, String hangSanXuat, int namSanXuat, String mauXe,
                 ChuSoHuu chuSoHuu, double dungTichXiLanh) {
        super(bienSo, hangSanXuat, namSanXuat, mauXe, chuSoHuu);
        if (!Double.isFinite(dungTichXiLanh) || dungTichXiLanh <= 0) {
            throw new IllegalArgumentException("Dung tích xi-lanh phải là số hữu hạn lớn hơn 0.");
        }
        this.dungTichXiLanh = dungTichXiLanh;
    }

    @Override
    public String getLoaiPhuongTien() {
        return "Xe máy";
    }

    @Override
    protected String getThongTinRieng() {
        return String.format("Dung tích: %.1f cc", dungTichXiLanh);
    }
}

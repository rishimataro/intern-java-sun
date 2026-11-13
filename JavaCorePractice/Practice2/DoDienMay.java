import java.time.LocalDate;

public class DoDienMay extends HangHoa {
    private static final double TY_LE_VAT = 0.10;

    private final int thoiGianBaoHanhThang;
    private final double congSuatKW;

    public DoDienMay(String maHang, String tenHang, int soLuongTon, double donGia,
                     int thoiGianBaoHanhThang, double congSuatKW) {
        super(maHang, tenHang, soLuongTon, donGia);
        if (thoiGianBaoHanhThang < 0 || !Double.isFinite(congSuatKW) || congSuatKW < 0) {
            throw new IllegalArgumentException("Bảo hành phải >= 0 và công suất phải hữu hạn, >= 0.");
        }
        this.thoiGianBaoHanhThang = thoiGianBaoHanhThang;
        this.congSuatKW = congSuatKW;
    }

    @Override
    public double getTyLeVAT() {
        return TY_LE_VAT;
    }

    @Override
    public String danhGiaTieuThu(LocalDate homNay) {
        return getSoLuongTon() < 3 ? "Bán chạy" : "Không đánh giá";
    }

    @Override
    public String getLoaiHang() {
        return "Điện máy";
    }

    @Override
    public void hienThiThongTin() {
        System.out.printf("%s | Bảo hành: %d tháng | Công suất: %.2f KW | VAT: 10%% | Đánh giá: %s%n",
                thongTinChung(), thoiGianBaoHanhThang, congSuatKW,
                danhGiaTieuThu(LocalDate.now()));
    }
}

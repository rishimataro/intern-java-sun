import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DoSanhSu extends HangHoa {
    private static final double TY_LE_VAT = 0.10;

    private final String nhaSanXuat;
    private final LocalDate ngayNhapKho;

    public DoSanhSu(String maHang, String tenHang, int soLuongTon, double donGia,
                    String nhaSanXuat, LocalDate ngayNhapKho) {
        super(maHang, tenHang, soLuongTon, donGia);
        if (nhaSanXuat == null || nhaSanXuat.trim().isEmpty() || ngayNhapKho == null) {
            throw new IllegalArgumentException("Nhà sản xuất và ngày nhập kho không được để trống.");
        }
        this.nhaSanXuat = nhaSanXuat.trim();
        this.ngayNhapKho = ngayNhapKho;
    }

    @Override
    public double getTyLeVAT() {
        return TY_LE_VAT;
    }

    @Override
    public String danhGiaTieuThu(LocalDate homNay) {
        long soNgayLuuKho = ChronoUnit.DAYS.between(ngayNhapKho, homNay);
        if (getSoLuongTon() > 50 && soNgayLuuKho > 10) {
            return "Bán chậm";
        }
        return "Không đánh giá";
    }

    @Override
    public String getLoaiHang() {
        return "Sành sứ";
    }

    @Override
    public void hienThiThongTin() {
        System.out.printf("%s | Nhà sản xuất: %s | Ngày nhập kho: %s | VAT: 10%% | Đánh giá: %s%n",
                thongTinChung(), nhaSanXuat, ngayNhapKho,
                danhGiaTieuThu(LocalDate.now()));
    }
}

import java.time.LocalDate;

public class ThucPham extends HangHoa {
    private static final double TY_LE_VAT = 0.05;

    private final LocalDate ngaySanXuat;
    private final LocalDate ngayHetHan;
    private final String nhaCungCap;

    public ThucPham(String maHang, String tenHang, int soLuongTon, double donGia,
                    LocalDate ngaySanXuat, LocalDate ngayHetHan, String nhaCungCap) {
        super(maHang, tenHang, soLuongTon, donGia);
        if (ngaySanXuat == null || ngayHetHan == null || ngayHetHan.isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Ngày hết hạn phải sau hoặc bằng ngày sản xuất.");
        }
        if (nhaCungCap == null || nhaCungCap.trim().isEmpty()) {
            throw new IllegalArgumentException("Nhà cung cấp không được để trống.");
        }
        this.ngaySanXuat = ngaySanXuat;
        this.ngayHetHan = ngayHetHan;
        this.nhaCungCap = nhaCungCap.trim();
    }

    @Override
    public double getTyLeVAT() {
        return TY_LE_VAT;
    }

    @Override
    public String danhGiaTieuThu(LocalDate homNay) {
        if (getSoLuongTon() > 0 && ngayHetHan.isBefore(homNay)) {
            return "Khó bán (đã hết hạn)";
        }
        return "Không đánh giá";
    }

    @Override
    public String getLoaiHang() {
        return "Thực phẩm";
    }

    @Override
    public void hienThiThongTin() {
        System.out.printf("%s | Ngày sản xuất: %s | Hạn sử dụng: %s | Nhà cung cấp: %s | VAT: 5%% | Đánh giá: %s%n",
                thongTinChung(), ngaySanXuat, ngayHetHan, nhaCungCap,
                danhGiaTieuThu(LocalDate.now()));
    }
}

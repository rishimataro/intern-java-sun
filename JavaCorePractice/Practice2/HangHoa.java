import java.time.LocalDate;

/** Lớp cơ sở cho các loại hàng hóa trong siêu thị. */
public abstract class HangHoa {
    private final String maHang;
    private final String tenHang;
    private final int soLuongTon;
    private final double donGia;

    protected HangHoa(String maHang, String tenHang, int soLuongTon, double donGia) {
        if (maHang == null || maHang.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hàng không được để trống.");
        }
        if (tenHang == null || tenHang.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên hàng không được để trống.");
        }
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("Số lượng tồn phải >= 0.");
        }
        if (!Double.isFinite(donGia) || donGia < 0) {
            throw new IllegalArgumentException("Đơn giá phải là số hữu hạn và >= 0.");
        }
        this.maHang = maHang.trim();
        this.tenHang = tenHang.trim();
        this.soLuongTon = soLuongTon;
        this.donGia = donGia;
    }

    public String getMaHang() {
        return maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public double getDonGia() {
        return donGia;
    }

    public abstract double getTyLeVAT();

    public abstract String danhGiaTieuThu(LocalDate homNay);

    public double tinhTienVATTonKho() {
        return soLuongTon * donGia * getTyLeVAT();
    }

    public abstract String getLoaiHang();

    public String thongTinChung() {
        return String.format("Mã: %s | Tên: %s | Tồn: %d | Đơn giá: %.2f",
                maHang, tenHang, soLuongTon, donGia);
    }

    public abstract void hienThiThongTin();
}

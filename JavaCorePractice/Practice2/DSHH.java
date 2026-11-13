import java.time.LocalDate;

/** Danh sách hàng hóa quản lý bằng mảng cố định. */
public class DSHH {
    private final HangHoa[] danhSach;
    private int soLuongHang;

    public DSHH(int sucChua) {
        if (sucChua <= 0) {
            throw new IllegalArgumentException("Sức chứa phải lớn hơn 0.");
        }
        danhSach = new HangHoa[sucChua];
    }

    public boolean themHang(HangHoa hangHoa) {
        if (hangHoa == null || soLuongHang == danhSach.length || timTheoMa(hangHoa.getMaHang()) != null) {
            return false;
        }
        danhSach[soLuongHang++] = hangHoa;
        return true;
    }

    public HangHoa timTheoMa(String maHang) {
        for (int i = 0; i < soLuongHang; i++) {
            if (danhSach[i].getMaHang().equalsIgnoreCase(maHang)) {
                return danhSach[i];
            }
        }
        return null;
    }

    public void hienThiDanhSach() {
        if (soLuongHang == 0) {
            System.out.println("Danh sách hàng hóa đang trống.");
            return;
        }
        for (int i = 0; i < soLuongHang; i++) {
            danhSach[i].hienThiThongTin();
        }
    }

    public int getSoLuongTonTheoLoai(Class<? extends HangHoa> loaiHang) {
        int tong = 0;
        for (int i = 0; i < soLuongHang; i++) {
            if (loaiHang.isInstance(danhSach[i])) {
                tong += danhSach[i].getSoLuongTon();
            }
        }
        return tong;
    }

    public double getTongVATTheoLoai(Class<? extends HangHoa> loaiHang) {
        double tong = 0;
        for (int i = 0; i < soLuongHang; i++) {
            if (loaiHang.isInstance(danhSach[i])) {
                tong += danhSach[i].tinhTienVATTonKho();
            }
        }
        return tong;
    }

    public void hienThiThongKe() {
        hienThiThongKeLoai("Thực phẩm", ThucPham.class);
        hienThiThongKeLoai("Điện máy", DoDienMay.class);
        hienThiThongKeLoai("Sành sứ", DoSanhSu.class);
    }

    private void hienThiThongKeLoai(String tenLoai, Class<? extends HangHoa> loaiHang) {
        System.out.printf("%s - tổng số lượng tồn: %d | VAT hàng tồn kho: %.2f%n",
                tenLoai, getSoLuongTonTheoLoai(loaiHang), getTongVATTheoLoai(loaiHang));
    }

    public String danhGiaTieuThu(String maHang) {
        HangHoa hangHoa = timTheoMa(maHang);
        return hangHoa == null ? "Không tìm thấy mã hàng." : hangHoa.danhGiaTieuThu(LocalDate.now());
    }
}

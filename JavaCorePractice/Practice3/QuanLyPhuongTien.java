import java.util.Arrays;

public class QuanLyPhuongTien {
    private static final String[] CAC_HANG = {"Honda", "Yamaha", "Toyota", "Suzuki"};

    private final PhuongTien[] danhSach;
    private int soLuong;

    public QuanLyPhuongTien(int sucChua) {
        if (sucChua <= 0) {
            throw new IllegalArgumentException("Sức chứa phải lớn hơn 0.");
        }
        danhSach = new PhuongTien[sucChua];
    }

    public boolean themPhuongTien(PhuongTien phuongTien) {
        if (phuongTien == null || soLuong == danhSach.length || timTheoBienSo(phuongTien.getBienSo()) != null) {
            return false;
        }
        ChuSoHuu chuSoHuuDaCo = timChuSoHuu(phuongTien.getChuSoHuu().getCmnd());
        if (chuSoHuuDaCo != null && !chuSoHuuDaCo.cungThongTin(phuongTien.getChuSoHuu())) {
            return false;
        }
        danhSach[soLuong++] = phuongTien;
        return true;
    }

    public PhuongTien timTheoBienSo(String bienSo) {
        if (bienSo == null) {
            return null;
        }
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getBienSo().equalsIgnoreCase(bienSo.trim())) {
                return danhSach[i];
            }
        }
        return null;
    }

    public void timXeCuaChu(String cmnd) {
        boolean timThay = false;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getChuSoHuu().getCmnd().equals(cmnd)) {
                danhSach[i].hienThiThongTin();
                timThay = true;
            }
        }
        if (!timThay) {
            System.out.println("Không tìm thấy phương tiện của chủ sở hữu có CMND/CCCD này.");
        }
    }

    public int xoaTheoHang(String hangSanXuat) {
        String hangChuan = PhuongTien.chuanHoaHangSanXuat(hangSanXuat);
        if (hangChuan == null) {
            return -1;
        }
        int soDaXoa = 0;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getHangSanXuat().equals(hangChuan)) {
                soDaXoa++;
            } else {
                danhSach[i - soDaXoa] = danhSach[i];
            }
        }
        for (int i = soLuong - soDaXoa; i < soLuong; i++) {
            danhSach[i] = null;
        }
        soLuong -= soDaXoa;
        return soDaXoa;
    }

    public String[] timHangNhieuXeNhat() {
        int max = 0;
        for (String hang : CAC_HANG) {
            max = Math.max(max, demTheoHang(hang));
        }
        if (max == 0) {
            return new String[0];
        }
        String[] ketQua = new String[CAC_HANG.length];
        int count = 0;
        for (String hang : CAC_HANG) {
            if (demTheoHang(hang) == max) {
                ketQua[count++] = hang;
            }
        }
        return Arrays.copyOf(ketQua, count);
    }

    public String[] sapXepHangTheoSoXeGiamDan() {
        String[] hangDaSapXep = CAC_HANG.clone();
        for (int i = 1; i < hangDaSapXep.length; i++) {
            String hangDangXet = hangDaSapXep[i];
            int j = i - 1;
            while (j >= 0 && demTheoHang(hangDaSapXep[j]) < demTheoHang(hangDangXet)) {
                hangDaSapXep[j + 1] = hangDaSapXep[j];
                j--;
            }
            hangDaSapXep[j + 1] = hangDangXet;
        }
        return hangDaSapXep;
    }

    public int demTheoHang(String hangSanXuat) {
        int count = 0;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getHangSanXuat().equalsIgnoreCase(hangSanXuat)) {
                count++;
            }
        }
        return count;
    }

    public int demTheoLoai(Class<? extends PhuongTien> loai) {
        int count = 0;
        for (int i = 0; i < soLuong; i++) {
            if (loai.isInstance(danhSach[i])) {
                count++;
            }
        }
        return count;
    }

    public void hienThiDanhSach() {
        if (soLuong == 0) {
            System.out.println("Danh sách phương tiện đang trống.");
            return;
        }
        for (int i = 0; i < soLuong; i++) {
            danhSach[i].hienThiThongTin();
        }
    }

    public int getSoLuong() {
        return soLuong;
    }

    private ChuSoHuu timChuSoHuu(String cmnd) {
        for (int i = 0; i < soLuong; i++) {
            ChuSoHuu chuSoHuu = danhSach[i].getChuSoHuu();
            if (chuSoHuu.getCmnd().equals(cmnd)) {
                return chuSoHuu;
            }
        }
        return null;
    }
}

import java.time.Year;

public abstract class PhuongTien {
    private static final String[] HANG_SAN_XUAT_HOP_LE = {"Honda", "Yamaha", "Toyota", "Suzuki"};

    private final String bienSo;
    private final String hangSanXuat;
    private final int namSanXuat;
    private final String mauXe;
    private final ChuSoHuu chuSoHuu;

    protected PhuongTien(String bienSo, String hangSanXuat, int namSanXuat,
                         String mauXe, ChuSoHuu chuSoHuu) {
        if (bienSo == null || !bienSo.trim().matches("[^\\s]{5}")) {
            throw new IllegalArgumentException("Biển số phải gồm đúng 5 ký tự chữ hoặc số.");
        }
        String hangChuan = chuanHoaHangSanXuat(hangSanXuat);
        if (hangChuan == null) {
            throw new IllegalArgumentException("Hãng chỉ gồm Honda, Yamaha, Toyota hoặc Suzuki.");
        }
        int namHienTai = Year.now().getValue();
        if (namSanXuat <= 2000 || namSanXuat > namHienTai) {
            throw new IllegalArgumentException("Năm sản xuất phải lớn hơn 2000 và không vượt quá năm hiện tại.");
        }
        if (mauXe == null || mauXe.trim().isEmpty()) {
            throw new IllegalArgumentException("Màu xe không được để trống.");
        }
        if (chuSoHuu == null) {
            throw new IllegalArgumentException("Chủ sở hữu không được để trống.");
        }
        this.bienSo = bienSo.trim().toUpperCase();
        this.hangSanXuat = hangChuan;
        this.namSanXuat = namSanXuat;
        this.mauXe = mauXe.trim();
        this.chuSoHuu = chuSoHuu;
    }

    public static String chuanHoaHangSanXuat(String hangSanXuat) {
        if (hangSanXuat == null) {
            return null;
        }
        for (String hang : HANG_SAN_XUAT_HOP_LE) {
            if (hang.equalsIgnoreCase(hangSanXuat.trim())) {
                return hang;
            }
        }
        return null;
    }

    public String getBienSo() {
        return bienSo;
    }

    public String getHangSanXuat() {
        return hangSanXuat;
    }

    public int getNamSanXuat() {
        return namSanXuat;
    }

    public String getMauXe() {
        return mauXe;
    }

    public ChuSoHuu getChuSoHuu() {
        return chuSoHuu;
    }

    public abstract String getLoaiPhuongTien();

    protected abstract String getThongTinRieng();

    public void hienThiThongTin() {
        System.out.printf("Loại: %s | Biển số: %s | Hãng: %s | Năm: %d | Màu: %s | %s | Chủ xe: %s | CMND: %s | Email: %s%n",
                getLoaiPhuongTien(), bienSo, hangSanXuat, namSanXuat, mauXe,
                getThongTinRieng(), chuSoHuu.getHoTen(), chuSoHuu.getCmnd(), chuSoHuu.getEmail());
    }
}

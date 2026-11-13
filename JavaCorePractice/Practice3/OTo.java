public class OTo extends PhuongTien {
    private final int soChoNgoi;
    private final String kieuDongCo;

    public OTo(String bienSo, String hangSanXuat, int namSanXuat, String mauXe,
               ChuSoHuu chuSoHuu, int soChoNgoi, String kieuDongCo) {
        super(bienSo, hangSanXuat, namSanXuat, mauXe, chuSoHuu);
        if (soChoNgoi <= 0) {
            throw new IllegalArgumentException("Số chỗ ngồi phải lớn hơn 0.");
        }
        if (kieuDongCo == null || kieuDongCo.trim().isEmpty()) {
            throw new IllegalArgumentException("Kiểu động cơ không được để trống.");
        }
        this.soChoNgoi = soChoNgoi;
        this.kieuDongCo = kieuDongCo.trim();
    }

    @Override
    public String getLoaiPhuongTien() {
        return "Ô tô";
    }

    @Override
    protected String getThongTinRieng() {
        return "Số chỗ: " + soChoNgoi + " | Động cơ: " + kieuDongCo;
    }
}

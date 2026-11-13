import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DSHH DANH_SACH = new DSHH(100);

    public static void main(String[] args) {
        boolean tiepTuc = true;
        while (tiepTuc) {
            hienThiMenu();
            int luaChon = nhapSoNguyen("Chọn chức năng: ");
            switch (luaChon) {
                case 1:
                    themHangHoa();
                    break;
                case 2:
                    DANH_SACH.hienThiDanhSach();
                    break;
                case 3:
                    DANH_SACH.hienThiThongKe();
                    break;
                case 4:
                    tiepTuc = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
        System.out.println("Đã kết thúc chương trình.");
    }

    private static void hienThiMenu() {
        System.out.println("\n=== QUẢN LÝ HÀNG HÓA SIÊU THỊ ===");
        System.out.println("1. Thêm hàng hóa");
        System.out.println("2. Hiển thị danh sách");
        System.out.println("3. Thống kê số lượng tồn và VAT");
        System.out.println("4. Thoát");
    }

    private static void themHangHoa() {
        System.out.println("1. Thực phẩm | 2. Điện máy | 3. Sành sứ");
        int loai = nhapSoNguyen("Chọn loại hàng: ");
        if (loai < 1 || loai > 3) {
            System.out.println("Loại hàng không hợp lệ.");
            return;
        }

        String ma = nhapChuoi("Mã hàng: ");
        if (DANH_SACH.timTheoMa(ma) != null) {
            System.out.println("Mã hàng đã tồn tại, không thể thêm.");
            return;
        }
        String ten = nhapChuoi("Tên hàng: ");
        int tonKho = nhapSoNguyenKhongAm("Số lượng tồn (>= 0): ");
        double donGia = nhapSoThucKhongAm("Đơn giá (>= 0): ");

        try {
            HangHoa hangHoa = taoHangHoa(loai, ma, ten, tonKho, donGia);
            if (DANH_SACH.themHang(hangHoa)) {
                System.out.println("Thêm hàng hóa thành công.");
            } else {
                System.out.println("Không thể thêm hàng hóa (danh sách đầy hoặc trùng mã).");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Dữ liệu không hợp lệ: " + exception.getMessage());
        }
    }

    private static HangHoa taoHangHoa(int loai, String ma, String ten, int tonKho, double donGia) {
        switch (loai) {
            case 1:
                return new ThucPham(ma, ten, tonKho, donGia,
                        nhapNgay("Ngày sản xuất (yyyy-MM-dd): "),
                        nhapNgay("Ngày hết hạn (yyyy-MM-dd): "),
                        nhapChuoi("Nhà cung cấp: "));
            case 2:
                return new DoDienMay(ma, ten, tonKho, donGia,
                        nhapSoNguyenKhongAm("Thời gian bảo hành (tháng, >= 0): "),
                        nhapSoThucKhongAm("Công suất (KW, >= 0): "));
            case 3:
                return new DoSanhSu(ma, ten, tonKho, donGia,
                        nhapChuoi("Nhà sản xuất: "),
                        nhapNgay("Ngày nhập kho (yyyy-MM-dd): "));
            default:
                throw new IllegalArgumentException("Loại hàng không hợp lệ.");
        }
    }

    private static String nhapChuoi(String loiNhac) {
        System.out.print(loiNhac);
        return SCANNER.nextLine().trim();
    }

    private static int nhapSoNguyen(String loiNhac) {
        while (true) {
            try {
                return Integer.parseInt(nhapChuoi(loiNhac));
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ.");
            }
        }
    }

    private static int nhapSoNguyenKhongAm(String loiNhac) {
        while (true) {
            int giaTri = nhapSoNguyen(loiNhac);
            if (giaTri >= 0) {
                return giaTri;
            }
            System.out.println("Giá trị phải >= 0.");
        }
    }

    private static double nhapSoThucKhongAm(String loiNhac) {
        while (true) {
            try {
                double giaTri = Double.parseDouble(nhapChuoi(loiNhac));
                if (Double.isFinite(giaTri) && giaTri >= 0) {
                    return giaTri;
                }
                System.out.println("Giá trị phải là số hữu hạn và >= 0.");
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    private static LocalDate nhapNgay(String loiNhac) {
        while (true) {
            try {
                return LocalDate.parse(nhapChuoi(loiNhac));
            } catch (DateTimeParseException exception) {
                System.out.println("Ngày không hợp lệ, hãy nhập theo định dạng yyyy-MM-dd.");
            }
        }
    }
}

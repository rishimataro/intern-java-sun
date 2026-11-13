public class Main {
    private static final java.util.Scanner SCANNER = new java.util.Scanner(System.in);
    private static final QuanLyPhuongTien QUAN_LY = new QuanLyPhuongTien(200);

    public static void main(String[] args) {
        boolean tiepTuc = true;
        while (tiepTuc) {
            hienThiMenu();
            int luaChon = nhapSoNguyen("Chọn chức năng: ");
            switch (luaChon) {
                case 1:
                    themPhuongTien();
                    break;
                case 2:
                    timTheoBienSo();
                    break;
                case 3:
                    timXeCuaChuSoHuu();
                    break;
                case 4:
                    xoaTheoHang();
                    break;
                case 5:
                    hienThiHangNhieuXeNhat();
                    break;
                case 6:
                    hienThiHangTheoSoXeGiamDan();
                    break;
                case 7:
                    thongKeTheoLoai();
                    break;
                case 8:
                    QUAN_LY.hienThiDanhSach();
                    break;
                case 9:
                    tiepTuc = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
        System.out.println("Đã kết thúc chương trình.");
    }

    private static void hienThiMenu() {
        System.out.println("\n=== QUẢN LÝ PHƯƠNG TIỆN CẢNH SÁT ===");
        System.out.println("1. Thêm phương tiện");
        System.out.println("2. Tìm phương tiện theo biển số");
        System.out.println("3. Tìm phương tiện theo CMND/CCCD chủ xe");
        System.out.println("4. Xóa tất cả phương tiện theo hãng");
        System.out.println("5. Hãng có nhiều phương tiện nhất");
        System.out.println("6. Sắp xếp hãng theo số phương tiện giảm dần");
        System.out.println("7. Thống kê theo loại phương tiện");
        System.out.println("8. Hiển thị tất cả phương tiện");
        System.out.println("9. Thoát");
    }

    private static void themPhuongTien() {
        System.out.println("Loại phương tiện: 1. Ô tô | 2. Xe máy | 3. Xe tải");
        int loai = nhapSoNguyen("Chọn loại: ");
        if (loai < 1 || loai > 3) {
            System.out.println("Loại phương tiện không hợp lệ.");
            return;
        }

        String bienSo = nhapChuoi("Biển số (đúng 5 ký tự): ");
        if (QUAN_LY.timTheoBienSo(bienSo) != null) {
            System.out.println("Biển số đã tồn tại.");
            return;
        }
        String hang = nhapChuoi("Hãng (Honda/Yamaha/Toyota/Suzuki): ");
        int nam = nhapSoNguyen("Năm sản xuất: ");
        String mau = nhapChuoi("Màu xe: ");
        ChuSoHuu chuSoHuu;
        try {
            chuSoHuu = new ChuSoHuu(
                    nhapChuoi("CMND/CCCD (12 chữ số): "),
                    nhapChuoi("Họ tên chủ xe: "),
                    nhapChuoi("Email: "));
            PhuongTien phuongTien = taoPhuongTien(loai, bienSo, hang, nam, mau, chuSoHuu);
            if (QUAN_LY.themPhuongTien(phuongTien)) {
                System.out.println("Thêm phương tiện thành công.");
            } else {
                System.out.println("Không thể thêm: biển số trùng, danh sách đầy hoặc CMND/CCCD đã gắn với thông tin chủ xe khác.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Dữ liệu không hợp lệ: " + exception.getMessage());
        }
    }

    private static PhuongTien taoPhuongTien(int loai, String bienSo, String hang,
                                             int nam, String mau, ChuSoHuu chuSoHuu) {
        switch (loai) {
            case 1:
                return new OTo(bienSo, hang, nam, mau, chuSoHuu,
                        nhapSoNguyen("Số chỗ ngồi: "), nhapChuoi("Kiểu động cơ: "));
            case 2:
                return new XeMay(bienSo, hang, nam, mau, chuSoHuu,
                        nhapSoThuc("Dung tích xi-lanh (cc): "));
            case 3:
                return new XeTai(bienSo, hang, nam, mau, chuSoHuu,
                        nhapSoThuc("Trọng tải (tấn): "));
            default:
                throw new IllegalArgumentException("Loại phương tiện không hợp lệ.");
        }
    }

    private static void timTheoBienSo() {
        PhuongTien phuongTien = QUAN_LY.timTheoBienSo(nhapChuoi("Nhập biển số cần tìm: "));
        if (phuongTien == null) {
            System.out.println("Không tìm thấy phương tiện.");
        } else {
            phuongTien.hienThiThongTin();
        }
    }

    private static void timXeCuaChuSoHuu() {
        QUAN_LY.timXeCuaChu(nhapChuoi("Nhập CMND/CCCD chủ xe (12 chữ số): "));
    }

    private static void xoaTheoHang() {
        String hang = nhapChuoi("Hãng cần xóa (Honda/Yamaha/Toyota/Suzuki): ");
        int soDaXoa = QUAN_LY.xoaTheoHang(hang);
        if (soDaXoa < 0) {
            System.out.println("Hãng không hợp lệ.");
        } else {
            System.out.println("Đã xóa " + soDaXoa + " phương tiện của hãng " + hang + ".");
        }
    }

    private static void hienThiHangNhieuXeNhat() {
        String[] hangNhieuNhat = QUAN_LY.timHangNhieuXeNhat();
        if (hangNhieuNhat.length == 0) {
            System.out.println("Chưa có phương tiện nào được quản lý.");
            return;
        }
        System.out.println("Hãng có nhiều phương tiện nhất (" + QUAN_LY.demTheoHang(hangNhieuNhat[0]) + " xe): ");
        for (String hang : hangNhieuNhat) {
            System.out.println("- " + hang);
        }
    }

    private static void hienThiHangTheoSoXeGiamDan() {
        System.out.println("Hãng | Số phương tiện");
        for (String hang : QUAN_LY.sapXepHangTheoSoXeGiamDan()) {
            System.out.printf("%s | %d%n", hang, QUAN_LY.demTheoHang(hang));
        }
    }

    private static void thongKeTheoLoai() {
        System.out.println("Ô tô: " + QUAN_LY.demTheoLoai(OTo.class));
        System.out.println("Xe máy: " + QUAN_LY.demTheoLoai(XeMay.class));
        System.out.println("Xe tải: " + QUAN_LY.demTheoLoai(XeTai.class));
        System.out.println("Tổng: " + QUAN_LY.getSoLuong());
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

    private static double nhapSoThuc(String loiNhac) {
        while (true) {
            try {
                double giaTri = Double.parseDouble(nhapChuoi(loiNhac));
                if (Double.isFinite(giaTri)) {
                    return giaTri;
                }
                System.out.println("Vui lòng nhập số hữu hạn.");
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }
}

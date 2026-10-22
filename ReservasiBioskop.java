import java.util.Scanner;

public class ReservasiBioskop {
    static Scanner input = new Scanner(System.in);

    // Data film disimpan dalam array satu dimensi.
    static String[] namaFilm = {
        "Petualangan Nusantara",
        "Misteri Tengah Malam",
        "Robot Masa Depan"
    };
    static int[] hargaFilm = {35000, 40000, 45000};

    // Array dua dimensi untuk denah kursi: 5 baris x 6 kolom.
    // true berarti kursi sudah dipesan, false berarti masih tersedia.
    static boolean[][] kursiTerisi = new boolean[5][6];

    static String[] namaBaris = {"A", "B", "C", "D", "E"};

    public static void main(String[] args) {
        int pilihan;

        do {
            tampilkanMenu();
            pilihan = inputInt("Pilih menu (1-4): ", 1, 4);

            switch (pilihan) {
                case 1:
                    tampilkanFilm();
                    break;
                case 2:
                    pesanTiket();
                    break;
                case 3:
                    tampilkanKursi();
                    break;
                case 4:
                    System.out.println("\nTerima kasih telah menggunakan Sistem Reservasi Tiket Bioskop.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia.");
            }
        } while (pilihan != 4);

        input.close();
    }

    // Method tanpa nilai kembali: menampilkan menu utama.
    static void tampilkanMenu() {
        System.out.println("\n==========================================");
        System.out.println("       SISTEM RESERVASI TIKET BIOSKOP");
        System.out.println("==========================================");
        System.out.println("1. Lihat daftar film");
        System.out.println("2. Pesan tiket");
        System.out.println("3. Lihat denah kursi");
        System.out.println("4. Keluar");
        System.out.println("------------------------------------------");
    }

    static void tampilkanFilm() {
        System.out.println("\nDAFTAR FILM");
        for (int i = 0; i < namaFilm.length; i++) {
            System.out.println((i + 1) + ". " + namaFilm[i]
                    + " - Rp" + hargaFilm[i]);
        }
    }

    static void tampilkanKursi() {
        System.out.println("\nDENAH KURSI");
        System.out.println("Keterangan: [O] tersedia, [X] sudah dipesan");
        System.out.print("     ");
        for (int kolom = 1; kolom <= kursiTerisi[0].length; kolom++) {
            System.out.print(kolom + "   ");
        }
        System.out.println();

        for (int baris = 0; baris < kursiTerisi.length; baris++) {
            System.out.print(" " + namaBaris[baris] + "   ");
            for (int kolom = 0; kolom < kursiTerisi[baris].length; kolom++) {
                if (kursiTerisi[baris][kolom]) {
                    System.out.print("[X] ");
                } else {
                    System.out.print("[O] ");
                }
            }
            System.out.println();
        }
    }

    static void pesanTiket() {
        tampilkanFilm();
        int pilihanFilm = inputInt("Pilih nomor film: ", 1, namaFilm.length);
        int indeksFilm = pilihanFilm - 1;

        System.out.println("\nPILIH JADWAL");
        System.out.println("1. 13:00");
        System.out.println("2. 16:00");
        System.out.println("3. 19:00");
        int pilihanJadwal = inputInt("Pilih jadwal (1-3): ", 1, 3);

        String jadwal;
        switch (pilihanJadwal) {
            case 1:
                jadwal = "13:00";
                break;
            case 2:
                jadwal = "16:00";
                break;
            default:
                jadwal = "19:00";
                break;
        }

        tampilkanKursi();
        int jumlahTiket = inputInt("Berapa tiket yang ingin dipesan (1-5): ", 1, 5);

        // Kursi yang dipilih ditampung sementara agar tidak langsung terpesan
        // sebelum semua pilihan valid.
        int[] pilihanBaris = new int[jumlahTiket];
        int[] pilihanKolom = new int[jumlahTiket];

        for (int i = 0; i < jumlahTiket; i++) {
            boolean valid = false;

            while (!valid) {
                System.out.println("\nPilih kursi ke-" + (i + 1));
                String baris = input.nextLine().trim().toUpperCase();

                if (baris.length() != 1 || baris.charAt(0) < 'A' || baris.charAt(0) > 'E') {
                    System.out.println("Baris tidak valid. Gunakan A, B, C, D, atau E.");
                    continue;
                }

                int nomorKursi = inputInt("Masukkan nomor kursi (1-6): ", 1, 6);
                int indeksBaris = baris.charAt(0) - 'A';
                int indeksKolom = nomorKursi - 1;

                if (kursiTerisi[indeksBaris][indeksKolom]) {
                    System.out.println("Maaf, kursi tersebut sudah dipesan. Pilih kursi lain.");
                    continue;
                }

                boolean duplikat = false;
                for (int j = 0; j < i; j++) {
                    if (pilihanBaris[j] == indeksBaris && pilihanKolom[j] == indeksKolom) {
                        duplikat = true;
                        break;
                    }
                }

                if (duplikat) {
                    System.out.println("Kursi itu sudah Anda pilih untuk pesanan ini. Pilih kursi lain.");
                } else {
                    pilihanBaris[i] = indeksBaris;
                    pilihanKolom[i] = indeksKolom;
                    valid = true;
                }
            }
        }

        int totalHarga = hargaFilm[indeksFilm] * jumlahTiket;
        System.out.println("\nRINGKASAN PESANAN");
        System.out.println("Film       : " + namaFilm[indeksFilm]);
        System.out.println("Jadwal     : " + jadwal);
        System.out.println("Jumlah     : " + jumlahTiket + " tiket");
        System.out.println("Harga/tiket: Rp" + hargaFilm[indeksFilm]);
        System.out.println("Total      : Rp" + totalHarga);

        int pembayaran = inputInt("Masukkan pembayaran (minimal Rp" + totalHarga + "): ",
                totalHarga, 1000000000);
        int kembalian = pembayaran - totalHarga;

        // Kursi baru ditandai terisi setelah pembayaran valid.
        for (int i = 0; i < jumlahTiket; i++) {
            kursiTerisi[pilihanBaris[i]][pilihanKolom[i]] = true;
        }

        System.out.println("\n========== RESERVASI BERHASIL ==========");
        System.out.println("Film       : " + namaFilm[indeksFilm]);
        System.out.println("Jadwal     : " + jadwal);
        System.out.print("Kursi      : ");
        for (int i = 0; i < jumlahTiket; i++) {
            System.out.print(namaBaris[pilihanBaris[i]] + (pilihanKolom[i] + 1));
            if (i < jumlahTiket - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("Total      : Rp" + totalHarga);
        System.out.println("Pembayaran : Rp" + pembayaran);
        System.out.println("Kembalian  : Rp" + kembalian);
        System.out.println("=========================================");
    }

    // Method dengan nilai kembali: memastikan input angka berada pada rentang yang diizinkan.
    static int inputInt(String pesan, int minimum, int maksimum) {
        while (true) {
            System.out.print(pesan);

            if (input.hasNextInt()) {
                int nilai = input.nextInt();
                input.nextLine(); // Membersihkan karakter newline setelah nextInt().

                if (nilai >= minimum && nilai <= maksimum) {
                    return nilai;
                }

                System.out.println("Input harus antara " + minimum + " dan " + maksimum + ".");
            } else {
                System.out.println("Input tidak valid. Masukkan angka.");
                input.nextLine(); // Membuang input yang bukan angka.
            }
        }
    }
}

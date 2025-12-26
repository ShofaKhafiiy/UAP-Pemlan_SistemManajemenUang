# 💰 MONEY MANAGER - Modern Finance Tracker

Money Manager adalah aplikasi manajemen keuangan pribadi berbasis desktop yang dirancang untuk membantu pengguna mencatat pemasukan dan pengeluaran dengan antarmuka yang modern, intuitif, dan user-friendly. Aplikasi ini dibangun menggunakan Java Swing dengan menerapkan arsitektur MVC (Model-View-Controller) dan prinsip Object-Oriented Programming (OOP) yang mendalam, serta menggunakan Excel sebagai basis data permanen.

---

## ✨ Fitur Utama

### Sistem Autentikasi Multiusers:
- **Register & Login**: Pengguna dapat membuat akun pribadi. Data antar pengguna terpisah secara aman.
- **Guest Mode**: Mencoba aplikasi tanpa perlu mendaftar (data bersifat sementara/in-memory).

### Dashboard Finansial:
- **Summary Cards**: Menampilkan total Saldo, Pemasukan, dan Pengeluaran secara real-time.
- **Visual Chart**: Grafik batang dinamis untuk analisis arus kas (pemasukan vs pengeluaran).

### Manajemen Transaksi (CRUD):
- **Create**: Menambah transaksi dengan kategori kustom (Gaji, Makan, Hiburan, dll).
- **Read**: Riwayat transaksi yang rapi dengan fitur filter pencarian.
- **Update**: Mengubah detail transaksi yang salah input melalui dialog modern.
- **Delete**: Menghapus catatan transaksi (Soft Delete) untuk menjaga integritas data.

### Persistensi Data (Excel): 
Menggunakan library Apache POI untuk menyimpan data ke file `.xlsx`. Data tetap tersimpan meskipun aplikasi ditutup.

### UI/UX Modern:
Menggunakan gradasi warna, sudut melengkung (rounded corners), efek fade animation, dan ikon yang menarik.

### Export Data:
Fitur untuk mengekspor riwayat transaksi ke dalam file Excel eksternal.

---

## 🛠️ Teknologi yang Digunakan
- **Bahasa Pemrograman**: Java (JDK 11+).
- **GUI Framework**: Java Swing & AWT.
- **Library Eksternal**: Apache POI (untuk pengolahan file Excel).
- **Penyimpanan**: File System (Excel `.xlsx`).
- **Arsitektur**: Model-View-Controller (MVC).

---

## 📂 Struktur Proyek

com.ShofaKhafiy.MoneyManager/
├── mainApp/
│ └── AppLauncher.java # Entry point utama aplikasi
├── model/
│ ├── Transaction.java # Objek data transaksi (Builder Pattern)
│ ├── User.java # Objek data pengguna
│ ├── Category.java # Base class kategori (Inheritance)
│ ├── TransactionType.java # Enum tipe (Income/Expense)
│ └── TransactionResult.java # Helper status operasi
├── controller/
│ └── TransactionController.java # Penghubung UI dan Business Logic
├── service/
│ ├── MoneyManagerService.java # Logika bisnis utama (Singleton)
│ ├── AuthService.java # Logika login & registrasi
│ └── CategoryFactory.java # Pembuat kategori (Factory Pattern)
├── repository/
│ ├── TransactionRepository.java # Interface abstraksi data
│ ├── ExcelTransactionRepository.java # Implementasi simpan ke Excel
│ └── InMemoryTransactionRepo.java # Penyimpanan sementara (Guest)
├── view/
│ ├── MainFrame.java # Frame utama dashboard
│ ├── LoginFrame.java # Frame login
│ ├── ModernInputDialog.java # Dialog input transaksi
│ ├── SimpleChartPanel.java # Visualisasi grafik kustom
│ └── components/ # Custom UI (RoundedPanel, StyledButton, dll)
└── util/
├── ExcelUtil.java # Helper operasional file Excel
├── FormatUtil.java # Pemformatan mata uang & tanggal
└── FadeAnimation.java # Logika animasi UI

yaml
Copy code

---

## 🏗️ Arsitektur Aplikasi

Aplikasi ini membagi tanggung jawab komponen secara ketat:
- **Model**: Mengelola data mentah dan aturan validasi data.
- **View**: Mengurus segala tampilan visual dan interaksi pengguna.
- **Controller**: Mengontrol alur aplikasi dan memproses input dari View untuk dikirim ke Service.
- **Repository**: Mengurus cara data disimpan (apakah ke Excel atau hanya di memori).

---

## 🎨 Prinsip OOP yang Diterapkan
- **Encapsulation**: Melindungi integritas data pada model (contoh: class `Transaction` menggunakan private fields dengan getter/setter).
- **Inheritance & Polymorphism**: Menggunakan kelas abstrak `Category` yang diturunkan menjadi `IncomeCategory` dan `ExpenseCategory`.
- **Singleton Pattern**: Memastikan hanya ada satu instans `MoneyManagerService` dan `AuthService` yang berjalan.
- **Factory Pattern**: Memusatkan pembuatan objek kategori melalui `CategoryFactory`.
- **Builder Pattern**: Digunakan pada `Transaction.Builder` untuk pembuatan objek transaksi yang fleksibel dan mudah dibaca.

---

## 🚀 Cara Menjalankan

### Prerequisites
- JDK 11 atau lebih tinggi.
- Library Apache POI terpasang (jika menggunakan Maven, tambahkan dependency di `pom.xml`).

### Langkah-langkah
1. **Clone repository**:

   ```bash
   git clone https://github.com/username/MoneyManager.git
Jalankan Aplikasi:

Buka proyek di IDE favorit Anda (IntelliJ/NetBeans/Eclipse).

Pastikan library Apache POI sudah masuk dalam classpath.

Jalankan AppLauncher.java.

Database Otomatis: Aplikasi akan otomatis membuat folder data/ dan file accounts.xlsx saat pertama kali dijalankan.

📊 Format Penyimpanan Data
Data disimpan secara terstruktur di dalam file Excel:

accounts.xlsx: Menyimpan username dan password (hashed/plain).

db_[username].xlsx: Menyimpan ID, Tanggal, Kategori, Deskripsi, Jumlah, dan Saldo Akhir khusus untuk setiap user.

👨‍💻 Author
Dibuat dengan ❤️ oleh Shofa Khafiy untuk memenuhi tugas Ujian Akhir Praktikum Pemrograman Lanjut.

Money Manager - Smart Way to Track Your Wealth.

markdown
Copy code

### **Penjelasan:**
- README ini memberikan penjelasan lengkap tentang aplikasi **Money Manager**, mulai dari fitur utama, teknologi yang digunakan, struktur proyek, hingga cara menjalankan aplikasi.
- **Fitur** yang disediakan seperti login, guest mode, transaksi CRUD, serta integrasi dengan Excel untuk penyimpanan data.
- **Arsitektur** aplikasi menggunakan **Model-View-Controller (MVC)** dan prinsip **Object-Oriented Programming (OOP)**.
- **Cara Menjalankan** dan **prerequisites** untuk menjalankan aplikasi ini, serta format penyimpanan data yang disimpan di file Excel.

Silakan sesuaikan dengan nama repository atau link yang relevan jika diperlukan.

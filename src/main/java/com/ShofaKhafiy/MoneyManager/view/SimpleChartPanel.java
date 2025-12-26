package com.ShofaKhafiy.MoneyManager.view;

import com.ShofaKhafiy.MoneyManager.util.FormatUtil;
import com.ShofaKhafiy.MoneyManager.view.components.RoundedPanel;

import java.awt.*;

public class SimpleChartPanel extends RoundedPanel {
    private double income = 0;
    private double expense = 0;

    public SimpleChartPanel() {
        super(25); // Membuat sudut lengkung 25px
        setBackground(Color.WHITE);
    }

    // Method untuk memperbarui data dari MainFrame
    public void updateData(double income, double expense) {
        this.income = income;
        this.expense = expense;
        repaint(); // Menggambar ulang grafik saat data berubah
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Menghaluskan garis/grafik
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Gambar Judul
        g2.setColor(new Color(44, 62, 80));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.drawString("Analisis Arus Kas", 30, 45);

        // 2. Setting Dimensi
        int w = getWidth();
        int h = getHeight();
        int paddingBottom = 80; // Jarak dari bawah untuk label teks
        int barWidth = 60;      // Lebar batang grafik

        // Cari nilai tertinggi untuk skala grafik
        double maxVal = Math.max(income, expense);
        if (maxVal <= 0) maxVal = 1; // Mencegah pembagian dengan nol

        int maxBarHeight = h - 180; // Tinggi maksimal batang di layar

        // Hitung tinggi proporsional masing-masing batang
        int hIn = (int) ((income / maxVal) * maxBarHeight);
        int hEx = (int) ((expense / maxVal) * maxBarHeight);

        // 3. Gambar Batang Pemasukan (Hijau)
        g2.setColor(new Color(46, 204, 113));
        g2.fillRoundRect(w / 4 - barWidth / 2, h - paddingBottom - hIn, barWidth, hIn, 15, 15);

        // 4. Gambar Batang Pengeluaran (Merah)
        g2.setColor(new Color(231, 76, 60));
        g2.fillRoundRect(3 * w / 4 - barWidth / 2, h - paddingBottom - hEx, barWidth, hEx, 15, 15);

        // 5. Gambar Label Angka (Format Rupiah) di atas batang
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.setColor(Color.DARK_GRAY);

        String strIn = FormatUtil.formatCurrency(income);
        String strEx = FormatUtil.formatCurrency(expense);

        // Posisikan teks tepat di tengah atas batang
        int widthIn = g2.getFontMetrics().stringWidth(strIn);
        int widthEx = g2.getFontMetrics().stringWidth(strEx);

        g2.drawString(strIn, w / 4 - (widthIn / 2), h - paddingBottom - hIn - 15);
        g2.drawString(strEx, 3 * w / 4 - (widthEx / 2), h - paddingBottom - hEx - 15);

        // 6. Gambar Label Nama di bawah batang
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(new Color(127, 140, 141));
        g2.drawString("MASUK", w / 4 - 25, h - 45);
        g2.drawString("KELUAR", 3 * w / 4 - 25, h - 45);
    }
}
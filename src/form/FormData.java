/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;

//pdf
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class FormData extends javax.swing.JFrame {
public Statement st;
    /**
     * Creates new form FormData
     */
    public FormData() {
        initComponents();
        tampilTable();
        combobox();
    }
    
    
   
public void createPDF() throws SQLException {
    Document document = new Document(PageSize.A4, 50, 50, 50, 50); // Ukuran A4 dengan batas 50 di setiap sisinya
    try {
        Connection konek = new koneksi().getKoneksi();
        String sql = "SELECT buku.kd_buku, buku.judul, kategori.nama as kategori, "
                + "COALESCE(kondisi.keterangan, '-') as kondisi, "
                + "COALESCE(penyimpanan.nama_rak, '-') as nama_rak FROM buku INNER JOIN kategori ON "
                + "buku.kategori_id = kategori.id LEFT JOIN kondisi ON buku.kd_buku = kondisi.kd_buku "
                + "LEFT JOIN penyimpanan ON buku.kd_buku = penyimpanan.kd_buku;";
        st = konek.createStatement();
        ResultSet hasil = st.executeQuery(sql);
        java.util.Date today = new java.util.Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss");
        String timestamp = dateFormat.format(today);

        String namaFile = "C:\\Users\\ASUS\\Documents\\NetBeansProjects\\pbo-arsip-perpus\\pdf\\arsip-perpustakaan-" + 
                timestamp + ".pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(namaFile));


        document.open();

     
        PdfContentByte content = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.setStrokeOpacity(1.0f);
        content.setGState(gs);
        content.setLineWidth(3); 
        content.rectangle(50, 50, PageSize.A4.getWidth() - 100, PageSize.A4.getHeight() - 100);
        content.stroke();

      
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
        Paragraph info = new Paragraph();
        info.setIndentationLeft(30);
        info.add("Nama Petugas: PT Arsip Perpustakaan Polmed\n\n");
        java.util.Date now = new java.util.Date();
        java.text.SimpleDateFormat dayformat = new java.text.SimpleDateFormat("dd MMMM yyyy", new java.util.Locale("id"));
        String tanggalDibuat = dayformat.format(now);
        info.add("Tanggal Dibuat: " + tanggalDibuat + "\n\n");

        info.add("Nama Perpustakaan : Perpustakaan POLMED\n\n");
        document.add(info);

     
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(90);
        table.setSpacingBefore(20);

        PdfPCell headerCell;
        BaseColor headerColor = new BaseColor(128, 0, 128);
        headerCell = new PdfPCell(new Phrase("Kode Buku", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE)));
        headerCell.setBackgroundColor(headerColor);
        headerCell.setPadding(8);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Buku", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE)));
        headerCell.setBackgroundColor(headerColor);
        headerCell.setPadding(8);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Kategori", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE)));
        headerCell.setBackgroundColor(headerColor);
        headerCell.setPadding(8);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Penyimpanan", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE)));
        headerCell.setBackgroundColor(headerColor);
        headerCell.setPadding(8);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Kondisi", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE)));
        headerCell.setBackgroundColor(headerColor);
        headerCell.setPadding(8);
        table.addCell(headerCell);

        // Data buku
        while (hasil.next()) {
            PdfPCell dataCell;

            dataCell = new PdfPCell(new Phrase(hasil.getString("kd_buku")));
            dataCell.setPadding(6); 
            table.addCell(dataCell);

            dataCell = new PdfPCell(new Phrase(hasil.getString("judul")));
            dataCell.setPadding(6); 
            table.addCell(dataCell);
            
            dataCell = new PdfPCell(new Phrase(hasil.getString("kategori")));
            dataCell.setPadding(6); 
            table.addCell(dataCell);
            
            dataCell = new PdfPCell(new Phrase(hasil.getString("nama_rak")));
            dataCell.setPadding(6); 
            table.addCell(dataCell);
            
            dataCell = new PdfPCell(new Phrase(hasil.getString("kondisi")));
            dataCell.setPadding(6); 
            table.addCell(dataCell);
        }

        document.add(table);
    

        // Konten daftar kontak
        Paragraph contacts = new Paragraph();
        contacts.setIndentationLeft(30);
        contacts.setSpacingAfter(20);
        contacts.setSpacingBefore(20);
        contacts.add("Daftar Kontak:\n");
        contacts.add("1. Email: info@perpuspolmed.com\n");
        contacts.add("2. Telepon: 081234567890\n");
        contacts.add("3. Alamat: Jl. Almamater No. 1\n\n");
        document.add(contacts);

        // Konten catatan atau informasi kebijakan
        Paragraph notes = new Paragraph();
        notes.setIndentationLeft(30);
        notes.setSpacingAfter(20);
        notes.setSpacingBefore(20);
        notes.add("Catatan:\n");
        notes.add("Kondisi dan Penyimpanan buku sewaktu waktu dapat berubah.\n\n");
        document.add(notes);


        Paragraph guide = new Paragraph();
        guide.setIndentationLeft(30);
        guide.setSpacingAfter(20);
        guide.setSpacingBefore(20);
        guide.add("Petunjuk Penggunaan:\n");
        guide.add("Untuk informasi lebih lanjut, kunjungi situs web kami di www.perpuspolmed.com\n\n");
        document.add(guide);

        PdfPCell signatureCell = new PdfPCell(new Phrase("Tanda Tangan: ____________________________"));
        signatureCell.setPadding(20);
        PdfPTable signatureTable = new PdfPTable(1);
      
        signatureTable.addCell(signatureCell);

        document.add(signatureTable);

        document.close();
        System.out.println("PDF berhasil dibuat.");
    } catch (DocumentException | FileNotFoundException e) {
        e.printStackTrace();
    }
}


    private DefaultTableModel tabmode;
    public void tampilTable() {
      Object[] baris = {"kd buku", "judul", "kategori", "penulis", "tahun terbit", "jlh cetakan", "stok"};
        tabmode = new DefaultTableModel(null, baris);
        String sql = "SELECT *, YEAR(tahun_terbit) as tahun , kategori.nama"
                + " as kategori FROM buku INNER JOIN kategori on buku.kategori_id = kategori.id";
        
       jTable1.setModel(tabmode);
        try {
            Connection konek = new koneksi().getKoneksi();

            st = konek.createStatement();
            ResultSet hasil = st.executeQuery(sql);
            
            while(hasil.next()){
                
                String kd_buku = hasil.getString("kd_buku");
                String judul = hasil.getString("judul");
                String kategori = hasil.getString("kategori");
                String penulis = hasil.getString("penulis");
                String tahun = hasil.getString("tahun");
                String jlh_cetakan = hasil.getString("jlh_cetakan");
                String stok = hasil.getString("stok");
                String []data = {kd_buku, judul, kategori, penulis, tahun, jlh_cetakan, stok};

                tabmode.addRow(data);

            }
            tabmode.fireTableDataChanged();
            konek.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "gagal menampilkan data! Error: " + e.getMessage());
}

}

    public void combobox(){
        try {
        Connection konek = new koneksi().getKoneksi();
        st = konek.createStatement();
        String sql = "SELECT nama from kategori";
        ResultSet hasil = st.executeQuery(sql);
        while (hasil.next()) {
            Object[] obj = new Object[1];
            obj[0] = hasil.getString(1);
            txtKategori.addItem((String) obj[0]);
        }
        hasil.close(); st.close();
        } catch (SQLException e) {
            e.printStackTrace();
          JOptionPane.showMessageDialog(null, "gagal menampilkan combobox! Error: " + e.getMessage());

        }
    }
    
     public void reset(){
       txtKdBuku.setText("");
       txtJdlBuku.setText("");
       txtKategori.setSelectedIndex(0);
       txtPenulis.setText("");
       txtThnTerbit.setText("");
       txtStkBuku.setText("");
       txtJlhCetakan.setText("");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtKdBuku = new javax.swing.JTextField();
        txtJdlBuku = new javax.swing.JTextField();
        txtPenulis = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtThnTerbit = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtJlhCetakan = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtStkBuku = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnHapus = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtKategori = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(101, 40, 247));
        jPanel1.setMaximumSize(new java.awt.Dimension(573, 40));
        jPanel1.setMinimumSize(new java.awt.Dimension(573, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(573, 40));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 828, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 828, 40);

        jPanel2.setBackground(new java.awt.Color(160, 118, 249));
        jPanel2.setPreferredSize(new java.awt.Dimension(828, 360));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(237, 228, 255));
        jLabel6.setText("Jumlah Cetakan");

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setFont(new java.awt.Font("Poppins SemiBold", 0, 10)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(237, 228, 255));
        jLabel7.setText("Stok Buku");

        txtJdlBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJdlBukuActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(237, 228, 255));
        jLabel1.setText("Kode Buku");

        txtThnTerbit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtThnTerbitActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(237, 228, 255));
        jLabel2.setText("Judul Buku");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(237, 228, 255));
        jLabel3.setText("ID Kategori");

        txtStkBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStkBukuActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(237, 228, 255));
        jLabel4.setText("Penulis");

        btnSimpan.setBackground(new java.awt.Color(0, 153, 51));
        btnSimpan.setFont(new java.awt.Font("Poppins SemiBold", 0, 10)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(237, 228, 255));
        jLabel5.setText("Tahun Terbit");

        btnHapus.setBackground(new java.awt.Color(153, 51, 0));
        btnHapus.setFont(new java.awt.Font("Poppins SemiBold", 0, 10)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins SemiBold", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(237, 228, 255));
        jLabel8.setText("Form Pengisian Data Buku");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode Buku", "Judul Buku", "ID Kategori", "Penulis", "Tahun Terbit", "Jumlah Cetakan", "Stok Buku"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        txtKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKategoriActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(102, 204, 255));
        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cetak");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(245, 245, 245))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 699, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtPenulis, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3)
                                        .addGap(89, 89, 89)
                                        .addComponent(txtKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtStkBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtJdlBuku, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(txtKdBuku))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtThnTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtJlhCetakan, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(64, 64, 64))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(btnEdit)
                .addGap(18, 18, 18)
                .addComponent(btnHapus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKdBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtThnTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtJdlBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJlhCetakan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPenulis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtStkBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(jButton1)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus)
                    .addComponent(jButton2))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 40, 828, 730);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtStkBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStkBukuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStkBukuActionPerformed

    private void txtThnTerbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtThnTerbitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThnTerbitActionPerformed

    private void txtJdlBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJdlBukuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJdlBukuActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        try {
             Connection konek = new koneksi().getKoneksi();
            st = konek.createStatement();
            String getKategori = this.txtKategori.getSelectedItem().toString();
   
            String query = "SELECT id FROM kategori WHERE nama = '"+getKategori+"'";
            ResultSet hasil = st.executeQuery(query);
            
            int kategori_id = 0;
            if(hasil.next()){
                kategori_id = hasil.getInt("id");
            }
            
            String kd_buku = this.txtKdBuku.getText();
            String judul = this.txtJdlBuku.getText();
            String kategori = this.txtKategori.getSelectedItem().toString();
            String penulis = this.txtPenulis.getText();
            String tahun_terbit = this.txtThnTerbit.getText();
            String jlh_cetakan = this.txtJlhCetakan.getText();
            String stok = this.txtStkBuku.getText();
            
             String insert = "INSERT INTO buku values ('"+kd_buku+"','"+judul+"','"+kategori_id+"','"
                     +penulis+"','"+tahun_terbit+"','"+jlh_cetakan+"','"+stok+"')";
             st.executeUpdate(insert);
             reset();
             tampilTable();
             
            JOptionPane.showMessageDialog(null, "Buku Berhasil Ditambahkan!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "gagal menambahkan buku, Error : "+e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       reset();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        txtKdBuku.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
        txtKategori.setSelectedItem(jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString());
        txtJdlBuku.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
        txtPenulis.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString());
        txtThnTerbit.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString());
        txtJlhCetakan.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString());
        txtStkBuku.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString());
        
         txtKdBuku.setEditable(false);
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        String kd_buku = this.txtKdBuku.getText();
        if(kd_buku.isEmpty()){
            JOptionPane.showMessageDialog(this, "silahkan pilih data terlebih dahulu!");
        }else{
            int jawaban = JOptionPane.showConfirmDialog(null, "apakah anda yakin ingin menghapus ? ");
            if(jawaban == 0){
                try {
                   Connection konek = new koneksi().getKoneksi();
                   st = konek.createStatement();
                   String sql = "DELETE FROM BUKU WHERE kd_buku ='"+kd_buku+"';";
                   st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Buku berhasil dihapus!");
                    reset();
                    tampilTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Buku gagal dihapus!, error"+e.getMessage());
                }
            }
        }
       
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
          String kd_buku = this.txtKdBuku.getText();
          String judul = this.txtJdlBuku.getText();
          String penulis = this.txtPenulis.getText();
          String tahun_terbit = this.txtThnTerbit.getText();
          String jlh_cetakan = this.txtJlhCetakan.getText();
          String stok = this.txtStkBuku.getText();
        if(kd_buku.isEmpty()){
            JOptionPane.showMessageDialog(this, "silahkan pilih data terlebih dahulu!");
        }else{
            int jawaban = JOptionPane.showConfirmDialog(null, "apakah anda yakin ingin Mengedit Data ? ");
            if(jawaban == 0){
                try {
                   Connection konek = new koneksi().getKoneksi();
                    st = konek.createStatement();
                    String getKategori = this.txtKategori.getSelectedItem().toString();
   
                    String query = "SELECT id FROM kategori WHERE nama = '"+getKategori+"'";
                    ResultSet hasil = st.executeQuery(query);

                    int kategori_id = 0;
                    if(hasil.next()){
                        kategori_id = hasil.getInt("id");
                    }
                   
                   String sql = "UPDATE buku SET judul = '"+judul+"', penulis = '"+penulis+"', "
                           + "kategori_id = '"+kategori_id+"', stok = '"+stok+"', tahun_terbit = '"+tahun_terbit+"', "
                           + "jlh_cetakan = '"+jlh_cetakan+"' "
                           + "WHERE kd_buku ='"+kd_buku+"';";
                   st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Buku berhasil diedit!");
                    reset();
                    tampilTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Buku gagal diedit!, error"+e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void txtKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKategoriActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    try {
        createPDF();
    } catch (SQLException ex) {
        Logger.getLogger(FormData.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormData().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtJdlBuku;
    private javax.swing.JTextField txtJlhCetakan;
    private javax.swing.JComboBox<String> txtKategori;
    private javax.swing.JTextField txtKdBuku;
    private javax.swing.JTextField txtPenulis;
    private javax.swing.JTextField txtStkBuku;
    private javax.swing.JTextField txtThnTerbit;
    // End of variables declaration//GEN-END:variables
}

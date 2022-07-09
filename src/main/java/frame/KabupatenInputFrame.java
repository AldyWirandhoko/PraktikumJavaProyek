package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KabupatenInputFrame extends JFrame{

    public KabupatenInputFrame(){
        kustomisasiKomponen();
        batalButton.addActionListener(e -> {
            dispose();
        });
        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            String tanggalMulai = tanggalMulaiDatePicker.getText();
            if (tanggalMulai.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Tanggal Mulai",
                        "Validasi Data Kosong",JOptionPane.WARNING_MESSAGE);
                tanggalMulaiDatePicker.requestFocus();
                return;
            }
            if(nama.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Kabupaten",
                        "Validasi data kosong", JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String cekSQL = "SELECT * FROM kabupaten WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    } else {
                        String insertSQL = "INSERT INTO kabupaten (id, nama, tanggalmulai) VALUES (NULL, ?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setString(2, tanggalMulai);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String cekSQL = "SELECT * FROM kabupaten WHERE nama = ? AND id != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    } else {
                        String updateSQL = "UPDATE kabupaten SET nama = ?, tanggalmulai = ? WHERE id = ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setString(2, tanggalMulai);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                        dispose();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        kustomisasiKomponen();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Kabupaten");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen() {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM kabupaten WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                tanggalMulaiDatePicker.setText(rs.getString("tanggalmulai"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void kustomisasiKomponen() {
        DatePickerSettings dps = new DatePickerSettings();
        dps.setFormatForDatesCommonEra("dd-MM-yyyy");
        tanggalMulaiDatePicker.setSettings(dps);
    }
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JPanel buttonPanel;
    private JButton batalButton;

    private int id;
    public void setId(int id) {
        this.id = id;
    }
    private JButton simpanButton;
    private DatePicker tanggalMulaiDatePicker;
}

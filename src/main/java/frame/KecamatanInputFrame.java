package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class KecamatanInputFrame extends JFrame{

    public KecamatanInputFrame(){
        batalButton.addActionListener(e -> {
            dispose();
        });
        kustomisasiKomponen();
        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            if (populasiTextField.getText().equals("")){
                populasiTextField.setText("0");
            }
            int populasi = Integer.parseInt(populasiTextField.getText());
            if(populasi == 0){
                JOptionPane.showMessageDialog(null,
                        "Isi Populasi",
                        "Validasi Data Kosong", JOptionPane.WARNING_MESSAGE);
                populasiTextField.requestFocus();
                return;
            }

            if(luasTextField.getText().equals("")){
                luasTextField.setText("0");
            }
            double luas = Float.parseFloat(luasTextField.getText());
            if(luas == 0){
                JOptionPane.showMessageDialog(null,
                        "Isi Luas",
                        "Validasi Data Kosong",JOptionPane.WARNING_MESSAGE);
                luasTextField.requestFocus();
                return;
            }
            if(nama.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Kecamatan",
                        "Validasi data kosong", JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }
            ComboBoxItem item = (ComboBoxItem) kabupatenComboBox.getSelectedItem();
            int kabupatenId = item.getValue();
            if(kabupatenId == 0){
                JOptionPane.showMessageDialog(null,
                        "Pilih kabupaten",
                        "Validasi Combobox",JOptionPane.WARNING_MESSAGE);
                kabupatenComboBox.requestFocus();
                return;
            }
            String klasifikasi = "";
            if(tipeARadioButton.isSelected()){
                klasifikasi = "Tipe A";
            } else if (tipeBRadioButton.isSelected()){
                klasifikasi = "Tipe B";
            } else {
                JOptionPane.showMessageDialog(null,
                        "Pilih klasifikasi",
                        "Validasi Data Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String cekSQL = "SELECT * FROM kecamatan WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    } else {
                        String insertSQL = "INSERT INTO kecamatan (id, nama, kabupaten_id, klasifikasi, " +
                                "populasi, luas) " +
                                "VALUES (NULL, ?, ?, ?, ?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, kabupatenId);
                        ps.setString(3, klasifikasi);
                        ps.setInt(4, populasi);
                        ps.setDouble(5, luas);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String cekSQL = "SELECT * FROM kecamatan WHERE nama = ? AND id != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    } else {
                        String updateSQL = "UPDATE kecamatan SET nama = ?, kabupaten_id = ?, klasifikasi = ?, " +
                                "populasi = ?, luas = ? WHERE id = ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, kabupatenId);
                        ps.setString(3, klasifikasi);
                        ps.setInt(4, populasi);
                        ps.setDouble(5, luas);
                        ps.setInt(6, id);
                        ps.executeUpdate();
                        dispose();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Kecamatan");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen() {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM kecamatan WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            populasiTextField.setText(String.valueOf(rs.getInt("populasi")));
            luasTextField.setText(String.valueOf(rs.getDouble("luas")));
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                int kabupatenId = rs.getInt("kabupaten_id");
                for (int i = 0; i < kabupatenComboBox.getItemCount(); i++){
                    kabupatenComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) kabupatenComboBox.getSelectedItem();
                    if(kabupatenId == item.getValue()){
                        break;
                    }
                }
                String klasifikasi = rs.getString("klasifikasi");
                if(klasifikasi != null){
                    if(klasifikasi.equals("TIPE A")){
                        tipeARadioButton.setSelected(true);
                    } else if(klasifikasi.equals("TIPE B")) {
                        tipeBRadioButton.setSelected(true);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void kustomisasiKomponen() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM kabupaten ORDER BY nama";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            kabupatenComboBox.addItem(new ComboBoxItem(0, "Pilih kabupaten"));
            while (rs.next()) {
                kabupatenComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("nama")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        klasifikasiButtonGroup = new ButtonGroup();
        klasifikasiButtonGroup.add(tipeARadioButton);
        klasifikasiButtonGroup.add(tipeBRadioButton);

        luasLabel.setText("Luas (Km\u00B2)");
        populasiTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        populasiTextField.setText("0");
        populasiTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                populasiTextField.setEditable(
                        (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') ||
                                e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                                e.getKeyCode() == KeyEvent.VK_LEFT ||
                                e.getKeyCode() == KeyEvent.VK_RIGHT
                        );
            }
        });

        luasTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        luasTextField.setText("0");
        luasTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            luasTextField.setEditable(
                    (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') ||
                            e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                            e.getKeyCode() == KeyEvent.VK_LEFT ||
                            e.getKeyCode() == KeyEvent.VK_RIGHT ||
                            e.getKeyCode() == KeyEvent.VK_PERIOD
            );
            }
        });
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
    private JComboBox kabupatenComboBox;
    private JRadioButton tipeARadioButton;
    private JRadioButton tipeBRadioButton;
    private JTextField populasiTextField;
    private JTextField luasTextField;
    private JLabel luasLabel;
    private ButtonGroup klasifikasiButtonGroup;
}

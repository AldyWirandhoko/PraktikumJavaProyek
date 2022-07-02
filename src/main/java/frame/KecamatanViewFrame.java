package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;


public class KecamatanViewFrame extends JFrame{
    public KecamatanViewFrame(){
        tambahButton.addActionListener(e -> {
            KecamatanInputFrame inputFrame = new KecamatanInputFrame();
            inputFrame.setVisible(true);
        });
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        cariButton.addActionListener(e -> {
            if(cariTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian",
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String searchSQl = "SELECT K.*,B.nama AS nama_kabupaten " +
                    "FROM kecamatan K " +
                    "LEFT JOIN kabupaten B ON K.kabupaten_id = B.id " +
                    "WHERE K.nama like ? OR B.nama like ?";
            try {
                PreparedStatement ps =c.prepareStatement(searchSQl);
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm =(DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[3];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    row[2] = rs.getString("nama_kabupaten");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(null,"Pilih data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(null,
                    "Yakin mau hapus?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if(pilihan == 0){
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM kecamatan WHERE id = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ubahButton.addActionListener(e ->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0 ).toString());
            KecamatanInputFrame inputFrame = new KecamatanInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    private void init() {
        setContentPane(mainPanel);
        setTitle("Data Kecamatan");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    public void isiTable(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT K.*,B.nama AS nama_kabupaten FROM kecamatan K " +
                "LEFT JOIN kabupaten B ON K.kabupaten_id = B.id";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String[] header  = {"Id", "Nama Kecamatan", "Nama Kabupaten"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            Object[] row = new Object[3];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("nama_kabupaten");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;
}

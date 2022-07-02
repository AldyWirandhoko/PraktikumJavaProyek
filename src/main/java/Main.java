import frame.KecamatanViewFrame;
import frame.KabupatenViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
//        Koneksi.getConnection();
//        KabupatenViewFrame viewFrame = new KabupatenViewFrame();
        KecamatanViewFrame viewFrame = new KecamatanViewFrame();
        viewFrame.setVisible(true);
    }
}

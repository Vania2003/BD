import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class przegladPosilkow {
    private JButton cofnijButton;
    private JPanel przegladPanel;
    private JButton wyswietlPrzepisyButton;
    private JTextArea wyswietlonePosilki;


    public przegladPosilkow(int id_uzytkownika, int id_planu){
        cofnijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cofnij(id_uzytkownika, id_planu);
            }
        });
        wyswietlPrzepisyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wyswietlPrzepisy();
            }
        });

    }


    private void wyswietlPrzepisy() {
        try {
            String url = "jdbc:mysql://localhost:3306/root";
            String user = "root";
            String pass = "";

            Connection conn = DriverManager.getConnection(url, user, pass);

            String selectPrzepisySQL = "SELECT nazwa_posilku, kcal_posilku FROM Posilki";

            try (PreparedStatement selectPrzepisyStmt = conn.prepareStatement(selectPrzepisySQL)) {
                ResultSet rs = selectPrzepisyStmt.executeQuery();

                StringBuilder posilkiStringBuilder = new StringBuilder();

                while (rs.next()) {
                    String nazwaPosilku = rs.getString("nazwa_posilku");
                    int kcalPosilku = rs.getInt("kcal_posilku");

                    posilkiStringBuilder.setLength(0); // Wyczyść StringBuilder przed każdym nowym posilkiem
                    posilkiStringBuilder.append("Nazwa: ").append(nazwaPosilku).append(", Kcal: ").append(kcalPosilku).append("\n");
                    wyswietlonePosilki.append(String.valueOf(posilkiStringBuilder));

                    // Dodaj składniki
                    dodajSkladnikiDoPrzepisu(conn, nazwaPosilku);
                }

                //wyswietlonePosilki.setText(posilkiStringBuilder.toString());
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dodajSkladnikiDoPrzepisu(Connection conn, String nazwaPosilku) {
        try {
            String selectSkladnikiSQL = "SELECT nazwa_skladnika, kcal FROM Skladniki " +
                    "JOIN Posilki_Skladniki ON Skladniki.id_skladnika = Posilki_Skladniki.Skladnikiid_skladnika " +
                    "JOIN Posilki ON Posilki_Skladniki.Posilkiid_posilku = Posilki.id_posilku " +
                    "WHERE Posilki.nazwa_posilku = ?";

            try (PreparedStatement selectSkladnikiStmt = conn.prepareStatement(selectSkladnikiSQL)) {
                selectSkladnikiStmt.setString(1, nazwaPosilku);
                ResultSet skladnikiRs = selectSkladnikiStmt.executeQuery();

                StringBuilder skladnikiStringBuilder = new StringBuilder();

                while (skladnikiRs.next()) {
                    String nazwaSkladnika = skladnikiRs.getString("nazwa_skladnika");
                    int kcalSkladnika = skladnikiRs.getInt("kcal");
                    skladnikiStringBuilder.append("\t- Składnik: ").append(nazwaSkladnika).append(", Kcal: ").append(kcalSkladnika).append("\n");
                }
                wyswietlonePosilki.append(skladnikiStringBuilder.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JPanel getPanel() {
        return przegladPanel;
    }

    private void cofnij(int id_uzytkownika, int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Menu");
        menu ekran = new menu(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class zmianaPlanu {


    private JPanel zmianaPlanuPanel;
    private JButton button2;
    private JButton zapiszButton;
    private JTextPane doUsuniecia;
    private JTextPane doDodania;

    String url = "jdbc:mysql://localhost:3306/root";
    String user = "root";
    String pass = "";

    public zmianaPlanu(int id_uzytkownika, int id_planu){
        zapiszButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zapisz(id_uzytkownika, id_planu);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cofnij(id_uzytkownika, id_planu);
            }
        });

    }
    private void zapisz(int id_uzytkownika, int id_planu) {
        String posilekDoUsuniecia = doUsuniecia.getText().trim();
        String posilekDoDodania = doDodania.getText().trim();

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            conn.setAutoCommit(false); // Włącz tryb transakcyjny

            try {
                // Usuń posilekDoUsuniecia z planu
                String usunPosilekSQL = "DELETE FROM Posilki_Plan_posilkow WHERE Posilkiid_posilku IN " +
                        "(SELECT id_posilku FROM Posilki WHERE nazwa_posilku = ?)";
                try (PreparedStatement usunPosilekStmt = conn.prepareStatement(usunPosilekSQL)) {
                    usunPosilekStmt.setString(1, posilekDoUsuniecia);
                    usunPosilekStmt.executeUpdate();
                }

                // Dodaj posilekDoDodania do planu
                String dodajPosilekSQL = "INSERT INTO Posilki_Plan_posilkow (Posilkiid_posilku, Plan_posilkowid_planu) " +
                        "VALUES ((SELECT id_posilku FROM Posilki WHERE nazwa_posilku = ?), ?)";
                try (PreparedStatement dodajPosilekStmt = conn.prepareStatement(dodajPosilekSQL)) {
                    dodajPosilekStmt.setString(1, posilekDoDodania);
                    // Tutaj podstaw odpowiednie ID planu
                    dodajPosilekStmt.setInt(2, id_uzytkownika); // Przykładowe ID planu (do zmiany)
                    dodajPosilekStmt.executeUpdate();
                }

                conn.commit(); // Potwierdź transakcję
            } catch (SQLException ex) {
                conn.rollback(); // Wycofaj transakcję w razie błędu
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cofnij(int id_uzytkownika, int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        frame.dispose();
        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Personalizacja");
        personalizacja ekran = new personalizacja(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
    public JPanel getPanel() {
        return zmianaPlanuPanel;
    }
}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ekranGlowny {
    private JPanel ekranGlownyPanel;
    private JLabel PlanPosilkow;
    private JLabel planLabel;
    private JButton wyswietlButton;
    private JButton cofnijButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    String url = "jdbc:mysql://localhost:3306/root";
    String user = "root";
    String pass = "";
    ekranGlowny(int id_uzytkownika, int id_planu){
        wyswietlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wyswietlPlan(id_uzytkownika, id_planu);
            }
        });
        cofnijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cofnij(id_uzytkownika, id_planu);
            }
        });

    }
    public void wyswietlPlan(int id_uzytkownika, int id_planu) {

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // Pobierz id_planu dla konkretnego użytkownika
            String selectIdPlanuSQL = "SELECT id_planu FROM Uzytkownik WHERE id_uzytkownika = ?";
            try (PreparedStatement selectIdPlanuStmt = conn.prepareStatement(selectIdPlanuSQL)) {
                selectIdPlanuStmt.setInt(1, id_uzytkownika);

                try (ResultSet resultSet = selectIdPlanuStmt.executeQuery()) {
                    if (resultSet.next()) {
                        wyswietlNazwePlanu(conn, id_planu);
                    } else {
                        System.out.println("Brak id_planu dla użytkownika o id_uzytkownika: " + id_uzytkownika);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void wyswietlNazwePlanu(Connection conn, int id_planu) throws SQLException {
        // Pobierz nazwę planu na podstawie id_planu
        String selectNazwaPlanuSQL = "SELECT nazwa_planu FROM Plan_posilkow WHERE id_planu = ?";
        try (PreparedStatement selectNazwaPlanuStmt = conn.prepareStatement(selectNazwaPlanuSQL)) {
            selectNazwaPlanuStmt.setInt(1, id_planu);

            try (ResultSet resultSet = selectNazwaPlanuStmt.executeQuery()) {
                if (resultSet.next()) {
                    // Wypisz nazwę planu
                    String nazwaPlanu = resultSet.getString("nazwa_planu");
                    textArea1.setText(nazwaPlanu);
                    // Pobierz posiłki przypisane do planu
                    String posilkiSQL = "SELECT Posilki.id_posilku, Posilki.nazwa_posilku, Posilki.kcal_posilku FROM Posilki " +
                            "JOIN Posilki_Plan_posilkow ON Posilki.id_posilku = Posilki_Plan_posilkow.Posilkiid_posilku " +
                            "WHERE Posilki_Plan_posilkow.Plan_posilkowid_planu = ?";
                    try (PreparedStatement posilkiStmt = conn.prepareStatement(posilkiSQL)) {
                        posilkiStmt.setInt(1, id_planu);

                        try (ResultSet posilkiResultSet = posilkiStmt.executeQuery()) {
                            StringBuilder posilkiStringBuilder = new StringBuilder();
                            while (posilkiResultSet.next()) {
                                String nazwaPosilku = posilkiResultSet.getString("nazwa_posilku");
                                int kcalPosilku = posilkiResultSet.getInt("kcal_posilku");
                                posilkiStringBuilder.append(" - Posiłek: ").append(nazwaPosilku)
                                        .append(", kcal: ").append(kcalPosilku).append("\n");
                                wyswietlSkladniki(posilkiResultSet.getInt("id_posilku"));

                            }
                            textArea1.setText(posilkiStringBuilder.toString());
                            System.out.println(posilkiStringBuilder.toString());

                        }
                    }
                } else {
                    textArea1.setText("Brak planu o id_planu: " + id_planu);

                }
            }
        }
    }
    public void wyswietlSkladniki(int id_posilku) {


        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String skladnikiSQL = "SELECT Skladniki.nazwa_skladnika, Skladniki.kcal FROM Skladniki " +
                    "JOIN Posilki_Skladniki ON Skladniki.id_skladnika = Posilki_Skladniki.Skladnikiid_skladnika " +
                    "WHERE Posilki_Skladniki.Posilkiid_posilku = ?";
            try (PreparedStatement skladnikiStmt = conn.prepareStatement(skladnikiSQL)) {
                skladnikiStmt.setInt(1, id_posilku);

                try (ResultSet skladnikiResultSet = skladnikiStmt.executeQuery()) {
                    StringBuilder skladnikiStringBuilder = new StringBuilder();
                    while (skladnikiResultSet.next()) {
                        String nazwaSkladnika = skladnikiResultSet.getString("nazwa_skladnika");
                        int kcalSkladnika = skladnikiResultSet.getInt("kcal");
                        skladnikiStringBuilder.append("- Składnik: ").append(nazwaSkladnika)
                                .append(", kcal: ").append(kcalSkladnika).append("\n");
                        textArea2.append(skladnikiStringBuilder.toString());

                    }
                    System.out.println(skladnikiStringBuilder.toString());



                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cofnij(int id_uzytkownika, int id_planu){
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
    public JPanel getPanel() {
        return ekranGlownyPanel;
    }

}

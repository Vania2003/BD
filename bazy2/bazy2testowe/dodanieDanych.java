import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dodanieDanych {


    private JTextPane textPane1;
    private JTextPane textPane2;
    private JTextPane textPane3;
    private JTextPane textPane4;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JButton button1;
    private JButton button2;
    private JPanel dodanieDanychPanel;
    private JComboBox comboBox5;

    String url = "jdbc:mysql://localhost:3306/root";
    String user = "root";
    String pass = "";

    public dodanieDanych(int id_uzytkownika, int id_planu){
        domyslneDane();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zapisz(id_uzytkownika, id_planu);
                obliczIZaktualizujDane(id_uzytkownika, id_planu);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anuluj(id_uzytkownika, id_planu);
            }
        });
    }

    public void zapisz(int id_uzytkownika, int id_planu){
        String wiek = textPane1.getText();
        String plec = textPane2.getText();
        String wzrost = textPane3.getText();
        String waga = textPane4.getText();
        String alergie = (String) comboBox1.getSelectedItem();
        int iloscPosilkow = Integer.parseInt((String) comboBox3.getSelectedItem());
        String cel = (String) comboBox4.getSelectedItem();

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            // Aktualizuj dane użytkownika w tabeli Uzytkownik
            String updatePreferencjeSQL1 = "UPDATE Uzytkownik SET wiek=?, wzrost=?, waga=?, plec=? WHERE id_uzytkownika=?";
            try (PreparedStatement updateUzytkownikStmt = conn.prepareStatement(updatePreferencjeSQL1)) {
                updateUzytkownikStmt.setInt(1, Integer.parseInt(wiek));
                updateUzytkownikStmt.setInt(2, Integer.parseInt(wzrost));
                updateUzytkownikStmt.setInt(3, Integer.parseInt(waga));
                updateUzytkownikStmt.setString(4, plec);
                updateUzytkownikStmt.setInt(5, id_uzytkownika);
                updateUzytkownikStmt.executeUpdate();
            }
            String updatePreferencjeSQL2 = "UPDATE Preferencje SET cel=?, ilosc_posilkow=? WHERE id_preferencji=?";
            try (PreparedStatement updatePreferencjeStmt = conn.prepareStatement(updatePreferencjeSQL2)) {
                updatePreferencjeStmt.setString(1, cel);
                //updatePreferencjeStmt.setString(2, alergie);
                updatePreferencjeStmt.setInt(2, iloscPosilkow);
                updatePreferencjeStmt.setInt(3, id_uzytkownika); // Tutaj wpisz odpowiednie id_preferencji użytkownika
                updatePreferencjeStmt.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void obliczIZaktualizujDane(int id_uzytkownika, int id_planu) {

        String aktywnosc = (String) comboBox5.getSelectedItem();
        int iloscPosilkow = Integer.parseInt((String) comboBox3.getSelectedItem());
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // Pobierz dane użytkownika
            String selectUzytkownikSQL = "SELECT wiek, plec, wzrost, waga FROM Uzytkownik WHERE id_uzytkownika=?";
            try (PreparedStatement selectUzytkownikStmt = conn.prepareStatement(selectUzytkownikSQL)) {
                selectUzytkownikStmt.setInt(1, id_uzytkownika);

                try (ResultSet uzytkownikResultSet = selectUzytkownikStmt.executeQuery()) {
                    if (uzytkownikResultSet.next()) {
                        int wiek = uzytkownikResultSet.getInt("wiek");
                        String plec = uzytkownikResultSet.getString("plec");
                        int wzrost = uzytkownikResultSet.getInt("wzrost");
                        int waga = uzytkownikResultSet.getInt("waga");

                        // Oblicz zapotrzebowanie kaloryczne
                        double zapotrzebowanieKcal = obliczZapotrzebowanieKcal(wiek, plec, wzrost, waga, aktywnosc);

                        // Aktualizuj dane preferencyjne
                        String updatePreferencjeSQL = "UPDATE Preferencje SET cel=?, ilosc_posilkow=? WHERE id_preferencji=?";
                        try (PreparedStatement updatePreferencjeStmt = conn.prepareStatement(updatePreferencjeSQL)) {
                            // Tutaj ustaw odpowiednie wartości dla celu, ilości posiłków i id_preferencji
                            updatePreferencjeStmt.setString(1, "Cel");
                            updatePreferencjeStmt.setInt(2, 3); // Przykładowa ilość posiłków
                            updatePreferencjeStmt.setInt(3, id_uzytkownika);

                            updatePreferencjeStmt.executeUpdate();
                        }
                        List<Integer> wybranePosilki = wybierzPosilki(conn, iloscPosilkow, zapotrzebowanieKcal);
                        aktualizujPlanPosilkow(conn, id_planu, wybranePosilki);


                        System.out.println("Zapotrzebowanie kaloryczne: " + zapotrzebowanieKcal + " kcal");
                    } else {
                        System.out.println("Nie znaleziono użytkownika o ID: " + id_uzytkownika);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private List<Integer> wybierzPosilki(Connection conn, int iloscPosilkow, double zapotrzebowanieKcal) throws SQLException {
        List<Integer> posilki = new ArrayList<>();

        // Przykładowe zapytanie - dostosuj je do swojej bazy danych
        String selectPosilkiSQL = "SELECT id_posilku FROM Posilki WHERE kcal_posilku <= ? LIMIT ?";
        try (PreparedStatement selectPosilkiStmt = conn.prepareStatement(selectPosilkiSQL)) {
            selectPosilkiStmt.setDouble(1, zapotrzebowanieKcal);
            selectPosilkiStmt.setInt(2, iloscPosilkow);

            try (ResultSet rs = selectPosilkiStmt.executeQuery()) {
                while (rs.next()) {
                    int idPosilku = rs.getInt("id_posilku");
                    posilki.add(idPosilku);
                }
            }
        }

        return posilki;
    }

    public void aktualizujPlanPosilkow(Connection conn, int id_planu, List<Integer> nowePosilki) throws SQLException {
        // Usuń wszystkie posiłki danego planu
        String deletePosilkiSQL = "DELETE FROM Posilki_Plan_posilkow WHERE Plan_posilkowid_planu = ?";
        try (PreparedStatement deletePosilkiStmt = conn.prepareStatement(deletePosilkiSQL)) {
            deletePosilkiStmt.setInt(1, id_planu);
            deletePosilkiStmt.executeUpdate();
        }

        // Dodaj nowe posiłki do planu
        String insertPosilkiSQL = "INSERT INTO Posilki_Plan_posilkow (Posilkiid_posilku, Plan_posilkowid_planu) VALUES (?, ?)";
        try (PreparedStatement insertPosilkiStmt = conn.prepareStatement(insertPosilkiSQL)) {
            for (int id_posilku : nowePosilki) {
                insertPosilkiStmt.setInt(1, id_posilku);
                insertPosilkiStmt.setInt(2, id_planu);
                insertPosilkiStmt.executeUpdate();
            }
        }
    }
    public static double obliczZapotrzebowanieKcal(int wiek, String plec, int wzrost, int waga, String aktywnosc) {
        double wspolczynnikKcal = (plec.equalsIgnoreCase("M") ? 1.0 : 0.9); // Dla mężczyzn 1.0, dla kobiet 0.9
        double wspolczynnikAktywnosci = 0;

        switch (aktywnosc) {
            case "niska":
                wspolczynnikAktywnosci = 1.2;
                break;
            case "umiarkowana":
                wspolczynnikAktywnosci = 1.55;
                break;
            case "wysoka":
                wspolczynnikAktywnosci = 1.9;
                break;
            // Możesz dostosować obsługę innych poziomów aktywności według potrzeb
        }

        // Wzór Harris-Benedicta
        return ((10 * waga) + (6.25 * wzrost) - (5 * wiek) + 5) * wspolczynnikAktywnosci * wspolczynnikKcal;
    }
    public void domyslneDane() {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            int id_uzytkownika = 1; // Podstaw odpowiednie id_uzytkownika

            // Pobierz dane użytkownika z bazy
            String selectDaneSQL = "SELECT wiek, plec, wzrost, waga FROM Uzytkownik WHERE id_uzytkownika=?";
            try (PreparedStatement selectDaneStmt = conn.prepareStatement(selectDaneSQL)) {
                selectDaneStmt.setInt(1, id_uzytkownika);

                try (ResultSet rs = selectDaneStmt.executeQuery()) {
                    if (rs.next()) {
                        // Ustaw dane w textPane
                        textPane1.setText(String.valueOf(rs.getInt("wiek")));
                        textPane2.setText(rs.getString("plec"));
                        textPane3.setText(String.valueOf(rs.getInt("wzrost")));
                        textPane4.setText(String.valueOf(rs.getInt("waga")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void anuluj(int id_uzytkownika, int id_planu){
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
        return dodanieDanychPanel;
    }


}

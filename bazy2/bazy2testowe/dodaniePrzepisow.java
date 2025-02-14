import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dodaniePrzepisow {
    private JPanel dodaniePrzepisowPanel;
    private JTextPane nazwaPrzepisuPane;
    private JTextPane skladnikPane;
    private JTextPane iloscPane;
    private JLabel wartosciLabel;
    private JButton zapiszButton;
    private JButton anulujButton;
    private JButton dodajButton;

    private List<Integer> skladnikiList = new ArrayList<>();
    public dodaniePrzepisow(int id_uzytkownika, int id_planu){
        zapiszButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zapisz();
            }
        });
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cofnij(id_uzytkownika, id_planu);
            }
        });
        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajSkladnik();
            }
        });


    }

    private void zapisz(){
        String url = "jdbc:mysql://localhost:3306/root";
        String user = "root";
        String pass = "";

        String nazwa_posilku = nazwaPrzepisuPane.getText();
        int sumaKcal = 0;
        float sumaTluszcz = 0;
        float sumaBialka = 0;
        float sumaWeglowodany = 0;
        float sumaSol = 0;

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // Oblicz łączne wartości odżywcze
            String[] skladniki = skladnikPane.getText().split(",");
            String[] ilosci = iloscPane.getText().split(",");

            if (skladniki.length == ilosci.length) {
                for (int i = 0; i < skladniki.length; i++) {
                    String nazwaSkladnika = skladniki[i].trim();
                    int iloscSkladnika = Integer.parseInt(ilosci[i].trim());

                    String selectSkladnikSQL = "SELECT kcal, tluszcz, bialka, weglowodany, sol FROM Skladniki WHERE nazwa_skladnika = ?";
                    try (PreparedStatement selectSkladnikStmt = conn.prepareStatement(selectSkladnikSQL)) {
                        selectSkladnikStmt.setString(1, nazwaSkladnika);

                        try (ResultSet skladnikResultSet = selectSkladnikStmt.executeQuery()) {
                            if (skladnikResultSet.next()) {
                                float kcalSkladnika = skladnikResultSet.getFloat("kcal");
                                float tluszczSkladnika = skladnikResultSet.getFloat("tluszcz");
                                float bialkaSkladnika = skladnikResultSet.getFloat("bialka");
                                float weglowodanySkladnika = skladnikResultSet.getFloat("weglowodany");
                                float solSkladnika = skladnikResultSet.getFloat("sol");

                                sumaKcal += kcalSkladnika * iloscSkladnika;
                                sumaTluszcz += tluszczSkladnika * iloscSkladnika;
                                sumaBialka += bialkaSkladnika * iloscSkladnika;
                                sumaWeglowodany += weglowodanySkladnika * iloscSkladnika;
                                sumaSol += solSkladnika * iloscSkladnika;
                            } else {
                                wartosciLabel.setText("Nie znaleziono składnika o nazwie: " + nazwaSkladnika);
                                return;  // Przerwij, jeżeli nie znaleziono składnika
                            }
                        }
                    }
                }
            } else {
                wartosciLabel.setText("Liczba składników nie zgadza się z liczbą ilości.");
                return;  // Przerwij, jeżeli liczba składników nie zgadza się z liczbą ilości
            }

            // Dodaj przepis z obliczonymi wartościami odżywczymi
            String updatePrzepisSQL = "INSERT INTO Posilki (nazwa_posilku, kcal_posilku, tluszcz_posilku, bialka_posilku, weglowodany_posilku, sol_posilku) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(updatePrzepisSQL, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, nazwa_posilku);
                statement.setInt(2, sumaKcal);
                statement.setFloat(3, sumaTluszcz);
                statement.setFloat(4, sumaBialka);
                statement.setFloat(5, sumaWeglowodany);
                statement.setFloat(6, sumaSol);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    // Jeśli zaktualizowano przepis, wyświetl obliczone wartości
                    String wyniki = "Kcal: " + sumaKcal + "\n"
                            + "Tłuszcz: " + sumaTluszcz + "\n"
                            + "Białko: " + sumaBialka + "\n"
                            + "Węglowodany: " + sumaWeglowodany + "\n"
                            + "Sól: " + sumaSol;
                    wartosciLabel.setText(wyniki);

                    // Pobierz ID utworzonego przepisu
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int idPrzepisu = generatedKeys.getInt(1);

                            // Przypisz składniki do przepisu w tabeli łączącej Posilki_Skladniki
                            for (Integer idSkladnika : skladnikiList) {
                                String insertSkladnikPrzepisSQL = "INSERT INTO Posilki_Skladniki (Posilkiid_posilku, Skladnikiid_skladnika) VALUES (?, ?)";
                                try (PreparedStatement skladnikPrzepisStmt = conn.prepareStatement(insertSkladnikPrzepisSQL)) {
                                    skladnikPrzepisStmt.setInt(1, idPrzepisu);
                                    skladnikPrzepisStmt.setInt(2, idSkladnika);
                                    skladnikPrzepisStmt.executeUpdate();
                                }
                            }
                        }
                    }
                } else {
                    wartosciLabel.setText("Nie udało się zaktualizować przepisu.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private int pobierzIdSkladnikaZBazy(String nazwaSkladnika) {
        String url = "jdbc:mysql://localhost:3306/root";
        String user = "root";
        String pass = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String selectSkladnikSQL = "SELECT id_skladnika FROM Skladniki WHERE nazwa_skladnika = ?";
            try (PreparedStatement selectSkladnikStmt = conn.prepareStatement(selectSkladnikSQL)) {
                selectSkladnikStmt.setString(1, nazwaSkladnika);

                try (ResultSet skladnikResultSet = selectSkladnikStmt.executeQuery()) {
                    if (skladnikResultSet.next()) {
                        return skladnikResultSet.getInt("id_skladnika");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Zwracamy -1, jeżeli nie znaleziono składnika
    }
    private void dodajSkladnik() {
        // Tutaj można dodać logikę do dodawania składników na podstawie wprowadzonych danych
        String nazwaSkladnika = skladnikPane.getText();
        int iloscSkladnika = Integer.parseInt(iloscPane.getText());

        // Pobierz ID składnika z bazy danych
        int idSkladnika = pobierzIdSkladnikaZBazy(nazwaSkladnika);

        if (idSkladnika != -1) {
            // Dodaj ID składnika do listy
            skladnikiList.add(idSkladnika);
            System.out.println("Dodano składnik: " + nazwaSkladnika);
        } else {
            System.out.println("Nie znaleziono składnika o nazwie: " + nazwaSkladnika);
        }

        // Możesz też zaktualizować interfejs graficzny, aby pokazać dodane składniki
        // np. dodając je do jakiegoś pola tekstowego lub listy na ekranie
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
        return dodaniePrzepisowPanel;
    }

}

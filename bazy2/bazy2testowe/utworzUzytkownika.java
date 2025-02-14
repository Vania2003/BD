import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class utworzUzytkownika {

    private JPanel panel2;
    private JTextPane imie;
    private JTextPane nazwisko;
    private JTextPane wiek;
    private JTextPane wzrost;
    private JTextPane waga;
    private JTextPane email;
    private JTextPane haslo;
    private JTextPane plec;
    private JTextPane id_planu;
    private JButton utworzButton;
    public utworzUzytkownika(){
        utworzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(utworzUzytkownik()) {
                    JOptionPane.showMessageDialog(null, "Użytkownik.");
                    wylogujsie();
                } else {
                    JOptionPane.showMessageDialog(null, "Zle.");
                }

            }
        });
    }
    private boolean utworzUzytkownik(){
            String url = "jdbc:mysql://localhost:3306/root";
            String user = "root";
            String pass = "";

            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                int idPlanu = utworzPlan(conn);
                String sql = "INSERT INTO Uzytkownik (imie, nazwisko, wiek, wzrost, waga, email, haslo, plec, id_planu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, getImie());
                    statement.setString(2, getNazwisko());
                    statement.setInt(3, getWiek());
                    statement.setInt(4, getWzrost());
                    statement.setInt(5, getWaga());
                    statement.setString(6, getEmail());
                    statement.setString(7, getHaslo());
                    statement.setString(8, getPlec());
                    statement.setInt(9, idPlanu);


                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        edytujNazwePlanu(conn, idPlanu, getImie(), getNazwisko());

                        return true;
                    }

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        return false;
    }
    private int utworzPlan(Connection conn) throws SQLException {
        // Utwórz plan posilkow
        String insertPlanSQL = "INSERT INTO Plan_posilkow (nazwa_planu) VALUES (?)";
        try (PreparedStatement insertPlanStmt = conn.prepareStatement(insertPlanSQL, Statement.RETURN_GENERATED_KEYS)) {
            insertPlanStmt.setString(1, "Plan uzytkownika:"); // Tutaj wpisz nazwę planu

            int rowsAffected = insertPlanStmt.executeUpdate();

            if (rowsAffected > 0) {
                // Pobierz id_planu utworzonego planu
                try (ResultSet generatedKeys = insertPlanStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }

        return -1; // Zwróć -1 w przypadku błędu
    }

    private boolean edytujNazwePlanu(Connection conn, int idPlanu, String imie, String nazwisko) {
        String nowaNazwaPlanu = "Plan uzytkownika "+imie+" "+nazwisko;

        String updatePlanSQL = "UPDATE Plan_posilkow SET nazwa_planu = ? WHERE id_planu = ?";
        try (PreparedStatement updatePlanStmt = conn.prepareStatement(updatePlanSQL)) {
            updatePlanStmt.setString(1, nowaNazwaPlanu);
            updatePlanStmt.setInt(2, idPlanu);

            int rowsAffected = updatePlanStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void wylogujsie(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Logowanie");
        logowanie ekran = new logowanie();
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
    private String getImie() {
        return imie.getText();
    }

    private String getNazwisko() {
        return nazwisko.getText();
    }

    private int getWiek() {
        return Integer.parseInt(wiek.getText());
    }

    private int getWzrost() {
        return Integer.parseInt(wzrost.getText());
    }

    private int getWaga() {
        return Integer.parseInt(waga.getText());
    }

    private String getEmail() {
        return email.getText();
    }

    private String getHaslo() {
        return haslo.getText();
    }

    private String getPlec() {
        return plec.getText();
    }

    private int getIdPlanu() {
        return Integer.parseInt(id_planu.getText());
    }

    public JPanel getPanel() {
        return panel2;
    }
}

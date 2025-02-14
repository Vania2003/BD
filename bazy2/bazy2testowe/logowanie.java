import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class logowanie {

    private JButton zalogujButton;
    private JPanel panel1;
    private JTextField hasloField;
    private JTextField loginField;
    private JButton utworzButton;

    private int id_uzytkownika, id_planu;
    public logowanie() {
        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getUsername();
                String password = getPassword();

                if (sprawdzHasloWBazie(username, password)) {
                    JOptionPane.showMessageDialog(null, "Zalogowano jako: " + username);
                    menu(getID(), getPlanID());
                    System.out.println(getPlanID());
                }
            }
        });

        utworzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                utworzUzytkownika();
            }
        });
    }


    public JPanel getPanel() {
        return panel1;
    }

    private void setUserID(int id) {
        this.id_uzytkownika = id;
    }

    public int getID() {
        return id_uzytkownika;
    }
    private void setPlanID(int id) {
        this.id_planu = id;
    }

    // Metoda do pobierania ID posiłku
    public int getPlanID() {
        return id_planu;
    }
    public String getUsername() {
        return loginField.getText();
    }

    public String getPassword() {
        return hasloField.getText();
    }

    private boolean sprawdzHasloWBazie(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/root";
        String user = "root";
        String pass = "Haslo123.";

            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String sql = "SELECT id_uzytkownika, id_planu FROM Uzytkownik WHERE email = ? AND haslo = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // Zalogowano pomyślnie - ustaw ID użytkownika
                    int userID = resultSet.getInt("id_uzytkownika");
                    setUserID(userID);
                    // Dodaj kod do pobrania id_posilku i ustawienia go
                    int planID = resultSet.getInt("id_planu");
                    setPlanID(planID);
                    return true;
                } else {
                    // Nie znaleziono pasujących danych
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    private void menu(int id_uzytkownika,int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Menu");
        menu Menu = new menu(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(Menu.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
    private void utworzUzytkownika() {
        utworzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Zamknij bieżące okno
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
                frame.dispose();

                // Otwórz nowe okno
                JFrame utworzFrame = new JFrame("Utwórz Uzytkownika");
                utworzUzytkownika utworzUzyt = new utworzUzytkownika();
                utworzFrame.setContentPane(utworzUzyt.getPanel());
                utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                utworzFrame.pack();
                utworzFrame.setVisible(true);
            }
        });
    }

}

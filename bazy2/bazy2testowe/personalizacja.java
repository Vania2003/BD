import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class personalizacja {


    private JPanel personalizacjaPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton cofnijButton;

    public personalizacja(int id_uzytkownika, int id_planu){
        cofnijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cofnij(id_uzytkownika, id_planu);
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodanieDanych(id_uzytkownika, id_planu);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodaniePrzepisow(id_uzytkownika, id_planu);
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zmianaPlanu(id_uzytkownika, id_planu);
            }
        });

    }
    private void dodanieDanych(int id_uzytkownika, int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Dodanie danych uzytkownika");
        dodanieDanych ekran = new dodanieDanych(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
    private void dodaniePrzepisow(int id_uzytkownika, int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Dodanie wlasnych przepisow");
        dodaniePrzepisow ekran = new dodaniePrzepisow(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
    private void zmianaPlanu(int id_uzytkownika, int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Zmiana planu posilkow");
        zmianaPlanu ekran = new zmianaPlanu(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
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
    public JPanel getPanel() {
        return personalizacjaPanel;
    }
}

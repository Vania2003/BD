import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menu {
    private JButton ekranGlownyButton;
    private JPanel menuPanel;
    private JButton personalizacjaButton;
    private JButton wprowadzenieDanychOPosilkuButton;
    private JButton przegladPosilkowButton;
    private JButton monitorowaniePostepowButton;
    private JButton wylogujSieButton;

    public menu(int id_uzytkownika, int id_planu){

        ekranGlownyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ekranGlowny(id_uzytkownika, id_planu);
            }
        });
        personalizacjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ekranPersonalizacja(id_uzytkownika, id_planu);
            }
        });
        wylogujSieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wylogujsie();
            }
        });
        przegladPosilkowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ekranPrzeglad(id_uzytkownika, id_planu);
            }
        });
    }

    public JPanel getPanel() {
        return menuPanel;
    }
    private void ekranGlowny(int id_uzytkownika, int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        //frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("EkranGlowny");
        ekranGlowny ekran = new ekranGlowny(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
    private void ekranPersonalizacja(int id_uzytkownika, int id_planu){
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
    private void ekranPrzeglad(int id_uzytkownika, int id_planu){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(getPanel());
        //frame.dispose();

        // Otwórz nowe okno
        JFrame utworzFrame = new JFrame("Przeglad posilkow");
        przegladPosilkow ekran = new przegladPosilkow(id_uzytkownika, id_planu);
        utworzFrame.setContentPane(ekran.getPanel());
        utworzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utworzFrame.pack();
        utworzFrame.setVisible(true);
    }
}

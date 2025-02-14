import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class aplikacja {

	Uzytkownik uzytkownik;
	Posilki posilki;
//	private Skladniki skladniki = new ArrayList<>();
//	private DaneUzytkownika daneUzytkownika = new ArrayList<>();
//	private RodzajDiety rodzajDiety = new ArrayList<>();
//	private PlanPosilkow planPosilkow = new ArrayList<>();

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Logowanie");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				logowanie logowanieUI = new logowanie();
				frame.setContentPane(logowanieUI.getPanel());

				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * 
	 * @param login
	 * @param haslo
	 */
	public void logowanie(String login, String haslo) {
		// TODO - implement aplikacja.logowanie
		throw new UnsupportedOperationException();
	}

	public void utworzUzytkownika() {
		// TODO - implement aplikacja.utworzUzytkownika
		throw new UnsupportedOperationException();
	}

	public void pokazZapotrzebowanie() {
		// TODO - implement aplikacja.pokazZapotrzebowanie
		throw new UnsupportedOperationException();
	}

	public void pokazListeZakupow() {
		// TODO - implement aplikacja.pokazListeZakupow
		throw new UnsupportedOperationException();
	}

	public void pokazPlanPosilkow() {
		// TODO - implement aplikacja.pokazPlanPosilkow
		throw new UnsupportedOperationException();
	}

	public void personalizuj() {
		// TODO - implement aplikacja.personalizuj
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param posilek
	 */
	public void dodajPosilek(Posilki posilek) {
		// TODO - implement aplikacja.dodajPosilek
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param val
	 */
	public void zmienDaneUzytkownika(ArrayList<DaneUzytkownika> val) {
		// TODO - implement aplikacja.zmienDaneUzytkownika
		throw new UnsupportedOperationException();
	}

	/**'
	 * '
	 * @param nazwa
	 * @param val
	 * @param rodzaj
	 */
	public Posilki dodajPrzepis(String nazwa, ArrayList<Skladniki> val, RodzajDiety rodzaj) {
		// TODO - implement aplikacja.dodajPrzepis
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param plan
	 */
	public void zmienPlanPosilkow(PlanPosilkow plan) {
		// TODO - implement aplikacja.zmienPlanPosilkow
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param posilek
	 */
	public Posilki dodajSpozytyPosilek(Posilki posilek) {
		// TODO - implement aplikacja.dodajSpozytyPosilek
		throw new UnsupportedOperationException();
	}

	public void wyswietlPrzepisy() {
		// TODO - implement aplikacja.wyswietlPrzepisy
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param posilek
	 */
	public void wyswietlSkladniki(Posilki posilek) {
		// TODO - implement aplikacja.wyswietlSkladniki
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param posilek_usuwany
	 * @param posilek_dodawany
	 */
	public void zmienPosilekNaWybrany(Posilki posilek_usuwany, Posilki posilek_dodawany) {
		// TODO - implement aplikacja.zmienPosilekNaWybrany
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param posilek
	 */
	public void wyswietlWartosci(Posilki posilek) {
		// TODO - implement aplikacja.wyswietlWartosci
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param posilek
	 */
	public void wyswietlZamienniki(Posilki posilek) {
		// TODO - implement aplikacja.wyswietlZamienniki
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param uzytkownik
	 */
	public void wyswietlOsiagniecia(Uzytkownik uzytkownik) {
		// TODO - implement aplikacja.wyswietlOsiagniecia
		throw new UnsupportedOperationException();
	}

}
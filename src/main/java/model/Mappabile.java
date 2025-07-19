package model;

import java.util.Map;

/**
 * Interfaccia per oggetti mappabili, ovvero trasformabili in {@link Map}
 * al fine di comunicarne i dati anche al di fuori dei package che ne
 * hanno accesso, come {@link dao}.
 * Le classi che implementano quest'interfaccia, dovrebbero anche
 * avere un metodo statico per creare un oggetto dalla mappa.
 */
public interface Mappabile {
    /**
     * Trasforma l'oggetto in mappa.
     *
     * @return la mappa dell'oggetto.
     */
    Map<String, Object> aMap();
}

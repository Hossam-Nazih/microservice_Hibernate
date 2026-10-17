package dao;

/**
 * Seconde implémentation du DAO : simule l'appel à un microservice distant.
 * <p>
 * Pour l'activer, il suffit de remplacer dao.DaoImpl1 par dao.DaoImpl2
 * dans config.txt : aucune ligne du métier ni de la présentation ne change.
 */
public class DaoImpl2 implements IDao {

    @Override
    public double getValue() {
        // Ici viendrait normalement un appel HTTP (HttpClient, RestTemplate...)
        System.out.println("[DAO-2] Appel du service distant pour obtenir la valeur...");
        return 50.0;
    }
}

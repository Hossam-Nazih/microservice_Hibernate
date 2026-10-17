package dao;

/**
 * Première implémentation du DAO : simule une base de données (version par défaut).
 * <p>
 * Aucun constructeur n'est déclaré : le constructeur public sans argument
 * implicite est celui qu'utilise Presentation2 pour instancier la classe.
 */
public class DaoImpl1 implements IDao {

    @Override
    public double getValue() {
        // Ici viendrait normalement une requête JDBC/JPA
        System.out.println("[DAO-1] Récupération de la valeur depuis la base de données...");
        return 100.0;
    }
}

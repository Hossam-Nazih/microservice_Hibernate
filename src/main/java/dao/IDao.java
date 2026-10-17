package dao;

/**
 * Contrat de la couche d'accès aux données.
 * <p>
 * Le métier ne manipule que ce type : la source réelle (base SQL, cache,
 * service REST distant...) reste un détail d'implémentation choisi dans config.txt.
 */
public interface IDao {

    /**
     * Fournit la donnée brute utilisée par le calcul métier.
     *
     * @return la valeur lue depuis la source de données
     */
    double getValue();
}

package metier;

import dao.IDao;

/**
 * Implémentation de la logique métier.
 * <p>
 * Couplage faible : la classe connaît uniquement l'interface IDao.
 * L'implémentation concrète lui est fournie de l'extérieur via setDao.
 */
public class MetierImpl implements IMetier {

    // Rempli par le conteneur (Presentation2) après l'instanciation
    private IDao dao;

    /**
     * Point d'injection de la dépendance, appelé par réflexion.
     * Le nom suit la convention set + NomDuChamp utilisée par les frameworks.
     *
     * @param dao l'implémentation de IDao à utiliser
     */
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        // On échoue tôt avec un message clair plutôt qu'avec un NullPointerException
        if (dao == null) {
            throw new IllegalStateException("Le DAO n'a pas été injecté.");
        }

        double valeurBase = dao.getValue();

        // Règle métier du TP : doubler la valeur
        double resultat = valeurBase * 2;

        System.out.println("[METIER] Calcul effectué. Résultat : " + resultat);
        return resultat;
    }
}

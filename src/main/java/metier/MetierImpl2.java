package metier;

import dao.IDao;

/**
 * Seconde implémentation métier : calcul d'un prix TTC.
 * <p>
 * La valeur fournie par le DAO est vue comme un prix hors taxes (HT).
 * On applique la TVA pour obtenir le prix toutes taxes comprises (TTC).
 * Elle s'active en remplaçant metier.MetierImpl par metier.MetierImpl2 dans config.txt,
 * sans toucher au DAO ni à Presentation2.
 */
public class MetierImpl2 implements IMetier {

    // Taux de TVA normal (20 %)
    private static final double TAUX_TVA = 0.20;

    // Rempli par le conteneur (Presentation2) après l'instanciation
    private IDao dao;

    /**
     * Point d'injection de la dépendance, appelé par réflexion.
     * Même signature que dans MetierImpl : le conteneur n'a pas besoin de connaître la classe.
     *
     * @param dao l'implémentation de IDao à utiliser
     */
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        if (dao == null) {
            throw new IllegalStateException("Le DAO n'a pas été injecté.");
        }

        double prixHT = dao.getValue();
        if (prixHT < 0) {
            throw new IllegalArgumentException("Un prix HT ne peut pas être négatif : " + prixHT);
        }

        // Règle métier : TTC = HT + TVA
        double montantTVA = prixHT * TAUX_TVA;
        double prixTTC = prixHT + montantTVA;

        System.out.println("[METIER-2] Prix HT : " + prixHT
                + " | TVA (" + (int) (TAUX_TVA * 100) + " %) : " + montantTVA
                + " | Prix TTC : " + prixTTC);
        return prixTTC;
    }
}

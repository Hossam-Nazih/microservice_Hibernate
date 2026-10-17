package metier;

import dao.IDao;

/**
 * Quatrième implémentation métier : la moyenne arithmético-géométrique (AGM) de Gauss.
 * <p>
 * On part de a = valeur et g = 1, puis on remplace à chaque tour a par (a + g) / 2
 * et g par √(a·g). Les deux suites convergent très vite vers la même limite,
 * qui sert notamment à calculer π ou les intégrales elliptiques.
 * Elle s'active en remplaçant le métier par metier.MetierImpl4 dans config.txt.
 */
public class MetierImpl4 implements IMetier {

    // Écart relatif en dessous duquel on considère que les deux suites ont convergé
    private static final double PRECISION = 1e-15;
    private static final int MAX_ITERATIONS = 100;

    // Rempli par le conteneur (Presentation2) après l'instanciation
    private IDao dao;

    /**
     * Point d'injection de la dépendance, appelé par réflexion.
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

        double valeur = dao.getValue();
        if (valeur <= 0) {
            throw new IllegalArgumentException("L'AGM n'est définie que pour une valeur strictement positive : " + valeur);
        }

        double a = valeur;
        double g = 1.0;
        int iterations = 0;
        while (Math.abs(a - g) > PRECISION * a && iterations < MAX_ITERATIONS) {
            double nouveauA = (a + g) / 2;
            g = Math.sqrt(a * g);
            a = nouveauA;
            iterations++;
        }

        System.out.println("[METIER-4] AGM(" + valeur + ", 1) = " + a
                + " (convergence en " + iterations + " itérations)");
        return a;
    }
}

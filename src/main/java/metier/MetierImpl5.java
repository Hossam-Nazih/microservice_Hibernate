package metier;

import dao.IDao;

/**
 * Cinquième implémentation métier : la fonction W de Lambert (branche principale).
 * <p>
 * W(x) est le nombre w tel que w·e^w = x. Elle n'a pas d'expression avec les
 * fonctions usuelles, on la calcule donc par la méthode itérative de Halley.
 * Elle s'active en remplaçant le métier par metier.MetierImpl5 dans config.txt.
 */
public class MetierImpl5 implements IMetier {

    // En dessous de -1/e, l'équation w·e^w = x n'a pas de solution réelle
    private static final double BORNE_INF = -1 / Math.E;
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

        double x = dao.getValue();
        if (x < BORNE_INF) {
            throw new IllegalArgumentException("W n'est pas définie dans les réels pour x < -1/e : " + x);
        }

        // Point de départ : ln(1 + x) est proche de W pour les petites et moyennes valeurs,
        // ln(x) - ln(ln(x)) l'est pour les grandes
        double w = x < 3 ? Math.log1p(x) : Math.log(x) - Math.log(Math.log(x));

        int iterations = 0;
        // Au point de branchement x = -1/e, W vaut -1 et la dérivée s'annule : on n'itère pas
        boolean pointDeBranchement = x == BORNE_INF;
        if (pointDeBranchement) {
            w = -1;
        }
        while (!pointDeBranchement && iterations < MAX_ITERATIONS) {
            double ew = Math.exp(w);
            double f = w * ew - x;
            // Halley : w ← w - f / (f' - f·f'' / (2f')), avec f' = e^w(w + 1) et f'' = e^w(w + 2)
            double pas = f / (ew * (w + 1) - (w + 2) * f / (2 * w + 2));
            w -= pas;
            iterations++;
            if (Math.abs(pas) <= PRECISION * (1 + Math.abs(w))) {
                break;
            }
        }

        System.out.println("[METIER-5] W(" + x + ") = " + w
                + " (vérification : w·e^w = " + w * Math.exp(w) + ", " + iterations + " itérations)");
        return w;
    }
}

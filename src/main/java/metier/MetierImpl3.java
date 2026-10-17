package metier;

import dao.IDao;

/**
 * Troisième implémentation métier : la fonction Gamma d'Euler, Γ(x).
 * <p>
 * Γ prolonge la factorielle aux nombres réels : Γ(n) = (n - 1)! pour n entier.
 * On utilise l'approximation de Lanczos (g = 7, 9 coefficients), précise à ~15 chiffres,
 * et la formule de réflexion d'Euler pour les valeurs inférieures à 0,5.
 * Elle s'active en remplaçant le métier par metier.MetierImpl3 dans config.txt.
 */
public class MetierImpl3 implements IMetier {

    // Paramètre et coefficients classiques de l'approximation de Lanczos
    private static final double G = 7;
    private static final double[] COEFFS = {
            0.99999999999980993,
            676.5203681218851,
            -1259.1392167224028,
            771.32342877765313,
            -176.61502916214059,
            12.507343278686905,
            -0.13857109526572012,
            9.9843695780195716e-6,
            1.5056327351493116e-7
    };

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
        // Γ a des pôles en 0, -1, -2... : la fonction n'y est pas définie
        if (x <= 0 && x == Math.rint(x)) {
            throw new IllegalArgumentException("Γ n'est pas définie pour un entier négatif ou nul : " + x);
        }

        double resultat = gamma(x);
        // Au-delà de x ≈ 171,6, Γ(x) dépasse la capacité d'un double
        if (Double.isInfinite(resultat)) {
            throw new ArithmeticException("Γ(" + x + ") est trop grand pour être représenté par un double");
        }

        System.out.println("[METIER-3] Γ(" + x + ") = " + resultat
                + (x == Math.rint(x) ? " (soit " + (long) (x - 1) + "!)" : ""));
        return resultat;
    }

    private static double gamma(double x) {
        // Formule de réflexion : Γ(x)·Γ(1 - x) = π / sin(πx)
        if (x < 0.5) {
            return Math.PI / (Math.sin(Math.PI * x) * gamma(1 - x));
        }

        x -= 1;
        double somme = COEFFS[0];
        for (int i = 1; i < COEFFS.length; i++) {
            somme += COEFFS[i] / (x + i);
        }
        double t = x + G + 0.5;

        // On passe par les logarithmes pour éviter un dépassement intermédiaire de t^(x + 0.5)
        return Math.exp(0.5 * Math.log(2 * Math.PI) + (x + 0.5) * Math.log(t) - t + Math.log(somme));
    }
}

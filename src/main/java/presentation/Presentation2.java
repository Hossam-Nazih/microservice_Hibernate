package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Mini-conteneur d'injection de dépendances basé sur la réflexion.
 * <p>
 * Il lit les noms de classes dans config.txt, les charge, les instancie,
 * branche le DAO dans le métier puis lance le calcul.
 * Usage : java presentation.Presentation2 [cheminDuFichierDeConfig]
 */
public class Presentation2 {

    private static final String DEFAULT_CONFIG = "config.txt";

    public static void main(String[] args) {
        String configFile = args.length > 0 ? args[0] : DEFAULT_CONFIG;
        try {
            // Étape 1 : lecture de la configuration (ligne 1 = DAO, ligne 2 = métier)
            String[] classNames = loadClassNames(configFile);

            // Étape 2 : création du DAO, en vérifiant qu'il respecte bien le contrat IDao
            IDao dao = instantiateClass(classNames[0], IDao.class);
            System.out.println("[CONTAINER] DAO instancié : " + dao.getClass().getName());

            // Étape 3 : création du métier
            IMetier metier = instantiateClass(classNames[1], IMetier.class);
            System.out.println("[CONTAINER] Métier instancié : " + metier.getClass().getName());

            // Étape 4 : injection du DAO via le setter, appelé par réflexion
            injectDependency(metier, "setDao", dao);
            System.out.println("[CONTAINER] Dépendance injectée via setDao");

            // Étape 5 : exécution
            double resultat = metier.calcul();
            System.out.println("Résultats = " + resultat);

        } catch (IOException e) {
            System.err.println("[ERREUR] Impossible de lire " + configFile + " : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("[ERREUR] Classe introuvable : " + e.getMessage()
                    + " (vérifiez le nom complet dans " + configFile + " et le classpath)");
        } catch (NoSuchMethodException e) {
            System.err.println("[ERREUR] Constructeur ou setter manquant : " + e.getMessage());
        } catch (InvocationTargetException e) {
            // La réflexion enveloppe l'exception levée par le code appelé : on affiche la vraie cause
            System.err.println("[ERREUR] Exception levée par le composant : " + e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère les deux noms de classes du fichier de configuration.
     * Les lignes vides et celles commençant par # sont ignorées.
     */
    private static String[] loadClassNames(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
        }
        if (lines.size() < 2) {
            throw new IOException("2 noms de classes attendus (DAO puis métier), " + lines.size() + " trouvé(s)");
        }
        return new String[]{lines.get(0), lines.get(1)};
    }

    /**
     * Charge une classe par son nom complet et l'instancie avec son constructeur sans argument.
     * Le paramètre expectedType évite un ClassCastException peu lisible si la configuration est fausse.
     */
    private static <T> T instantiateClass(String className, Class<T> expectedType) throws Exception {
        Class<?> clazz = Class.forName(className);
        if (!expectedType.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(className + " n'implémente pas " + expectedType.getName());
        }
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return expectedType.cast(constructor.newInstance());
    }

    /**
     * Appelle le setter nommé setterName sur target en lui passant dependency.
     * <p>
     * Plutôt que de deviner le type du paramètre (getInterfaces()[0]), on parcourt
     * les méthodes publiques et on retient celle dont le paramètre accepte la dépendance.
     * Cela fonctionne même si l'implémentation a plusieurs interfaces ou hérite d'une classe.
     */
    private static void injectDependency(Object target, String setterName, Object dependency) throws Exception {
        for (Method method : target.getClass().getMethods()) {
            if (method.getName().equals(setterName)
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0].isInstance(dependency)) {
                method.invoke(target, dependency);
                return;
            }
        }
        throw new NoSuchMethodException(target.getClass().getName() + "." + setterName
                + "(" + dependency.getClass().getName() + ")");
    }
}

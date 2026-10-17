package presentation;

import dao.DaoImpl1;
import metier.MetierImpl;

/**
 * Version statique, à comparer avec Presentation2.
 * <p>
 * Les classes concrètes sont écrites en dur avec "new" : pour passer à
 * DaoImpl2, il faut modifier ce fichier, recompiler et redéployer.
 * C'est ce couplage que la version dynamique supprime.
 */
public class Presentation1 {

    public static void main(String[] args) {
        DaoImpl1 dao = new DaoImpl1();
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);
        System.out.println("Résultats = " + metier.calcul());
    }
}

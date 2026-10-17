package metier;

/**
 * Contrat de la couche métier : c'est le cœur du microservice.
 * <p>
 * Il ne dépend d'aucun framework, ce qui permet de le réutiliser
 * (API, batch, consommateur d'événements) et de le tester isolément.
 */
public interface IMetier {

    /**
     * Applique la règle métier à la donnée fournie par le DAO.
     *
     * @return le résultat du calcul
     */
    double calcul();
}

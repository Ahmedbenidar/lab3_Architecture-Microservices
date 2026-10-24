package metier;

/**
 * Contrat du service métier. Il ne connaît que {@link dao.IDao}, jamais une
 * implémentation concrète : c'est ce découplage qui rend l'application
 * extensible sans modification (principe OCP).
 */
public interface IMetier {

    /**
     * Applique le traitement métier à la valeur lue dans la source de données.
     *
     * @return le résultat du calcul
     */
    double calcul();
}

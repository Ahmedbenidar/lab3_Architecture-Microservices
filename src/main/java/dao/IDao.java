package dao;

/**
 * Contrat unique d'accès aux données.
 * <p>
 * Toutes les sources (base SQL, fichier, API distante) exposent la même
 * signature : c'est ce contrat qui rend les implémentations interchangeables
 * côté métier.
 */
public interface IDao {

    /**
     * Lit la valeur courante exposée par la source de données.
     *
     * @return valeur lue, en unités métier
     */
    double getValue();
}

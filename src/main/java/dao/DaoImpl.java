package dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Source de référence : base de données de production.
 * <p>
 * {@code @Primary} en fait le candidat retenu par défaut dès que plusieurs
 * beans {@link IDao} coexistent dans le conteneur (stratégie A du TP).
 */
@Primary
@Component("dao")
public class DaoImpl implements IDao {

    @Override
    public double getValue() {
        System.out.println("[DAO] Lecture depuis la base de données (prod)");
        return 100.0;
    }
}

package metier;

import dao.IDao;
import org.springframework.stereotype.Service;

/**
 * Cœur métier du TP.
 * <p>
 * Cette classe est volontairement figée : elle reçoit son {@link IDao} par
 * constructeur et ne contient aucune logique de sélection. Changer de source de
 * données ne doit jamais conduire à la modifier (OCP).
 */
@Service
public class MetierImpl implements IMetier {

    private final IDao dao;

    public MetierImpl(IDao dao) {
        this.dao = dao;
        System.out.println("[TRACE] Implémentation DAO injectée : " + dao.getClass().getSimpleName());
    }

    @Override
    public double calcul() {
        double valeur = dao.getValue();
        return valeur * 2.0;
    }
}

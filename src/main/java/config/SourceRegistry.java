package config;

import dao.DaoApi;
import dao.DaoFile;
import dao.DaoImpl;
import dao.DaoImpl2;
import dao.IDao;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Catalogue des sources de données disponibles, indexé par nom de cible.
 * <p>
 * Il n'est pas un bean Spring : il sert de table de correspondance au bean de
 * sélection, ce qui évite l'auto-référence du conteneur (le bean sélectionné
 * implémente {@code IDao} et ne peut donc pas consommer la liste de tous les
 * {@code IDao}).
 */
public final class SourceRegistry {

    private final Map<String, IDao> sources = new LinkedHashMap<>();

    public SourceRegistry() {
        sources.put("dao", new DaoImpl());
        sources.put("dao2", new DaoImpl2());
        sources.put("daoFile", new DaoFile());
        sources.put("daoApi", new DaoApi());
    }

    public IDao pourCible(String cible) {
        IDao source = sources.get(cible);
        if (source == null) {
            throw new IllegalArgumentException(
                    "Cible DAO inconnue : '" + cible + "'. Valeurs acceptées : " + cibles());
        }
        return source;
    }

    public Set<String> cibles() {
        return Collections.unmodifiableSet(sources.keySet());
    }
}

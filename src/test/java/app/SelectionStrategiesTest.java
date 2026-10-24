package app;

import config.AliasSelectionConfig;
import config.AppConfig;
import config.PropertySelectionConfig;
import config.SourceRegistry;
import metier.IMetier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de non-régression : quelle que soit la stratégie de sélection, la
 * classe {@code MetierImpl} renvoie la valeur de la source configurée,
 * multipliée par deux.
 */
class SelectionStrategiesTest {

    @Test
    @DisplayName("@Primary designe DaoImpl par defaut")
    void primarySelectionneDaoImpl() {
        try (AnnotationConfigApplicationContext contexte = contexteAvec(AppConfig.class, null)) {
            assertEquals(200.0, contexte.getBean(IMetier.class).calcul(), 1e-9);
        }
    }

    @Test
    @DisplayName("L'alias @Bean designe DaoApi")
    void aliasSelectionneDaoApi() {
        try (AnnotationConfigApplicationContext contexte = contexteAvec(AliasSelectionConfig.class, null)) {
            assertEquals(440.0, contexte.getBean(IMetier.class).calcul(), 1e-9);
        }
    }

    @ParameterizedTest(name = "dao.target={0} => {1}")
    @CsvSource({"dao,200.0", "dao2,300.0", "daoFile,360.0", "daoApi,440.0"})
    @DisplayName("La propriete dao.target selectionne la bonne source")
    void selectionParPropriete(String cible, double attendu) {
        try (AnnotationConfigApplicationContext contexte =
                     contexteAvec(PropertySelectionConfig.class, cible)) {
            assertEquals(attendu, contexte.getBean(IMetier.class).calcul(), 1e-9);
        }
    }

    @Test
    @DisplayName("Une cible inconnue est rejetee explicitement")
    void cibleInconnueRejetee() {
        IllegalArgumentException erreur = assertThrows(IllegalArgumentException.class,
                () -> new SourceRegistry().pourCible("daoMongo"));
        assertTrue(erreur.getMessage().contains("daoMongo"));
    }

    private static AnnotationConfigApplicationContext contexteAvec(Class<?> configuration, String cible) {
        AnnotationConfigApplicationContext contexte = new AnnotationConfigApplicationContext();
        contexte.register(configuration);
        if (cible != null) {
            contexte.getEnvironment().getPropertySources()
                    .addFirst(new MapPropertySource("test", Map.of("dao.target", cible)));
        }
        contexte.refresh();
        return contexte;
    }
}

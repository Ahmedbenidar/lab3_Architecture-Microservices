package config;

import dao.IDao;
import metier.IMetier;
import metier.MetierImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

/**
 * Stratégie C : sélection par propriété externe.
 * <p>
 * La cible est lue dans {@code app.properties} (clé {@code dao.target}) et peut
 * être surchargée par une variable d'environnement {@code DAO_TARGET} ou par un
 * argument de ligne de commande, sans recompilation.
 */
@Configuration
@PropertySource("classpath:app.properties")
public class PropertySelectionConfig {

    @Bean
    public SourceRegistry sourceRegistry() {
        return new SourceRegistry();
    }

    @Bean
    @Primary
    public IDao daoSelectionne(SourceRegistry registry,
                               @Value("${dao.target:dao}") String cible) {
        IDao choisi = registry.pourCible(cible);
        System.out.println("[CONFIG] Cible 'dao.target' = " + cible);
        return choisi;
    }

    @Bean
    public IMetier metier(IDao daoSelectionne) {
        return new MetierImpl(daoSelectionne);
    }
}

package config;

import dao.DaoApi;
import dao.IDao;
import metier.IMetier;
import metier.MetierImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Stratégie B : un bean "pointeur" déclaré en configuration.
 * <p>
 * Le conteneur ne scanne pas le paquet {@code dao} : l'implémentation ciblée est
 * choisie ici, en un seul endroit. Basculer vers une autre source revient à
 * changer la ligne de {@code return}, sans toucher aux classes DAO ni au métier.
 */
@Configuration
public class AliasSelectionConfig {

    @Bean
    @Primary
    public IDao daoPointeur() {
        return new DaoApi();
    }

    @Bean
    public IMetier metier(IDao daoPointeur) {
        return new MetierImpl(daoPointeur);
    }
}

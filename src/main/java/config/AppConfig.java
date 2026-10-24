package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration de base : scan des composants applicatifs et chargement du
 * fichier de propriétés externe.
 * <p>
 * Quatre beans {@code IDao} sont détectés ; la sélection est assurée ici par
 * {@code @Primary} posé sur {@link dao.DaoImpl} (stratégie A).
 */
@Configuration
@ComponentScan(basePackages = {"dao", "metier"})
@PropertySource("classpath:app.properties")
public class AppConfig {
}

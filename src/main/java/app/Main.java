package app;

import config.AliasSelectionConfig;
import config.AppConfig;
import config.PropertySelectionConfig;
import metier.IMetier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.MapPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Point d'entrée du TP : chaque stratégie de sélection est exécutée dans son
 * propre conteneur Spring, puis le résultat obtenu est comparé au résultat
 * attendu. Toutes les stratégies réutilisent la même classe {@code MetierImpl},
 * qui n'est jamais modifiée.
 */
public final class Main {

    private static final String LIGNE = "-".repeat(68);

    /** Un scénario = une configuration Spring, une cible éventuellement surchargée, un résultat attendu. */
    private record Scenario(String code, String libelle,
                            Class<?> configuration, String cibleSurchargee, double attendu) {
    }

    public static void main(String[] args) {
        List<Scenario> scenarios = List.of(
                new Scenario("A", "@Primary sur DaoImpl", AppConfig.class, null, 200.0),
                new Scenario("B", "alias @Bean vers DaoApi", AliasSelectionConfig.class, null, 440.0),
                new Scenario("C", "propriete dao.target (app.properties)", PropertySelectionConfig.class, null, 300.0),
                new Scenario("C'", "surcharge de dao.target a l'execution", PropertySelectionConfig.class, "daoApi", 440.0)
        );

        entete();

        List<String> bilan = new ArrayList<>();
        int conformes = 0;

        for (Scenario scenario : scenarios) {
            System.out.printf(">> %s | %s  [%s]%n",
                    scenario.code(), scenario.libelle(), scenario.configuration().getSimpleName());

            double resultat = executer(scenario);
            boolean ok = Math.abs(resultat - scenario.attendu()) < 1e-9;
            if (ok) {
                conformes++;
            }

            System.out.printf("   [%s] calcul() = %.1f  (attendu %.1f)%n",
                    ok ? "OK" : "ECHEC", resultat, scenario.attendu());
            bilan.add(String.format("   %-3s %-34s %7.1f  %s",
                    scenario.code(), scenario.libelle(), resultat, ok ? "OK" : "ECHEC"));
        }

        resume(bilan, conformes, scenarios.size());
    }

    /** Exécute un scénario dans un conteneur isolé (fermé automatiquement en fin de bloc). */
    private static double executer(Scenario scenario) {
        try (AnnotationConfigApplicationContext contexte = new AnnotationConfigApplicationContext()) {
            contexte.register(scenario.configuration());
            if (scenario.cibleSurchargee() != null) {
                contexte.getEnvironment()
                        .getPropertySources()
                        .addFirst(new MapPropertySource("surcharge-execution",
                                Map.of("dao.target", scenario.cibleSurchargee())));
            }
            contexte.refresh();

            IMetier metier = contexte.getBean(IMetier.class);
            return metier.calcul();
        }
    }

    private static void entete() {
        System.out.println(LIGNE);
        System.out.println(" TP 3 : Spring IoC & OCP - choisir un DAO sans modifier le metier");
        System.out.printf(" JVM %s | Spring Framework %s | regle metier : valeur x 2%n",
                System.getProperty("java.version"), SpringVersion.getVersion());
        System.out.println(LIGNE);
    }

    private static void resume(List<String> bilan, int conformes, int total) {
        System.out.println(LIGNE);
        System.out.println(" RECAPITULATIF");
        bilan.forEach(System.out::println);
        System.out.printf(" %d/%d scenarios conformes - MetierImpl n'a jamais ete modifie.%n",
                conformes, total);
        System.out.println(conformes == total
                ? " Conclusion : OCP respecte, la source de donnees est choisie hors du code metier."
                : " Conclusion : au moins une strategie ne respecte pas le contrat attendu.");
        System.out.println(LIGNE);
    }
}

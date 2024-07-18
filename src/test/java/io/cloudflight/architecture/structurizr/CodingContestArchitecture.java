package io.cloudflight.architecture.structurizr;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.*;
import io.cloudflight.architectureicons.azure.AzureMonoIcons;
import io.cloudflight.architectureicons.tupadr3.DevIcons2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * @author Klaus Lehner, Cloudflight
 */
@SpringBootApplication
public class CodingContestArchitecture {

    static final String FileSystem = "FileSystem";
    static final String Database = "Database";

    @Component
    static class ViewConfigurer implements ViewProvider {

        private final Workspace workspace;

        ViewConfigurer(Workspace workspace) {
            this.workspace = workspace;
            Styles styles = workspace.getViews().getConfiguration().getStyles();
            styles.addElementStyle(Database).shape(Shape.Cylinder);
            styles.addElementStyle(FileSystem).shape(Shape.Folder);
            styles.addElementStyle(Tags.PERSON).shape(Shape.Person);
            styles.addElementStyle(DevIcons2.SPRING.getName()).background("#6DB33F").color("#000000");

            Configuration configuration = workspace.getViews().getConfiguration();
            configuration.addTheme(AzureMonoIcons.STRUCTURIZR_THEME_URL);
            configuration.addTheme(DevIcons2.STRUCTURIZR_THEME_URL);
        }

        @Override
        public void createViews(@Nonnull ViewSet viewSet) {
            SystemLandscapeView view = viewSet.createSystemLandscapeView("codingcontest", "");
            view.addAllElements();
        }
    }

    @Component
    static class Personas {

        final Person user;
        final Person admin;

        Personas(Model model) {
            this.user = model.addPerson("Contest Participant", "");
            this.admin = model.addPerson("Administrator", "");
        }
    }

    @Component
    static class CodingContest implements ViewProvider {

        final SoftwareSystem platform;
        private final CodingContestArchitecture.Personas personas;
        private final Container registration;
        private final Container catCoder;

        CodingContest(Model model, Personas personas) {
            this.platform = model.addSoftwareSystem("Coding Contest Platform");
            this.personas = personas;

            this.registration = platform.addContainer("Registration", "maintains all users and contests, provides SSO");
            this.catCoder = platform.addContainer("CatCoder", "provides the possibility to solve coding challenges");

            registration.uses(catCoder, "fetches contests", "REST");

            personas.admin.uses(registration, "creates a new contest");
            personas.admin.uses(catCoder, "creates a new game");
        }


        @Override
        public void createViews(@Nonnull ViewSet viewSet) {
            ContainerView containerView = viewSet.createContainerView(platform, "ccp", "Coding Contest Platform");
            containerView.addAllContainersAndInfluencers();
            containerView.addAllPeople();

            DynamicView dynamicView = viewSet.createDynamicView(platform, "admin-visit", "");
            dynamicView.add(personas.admin, registration);
            dynamicView.add(personas.admin, catCoder);

        }
    }
}
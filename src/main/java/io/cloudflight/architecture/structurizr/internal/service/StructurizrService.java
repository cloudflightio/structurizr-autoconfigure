package io.cloudflight.architecture.structurizr.internal.service;

import com.structurizr.Workspace;
import io.cloudflight.architecture.structurizr.ModelPostProcessor;
import io.cloudflight.architecture.structurizr.ViewProvider;
import io.cloudflight.architecture.structurizr.WorkspaceExportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

/**
 * @author Klaus Lehner
 */
@Order(StructurizrService.ORDER)
public class StructurizrService implements CommandLineRunner {

    private final Workspace workspace;
    private final ApplicationContext applicationContext;

    static final int ORDER = 0;

    public StructurizrService(Workspace workspace, ApplicationContext applicationContext) {
        this.workspace = workspace;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        applicationContext.getBeansOfType(ModelPostProcessor.class)
                .values()
                .stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .forEach(modelPostProcessor -> modelPostProcessor.postProcess(workspace.getModel()));

        applicationContext.getBeansOfType(ViewProvider.class)
                .values()
                .forEach(viewProvider -> viewProvider.createViews(workspace.getViews()));

        applicationContext.getBeansOfType(WorkspaceExportService.class)
                .values()
                .stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .forEach(exporter -> exporter.exportWorkspace(workspace));
    }
}
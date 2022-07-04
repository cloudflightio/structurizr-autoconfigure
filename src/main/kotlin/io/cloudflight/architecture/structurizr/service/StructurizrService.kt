package io.cloudflight.architecture.structurizr.service

import com.structurizr.Workspace
import io.cloudflight.architecture.structurizr.ModelPostProcessor
import io.cloudflight.architecture.structurizr.ViewProvider
import io.cloudflight.architecture.structurizr.WorkspaceExportService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.core.annotation.Order

/**
 * @author Klaus Lehner, Catalysts GmbH
 */
@Order(StructurizrService.ORDER)
internal class StructurizrService(
    private val workspace: Workspace,
    private val applicationContext: ApplicationContext
) : CommandLineRunner {
    override fun run(vararg args: String) {
        val postProcessors = mutableListOf<ModelPostProcessor>().apply {
            addAll(applicationContext.getBeansOfType(ModelPostProcessor::class.java).values)
        }
        AnnotationAwareOrderComparator.sort(postProcessors)
        for (postProcessor in postProcessors) {
            postProcessor.postProcess(workspace.model)
        }
        applicationContext
            .getBeansOfType(ViewProvider::class.java)
            .values
            .forEach { it.createViews(workspace.views) }

        val exporters = mutableListOf<WorkspaceExportService>().apply {
            addAll(applicationContext.getBeansOfType(WorkspaceExportService::class.java).values)
        }
        AnnotationAwareOrderComparator.sort(exporters)
        for (exporter in exporters) {
            exporter.exportWorkspace(workspace)
        }
    }

    companion object {
        const val ORDER = 0
    }
}
package io.cloudflight.architecture.structurizr

import com.structurizr.view.ViewSet

/**
 * Implement this interface if you want to create [com.structurizr.view.View]s for your [com.structurizr.model.Model].
 * The reason why this is done in a separate method is to ensure that all [org.springframework.stereotype.Component]s which
 * might manipulate the model during startup phase have been initialized and that the [com.structurizr.model.Model] is complete.
 *
 * @author Klaus Lehner
 */
interface ViewProvider {
    /**
     *
     * This method is called automatically after the whole [org.springframework.context.ApplicationContext] is refreshed,
     * thus the [com.structurizr.model.Model] definition should be complete.
     *
     * Implementations of this method might create one or more [com.structurizr.view.View] inside this method
     *
     * @param viewSet the [ViewSet] of the [com.structurizr.Workspace] after the whole [com.structurizr.model.Model]
     * has been initialized
     */
    fun createViews(viewSet: ViewSet)
}
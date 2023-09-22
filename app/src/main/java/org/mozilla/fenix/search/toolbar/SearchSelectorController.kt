/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.search.toolbar

import androidx.navigation.NavController
import org.mozilla.fenix.GleanMetrics.Events
import org.mozilla.fenix.HomeActivity
import org.mozilla.fenix.NavGraphDirections
import org.mozilla.fenix.R
import org.mozilla.fenix.browser.BrowserAnimator
import org.mozilla.fenix.ext.components
import org.mozilla.fenix.ext.nav
import org.mozilla.fenix.ext.telemetryName

/**
 * An interface that handles the view manipulation of the search selector menu.
 */
interface SearchSelectorController {
    /**
     * @see [SearchSelectorInteractor.onMenuItemTapped]
     */
    fun handleMenuItemTapped(item: SearchSelectorMenu.Item)
}

/**
 * The default implementation of [SearchSelectorController].
 */
class DefaultSearchSelectorController(
    private val activity: HomeActivity,
    private val navController: NavController,
) : SearchSelectorController {

    override fun handleMenuItemTapped(item: SearchSelectorMenu.Item) {
        when (item) {
            SearchSelectorMenu.Item.SearchSettings -> {
                navController.nav(
                    R.id.homeFragment,
                    NavGraphDirections.actionGlobalSearchEngineFragment(),
                )
            }

            is SearchSelectorMenu.Item.SearchEngine -> {
                val directions = NavGraphDirections.actionGlobalSearchDialog(
                    sessionId = null,
                    searchEngine = item.searchEngine.id,
                )

                //设置为默认引擎
                activity.components.useCases.searchUseCases.selectSearchEngine(item.searchEngine)
                Events.defaultEngineSelected.record(Events.DefaultEngineSelectedExtra(item.searchEngine.telemetryName()))

                navController.nav(
                    R.id.homeFragment,
                    directions,
                    BrowserAnimator.getToolbarNavOptions(activity),
                )
            }
        }
    }
}

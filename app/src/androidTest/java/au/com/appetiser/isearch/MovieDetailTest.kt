package au.com.appetiser.isearch

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.*
import au.com.appetiser.isearch.movie.MovieListActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class MovieDetailTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }


    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun viewMovieDetail_success() {
        runBlocking {
            val activityScenario = ActivityScenario.launch(MovieListActivity::class.java)
            dataBindingIdlingResource.monitorActivity(activityScenario)

            val trackName = "Star Trek"

            onView(withId(R.id.item_list)).perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(trackName)), click()));
            onView(withId(R.id.movie_detail_trackname)).check(ViewAssertions.matches(withText(trackName)))

            activityScenario.close()
        }
    }

}
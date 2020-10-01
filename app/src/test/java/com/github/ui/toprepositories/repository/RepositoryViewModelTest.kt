package com.github.ui.toprepositories.repository

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.github.RxRule
import com.github.TestFileLoader
import com.github.di.DaggerTestAppComponent
import com.github.di.GenericViewModelFactory
import com.github.di.TestAppModule
import com.github.domain.model.PullRequest
import com.github.domain.model.Repository
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


@RunWith(PowerMockRunner::class)
@PrepareForTest(DateFormat::class)
@PowerMockIgnore("javax.net.ssl.*")
class RepositoryViewModelTest {

    @Rule
    @JvmField
    val rxRules = RxRule()

    @Mock
    lateinit var mockApplicationContext: Context

    @Mock
    private lateinit var mockContextResources: Resources

    private var okHttpCountDownLatch: CountDownLatch = CountDownLatch(1)


    private lateinit var testAppModule: TestAppModule

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<RepositoryViewModel>

    private val viewModelStore = ViewModelStore()
    private val viewModel: RepositoryViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[RepositoryViewModel::class.java]
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        PowerMockito.mockStatic(DateFormat::class.java)
        Mockito.`when`(
            DateFormat.format(
                Matchers.anyString(),
                Matchers.any(Date::class.java)
            )
        ).thenReturn("Apr 25, 2020 13:37")

        Mockito.`when`(mockApplicationContext.resources).thenReturn(mockContextResources)
        Mockito.`when`(
            mockContextResources.getQuantityString(
                Matchers.anyInt(),
                Matchers.anyInt(),
                Matchers.anyInt()
            )
        ).thenReturn("Some string with a quantity in it")
        Mockito.`when`(
            mockContextResources.getString(Matchers.anyInt())
        ).thenReturn("Some string")
        Mockito.`when`(
            mockContextResources.getString(Matchers.anyInt(), Matchers.any())
        ).thenReturn("Some string with a parameter in it")

        okHttpCountDownLatch = CountDownLatch(1)
        testAppModule = TestAppModule(
            mockApplicationContext,
            Runnable {
                okHttpCountDownLatch.countDown()
            },
            "/repos/freeCodeCamp/freeCodeCamp/pulls?sort=updated&direction=desc&state=all" to TestFileLoader.readJsonFileFromAssets(
                "pull_requests.json"
            )
        )
        DaggerTestAppComponent.builder().appModule(testAppModule).build()
            .repositoryViewModelTestBuilder()
            .setRepository(
                Repository(
                    28457823,
                    "freeCodeCamp",
                    "freeCodeCamp",
                    310313,
                    23985,
                    310313,
                    245,
                    "JavaScript",
                    Calendar.getInstance().time
                )
            ).build().inject(this)
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
        testAppModule.shutDownServer()
    }

    @Test
    fun testAmountOfPullRequests() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount of pull requests does not match",
            30,
            viewModel.items.get()!!.size
        )
    }

    @Test
    fun testUserNameAtIndexThree() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected user name does not match",
            "cslv",
            viewModel.items.get()!![3].userName
        )
    }

    @Test
    fun testTitleAtIndexZero() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected title does not match",
            "Update README.md",
            viewModel.items.get()!![0].title
        )
    }

    @Test
    fun testPRStateAtIndexTwo() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected PR state name does not match",
            PullRequest.State.CLOSED,
            viewModel.items.get()!![2].pullRequest.state
        )
    }
}
package com.github.ui.toprepositories

import android.text.format.DateFormat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.github.AndroidMocks
import com.github.RxRule
import com.github.TestFileLoader
import com.github.di.DaggerTestAppComponent
import com.github.di.GenericViewModelFactory
import com.github.di.TestAppModule
import com.github.ui.toprepositories.vm.RepositoryItemViewModel
import org.junit.*
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


@RunWith(PowerMockRunner::class)
@PrepareForTest(DateFormat::class)
@PowerMockIgnore("javax.net.ssl.*")
class TopRepositoriesViewModelTest {

    @Rule
    @JvmField
    val rxRules = RxRule()

    val androidMocks = AndroidMocks()

    private var okHttpCountDownLatch: CountDownLatch = CountDownLatch(1)


    private lateinit var testAppModule: TestAppModule

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<TopRepositoriesViewModel>

    private val viewModelStore = ViewModelStore()
    private val viewModel: TopRepositoriesViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[TopRepositoriesViewModel::class.java]
    }

    @Before
    fun setup() {
        okHttpCountDownLatch = CountDownLatch(1)
        testAppModule = TestAppModule(
            androidMocks.mockApplicationContext,
            {
                okHttpCountDownLatch.countDown()
            },
            "/search/repositories?o=desc&s=stars&q=stars:%3E=60000&page=1&per_page=20" to TestFileLoader.readJsonFileFromAssets(
                "top_repositories.json"
            )
        )
        DaggerTestAppComponent.builder().appModule(testAppModule).build().inject(this)
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
        testAppModule.shutDownServer()
    }

    @Test
    fun testAmountOfRepositories() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount of repositories does not match",
            30,
            viewModel.items.get()!!.size
        )
    }

    @Test
    fun testStarCountAtIndexThree() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            " The expected amount of stars does not match",
            "147695",
            (viewModel.items.get()!![3] as RepositoryItemViewModel).starCount
        )
    }

    @Test
    fun testOwnerAtIndexZero() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected owner does not match",
            "freeCodeCamp",
            (viewModel.items.get()!![0] as RepositoryItemViewModel).owner
        )
    }

    @Test
    fun testRepositoryNameAtIndexTwo() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected repository name does not match",
            "vue",
            (viewModel.items.get()!![2] as RepositoryItemViewModel).title
        )
    }

    @Test
    fun testCorrectDateParsedAtIndexOne() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        val expected = Calendar.getInstance().apply {
            set(2020, 3, 28, 22, 4, 40)
            timeZone = TimeZone.getTimeZone("CET")
        }

        val actual = Calendar.getInstance()
        actual.time = (viewModel.items.get()!![1] as RepositoryItemViewModel).repository.updatedAt


        Assert.assertTrue(
            "The expected updated at was: ${expected.time}. but the actual was: ${actual.time}",
            expected.get(Calendar.YEAR) == actual.get(Calendar.YEAR)
                    && expected.get(Calendar.DAY_OF_YEAR) == actual.get(Calendar.DAY_OF_YEAR)
                    && expected.get(Calendar.HOUR_OF_DAY) == actual.get(Calendar.HOUR_OF_DAY)
                    && expected.get(Calendar.MINUTE) == actual.get(Calendar.MINUTE)
        )
    }
}
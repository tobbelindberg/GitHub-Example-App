package com.github.ui.toprepositories

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
class TopRepositoriesViewModelTest {

    @Rule
    @JvmField
    val rxRules = RxRule()

    @Mock
    lateinit var mockApplicationContext: Context

    @Mock
    private lateinit var mockContextResources: Resources

    var okHttpCountDownLatch: CountDownLatch = CountDownLatch(1)


    private lateinit var testAppModule: TestAppModule

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<TopRepositoriesViewModel>

    private val viewModelStore = ViewModelStore()
    private val viewModel: TopRepositoriesViewModel by lazy {
        ViewModelProvider(viewModelStore, viewModelFactory)[TopRepositoriesViewModel::class.java]
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
            TestFileLoader.readJsonFileFromAssets("top_repositories.json"),
            Runnable {
                okHttpCountDownLatch.countDown()
            }
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
    fun testOwnerAtIndexZero() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected owner does not match",
            "freeCodeCamp",
            viewModel.items.get()!![0].owner
        )
    }

    @Test
    fun testRepositoryNameAtIndexTwo() {
        viewModel.initStateObservable()
        okHttpCountDownLatch.await()

        Assert.assertEquals(
            "The expected repository name does not match",
            "vue",
            viewModel.items.get()!![2].title
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
        actual.time = viewModel.items.get()!![1].repository.updatedAt


        Assert.assertTrue(
            "The expected updated at was: ${expected.time}. but the actual was: ${actual.time}",
            expected.get(Calendar.YEAR) == actual.get(Calendar.YEAR)
                    && expected.get(Calendar.DAY_OF_YEAR) == actual.get(Calendar.DAY_OF_YEAR)
                    && expected.get(Calendar.HOUR_OF_DAY) == actual.get(Calendar.HOUR_OF_DAY)
                    && expected.get(Calendar.MINUTE) == actual.get(Calendar.MINUTE)
        )
    }
}
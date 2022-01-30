package com.github

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import org.mockito.*
import org.powermock.api.mockito.PowerMockito
import java.util.*

class AndroidMocks {

    @Mock
    lateinit var mockApplicationContext: Context

    @Mock
    private lateinit var mockContextResources: Resources

    init {
        MockitoAnnotations.initMocks(this)

        PowerMockito.mockStatic(DateFormat::class.java)
        Mockito.`when`(
            DateFormat.format(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Date::class.java)
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
            mockContextResources.getString(ArgumentMatchers.anyInt())
        ).thenReturn("Some string")
        Mockito.`when`(
            mockContextResources.getString(ArgumentMatchers.anyInt(), ArgumentMatchers.any())
        ).thenReturn("Some string with a parameter in it")
    }

}
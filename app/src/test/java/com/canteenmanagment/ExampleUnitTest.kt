package com.canteenmanagment

import com.canteenmanagment.helper.getTimeLabel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun timeLabel_isCorrect1() {
        assertEquals(getTimeLabel(8,15), "Close")
    }
    @Test

    fun timeLabel_isCorrect2() {
        assertEquals(getTimeLabel(8,30), "Morning")
    }

    @Test
    fun timeLabel_isCorrect3() {
        assertEquals(getTimeLabel(10,30), "Morning")
    }

    @Test
    fun timeLabel_isCorrect4() {
        assertEquals(getTimeLabel(11,0), "Afternoon")
    }
    @Test
    fun timeLabel_isCorrect5() {
        assertEquals(getTimeLabel(13,0), "Afternoon")
    }

    @Test
    fun timeLabel_isCorrect6() {
        assertEquals(getTimeLabel(15,0), "Evening")
    }

    @Test
    fun timeLabel_isCorrect7() {
        assertEquals(getTimeLabel(18,0), "Close")
    }

    @Test
    fun timeLabel_isCorrect8() {
        assertEquals(getTimeLabel(20,0), "Close")
    }



}
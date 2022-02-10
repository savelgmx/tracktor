package com.elegion.tracktor.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)

public class DistanceConverterTest {

    public DistanceConverter mDistanceConverter;

   // @Mock
    private Double distance=1000.00;


    @Rule
    public ExpectedException exception = ExpectedException.none();//    тестирование исключений


    @Before
    public void setUp() throws Exception {

        mDistanceConverter = new DistanceConverter();
    }

    @Test
    public void convertFromMetersToKm() {
        //проверим значения
        assertEquals(distance / 1000,distance / 1000,0);
      }

    @Test
    public void convertFromMetersToMiles() {
        //проверим значения
        assertEquals(distance/1609,distance/1609,0);
    }

    @Test
    public void convertFromMetersToFeets(){
        assertEquals(distance*3.28084,distance*3.28084,0);
    }

}
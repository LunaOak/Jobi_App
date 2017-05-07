package edu.umd.cs.agileandroid;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class InMemoryStoryServiceTest extends BaseActivityEspressoTest {
    @Test
    public void testAddStoryToBacklog(){

    }

    @Test
    public void testGetStoryById(){

    }

    @Test
    public void testGetAllStories(){

    }

    @Test
    public void testGetCurrentSprintStories(){

    }
}

package com.github

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class RxRule : TestRule {

    private val immediate = object : Scheduler() {

        override fun scheduleDirect(
            @NonNull run: Runnable,
            delay: Long,
            @NonNull unit: TimeUnit
        ): Disposable {
            // this prevents StackOverflowErrors when scheduling with a delay
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
        }
    }

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { _ -> immediate }
                RxJavaPlugins.setInitComputationSchedulerHandler { _ -> immediate }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { _ -> immediate }
                RxJavaPlugins.setInitSingleSchedulerHandler { _ -> immediate }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> immediate }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}
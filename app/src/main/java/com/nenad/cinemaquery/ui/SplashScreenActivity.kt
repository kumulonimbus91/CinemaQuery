package com.nenad.cinemaquery.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    lateinit var mBinding: ActivitySplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashScreenBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.animation)
        mBinding.appName.animation = splashAnimation
        splashAnimation.fillAfter = true


        splashAnimation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()


                val runnable = Runnable {
                    kotlin.run {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                    }
                }

                scheduledExecutorService.schedule(runnable, 2000, TimeUnit.MILLISECONDS)

            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })









        setContentView(mBinding.root)
    }
}
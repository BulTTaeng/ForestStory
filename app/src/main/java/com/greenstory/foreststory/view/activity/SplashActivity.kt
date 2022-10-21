package com.greenstory.foreststory.view.activity

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.greenstory.foreststory.R
import com.greenstory.foreststory.databinding.ActivitySplashBinding
import com.rommansabbir.animationx.*

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1000 // 1 sec
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.activity = this@SplashActivity

        Handler().postDelayed({
            animate()
        }, SPLASH_TIME_OUT)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }


    fun animate() {
        binding.imgSplashBackground.animationXFade(Fade.FADE_OUT, duration = 500,)
        binding.imgSplashMountains.animationXSlide(Slide.SLIDE_OUT_DOWN, duration = 500,
            listener = object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    Log.d("ssss" , "start")
                }

                override fun onAnimationEnd(p0: Animator?) {
                    toContentsActivity()
                    Log.d("eee" , "eeeeeee")
                }

                override fun onAnimationCancel(p0: Animator?) {
                    Log.d("cccccccc" , "cccccccc")
                }

                override fun onAnimationRepeat(p0: Animator?) {
                    Log.d("rrrrrr" , "rrrrrrrr")
                }

            })
        binding.imgSplashForestStoryWhite.animationXSlide(Slide.SLIDE_OUT_UP, duration = 500,)
        Handler().postDelayed({
            toContentsActivity()
        }, 400)
    }

    fun toContentsActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(Intent(intent))
        finish()
    }


}
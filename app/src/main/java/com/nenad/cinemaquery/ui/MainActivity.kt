package com.nenad.cinemaquery.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.databinding.ActivityMainBinding
import com.nenad.cinemaquery.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController: NavController
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setOnClickListeners()

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        supportActionBar?.hide()


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController
        mBinding.navView.setupWithNavController(mNavController)
    }

    fun setOnClickListeners() {
        mBinding.tvPopular.setOnClickListener {
            mNavController.navigate(R.id.homeFragment)
            mBinding.underline1.visibility = View.VISIBLE
            mBinding.underline2.visibility = View.INVISIBLE
        }
        mBinding.inTheaters.setOnClickListener {
            mNavController.navigate(R.id.intheatresFragment)
            mBinding.underline2.visibility = View.VISIBLE
            mBinding.underline1.visibility = View.INVISIBLE
        }
    }
}
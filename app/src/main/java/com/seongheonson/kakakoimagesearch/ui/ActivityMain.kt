package com.seongheonson.kakakoimagesearch.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.seongheonson.kakakoimagesearch.R
import com.seongheonson.kakakoimagesearch.ui.detail.DetailFragment
import com.seongheonson.kakakoimagesearch.ui.search.SearchFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var actionManager: ActionManager

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        actionManager.onActionListener = ::fireAction

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            actionManager.fire(Action(ActionType.SEARCH_IMAGE))
        }
    }

    private fun fireAction(action: Action) {
        when (action.type) {
            ActionType.UNKNOWN -> Log.w(javaClass.simpleName, "Unknown Action Fired!")
            ActionType.SEARCH_IMAGE -> transition(SearchFragment.newInstance(), replace = false)
            ActionType.DETAIL_IMAGE -> transition(DetailFragment.newInstance(action.data))
        }
    }

    private fun transition(fragment: Fragment, keepCurrent: Boolean = true, replace: Boolean = true) {
        if (!keepCurrent && supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        val transaction = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        when (replace) {
            true -> transaction.replace(R.id.container, fragment).addToBackStack(fragment.javaClass.simpleName)
            false -> transaction.add(R.id.container, fragment)
        }
        transaction.commit()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}

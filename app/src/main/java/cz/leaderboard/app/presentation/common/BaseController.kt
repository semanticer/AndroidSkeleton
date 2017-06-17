package cz.leaderboard.app.presentation.common

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController

/**
 * Created by tomas.valenta on 5/11/2017.
 */

abstract class BaseController<V : BaseView, P : MvpPresenter<V>> : MvpController<V, P>, BaseView, RefWatcherDelegate, ViewBindingDelegate {

    protected constructor() {}

    protected constructor(args: Bundle) : super(args) {}

    override var hasExited: Boolean = false

    override fun getParentActivity(): Activity? = activity

    override fun onDestroy() {
        super<RefWatcherDelegate>.onDestroy()
    }

    override fun onChangeEnded(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super<RefWatcherDelegate>.onChangeEnded(changeHandler, changeType)
    }

    override fun onDestroyView(view: View) {
        super<ViewBindingDelegate>.onDestroyView(view)
    }
}
package au.com.appetiser.isearch

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseActivity: AppCompatActivity() {

    protected fun <T: ViewDataBinding> setContentView(activity: AppCompatActivity, layoutId: Int): T {
        return DataBindingUtil.setContentView(activity, layoutId)
    }

}
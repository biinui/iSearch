package au.com.appetiser.isearch.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import au.com.appetiser.isearch.ViewModelFactory
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseViewModelActivity<B : ViewDataBinding, VM : ViewModel> : BaseActivity<B>() {

    @Inject
    lateinit var factory: ViewModelFactory<VM>

    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelClass = (javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[1] as Class<VM>

        viewModel = ViewModelProviders.of(this, factory)
                                      .get(viewModelClass)
    }
}
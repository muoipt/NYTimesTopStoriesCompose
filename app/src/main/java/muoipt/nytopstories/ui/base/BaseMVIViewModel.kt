package muoipt.nytopstories.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseMVIViewModel<A, P, S>(protected val initialUIState: S): ViewModel() {

    protected val actionFlow = MutableSharedFlow<A>()

    val uiState: StateFlow<S> by lazy {
        initUIState()
    }

    fun handleAction(action: A) {
        viewModelScope.launch {
            actionFlow.emit(action)
        }
    }

    protected abstract fun Flow<A>.toVMState(): Flow<P?>

    protected abstract fun initUIState(): StateFlow<S>
}


interface State
interface Action
package muoipt.nytopstories.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<I: UIAction, S: UIState, _S: VMState>(
    initUIState: S,
    initVMState: _S,
): ViewModel() {

    protected val vmStates: MutableStateFlow<_S> = MutableStateFlow(initVMState)
    val uiStates =
        vmStates.map { it.toUIState() }.stateIn(viewModelScope, SharingStarted.Eagerly, initUIState)

    fun sendAction(action: I) {
        handleAction(action)
    }

    protected abstract fun handleAction(action: I)

    protected fun setVMState(state: _S) {
        vmStates.value = state
    }

    protected fun currentVMState() = vmStates.value
}

interface UIAction
interface UIState
abstract class VMState {
    abstract fun toUIState(): UIState
}